package CWW

import digitalMetering.Address
import geotrellis.proj4

import scala.collection.mutable.ListBuffer
import spray.json._

object Richmond extends DMA {

  def values = Gentrack.richmond
  def toLoRaDM = nonPulse.map(_.toRollout(x => "DM-L"))
  def ageSorted = (values diff nonPulse).sortBy(x => -x.METER_TYPE.AGE.getOrElse(0))

  def pf: PartialFunction[DMA, List[R]] = {
    var LoRacounter = 520;
    {
      case `Richmond` => {
        val nonPulse = this.toLoRaDM
        LoRacounter = LoRacounter - nonPulse.size

        def loop(in: List[G] = this.ageSorted,
                 counter: Int = LoRacounter,
                 res: ListBuffer[R] = nonPulse.to[ListBuffer]): List[R] = in match {
          case Nil => res.toList
          case x::xtail =>
            if(counter > 0)
              if(!x.hasExistingDevice & x.METER_TYPE.LoRaDM) loop(xtail, counter - 1, res += x.toRollout(x => "DM-L"))
              else loop(xtail, counter, res += x.toRollout(_.existingDevice.getOrElse("PLUG-IN")))
            else loop(xtail, counter, res += x.toRollout(_.existingDevice.getOrElse("PLUG-IN")))
        }

        loop()
      }
    }
  }

  import DMAGeo._

  val input = scala.io.Source.fromFile("Y:\\Strategic Projects\\Intelligent Networks Assets\\Digital Metering\\Stage 1 E2E POC Trial\\WS 1.1 OT\\spatial\\RICH_DMA.geojson")("UTF-8").mkString.parseJson
  //val planningJsonRichmond = inputRichmond.asJsObject().fields("features")

  val JsonRichmond = input.asJsObject().fields("features")

  //val planningGeoRichmond = planningJsonRichmond.toJson.convertTo[rootCollection]

  val bounds = JsonRichmond.toJson.convertTo[rootCollection].items.head.geometry
  //val bounds = boundsUTM.items.head.geometry.reproject(tranformer)

}