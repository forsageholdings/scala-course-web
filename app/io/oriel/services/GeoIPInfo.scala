package io.oriel.services

import java.net.InetAddress
import com.maxmind.geoip2.DatabaseReader
import io.oriel.models.Location
import scala.concurrent.Future
import monix.eval.Task

final class GeoIPInfo private (reader: DatabaseReader) {

  def getLocation(ip: String): Task[Option[Location]] = {
    Task {
      for {
        location <- Option(reader.city(InetAddress.getByName(ip)))
        latLon   <- Option(location.getLocation)
        latitude <- Option(latLon.getLatitude)
        long     <- Option(latLon.getLongitude)
      } yield {
        Location(latitude, long)
      }
    }
  }
}

object GeoIPInfo {
  /** Default builder. */
  def apply(): GeoIPInfo = {
    val in = getClass.getResourceAsStream("/GeoLite2/GeoLite2-City.mmdb")
    val ref = new DatabaseReader.Builder(in).build()
    new GeoIPInfo(ref)
  }
}
