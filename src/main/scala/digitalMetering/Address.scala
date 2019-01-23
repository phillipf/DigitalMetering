package digitalMetering

import CWW.{Docklands, Location, Richmond}
import geotrellis.vector.Point

case class Address(
                    UNIT_TYPE: Option[String],
                    UNIT_NUMBER: Option[Int],
                    NUM_FIRST: Option[Int],
                    NUM_LAST: Option[Int],
                    ST_NAME: String,
                    SUBURB: String,
                    POSTCODE: String,
                    LONGITUDE: Option[Double],
                    LATITUDE: Option[Double]
                  ) extends Location {

  def ADDRESS = this

  def baseAddress = this.copy(UNIT_TYPE = None, UNIT_NUMBER = None, LONGITUDE = None, LATITUDE = None)

  def unitAddress = this.copy(UNIT_TYPE = None, LONGITUDE = None, LATITUDE = None)

  def clueAddress = this.copy(UNIT_TYPE = None, UNIT_NUMBER = None, POSTCODE = "0000", LONGITUDE = None, LATITUDE = None)

  val streetAddress =
    if(NUM_FIRST.isDefined & NUM_LAST.isDefined) UNIT_TYPE.getOrElse("") + UNIT_NUMBER.getOrElse("") + NUM_FIRST.get + "-" + NUM_LAST.get + ST_NAME + SUBURB + POSTCODE
    else UNIT_TYPE.getOrElse("") + UNIT_NUMBER.getOrElse("") + NUM_FIRST.getOrElse("") + NUM_LAST.getOrElse("") + ST_NAME + SUBURB + POSTCODE


}
