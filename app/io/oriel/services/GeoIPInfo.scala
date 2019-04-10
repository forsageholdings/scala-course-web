package io.oriel.services

import java.net.InetAddress
import com.maxmind.geoip2.DatabaseReader
import play.api.libs.json._
import scala.concurrent.{ExecutionContext, Future}

final case class Location(lat: Double, lon: Double)

object Location {
  implicit val writes = Json.format[Location]
}

final class GeoIPInfo private (reader: DatabaseReader) {

  def getLocation(ip: String)
    (implicit ec: ExecutionContext): Future[Option[Location]] = {

    Future {
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
