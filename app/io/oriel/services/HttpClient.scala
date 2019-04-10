package io.oriel.services

import java.util.function.BiFunction

import io.oriel.models.WeatherInfo
import java.io

import org.asynchttpclient.Dsl._
import org.asynchttpclient._
import play.api.libs.json.{JsError, JsSuccess, Json}

import scala.concurrent.{ExecutionContext, Future, Promise}

class HttpClient private (client: AsyncHttpClient) {
  import HttpClient.Error

  def requestGET(url: String): Future[Either[Error, String]] = {
    val javaFuture = client.prepareGet(url).execute().toCompletableFuture
    val p: Promise[Either[Error, String]] = Promise()

    javaFuture.handle[Unit](
      new BiFunction[Response, Throwable, Unit] {
        def apply(resp: Response, error: Throwable): Unit = {
          if (error != null) {
            p.failure(error)
          } else if (resp.getStatusCode >= 400) {
            p.success(Left("HTTP Error: " + resp.getStatusCode))
          } else {
            p.success(Right(resp.getResponseBody))
          }
        }
      })

    p.future
  }

  def fetchWeather(location: Location)
    (implicit ec: ExecutionContext): Future[Either[Error, WeatherInfo]] = {

    val response = requestGET(
      "https://api.openweathermap.org/data/2.5/forecast" +
      "?APPID=8a654755117d4187fe196da6cc828b8c" +
      s"&lat=${location.lat}&lon=${location.lon}"
    )

    response.map {
      case Left(error) =>
        Left(error)

      case Right(body) =>
        Json.parse(body).validate[WeatherInfo] match {
          case JsSuccess(obj, _) =>
            Right(obj)
          case JsError(_) =>
            Left("Invalid JSON")
        }
    }
  }

}

object HttpClient {
  /**
    * For readability.
    */
  type Error = String

  /**
    * Builder.
    */
  def apply(): HttpClient = {
    val client: AsyncHttpClient = asyncHttpClient()
    new HttpClient(client)
  }
}
