package CWW

import java.io.File

import ABR.lucene.luceneBusiness
import Utils.{Cleaners, Settings}
import digitalMetering._
import GNAF.gnafData.addressZone
import GNAF.gnafPID


case class Gentrack (
                      MMETERNO: String,
                      //CUSTOMER: luceneBusiness,
                      INSTALLNO: Int,
                      DEVICENO: Option[Int],
                      RESNRES: String,
                      IMETNETWORK: Option[Int],
                      NETWK_FLAG: String,
                      ADDRESS: Address,
                      METER_TYPE: meterType,
                      METER_SEAL: String,
                      BILLSEQ: String,
                      METER_LOCATION: String
                    ) extends Meter {

  type Meters = List[Gentrack]

  type toReplacement = Gentrack => String

  def isbig[Gentrack](G: Gentrack = this) = METER_TYPE.MSIZE
  def map[Gentrack, Rollout](G: Gentrack)(implicit f: Gentrack => Int = isbig _) = f(G)
  //val x = Gentrack.isbig _
  //def map2[Gentrack, Rollout](G: Gentrack)(implicit f: Gentrack => Rollout): Rollout = f(G)
  //def r: Rollout = Rollout(this.MMETERNO, this.INSTALLNO, this.DEVICENO, this.IMETNETWORK, this.NETWK_FLAG, this.ADDRESS, this.METER_TYPE, this.BILLSEQ, dlDate, exDate, "aaa")

  val (dlDate, exDate) = readingRound.roundsMap.getOrElse(this.BILLSEQ.toInt, ("NA", "NA"))

  def toRollout(f: toReplacement) = Rollout(this.MMETERNO, this.INSTALLNO, this.DEVICENO, this.RESNRES ,this.IMETNETWORK, this.NETWK_FLAG, this.ADDRESS, this.METER_TYPE, this.METER_LOCATION, this.BILLSEQ, dlDate, exDate, f(this))

  def toBusiness: Business = {

    if(isEmis) {
      val (anszicCode, anszicDescription) = getEmis

      if(isGNAF()) {
        Business(MMETERNO, INSTALLNO, RESNRES, getGNAF, ADDRESS, anszicCode, anszicDescription, "EMIS")
      }
      else Business(MMETERNO, INSTALLNO, RESNRES, gnafPID("NA"), ADDRESS, anszicCode, anszicDescription, "EMIS")
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

object Gentrack extends Cleaners with Settings {

  import scala.collection.JavaConversions._

  /*def dataReader(pathname:String) = {
    reader.parseAll(new File(pathname))
      .map(values => {
        val installNo = values(14).toInt
        val NumClean = tryToString(values(18)).fold("NA")(cleanHouseNumber)
        val First = numFirst(NumClean)
        val Last = numLast(NumClean)
        val unitType = cleanUnitType(tryToString(values(16)))
        val unitNumber = cleanUnitNumber(tryToString(values(17)))
        val Age = age(values(30) + " 12:00:00") / 365
        val Size = values(27).toInt
        val meterLocation = values(11)

        Gentrack(values(0),
          //luceneBusiness(installNo, values(38)),
          installNo, tryToInt(values(5)), tryToInt(values(49)), values(50),
          Address(unitType, unitNumber, First, Last, values(19), values(20), values(21), None, None),
          meterType(Size, values(28), values(29), Age),
          values(45).toInt,
          meterLocation
        )
      }).toList
  }*/

  def dataReader(pathname:String) = {
    reader.parseAll(new File(pathname))
      .map(values => {

        //val NumClean = tryToString(values(7)).fold("NA")(cleanHouseNumber)

        val MMETERNO = values(0)
        val INSTALLNO = values(1).toInt
        val METER_SEAL = values(2)
        val RESNRES = values(3)
        val DEVICENO = tryToInt(values(4))
        val IMETNETWORK = tryToInt(values(5))
        val NETWK_FLAG = values(6)
        val UNIT_TYPE = cleanUnitType(tryToString(values(7)))
        val UNIT_NUMBER = cleanUnitNumber(tryToString(values(8)))
        val NUM_FIRST = tryToInt(values(9))
        val NUM_LAST = tryToInt(values(10))
        val ST_NAME = values(11)
        val SUBURB = values(12)
        val POSTCODE = values(13)
        val x = tryToDbl(values(14))
        val y = tryToDbl(values(15))
        val MSIZE = values(16).toInt
        val MODEL = tryToString(values(17))
        val MANUFACTURER = tryToString(values(18))
        val AGE = tryToInt(values(19))
        val BILLSEQ = values(20).toInt
        val LOCATION = values(21)

        Gentrack(MMETERNO,
          //luceneBusiness(installNo, values(38)),
          INSTALLNO, DEVICENO, RESNRES, IMETNETWORK, NETWK_FLAG,
          Address(UNIT_TYPE, UNIT_NUMBER, NUM_FIRST, NUM_LAST, ST_NAME, SUBURB, POSTCODE, x, y),
          meterType(MSIZE, MODEL, MANUFACTURER, AGE),
          METER_SEAL,
          f"$BILLSEQ%05d",
          LOCATION
        )
      }).toList
  }

  val raw: List[Gentrack] = dataReader("N:/DigitalMetering/RDM/All_meters_100119.csv")
  val processed = raw.map(record => record.copy(METER_TYPE = record.METER_TYPE.meterTypeCleanser))
  //val docklands: List[Gentrack] = dataReader("N:/DigitalMetering/RDM/Docklands Sth DMA latest data 21st Nov(original).csv")
  //val richmond: List[Gentrack] = dataReader("N:/DigitalMetering/RDM/Church St DMA latest data 21st Nov(original).csv")
  val docklands: List[Gentrack] = processed.filter(x => x.METER_SEAL == "DOCK_DMA")
  val richmond: List[Gentrack] = processed.filter(x => x.METER_SEAL == "RICH_DMA")
}
