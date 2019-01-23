package digitalMetering

import java.io.{BufferedWriter, FileOutputStream, OutputStreamWriter}

import CWW.{Gentrack, nonRes}
import GNAF.gnafPID

case class Business(MMETERNO: String,
                    INSTALLNO: Int,
                    RESNRES: String,
                    GNAF_PID: gnafPID,
                    ADDRESS: Address,
                    ANSZIC_CODE: Int,
                    ANSZIC_DESCRIPTION: String,
                    SOURCE: String) {
}

object Business {

  def businessWriter(file: String, output: List[(Business, (String, Map[String, String]), String)]) {
    val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))

    writer.write("MMETERNO | INSTALLNO | RESNRES | GNAF_PID | PRIMARY_SECONDARY | ZONE_CODE | ZONE_GROUP | ANSZIC_CODE | ANSZIC_DESCRIPTION | SOURCE")

    writer.newLine()
    for (x <- output) {
      val business = x._1
      val zoneGroup =  if(x._2._2.nonEmpty) x._2._2.head._2 else "NA"
      val zoneCode = x._2._1
      writer.write(
        business.MMETERNO + "|" +
          business.INSTALLNO + "|" +
          business.RESNRES + "|" +
          business.GNAF_PID.GNAFPID + "|" +
          x._3 + "|" +
          zoneCode + "|" +
          zoneGroup + "|" +
          business.ANSZIC_CODE + "|" +
          business.ANSZIC_DESCRIPTION + "|" +
          business.SOURCE  + "\n")
    }// however you want to format it

    writer.close()
  }

  def main(args: Array[String]): Unit = {

    //val nonResdata = nonRes.data
    val nonResdata = Gentrack.processed
    val nonResABR = nonResdata.map(x => (x.toBusiness, x.toZone, x.primarySecondary))

    businessWriter("N:/DigitalMetering/RDM/scala/nonResABR.psv", nonResABR)
  }

}
