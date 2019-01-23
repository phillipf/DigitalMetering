package digitalMetering

import java.io.{BufferedWriter, FileOutputStream, OutputStreamWriter}
import java.util.Date

import CWW.{Docklands, Meter, Richmond, meterType}

case class Rollout (
                     MMETERNO: String,
                     INSTALLNO: Int,
                     DEVICENO: Option[Int],
                     RESNRES: String,
                     IMETNETWORK: Option[Int],
                     NETWK_FLAG: String,
                     ADDRESS: Address,
                     METER_TYPE: meterType,
                     METER_LOCATION: String,
                     BILLSEQ: String,
                     DOWNLOAD_DATE: String,
                     EXCEPTION_DATE: String,
                     REPLACEMENT: String
                   ) extends Meter

object Rollout {

  val richmondRollout = Richmond.toRollout
  val docklandsRollout = Docklands.toRollout
  val plan = richmondRollout ++ docklandsRollout

  def rolloutWriter(file: String, output: List[Rollout]) {
    val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))

    writer.write("MMETERNO| INSTALLNO| DEVICENO| RESNRES| IMETNETWORK| NETWK_FLAG| UNIT_TYPE| UNIT_NUMBER| NUMFIRST| NUMLAST| STNAME| SUBURB| POSTCODE| MISZE| MODEL| MANUFACTURER| MANUFACTURER-MODEL| METER_LOCATION| AGE| BILLSEQ| DL_DATE| EX_DATE| DEVICE_TYPE")

    writer.newLine()
    for (x <- output) {

      writer.write(
        x.MMETERNO + "|" +
          x.INSTALLNO + "|" +
          x.DEVICENO.getOrElse("NA") + "|" +
          x.RESNRES + "|" +
          x.IMETNETWORK.getOrElse("NA") + "|" +
          x.NETWK_FLAG + "|" +
          x.ADDRESS.UNIT_TYPE.getOrElse("NA") + "|" +
          x.ADDRESS.UNIT_NUMBER.getOrElse("NA") + "|" +
          x.ADDRESS.NUM_FIRST.getOrElse("NA") + "|" +
          x.ADDRESS.NUM_LAST.getOrElse("NA") + "|" +
          x.ADDRESS.ST_NAME + "|" +
          x.ADDRESS.SUBURB + "|" +
          x.ADDRESS.POSTCODE + "|" +
          x.METER_TYPE.MSIZE + "|" +
          x.METER_TYPE.MODEL.getOrElse("NA") + "|" +
          x.METER_TYPE.MANUFACTURER.getOrElse("NA") + "|" +
          x.METER_TYPE.MANUFACTURER.getOrElse("NA") + "-" + x.METER_TYPE.MODEL.getOrElse("NA") + "|" +
          x.METER_LOCATION + "|" +
          x.METER_TYPE.AGE.getOrElse("NA") + "|" +
          x.BILLSEQ + "|" +
          x.DOWNLOAD_DATE  + "|" +
          x.EXCEPTION_DATE + "|" +
          x.REPLACEMENT + "\n")
    }// however you want to format it

    writer.close()
  }

  def main(args: Array[String]): Unit = {
    rolloutWriter("N:/DigitalMetering/RDM/scala/docklandsRollout.psv", docklandsRollout)
    rolloutWriter("N:/DigitalMetering/RDM/scala/richmondRollout.psv", richmondRollout)
    rolloutWriter("N:/DigitalMetering/RDM/scala/totalRollout.psv", plan)

  }

}