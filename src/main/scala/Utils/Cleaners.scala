package Utils

import java.util.Locale

import org.joda.time.DateTime.now
import org.joda.time.Days
import org.joda.time.format.DateTimeFormat

import scala.util.Try
import scala.util.matching.Regex

class Cleaners {

  def tryToDbl( s: String ) = Try(s.toDouble).toOption
  def tryToInt( s: String ) = Try(s.toInt).toOption
  def tryToString( s: String ) = Try(s.toString).toOption

  def cleanHouseNumber(x: String): String = {
    x.replaceAll("[a-zA-Z]", "")
  }

  def cleanUnitNumber(x: Option[String]): Option[Int] = {
    if(x.nonEmpty) {
      val num = x.get.replaceAll("[a-zA-Z]", "").trim
      if(num != "") Some(num.toInt)
      else None }

    else None
  }

  def cleanUnitType(x: Option[String]): Option[String] = {
    if(x.nonEmpty) {
      val unitType = x.get
      if(unitType == "UN") Some("UNIT")
      else x
    }
    else x
  }

  val numPattern: Regex = "[^\\d]".r

  def numFirst(x: String, pattern: Regex = numPattern): Option[Int] = x match {
    case a if a.contains('-') => tryToInt(a.take(a.indexOf('-')))
    case c if c.contains("NA") => None
    case b if pattern.findFirstIn(b).isDefined => tryToInt(b.replaceAll("[^\\d].*", ""))
    case d if d == "" => None
    case e if e.isEmpty => None
    case _ => Some(x.toInt)
  }

  def numLast(x: String): Option[Int] = x match {
    case a if a.contains('-') => tryToInt(a.drop(a.indexOf('-') + 1))
    case _ => None
  }

  def age(dateTime: String): Double = {
    //val timePattern = "MMM/dd/yyy HH:mm:ss"
    val timePattern = "dd/mm/yyy HH:mm:ss"
    val AMPMpattern = "AM|PM".r
    val format = DateTimeFormat.forPattern(timePattern).withLocale(Locale.ENGLISH)
    //val format = DateFormat.forPattern(timePattern).withLocale(Locale.ENGLISH)
    val date = format.parseDateTime(AMPMpattern.replaceAllIn(dateTime, "").trim())

    val today = now.toLocalDateTime
    Days.daysBetween(date.toLocalDate(), today.toLocalDate()).getDays.toDouble
  }


}
