package GNAF

import cats.effect.IO
import digitalMetering.Address
import doobie._
import doobie.implicits._
import geotrellis.vector.Point
import vicMaps.planningGeo.planningGeo
import vicMaps.zoneCodeGroup

object gnafData {

  val xa = Transactor.fromDriverManager[IO](
    "com.microsoft.sqlserver.jdbc.SQLServerDriver",
    "jdbc:sqlserver://;servername=wvdb1devsql;databaseName=ABR;integratedSecurity=true;"
  )

  val gnafAddresses: Map[gnafPID, Address] =
    sql"""select address_detail_pid, flat_type, flat_number, number_first,
         number_last, LTRIM(RTRIM(dbo.strip_spaces(ISNULL(CAST([STREET_NAME] AS CHAR),'') + ' ' + ISNULL(CAST([STREET_TYPE_CODE] AS CHAR),''))))
         AS street, locality_name, postcode, longitude, latitude from address_view where state_abbreviation = 'VIC'
         and locality_name = 'RICHMOND' or locality_name = 'CREMORNE' or locality_name = 'MELBOURNE' or locality_name = 'DOCKLANDS'"""
      .query[(gnafPID, Address)]
      .to[List]
      .transact(xa)
      .unsafeRunSync()
      .toMap

  /*val gnafSource: IO[List[(gnafPID, Address)]] =
    sql"""select address_detail_pid, flat_type, flat_number, number_first,
         number_last, LTRIM(RTRIM(dbo.strip_spaces(ISNULL(CAST([STREET_NAME] AS CHAR),'') + ' ' + ISNULL(CAST([STREET_TYPE_CODE] AS CHAR),''))))
         AS street, locality_name, postcode, longitude, latitude from address_view where state_abbreviation = 'VIC'
         and locality_name = 'RICHMOND' or locality_name = 'CREMORNE' or locality_name = 'MELBOURNE' or locality_name = 'DOCKLANDS'"""
      .query[(gnafPID, Address)]
      .to[List]
      .transact(xa)*/


  val gnafPrimarySecondary: Map[gnafPID, Option[String]] =
    sql"""select address_detail_pid, primary_secondary from address_view where state_abbreviation = 'VIC'
         and locality_name = 'RICHMOND' or locality_name = 'CREMORNE' or locality_name = 'MELBOURNE' or locality_name = 'DOCKLANDS'"""
      .query[(gnafPID, Option[String])]
      .to[List]
      .transact(xa)
      .unsafeRunSync()
      .toMap

  lazy val addressZone: Map[gnafPID, (String, Map[String, String])] = gnafAddresses.map(x =>
                                                                x._1 -> getZone(x._2.coord)
                                                                  .map(_.properties.ZONE_CODE))
                                                                .filter(_._2.nonEmpty)
                                                                .map(zoneCode => zoneCode._1 -> getZoneGroup(zoneCode._2.head))

  def getZoneGroup(zoneCode: String) = {
    val group = zoneCodeGroup.zoneCodeGroup.filterKeys(k => zoneCode matches k + "[0-9]*")
    (zoneCode, group)
  }

  def getZone(p: Point) =  {
    planningGeo.filter(_.geometry.contains(p))
  }
}
