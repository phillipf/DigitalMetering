package vicMaps

import geotrellis.vector.Polygon

case class planningFeature(`type`: String,
                           properties: planningData,
                           geometry: Polygon
                          ) {

}
