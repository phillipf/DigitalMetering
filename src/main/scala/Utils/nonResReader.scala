package Utils

import java.io.File

import CWW.nonRes
import digitalMetering.Address

import scala.collection.JavaConversions._

class nonResReader (pathname: String) extends Cleaners with Settings {

  /*val settings = new CsvParserSettings()
  settings.setNumberOfRowsToSkip(1)
  val reader = new CsvParser(settings)*/

  /*val nonResData: List[nonRes] = reader.parseAll(new File(pathname))
    .map(values => {
      val installNo = values(1).toInt
      val NumClean = tryToString(values(4)).fold("NA")(cleanHouseNumber)
      val First = numFirst(NumClean)
      val Last = numLast(NumClean)
      val unitType = cleanUnitType(tryToString(values(2)))
      val unitNumber = cleanUnitNumber(tryToString(values(3)))


      nonRes(values(0),
             installNo,
             Address(unitType, unitNumber, First, Last, values(5), values(6), values(7), None, None))
    }).toList*/
}