package digitalMetering

import cats.effect.IO

import scala.io._
import java.io.{BufferedWriter, File, FileOutputStream, OutputStreamWriter}
  import java.time.Instant

import doobie.Transactor
import doobie.implicits._
import play.api.libs.json._

import scala.collection.mutable.{ArrayBuffer, ListBuffer}
import scala.util.matching.Regex

object NBParser {
  //"updates":[{"serialNumber":"urn:imei:863703032742533","subscriptionId":"361beed2-8a48-44b8-b90b-0bc01844fea0","timestamp":1539878864479,"deviceType":null}]
  implicit val BigIntWrite: Writes[BigInt] = new Writes[BigInt] {
    override def writes(bigInt: BigInt): JsValue = JsString(bigInt.toString())
  }

  implicit val BigIntRead: Reads[BigInt] = Reads {
    case JsString(value) => JsSuccess(scala.math.BigInt(value))
    case JsNumber(value) => JsSuccess(value.toBigInt())
    case unknown => JsError(s"Invalid BigInt")
  }

  final case class Updates(serialNumber: String, subscriptionId: String, timestamp: BigInt, deviceType: Option[String])
  final case class simplifiedJson(metric: String, value: Int, timestamp: String, meterid:String)
  final case class Reports(serialNumber: String, timestamp: BigInt, subscriptionId: String, resourcePath: String, value: String) {

    def createIntervals(in: (String,String,List[String])): List[(Int, Int)] = {

      val payloads = in._3.map(_.toInt)
      val initial = in._1.toInt + in._2.toInt
      val step = in._2.toInt

      val result = new ListBuffer[(Int, Int)]()
      /*result += ((initial + step, payloads.head))*/
      def loop(in: List[Int] = payloads,
               initial: Int = initial,
               step: Int = step,
               res: ListBuffer[(Int,Int)] = result): List[(Int, Int)] = in match {
        case Nil => res.toList
        case x::xtail => loop(xtail, initial + step, step, res += ((initial, x)))
      }

      loop()
    }

    val simplifyjson = {

      val in = this.value
      val objectPattern = new Regex("""\[\d+, \d+, (.*)\]""")
      val intervalPattern = new Regex("""([,\\s]*\[\d+, \d+, \[[\d+|\d+, ]*\]\])""")
      val datapattern = new Regex("""\[(\d+), (\d+), \[([\d+|\d+, ]*)\]\]""", "timestamp", "interval", "data")

      def objects(x:String) = objectPattern.findAllMatchIn(x).map {
        case objectPattern(x) =>  x
      }.toList

      def intervalStrings(x:String) = intervalPattern.findAllIn(x).map {
        case intervalPattern(x) =>  x
      }.toList

      def intervals(x:String) = datapattern.findAllIn(x).map {
        case datapattern(x, y, z) => (x, y, z.split(", ").toList)
      }.toList

      val intervalData = objects(in).flatMap(intervalStrings).flatMap(intervals).flatMap(createIntervals)

      intervalData

    }

    //val simplified = simplifyjson

    def finalPayload(s: (Int,Int)) = simplifiedJson(resourcePath, s._2, s._1.toString, serialNumber)

    val jsonPayloads = simplifyjson.map(s => finalPayload(s))

  }
  final case class Meter(reports: List[Reports], registrations: List[String], deregistrations: List[String], updates: List[Updates], expirations: List[String], responses: List[String]) {



  }

  implicit val updatesFormat = Json.format[Updates]
  implicit val reportsFormat = Json.format[Reports]
  implicit val meterFormat = Json.format[Meter]
  implicit val simplifiedFormat = Json.format[simplifiedJson]

  //parsed.flatMap(x => x.reports.flatMap(_.jsonPayloads))

  def getListOfFiles(dir: File, extensions: List[String]): List[File] = {
    dir.listFiles.filter(_.isFile).toList.filter { file =>
      extensions.exists(file.getName.endsWith(_))
    }
  }

  val okFileExtensions = List("json")

  val files = getListOfFiles(new File("C:/Users/farrelp1/Documents/DigitalMetering/data/Nb-IoT Payloads_imei-863703032742533_19-10-2018"), okFileExtensions)

  val json = files.flatMap(f => Source.fromFile(f.getPath()).getLines().toList)

  //val json = Source.fromFile("C:/Users/farrelp1/Documents/DigitalMetering/data/Nb-IoT Payloads_imei-863703032742533_19-10-2018/cww-2018-10-18-16-07-46.json")

  /*val xa = Transactor.fromDriverManager[IO](
    "com.microsoft.sqlserver.jdbc.SQLServerDriver",
    "jdbc:sqlserver://;servername=wvdpdevsql01;databaseName=ODS_CWWDM;integratedSecurity=true;"
  )

  val payloads: List[String] =
    sql"""SELECT DISTINCT
      [PAYLOAD]
      FROM [ODS_CWWDM].[dbo].[NB_IOT_test]
      where [PAYLOAD] IS NOT NULL"""
      .query[String]
      .to[List]
      .transact(xa)
      .unsafeRunSync()*/

