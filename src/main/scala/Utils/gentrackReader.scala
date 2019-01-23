package Utils

import java.io.File
import java.util.Locale

import ABR.lucene.luceneBusiness
import CWW.{Gentrack, meterType}
import com.univocity.parsers.csv.{CsvParser, CsvParserSettings}
import digitalMetering.Address
import org.joda.time.DateTime.now
import org.joda.time.Days
import org.joda.time.format.DateTimeFormat

import scala.collection.JavaConversions._
import scala.util.Try
import scala.util.matching.Regex

class gentrackReader(pathname: String) extends Cleaners with Settings {

  /*val settings = new CsvParserSettings()
  settings.setNumberOfRowsToSkip(1)
  val reader = new CsvParser(settings)*/

  val gentrackData: List[Gentrack] = reader.parseAll(new File(pathname))
    .map(values => {
      val installNo = values(14).toInt
      val NumClean = tryToString(values(18)).fold("NA")(cleanHouseNumber)
      val First = numFirst(NumClean)
      val Last = numLast(NumClean)
      val unitType = cleanUnitType(tryToString(values(16)))
      val unitNumber = cleanUnitNumber(tryToString(values(17)))
      val Age = tryToInt((age(values(30) + " 12:00:00") / 365).toString)
      val Size = values(27).toInt
      val meterLocation = values(11)
      val METER_SEAL = values(2)

      Gentrack(values(0),
        //luceneBusiness(installNo, values(38)),
        installNo, tryToInt(values(5)), values(3), tryToInt(values(49)), values(50),
        Address(unitType, unitNumber, First, Last, values(19), values(20), values(21), None, None),
        meterType(Size, tryToString(values(28)), tryToString(values(29)), Age),
        METER_SEAL,
        values(45),
        meterLocation)
    }).toList


}
