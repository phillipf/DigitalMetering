package digitalMetering

import org.apache.commons.csv._
import org.apache.commons.csv.CSVPrinter

import java.io.FileReader
import java.io.FileWriter

import scala.collection.JavaConverters._

object Footscray {

  val file = new FileReader("C:\\Users\\farrelp1\\Documents\\DigitalMetering\\data\\Yarraville.csv")

  val uniMelbformat = CSVFormat.RFC4180.withSkipHeaderRecord(true).withIgnoreEmptyLines(true)

  val parser = CSVParser.parse(file, uniMelbformat)

  val rows = parser.getRecords.asScala


  // val items = rows.iterator().dropWhile(_.iterator().next().isEmpty())
  val items = rows.drop(1)

  def collectRecords(x: CSVRecord) = {
    val install = x.get(3)
    val name = x.get(7)
    val add = x.get(6)
    //val values = x.iterator()
    //val timestamp = values.next()
    //val flow = values.next()
    (install, add, name)

  }

  val records = items.map(r => collectRecords(r)).toSeq

  val file2 = new FileReader("C:\\Users\\farrelp1\\Desktop\\Contacts.csv")

  //val uniMelbformat = CSVFormat.RFC4180.withSkipHeaderRecord(true).withIgnoreEmptyLines(true)

  val parser2 = CSVParser.parse(file2, uniMelbformat)

  val rows2 = parser2.getRecords.asScala

  // val items = rows.iterator().dropWhile(_.iterator().next().isEmpty())
  val items2 = rows2.drop(1)

  def collectRecords2(x: CSVRecord) = {
    val fname = x.get(1)
    val lname = x.get(3)
    val acname = x.get(1).trim.charAt(0) + " " + x.get(3).trim.toUpperCase
    //val add = x.get(6)
    //val values = x.iterator()
    //val timestamp = values.next()
    //val flow = values.next()
    (fname, lname, acname)

  }

  val records2 = items2.map(r => collectRecords2(r)).distinct

  val contacts = records2.flatMap(x =>
    records.filter(y => y._2.matches(".*[MR|MS|MRS] " + x._3 + ".*"))
           .map(y => (x._1, x._2, y._1, y._3))
  )

  val fileWriter = new FileWriter("C:\\Users\\farrelp1\\Desktop\\CWWemployees.csv")

  val csvFilePrinter = new CSVPrinter(fileWriter, CSVFormat.DEFAULT)

  def main(args: Array[String]): Unit = {
    println(contacts)
    contacts.map(csvFilePrinter.printRecord(_))
  }

}
