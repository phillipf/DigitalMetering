package vicMaps

import spray.json._
import planningJsonProtocol._

object planningGeo {

  val inputRichmond = scala.io.Source.fromFile("N:\\DigitalMetering\\RDM\\Vicmaps\\richmondDMA_3121\\planZoneRichmond.geojson")("UTF-8").mkString.parseJson

  val inputDocklands = scala.io.Source.fromFile("N:\\DigitalMetering\\RDM\\Vicmaps\\docklandsDMA_3008\\planZoneDocklands.geojson")("UTF-8").mkString.parseJson

  //val input = inputRichmond. ++ inputDocklands

  val planningJsonRichmond = inputRichmond.asJsObject().fields("features")

  val planningJsonDocklands = inputDocklands.asJsObject().fields("features")

  val planningGeoRichmond = planningJsonRichmond.toJson.convertTo[rootCollection]

  val planningGeoDocklands = planningJsonDocklands.toJson.convertTo[rootCollection]

  val planningGeo = planningGeoRichmond ++ planningGeoDocklands

}
