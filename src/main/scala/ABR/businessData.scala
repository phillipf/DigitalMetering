package ABR

import ABR.lucene.luceneBusiness
import GNAF.gnafPID
import cats.effect.IO
import doobie._
import doobie.implicits._

import scala.util.Random

object businessData {

  //type rawBusiness = (Option[Int], String)

  val rawBusinesses = new businessReader("N:/DigitalMetering/RDM/BUSINESS_NAMES_201805.csv").asicBusinessData

  /*val ID = new Random

  val processedBusinesses: List[luceneBusiness] = rawBusinesses.filterNot(business => business.ABN.isEmpty && business.BN_NAME == "").map(
    business => luceneBusiness(business.ABN.getOrElse(ID.nextInt()), business.BN_NAME.toLowerCase)
  )*/

  val xa = Transactor.fromDriverManager[IO](
    "com.microsoft.sqlserver.jdbc.SQLServerDriver",
    "jdbc:sqlserver://;servername=wvdb1devsql;databaseName=ABR;integratedSecurity=true;"
  )

  val ABRLocation: Map[gnafPID, businessLocation] =
    sql"""SELECT DISTINCT [GNAFPID]
                  ,[PID]
                  ,[LocationIndustryClass]
                  ,[LocationIndustryClassDescription]
                  FROM [ABR].[dbo].[ABR_Businesslocation]
         		 LEFT JOIN [dbo].[ADDRESS_VIEW] ON [GNAFPID] = [ADDRESS_DETAIL_PID]
                  WHERE [State] = 'VIC'
         		 and ([LOCALITY_NAME] = 'RICHMOND'
         		 OR [LOCALITY_NAME] = 'CREMORNE'
         		 OR [LOCALITY_NAME] = 'DOCKLANDS'
         		 OR [LOCALITY_NAME] = 'MELBOURNE')
                  and [GNAFPID] is not null
                  and [LocationIndustryClass] != '9999'
                  and [LocationIndustryClass] != ''
                  and [LocationIndustryClass] is not null"""
      .query[(gnafPID, businessLocation)]
      .to[List]
      .transact(xa)
      .unsafeRunSync()
      .toMap
}
