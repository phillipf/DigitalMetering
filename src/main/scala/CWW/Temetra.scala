package CWW

import java.io.File

import Utils.Settings

case class Temetra(MMETERNO: String,
                   COLLECTIONMETHOD: String)

object Temetra extends Settings {

  import scala.collection.JavaConversions._

  val data: List[Temetra] = reader.parseAll(new File("N:/DigitalMetering/RDM/temetra-city-west-water-meters-details.csv"))
    .map(values => {
      Temetra(values(11), values(28))
    }).toList

  val temetraMap: Map[String, List[Temetra]] = this.data.groupBy(_.MMETERNO)

  val temetraFutureNetworks =  Array(
    22261,
    23239,
    23481
  )

}
