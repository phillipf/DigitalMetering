package vicMaps

import spray.json._

import geotrellis.vector.io.json.GeometryFormats.PolygonFormat

object planningJsonProtocol extends DefaultJsonProtocol {
  implicit val PlanningFeatureFormat = jsonFormat12(planningData)
  implicit val PlanningFormat = jsonFormat3(planningFeature)

  implicit object RootCollectionFormat extends RootJsonFormat[rootCollection] {
    def read(value: JsValue) = rootCollection(value.convertTo[List[planningFeature]])
    def write(f: rootCollection) = JsArray(f.toJson)
  }
}