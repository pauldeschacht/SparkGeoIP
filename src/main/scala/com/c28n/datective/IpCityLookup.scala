package com.c28n.datective

import com.maxmind.geoip2.model.CityResponse
import com.github.blemale.scaffeine.{Cache, Scaffeine}
import com.maxmind.db.{DatabaseRecord, Reader}

import java.io.File
import java.net.InetAddress

class IpCityLookup(file: File, cacheSize: Long = 0L) extends java.io.Serializable {
//  private val db: DatabaseReader = new DatabaseReader.Builder(file).build()
  private val db: Reader = new Reader(file)
  private val maybeCache: Option[Cache[String, CityResponse]] = if (cacheSize > 0)
    Some(Scaffeine()
      .maximumSize(cacheSize)
      .build[String, CityResponse]())
  else
    None

  def resolve(ip: String): Option[InetAddress] =
    try
      Some(InetAddress.getByName(ip))
    catch {
      case _: Throwable => None
    }

  def getCityResponse(ipA: InetAddress): Option[CityResponse] =
    try {
      val record: DatabaseRecord[CityResponse] = db.getRecord(ipA, classOf[CityResponse])
      if (record.getData == null)
        None
      else
        Some(record.getData)
    } catch {
      case _: Throwable =>
        None

    }

  def lookupInCache(ip: String): Option[CityResponse] =
    maybeCache.flatMap(cache => cache.getIfPresent(ip))

  def insertInCache(ip: String, cityResponse: CityResponse): Option[CityResponse] =
    maybeCache.map(cache => {
      cache.put(ip, cityResponse)
      cityResponse
    })

  def lookup(ip: String): Option[CityResponse] = {
    if (ip == null)
      None
    else
      lookupInCache(ip) match {
        case Some(cityResponse) => Some(cityResponse)
        case None => for {
          ipA <- resolve(ip)
          cityResponse <- getCityResponse(ipA)
          _ = insertInCache(ip, cityResponse)
        } yield cityResponse
      }
  }
}