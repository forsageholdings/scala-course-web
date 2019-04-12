package io.oriel.models

import play.api.libs.json.Json

case class LookupResponse(
  geoIP: Location,
  weather: WeatherInfo
)

object LookupResponse {
  implicit val format = Json.format[LookupResponse]
}
