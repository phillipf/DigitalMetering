package Utils

import java.io.File

import CWW.Emis
import com.univocity.parsers.csv.{CsvParser, CsvParserSettings}

import scala.collection.JavaConversions._

class emisReader(pathname: String) extends Cleaners {

  val settings = new CsvParserSettings()
  settings.setNumberOfRowsToSkip(7)
  val reader = new CsvParser(settings)

  val emisData: List[Emis] = reader.parseAll(new File(pathname))
    .map(values => {
      val consumerNo = values(0).toLong
      val installNo = values(3).toLong
      val (anszicCode, anszicIndustry) = Emis.anszic(values(14))


      Emis(consumerNo,
          installNo,
          anszicCode,
          anszicIndustry)
    }).toList

}
