package com.c28n.datective.ip2location

import com.c28n.datective.IpCityLookupUDF.IpLocationShort
import com.c28n.datective.embedded.EmbeddedHdfsSpark
import com.c28n.datective.{IpCityLookup, IpCityLookupRegister, IpCityLookupUDF}
import org.apache.commons.lang3.SystemUtils
import org.apache.log4j.{Level, Logger}
import org.apache.spark.SparkFiles
import org.apache.spark.sql.{Row, SparkSession}
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite

import java.io.File


class Ip2LocationTest
  extends AnyFunSuite
    with BeforeAndAfterAll
    with EmbeddedHdfsSpark {

  override def beforeAll(): Unit = {
    Logger.getRootLogger().setLevel(Level.ERROR)
    Logger.getLogger("com.c28n").setLevel(Level.INFO)

    if (SystemUtils.IS_OS_WINDOWS)
    // on Windows, use a tmp folder without spaces
      startHdfs(new File("c:\\tmp"))
    else
      startHdfs()

    copyFromLocal("src/it/resources/ips", "/ips")
    val spark: SparkSession = getSpark()
    spark.sparkContext.setLogLevel("ERROR")

    spark.sparkContext.addFile("src/it/resources/GeoLite2-City.mmdb")
  }

  override def afterAll(): Unit = {
    stopHdfs()
  }

  test("Run the ip2location using mapPartition ") {
    implicit val spark = getSpark()
    import spark.implicits._
    val df = getSpark().read.parquet("/ips").repartition(2)
    val sparkFilename = SparkFiles.get("GeoLite2-City.mmdb")
    val dfWithLocation = df.mapPartitions( iterator => {
      val _lookup: Option[IpCityLookup] = Some(new IpCityLookup(new File(sparkFilename), 1000))
      iterator.map( row => {
        val ipLocationShort: Option[IpLocationShort] = IpCityLookupRegister.ip2location(_lookup, row.getString(0))
        (row.getString(0), ipLocationShort)
      })
    })
    dfWithLocation.write.mode("overwrite").parquet("ip_location_with_map_partition")
    val c = dfWithLocation.where("_2.country IS NOT NULL").count()

    assert(df.count() == c)
  }

  test("Run the iplocation using registered function") {
    IpCityLookupRegister.register(getSpark(), "ip2location", "GeoLite2-City.mmdb")
    val df = getSpark().read.parquet("/ips").repartition(2)
    df.createOrReplaceTempView("ips")
    val dfWithLocation = getSpark().sql("""SELECT genericIp, ip2location(genericIp) as location FROM ips""")
    dfWithLocation.write.mode("overwrite").parquet("ip_location_with_udf")
    val c = dfWithLocation.where("location.country IS NOT NULL").count()

    assert(df.count() == c)
  }

  test("Run ip2location on faulty ip") {
    IpCityLookupRegister.register(getSpark(), "ip2location", "GeoLite2-City.mmdb")
    val rows: Array[Row] = getSpark().sql("""SELECT ip, ip2location(ip) as location FROM VALUES (NULL), ("111"), ("192.168.10.3"), ("92.151.171.200"), ("92.173.173.67") AS DATA (ip) ORDER BY ip""").collect
    assert(rows.length == 5)
    assert(rows(0).getString(0) == null)
    assert(rows(0).getAs[IpLocationShort](1) == null)

    assert(rows(1).getString(0) == "111")
    assert(rows(1).getAs[IpLocationShort](1) == null)

    assert(rows(2).getString(0) == "192.168.10.3")
    assert(rows(2).getAs[IpLocationShort](1) == null)

    assert(rows(3).getString(0) == "92.151.171.200")
    assert(rows(3).getAs[Row](1).getString(0) == "Callian")
    assert(rows(3).getAs[Row](1).getString(1) == "PAC")
    assert(rows(3).getAs[Row](1).getString(2) == "FR")
    assert(rows(3).getAs[Row](1).getString(3) == "EU")
    assert(rows(3).getAs[Row](1).getString(4) == "83440")

    assert(rows(4).getString(0) == "92.173.173.67")
    assert(rows(4).getAs[Row](1).getString(0) == "La Seyne-sur-Mer")
    assert(rows(4).getAs[Row](1).getString(1) == "PAC")
    assert(rows(4).getAs[Row](1).getString(2) == "FR")
    assert(rows(4).getAs[Row](1).getString(3) == "EU")
    assert(rows(4).getAs[Row](1).getString(4) == "83500")
  }
}