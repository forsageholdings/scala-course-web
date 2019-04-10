package io.oriel

import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.json.{Format, JsError, JsNumber, JsResult, JsSuccess, JsValue}

package object models {

  implicit val dateTimeFormat: Format[DateTime] =
    new Format[DateTime] {
      def writes(o: DateTime): JsValue =
        JsNumber(o.getMillis)

      def reads(json: JsValue): JsResult[DateTime] =
        json match {
          case JsNumber(value) =>
            JsSuccess(new DateTime(value.toLongExact, DateTimeZone.UTC))
          case _ =>
            JsError("Invalid DateTime")
        }
    }
}
