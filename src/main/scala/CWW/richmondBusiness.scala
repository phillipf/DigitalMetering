package CWW

/*import GNAF.gnafPID
import cats.effect.IO
import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import doobie.contrib.hikari.hikaritransactor._
import doobie.free.connection.ConnectionIO
import doobie.util.{query, transactor}

import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import doobie.implicits._
import monix.eval.Task*/


/*object richmondBusiness extends Segmented {
  def values = Gentrack.richmond

  //def result(in: Either[Throwable, Unit]): Unit = Right(in)

  val config = new HikariConfig()
  config.setJdbcUrl("jdbc:sqlserver://;servername=wvdb1devsql;databaseName=ABR;integratedSecurity=true;")
  //config.setUsername(Username)
  //config.setPassword(Password)
  config.setMaximumPoolSize(5)

  val dataSource = new HikariDataSource()
  dataSource.setJdbcUrl(s"jdbc:sqlserver://;servername=wvdb1devsql;databaseName=ABR;integratedSecurity=true;")


  /*val DbTransactor: IO[HikariTransactor[IO]] =
    IO.pure(HikariTransactor.apply[IO](new HikariDataSource(config)))*/
  //val xa = HikariTransactor[IO](dataSource).unsafePerformIO
  val pgTransactor: HikariTransactor[IO] = HikariTransactor[IO](dataSource)

  /*def interactWithDb = {
    val q: ConnectionIO[Int] = sql"""..."""
    q.transact(xa)
  }*/

  /*sql"""select DISTINCT gcpProject FROM JobStatus"""
    .query[String]
    .compile

  val prog = for {
    xa <- transactor
    result <- query.transact(xa)
  } yield result
  prog.unsafeRunSync()*/

  def main(args: Array[String]): Unit = {
    gnaf(854).onComplete {case Success(x) => println(x.get)}
  }

}*/