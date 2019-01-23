package digitalMetering

import java.io.{BufferedWriter, FileOutputStream, OutputStreamWriter}

import CWW.{Docklands, Location, Richmond}
import GNAF.{gnafData, gnafPID}

case class Spatial (
                     MMETERNO: String,
                     INSTALLNO: Int,
                     ADDRESS: Address,
                     REPLACEMENT: String
                   ) extends Location {

}

object Spatial {
  val spatialLayer = Richmond.toSpatial ++ Docklands.toSpatial


  def spatialWriter(file: String, output: Map[String, (Address, String, Boolean)]) {
    val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))

    writer.write("MMETERNO| UNIT_TYPE| UNIT_NUMBER| NUMFIRST| NUMLAST| STNAME| SUBURB| POSTCODE| LATITUDE| LONGITUDE| DEVICE_TYPE| IN_DMA")

    writer.newLine()
    for (x <- output) {

      writer.write(
        x._1 + "|" +
          x._2._1.ADDRESS.UNIT_TYPE.getOrElse("NA") + "|" +
          x._2._1.ADDRESS.UNIT_NUMBER.getOrElse("NA") + "|" +
          x._2._1.ADDRESS.NUM_FIRST.getOrElse("NA") + "|" +
          x._2._1.ADDRESS.NUM_LAST.getOrElse("NA") + "|" +
          x._2._1.ADDRESS.ST_NAME + "|" +
          x._2._1.ADDRESS.SUBURB + "|" +
          x._2._1.ADDRESS.POSTCODE + "|" +
          x._2._1.ADDRESS.LATITUDE.getOrElse("NA") + "|" +
          x._2._1.ADDRESS.LONGITUDE.getOrElse("NA") + "|" +
          x._2._2 + "|" +
          x._2._3 + "\n")
      //x.REPLACEMENT + "\n")
    }// however you want to format it

    writer.close()
  }

  def main(args: Array[String]): Unit = {
    spatialWriter("N:/DigitalMetering/RDM/scala/rolloutSpatial.psv", spatialLayer)

  }

}
