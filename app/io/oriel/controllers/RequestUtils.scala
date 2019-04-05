package io.oriel.controllers

import java.net.InetAddress
import play.api.mvc.Request
import scala.util.control.NonFatal

object RequestUtils {
  /**
    * Extracts the public IP from an HTTP request, by parsing
    * the `X-Forwarded-For` header.
    */
  def getRealIP(request: Request[_]): Option[String] =
    request.headers.get("X-Forwarded-For") match {
      case Some(header) =>
        header.split("\\s*,\\s*").find(validatePublicIP) match {
          case ip @ Some(_) =>
            ip
          case None =>
            remoteAddress(request)
        }
      case None =>
        remoteAddress(request)
    }

  private def remoteAddress(request: Request[_]): Option[String] =
    Option(request.remoteAddress).filter(validateIPRef)

  /**
    * Returns `true` if the given IP address is a real and public IP
    * address — e.g. `127.0.0.1` isn't allowed.
    */
  def validatePublicIP(ip: String): Boolean = {
    try {
      val parsed = InetAddress.getByName(ip)
      !(parsed.isLoopbackAddress || parsed.isSiteLocalAddress)
    } catch {
      case NonFatal(_) => false
    }
  }

  private[this] val validateIPRef = validatePublicIP _
}
