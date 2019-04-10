package io.oriel.models

import org.joda.time.DateTime
import play.api.libs.json.Json

final case class WeatherInfo(
  list: List[WeatherSegment]
)

final case class WeatherSegment(
  dt: DateTime,
  main: WeatherMain,
  weather: List[WeatherSegmentInfo]
)

final case class WeatherMain(
  temp: Double,
  temp_min: Double,
  temp_max: Double
)

final case class WeatherSegmentInfo(
  id: Int,
  main: String,
  description: String
)

// Watch for ordering

object WeatherSegmentInfo {
  implicit val jsonFormat = Json.format[WeatherSegmentInfo]
}

object WeatherMain {
  implicit val jsonFormat = Json.format[WeatherMain]
}

object WeatherSegment {
  implicit val jsonFormat = Json.format[WeatherSegment]
}

object WeatherInfo {
  implicit val jsonFormat = Json.format[WeatherInfo]
}
