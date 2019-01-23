package digitalMetering

/*import CWW.{Gentrack, Location}
import GNAF.gnafPID
import akka.actor.ActorSystem
import akka.stream._
import cats.data.NonEmptyList
import com.ning.http.client.Request
import doobie.{Query => _, _}

import scala.collection.mutable.ListBuffer
//import akka.stream.actor.ActorPublisherMessage.Request
import akka.stream.scaladsl._
import cats.effect.IO
import doobie.free.connection.ConnectionIO

import scala.concurrent.Future
import scala.concurrent.duration._
import doobie.implicits._
import doobie.util.transactor.Transactor
import doobie.free.connection._
import cats.effect.{Async, IO}
import monix.eval.Task*/

/*object akkaTest {

  val xa = Transactor.fromDriverManager[IO](
    "com.microsoft.sqlserver.jdbc.SQLServerDriver",
    "jdbc:sqlserver://;servername=wvdb1devsql;databaseName=ABR;integratedSecurity=true;"
  )

  //val cio: ConnectionIO[Request] = Async[ConnectionIO].liftIO(transaction.transact(xa))
  val eg1 = Gentrack.richmond.take(100).map(_.ADDRESS).distinct
  def fetchByAddress(add: Address): ConnectionIO[Option[gnafPID]] =
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

    """.query[gnafPID].option


  import cats._, cats.data._, cats.implicits._

  /*def findWidgetByOwner(name: List[Address]): List[gnafPID] = for {
    opt <- name
    widget <- fetchByAddress(opt).transact(xa).unsafeRunSync()
  } yield widget*/
  /*def findWidgetByOwner(name: List[Address]): List[ConnectionIO[Option[gnafPID]]] = for {
    opt <- name
    widget <- fetchByAddress(opt)
  } yield widget*/
  /*trait addList {
    implicit val system = ActorSystem("pi")
    implicit val dispatcher = system.dispatcher
    implicit val materializer = ActorMaterializer()

    type A = Address

    def values: List[A]
    val data = Source(values)
    val throttledAndZipped = Flow[A]
      //.zip(Source(values)).map{case (index, fact) => s"factorial(${index}) = ${fact}"}
      //.throttle(1, 5 millis, 1, ThrottleMode.shaping)
      .mapAsync(10){ a => Future(a.fetchByAddress)}

    var res: ListBuffer[gnafPID] = ListBuffer[gnafPID]()
    val runner = throttledAndZipped.
      to(Sink.foreachParallel(10){
        ea => res ++ ea.transact(xa).unsafeRunSync()
      }).runWith(data)
  }

  object eg extends addList {
    def values = Gentrack.richmond.map(_.ADDRESS).distinct.take(10)
  }

  def main(args: Array[String]): Unit = {
    //println(eg(50).fetchByAddress.transact(xa).unsafeRunSync())
    println(eg.res.toList)
    //println(getAddress(eg))
    //println(findWidgetByOwner(eg))
  }*/

  //def getObject: ConnectionIO[Address]                      = ???
  //def saveObject(obj: Address): ConnectionIO[Address]       = ???
  //def processObject(obj: Address): monix.eval.Task[Address] = ???

  //val transaction: ConnectionIO[Address] = for {

    //obj       <- getObject                                           //ConnectionIO[Request]
    //processed <- Async[ConnectionIO].liftIO(processObject(obj).toIO) //ConnectionIO[Request]
    //updated   <- saveObject(processed)                               //ConnectionIO[Request]
  //} yield updated

  //val result: Task[Address] = transaction.transact(xa)


  /*implicit val system = ActorSystem("pi")
  implicit val dispatcher = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val xa = Transactor.fromDriverManager[IO](
    "com.microsoft.sqlserver.jdbc.SQLServerDriver",
    "jdbc:sqlserver://;servername=wvdb1devsql;databaseName=ABR;integratedSecurity=true;"
  )

  /*def biggerThan(add: Address) = {
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

    """
      .query[gnafPID]
  }*/

  def q(add: Address): ConnectionIO[List[gnafPID]] =
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


  val gnafAddresses =
    sql"""select address_detail_pid, flat_type, flat_number, number_first,
         number_last, LTRIM(RTRIM(dbo.strip_spaces(ISNULL(CAST([STREET_NAME] AS CHAR),'') + ' ' + ISNULL(CAST([STREET_TYPE_CODE] AS CHAR),''))))
         AS street, locality_name, postcode, longitude, latitude from address_view where state_abbreviation = 'VIC'
         and locality_name = 'RICHMOND' or locality_name = 'CREMORNE' or locality_name = 'MELBOURNE' or locality_name = 'DOCKLANDS'"""
      .query[(gnafPID, Address)]
      .to[List]
      .transact(xa)

  //val factorial = Source(gnafAddresses.).map(add => Gentrack.richmond.take(20).map(_.ADDRESS).distinct.filter(_.equals(add)))
  val factorial = Source(gnafAddresses.unsafeRunSync()).map(add => Gentrack.richmond.take(100).map(_.ADDRESS).distinct.filter(_.equals(add)))
  //val factorial = Source(Gentrack.richmond.take(20).map(_.ADDRESS).distinct).map(add => gnafAddresses.unsafeRunSync().filter(_._1.equals(add)))
  //val factorial = Source(Gentrack.richmond.take(20).map(_.ADDRESS).distinct).map(add => biggerThan(add).to[List].transact(xa).unsafeRunSync())
  val strings = Source(Gentrack.richmond.take(100).map(_.ADDRESS).distinct)
  //val strings =  Source(1 to 10).map(_.toString)

  val throttledAndZipped = Flow[Address]
    .zip(factorial).map{case (index, fact) => s"factorial(${index}) = ${fact}"}
    .throttle(1, 5 millis, 1, ThrottleMode.shaping)
    .mapAsync(10)(a => Future{s"async -> $a"})

  val flow =
    throttledAndZipped.
      to(Sink.foreach{
        ea: String => println(s"sink: ${ea}")
      })

  val dbParallelism = 5 // or whatevs
  val postProcessParallelism = 10 // or whatevs

  // Whatever operations you want the DB to do should be baked into the query action
  def singleYearQuery(add: Address): IO[List[gnafPID]] = for {
    //address <- add
    row <- q(add)
  } yield row
  /*val singleYearQuery: IO[List[gnafPID]] = for {
    add <- List[Address]
    row <- q(add)
  } yield row*/

  /*def postProcess(row: DataTableRow) = {
    Future.successful(row.toString)
  }*/

  val combinedAction = for {
    _ <- q("Invalid", 0, 0, None) // has type: ConnectionIO[..]
    //_ <- action2 // has type: ConnectionIO[..]
  } yield row



  //Flow[Address]
    //.mapAsync(dbParallelism) { address => singleYearQuery.unsafeToFuture()}



  def main(args: Array[String]): Unit = {
    /*system.scheduler.scheduleOnce(5 millis){
      flow.runWith(strings)
    }*/
    println(Flow[Address]
      .mapAsync(dbParallelism) { singleYearQuery(_)}
      .runWith(Source(Gentrack.richmond.take(100).map(_.ADDRESS).distinct), Sink.seq))

  }*/



}*/
