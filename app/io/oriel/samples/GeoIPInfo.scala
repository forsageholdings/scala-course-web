package io.oriel.samples

import java.net.InetAddress

import com.maxmind.geoip2.DatabaseReader

class GeoIPInfo {

  private val client = {
    val in = getClass.getResourceAsStream("/GeoLite2/GeoLite2-City.mmdb")
    new DatabaseReader.Builder(in).build()
  }

  def queryLocation(ip: String): Unit = {
    val address = InetAddress.getByName(ip)
    val loc = client.city(address).getLocation

    if (loc == null) {
      println("No location")
    } else {
      println(s"Location — latitude: ${loc.getLatitude}, longitude: ${loc.getLongitude}")
    }
  }
}
