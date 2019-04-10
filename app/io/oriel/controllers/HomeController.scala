package io.oriel.controllers

import io.oriel.models.LookupResponse
import io.oriel.services.{GeoIPInfo, HttpClient}
import javax.inject._
import play.api._
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.Future

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
  import scala.concurrent.ExecutionContext.Implicits.global
  private val geoIP = GeoIPInfo()
  private val httpClient = HttpClient()

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index() = Action { implicit request: Request[AnyContent] =>
    Ok(io.oriel.views.html.index())
  }

  def lookup = Action.async { request =>
    val ipOpt: Option[String] =
      request.getQueryString("ip")
        .orElse(RequestUtils.getRealIP(request))

    ipOpt match {
      case None =>
        Future.successful(NotFound("Missing IP"))

      case Some(ip) =>
        geoIP.getLocation(ip).flatMap {
          case None =>
            Future.successful(NotFound("Unknown or invalid IP"))

          case Some(location) =>
            httpClient.fetchWeather(location).map {
              case Left(error) =>
                ServiceUnavailable(error)

              case Right(info) =>
                Ok(Json.toJson(
                  LookupResponse(location, info)
                ))
            }
        }
    }
  }
}
