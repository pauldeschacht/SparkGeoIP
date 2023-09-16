package com.c28n.datective

import com.c28n.datective.IpCityLookupUDF.IpLocationShort
import org.scalatest.funsuite.AnyFunSuite

import java.io.File

class IpCityLookupTest
  extends AnyFunSuite {

  val expectedCity: Option[IpLocationShort] = Some(IpLocationShort(city = Some("Callian"), state = Some("PAC"), country = Some("FR"), continent = Some("EU")))
  test ("find ip addresses without cache") {

    val ipCityLookup = new IpCityLookup(new File("src/it/resources/GeoLite2-City.mmdb"), cacheSize = 0L)
    assert(ipCityLookup.lookup("92.151.171.200").get.getCity.getNames.get("en") == "Callian")
    assert(ipCityLookup.lookup(null) == None)
    assert(ipCityLookup.lookup("111") == None)
    assert(ipCityLookup.lookup("92.151.171.200").get.getCity.getNames.get("en") == "Callian")

  }
  test ("find ip addresses with cache") {
    val ipCityLookup = new IpCityLookup(new File("src/it/resources/GeoLite2-City.mmdb"), cacheSize = 1000L)
    assert(ipCityLookup.lookup("92.151.171.200").get.getCity.getNames.get("en") == "Callian")
    assert(ipCityLookup.lookup(null) == None)
    assert(ipCityLookup.lookup("111") == None)
    assert(ipCityLookup.lookup("92.151.171.200").get.getCity.getNames.get("en") == "Callian")
  }
}
