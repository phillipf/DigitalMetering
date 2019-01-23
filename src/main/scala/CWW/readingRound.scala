package CWW

import scala.collection.JavaConversions._
import java.io.File
import java.util.Date
import java.text.SimpleDateFormat

import Utils.{Cleaners, Settings}

import scala.collection.mutable

case class readingRound(rounds: List[Int],
                        downloadDate: String,
                        exceptionDate: String) {

}

object readingRound extends Cleaners with Settings {

  val read: List[(String, String, String)] = reader.parseAll(new File("N:/DigitalMetering/RDM/meterReadingRounds.csv"))
    .map(values => {

      //val rounds = values(3).split(",|-").toList

      (values(3),
        values(6),
        values(9))
    }).toList

  val data = read.map(values => {
    val rounds = values._1.split(",|-").toList
    val numRange = {
      if(values._1.contains('-'))
        rounds(0).toFloat.toInt.to(rounds(1).toFloat.toInt).toList
      else rounds.map(x => x.toFloat.toInt)
    }
    val dl = values._2 + " 2019"
    val readformat = new SimpleDateFormat("EEE, dd MMM yyyy")
    val writeformat = new SimpleDateFormat("dd/MM/yy")

    val dldate = readformat.parse(dl)

    val ex = values._3 + " 2019"
    val exdate = readformat.parse(ex)

    readingRound(numRange,
      writeformat.format(dldate),
      writeformat.format(exdate))

  })

  var readMap = scala.collection.mutable.Map[Int, (String, String)]()
  type readMap =  mutable.Map[Int, (String, String)]
  def newMap(in: readingRound, res: readMap = readMap) = {

    def loop(rounds: List[Int] = in.rounds, res: readMap = readMap): Map[Int, (String, String)] = rounds match {
      case Nil => res.toMap
      case x :: xtail => loop(xtail, res ++ Map(x -> (in.downloadDate, in.exceptionDate)))
    }
    loop()
  }

  val roundsMap = data.flatMap(newMap(_)).toMap

  def main(args: Array[String]): Unit = {
    //println(data(20))
    /*var readMap = scala.collection.mutable.Map[String, (Date, Date)]()
    type readMap =  mutable.Map[String, (Date, Date)]
    def newMap(in: readingRound, res: readMap = readMap) = {
      def loop(rounds: List[String] = in.rounds, res: readMap = readMap): Map[String, (Date, Date)] = rounds match {
        case Nil => res.toMap
        case x :: xtail => loop(xtail, res ++ Map(x -> (in.downloadDate, in.exceptionDate)))
      }

      loop()
    }
    data.map(newMap(_))
    println(newMap(data(20)))*/
    println(roundsMap)
  }
}
