package CWW

import java.io.File

import GNAF.gnafData.addressZone
import GNAF.gnafPID
import Utils.{Cleaners, Settings}
import digitalMetering.{Address, Business}

case class nonRes(MMETERNO: String,
                  INSTALLNO: Int,
                  RESNRES: String,
                  ADDRESS: Address) extends Id with Location {

  def toBusiness: Business = {

    if(isEmis) {
      val (anszicCode, anszicDescription) = getEmis

      if(isGNAF()) {
        Business(MMETERNO, INSTALLNO, RESNRES, getGNAF, ADDRESS, anszicCode, anszicDescription, "EMIS")
      }
      else Business(MMETERNO, INSTALLNO, RESNRES, gnafPID("NA"), ADDRESS, anszicCode, anszicDescription, "EMIS")
    }

    else if(isclueGNAF & isCLUE) {
      val gnaf = getclueGNAF
      Business(MMETERNO, INSTALLNO, RESNRES, gnaf, ADDRESS, 0, getCLUE.ANSZIC_DESCRIPTION, "CLUE")
    }

    else if(isGNAF()) {
      val gnaf = getGNAF
      if (isABR) {
        Business(MMETERNO, INSTALLNO, RESNRES, gnaf, ADDRESS, getABR.ANSZIC_CODE, getABR.ANSZIC_DESCRIPTION, "ABR")
      }
      else Business(MMETERNO, INSTALLNO, RESNRES, gnaf, ADDRESS, 0, "NA", "NA")
    }

    else Business(MMETERNO, INSTALLNO, RESNRES, gnafPID("NA"), ADDRESS, 0, "NA", "NA")
  }

  def toZone: (String, Map[String, String]) = {

    if(isGNAF()) {
      val gnaf = getGNAF
      addressZone(gnafPID(gnaf.GNAFPID))
    }

    else ("NA", Map("NA" -> "NA"))
  }
}

/*object nonRes extends Cleaners with Settings {

  import scala.collection.JavaConversions._

  val data = reader.parseAll(new File("N:/DigitalMetering/RDM/NonResAddresses.csv"))
    .map(values => {
      val installNo = values(1).toInt
      val installNo = values(1).toInt
      val NumClean = tryToString(values(4)).fold("NA")(cleanHouseNumber)
      val First = numFirst(NumClean)
      val Last = numLast(NumClean)
      val unitType = cleanUnitType(tryToString(values(2)))
      val unitNumber = cleanUnitNumber(tryToString(values(3)))


      nonRes(values(0),
        installNo,
        RESNRES,
        Address(unitType, unitNumber, First, Last, values(5), values(6), values(7), None, None))
    }).toList

}*/