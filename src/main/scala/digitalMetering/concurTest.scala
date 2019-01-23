package digitalMetering


import CWW.Docklands
import GNAF.gnafPID
import cats.Comonad
import cats.effect.IO
import doobie.free.connection.ConnectionIO

import scala.concurrent.{Await, ExecutionContext, Future}
import ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.language.implicitConversions
import scala.util.{Failure, Success}
import doobie.implicits._
import doobie.util.transactor.Transactor

/*object concurTest {


  val xa = Transactor.fromDriverManager[IO](
    "com.microsoft.sqlserver.jdbc.SQLServerDriver",
    "jdbc:sqlserver://;servername=wvdb1devsql;databaseName=ABR;integratedSecurity=true;"
  )

  def fetchByAddress(add: Address) =
    sql"""WITH ADDRESS AS(select address_detail_pid, flat_type, flat_number, number_first,
         number_last, LTRIM(RTRIM(dbo.strip_spaces(ISNULL(CAST([STREET_NAME] AS CHAR),'') + ' ' + ISNULL(CAST([STREET_TYPE_CODE] AS CHAR),''))))
         AS street, locality_name, postcode, longitude, latitude from address_view where state_abbreviation = 'VIC')
         select address_detail_pid
         from ADDRESS
         where flat_number = ${add.UNIT_NUMBER}
         AND number_first = ${add.NUM_FIRST}
         AND street = ${add.ST_NAME}
         AND locality_name = ${add.SUBURB}
         AND postcode = ${add.POSTCODE}

    """.query[gnafPID].to[List]

  /*def gnaf(add: Address)(implicit db: ConnectionIO[List[gnafPID]]) =
    db.run()
    //db.transact(xa).unsafeToFuture()
    //db.run(qLocalityAliasName(add))

  val qLocalityAliasName  = {
    //def q(add: Address) = for (a <- add) yield fetchByAddress(a)
    def q(add: Address) = fetchByAddress(add)
    q(_)//Compiled(q _)
  }*/


  def main(args: Array[String]): Unit = {
    val a = Docklands.values(0)
    //println(gnaf(a)())

    //println(a.map2())

  }

}*/
