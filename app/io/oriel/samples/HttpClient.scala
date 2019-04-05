package io.oriel.samples

import java.util.function.BiFunction
import org.asynchttpclient.Dsl._
import org.asynchttpclient.Response

/**
  * [[https://github.com/AsyncHttpClient/async-http-client]]
  */
class HttpClient {

  private val client = asyncHttpClient()

  def get(url: String): Unit = {
    val javaFuture = client.prepareGet(url).execute().toCompletableFuture

    javaFuture.handle(new BiFunction[Response, Throwable, Unit] {
      def apply(resp: Response, error: Throwable): Unit = {
        println(s"HTTP ${resp.getStatusCode}")
        println()
        println(resp.getResponseBody)
      }
    })
  }

  /**
    * Docs: [[https://openweathermap.org/current]]
    */
  def fetchWeather(): Unit = {
    get("https://samples.openweathermap.org/data/2.5/forecast?APPID=8a654755117d4187fe196da6cc828b8c&lat=44.4333&lon=26.1")
  }
}