  val parsed = json.map { raw =>

    //raw.map {
      Json.parse(raw).as[Meter]
    //}

  }
  //val processed = parsed.groupBy(_.reports.map(_.serialNumber))
  val processed = parsed.map { p =>

    p.reports.groupBy(_.serialNumber)

  }

  parsed.flatMap(x => x.reports.map(_.jsonPayloads))

  val result = parsed.flatMap(x => x.reports.flatMap(_.jsonPayloads))
  //[{"metric":"10262/0/2/0","value":0,"timestamp":1539352800,"tags":{"telco":"optus","meterid":"863703032743002","type":"measure","measurement":"registered_read"}}

  val objectPattern = new Regex("""\[\d+, \d+, (.*)\]""")
  val intervalPattern = new Regex("""([,\\s]*\[\d+, \d+, \[[\d+|\d+, ]*\]\])""")
  val datapattern = new Regex("""\[(\d+), (\d+), \[(\d+)\]\]""", "timestamp", "interval", "data")

  val intervalData = processed(2)("urn:imei:863703032742533").filter(_.resourcePath == "10262/0/2/1").map(_.value).head

  def simplifyjson(in: String) = {

    val objectPattern = new Regex("""\[\d+, \d+, (.*)\]""")
    val intervalPattern = new Regex("""([,\\s]*\[\d+, \d+, \[[\d+|\d+, ]*\]\])""")
    val datapattern = new Regex("""\[(\d+), (\d+), \[([\d+|\d+, ]*)\]\]""", "timestamp", "interval", "data")

    def objects(x:String) = objectPattern.findAllMatchIn(x).map {
      case objectPattern(x) =>  x
    }.toList

    def intervalStrings(x:String) = intervalPattern.findAllIn(x).map {
      case intervalPattern(x) =>  x
    }.toList

    def intervals(x:String) = datapattern.findAllIn(x).map {
      case datapattern(x, y, z) => (x, y, z.split(", ").toList)
    }.toList

    val intervalData = objects(in).flatMap(intervalStrings).flatMap(intervals).flatMap(createIntervals)

    intervalData

  }

  def createIntervals(in: (String,String,List[String])): List[(Int, Int)] = {

    val payloads = in._3.map(_.toInt)
    val initial = in._1.toInt + in._2.toInt
    val step = in._2.toInt

    val result = new ListBuffer[(Int, Int)]()
    /*result += ((initial + step, payloads.head))*/
    def loop(in: List[Int] = payloads,
             initial: Int = initial,
             step: Int = step,
             res: ListBuffer[(Int,Int)] = result): List[(Int, Int)] = in match {
      case Nil => res.toList
      case x::xtail => loop(xtail, initial + step, step, res += ((initial, x)))
    }

    loop()
  }

  val objects = objectPattern.findAllMatchIn(intervalData).map {
    case objectPattern(x) =>  x
  }.toList

  val intervalStrings = intervalPattern.findAllIn(intervalData).map {
    case intervalPattern(x) =>  x
  }.toList

  /*val result = datapattern.findAllIn(intervalStrings(0)).map {
    case datapattern(x, y, z) => (x, y, z.split(", ").toList)
  }.toList*/



  //val result2 = result.flatMap(x => createIntervals(x))

  def objects(x:String) = objectPattern.findAllMatchIn(x).map {
    case objectPattern(x) =>  x
  }.toList

  def intervalStrings(x:String) = intervalPattern.findAllIn(x).map {
    case intervalPattern(x) =>  x
  }.toList

  def intervals(x:String) = datapattern.findAllIn(x).map {
    case datapattern(x, y, z) => (x, y, z.split(", ").toList)
  }.toList

  def rolloutWriter(file: String, output: List[simplifiedJson]) {
    val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))

    writer.write("METRIC| VALUE| TIMESTAMP| METERID")

    writer.newLine()
    for (x <- output) {
      val time = (Instant.ofEpochSecond(x.timestamp.toInt))
      writer.write(
        x.metric + "|" +
          x.value + "|" +
          time + "|" +
          x.meterid + "|" + "\n")
    }// however you want to format it

    writer.close()
  }

  def main(args: Array[String]): Unit = {
    println(processed(2)("urn:imei:863703032742533").filter(_.resourcePath == "10262/0/2/4").map(_.value))
    println(processed(2)("urn:imei:863703032742533"))
    println(intervalStrings(objects(intervalData).head).head)
    //println(intervalStrings)
    println(objects(intervalData).head)
    //println(result)
    //println(result2)
    println(simplifyjson(intervalData))
    println(parsed.flatMap(x => x.reports.flatMap(_.jsonPayloads)))
    println(Json.toJson(result))

    rolloutWriter("N:/DigitalMetering/RDM/scala/NBpayloads.csv", result)

    //println(allIntervals.toList)
    //pattern.findAllIn(intervalData).foreach(println)
    //println(processed(1))
    //println(json.getLines().toList)

  }

}
//[1893592800, 86400, [[1893592799, 0]]]