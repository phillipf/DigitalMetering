package CWW

import geotrellis.vector.Polygon
import spray.json._
import geotrellis.vector.io.json.GeometryFormats.PolygonFormat

/*case class DMAID(ID: Int)

case class DMAFeature(`type`: String,
                      properties: DMAID,
                      geometry: Polygon
                     )

case class name(name: String)

case class crs(`type`: String,
               properties: name)

case class DMASpatial(`type`: String,
               name: String,
               crs: crs,
               features: List[DMAFeature]
              )*/

case class DMAID(ID: Int)

case class DMAFeature(`type`: String,
                      properties: DMAID,
                      geometry: Polygon
                     )

case class DMASpatial(`type`: Int,
               name: String,
               crs: String,
               features: DMAFeature
              )

object DMAGeo extends DefaultJsonProtocol {
  implicit val IDFormat = jsonFormat1(DMAID)
  //implicit val nameFormat = jsonFormat1(name)
  //implicit val crsFormat = jsonFormat2(crs)
  implicit val PlanningFeatureFormat = jsonFormat3(DMAFeature)
  implicit val PlanningFormat = jsonFormat4(DMASpatial)

  implicit object RootCollectionFormat extends RootJsonFormat[rootCollection] {
    def read(value: JsValue) = rootCollection(value.convertTo[List[DMAFeature]])
    def write(f: rootCollection) = JsArray(f.toJson)
  }
}

