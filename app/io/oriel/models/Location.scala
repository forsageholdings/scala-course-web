package io.oriel.models

import play.api.libs.json.Json

final case class Location(lat: Double, lon: Double)

object Location {
  implicit val writes = Json.format[Location]
}
