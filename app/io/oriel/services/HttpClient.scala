package io.oriel.services

import java.util.function.BiFunction

import io.oriel.models._
import monix.eval.Task
import org.asynchttpclient.Dsl._
import org.asynchttpclient._
import play.api.libs.json.{JsError, JsSuccess, Json}

class HttpClient private (client: AsyncHttpClient) {
  import HttpClient.HttpError

  def requestGET(url: String): Task[Either[HttpError, String]] = {
    Task.cancelable { cb =>
      val javaFuture = client.prepareGet(url).execute().toCompletableFuture

      javaFuture.handle[Unit](
        new BiFunction[Response, Throwable, Unit] {
          def apply(resp: Response, error: Throwable): Unit = {
            if (error != null) {
              cb.onError(error)
            } else if (resp.getStatusCode >= 400) {
              cb.onSuccess(Left("HTTP Error: " + resp.getStatusCode))
            } else {
              cb.onSuccess(Right(resp.getResponseBody))
            }
          }
        })

      // Logic for cancellation, to be used in race conditions
      Task(javaFuture.cancel(false))
    }
  }

  def fetchWeather(location: Location): Task[Either[HttpError, WeatherInfo]] = {
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
  type HttpError = String

  /**
    * Builder.
    */
  def apply(): HttpClient = {
    val client: AsyncHttpClient = asyncHttpClient()
    new HttpClient(client)
  }
}
