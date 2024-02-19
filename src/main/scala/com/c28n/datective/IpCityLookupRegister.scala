package com.c28n.datective

import com.c28n.datective.IpCityLookupUDF.{IpLocationShort, createUdf}
import org.apache.spark.sql.SparkSession

object IpCityLookupRegister {

  def register(spark: SparkSession, name: String, filename: String): Unit = {
    val udf: String => Option[IpLocationShort] = createUdf(filename)
    spark.udf.register(name, udf)
  }
  // the non-closure version - use in Dataset.mapPartition
  def ip2location(ipCityLookup: Option[IpCityLookup], ip: String): Option[IpLocationShort] = {
    ipCityLookup
      .flatMap(_.lookup(ip))
    match {
      case Some(cityResponse) =>
        Some(IpLocationShort(
          city = if (cityResponse.getCity.getNames.containsKey("en")) Some(cityResponse.getCity.getNames.get("en")) else None,
          state = if (cityResponse.getSubdivisions.isEmpty) None else Some(cityResponse.getSubdivisions.get(0).getIsoCode),
          country = if (cityResponse.getCountry != null) Some(cityResponse.getCountry.getIsoCode) else None,
          continent = if (cityResponse.getContinent != null) Some(cityResponse.getContinent.getCode) else None,
          postalCode = if (cityResponse.getPostal != null && cityResponse.getPostal.getCode != null) Some(cityResponse.getPostal.getCode) else None))
      case None => None
    }
  }
}
