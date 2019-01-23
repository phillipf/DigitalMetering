package CLUE

import GNAF.gnafPID
import digitalMetering.Address
import play.api.libs.json.{Json, Writes}

case class clueProperty(property_id: Option[String],
                        predominant_space_use: String,
                        address : Address,
                        gnafPID: gnafPID) {

}

object clueProperty {
  implicit val PIDFormat = Json.format[gnafPID]
  implicit val AddressFormat = Json.format[Address]
  implicit val CLUEFormat = Json.format[clueProperty]
}