package CLUE

import CWW.Location
import digitalMetering.Address
import geotrellis.vector.Point
import org.json4s.JsonAST.JObject
import Utils.Cleaners
import play.api.libs.json.Json

case class CLUEFeature(accessibility_rating: Option[Int],
                           accessibility_type: Option[String],
                           accessibility_type_description: Option[String],
                           base_property_id: Option[String],
                           block_id: Option[String],
                           building_height_highest_floor: Option[String],
                           census_year: Option[String],
                           construction_year : Option[String],
                           location : Option[JObject],
                           predominant_space_use: String,
                           property_id: Option[String],
                           refurbished_year : Option[String],
                           street_address : String,
                           suburb: String,
                           x_coordinate : Option[String],
                           y_coordinate : Option[String]
) extends Cleaners {

  override val numPattern = """(\d+)""".r
  val streetNum = numPattern.findAllIn(street_address).matchData
  val numPattern2 = """\d+|-""".r
  val streetName = numPattern2.replaceAllIn(street_address, "").trim

  val (numFirst, numLast): (Option[Int], Option[Int]) = {
    if(streetNum.nonEmpty) {
      val first = streetNum.next()
      if (streetNum.hasNext) (Some(first.toString.toInt), Some(streetNum.next.toString.toInt))
      else (Some(first.toString.toInt), None)
    }
    else (None, None)
  }

  val subPattern = """([a-zA-Z\s]+)""".r
  val suburbClean = subPattern.findFirstIn(this.suburb).get.trim

  val address: Address = Address(None, None, numFirst, numLast, streetName.toUpperCase, suburbClean.toUpperCase, "0000", tryToDbl(x_coordinate.getOrElse("NA")), tryToDbl(y_coordinate.getOrElse("NA")))

  val property: clueProperty = clueProperty(property_id, predominant_space_use, address, address.getclueGNAF)

  //val property = clueProperty(property_id, predominant_space_use, street_address, suburb, x_coordinate, y_coordinate)

}

/*object CLUEFeature {

  implicit val FeatureFormat = Json.format[CLUEFeature]

}*/
