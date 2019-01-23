package ABR

import java.io.File

import com.univocity.parsers.csv.{CsvParser, CsvParserSettings}

import scala.collection.JavaConversions._
import scala.util.Try


class businessReader(pathname: String) {

  val settings = new CsvParserSettings()
  settings.setNumberOfRowsToSkip(1)
  val reader = new CsvParser(settings)

  //type rawBusiness = Vector[String]

  val asicBusinessData: List[rawBusiness] = reader.parseAll(new File(pathname))
    //.filterNot(values => Try(values(8).toInt).toOption.isEmpty)
    .map(values => {
      val ABN = Try(values(8).toInt).toOption
      //val ABN = values(8)
    //Vector(ABN, values(1))
    rawBusiness(ABN, values(1))
    }).toList


}
