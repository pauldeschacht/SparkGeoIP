package com.c28n.datective

import com.maxmind.geoip2.model.CityResponse
import org.apache.spark.SparkFiles
import org.apache.spark.sql.types.{StringType, IntegerType, StructField, StructType}

import java.io.File

object IpCityLookupUDF {

  case class IpLocationShort(city: Option[String], state: Option[String], country: Option[String], continent: Option[String], postalCode: Option[String])

  val ipLocationSchemaShort: StructType = StructType(
    Seq(
      StructField("city", StringType, true),
      StructField("state", StringType, true),
      StructField("country", StringType, true),
      StructField("continent", StringType, true),
      StructField("postalCode", StringType, true),
      StructField("postalConfidence", IntegerType, true)
    )
  )
  private val cityResponseToIpLocationShort: CityResponse => IpLocationShort = (cityResponse: CityResponse) =>
    IpLocationShort(
      city = if (cityResponse.getCity.getNames.containsKey("en")) Some(cityResponse.getCity.getNames.get("en")) else None,
      state = if (cityResponse.getSubdivisions.isEmpty) None else Some(cityResponse.getSubdivisions.get(0).getIsoCode),
      country = if (cityResponse.getCountry != null) Some(cityResponse.getCountry.getIsoCode) else None,
      continent = if (cityResponse.getContinent != null) Some(cityResponse.getContinent.getCode) else None,
      postalCode = if (cityResponse.getPostal != null && cityResponse.getPostal.getCode != null) Some(cityResponse.getPostal.getCode) else None
    )

  // the closure version - use as UDF
  def createUdf(filename: String): String => Option[IpLocationShort]  = {
    var _lookup: Any = None
    val ip2location: String => Option[IpLocationShort] = (ip: String) => {
      if (_lookup == None) {
        _lookup = Some(new IpCityLookup(new File(SparkFiles.get(filename)), 10000))
      }
      _lookup.asInstanceOf[Option[IpCityLookup]]
        .flatMap(_.lookup(ip))
        .map(cityResponseToIpLocationShort(_))
    }
    ip2location
  }
}