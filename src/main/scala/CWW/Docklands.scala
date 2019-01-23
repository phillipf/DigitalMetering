package CWW

import CWW.Richmond.bounds

import scala.collection.mutable.ListBuffer
import spray.json._


object Docklands extends DMA {

  def values = Gentrack.docklands
  def ageSorted = values.sortBy(x => -x.METER_TYPE.AGE.getOrElse(0))

  def pf: PartialFunction[DMA, List[R]] = {
    {
      case `Docklands` => {

        def loop(in: List[G] = this.ageSorted,
                 res: ListBuffer[R] = new ListBuffer[R]()): List[R] = in match {
          case Nil => res.toList
          case x::xtail =>
            if(!x.hasExistingDevice & x.METER_TYPE.NBIoTDM) loop(xtail,  res += x.toRollout(x => "DM-N"))
            else loop(xtail, res += x.toRollout(_.existingDevice.getOrElse("PLUG-IN")))
        }

        loop()
      }
    }
  }

  import DMAGeo._

  val input = scala.io.Source.fromFile("Y:\\Strategic Projects\\Intelligent Networks Assets\\Digital Metering\\Stage 1 E2E POC Trial\\WS 1.1 OT\\spatial\\DOCK_DMA.geojson")("UTF-8").mkString.parseJson
  val JsonDocklands = input.asJsObject().fields("features")

  val bounds = JsonDocklands.toJson.convertTo[rootCollection].items.head.geometry
  //val bounds = boundsUTM.items.head.geometry.reproject(tranformer)

}
