package Utils

import com.univocity.parsers.csv.{CsvParser, CsvParserSettings}

trait Settings {

  val settings = new CsvParserSettings()
  settings.setNumberOfRowsToSkip(1)
  val reader = new CsvParser(settings)

}
