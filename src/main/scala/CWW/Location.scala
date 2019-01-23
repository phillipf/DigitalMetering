package CWW

import ABR.businessData.ABRLocation
import ABR.businessLocation
import GNAF.gnafData.{gnafAddresses, gnafPrimarySecondary}
import GNAF.{gnafData, gnafPID}
import digitalMetering.Address
import CLUE.clueData.clueMap
import cats.effect.IO
import com.koddi.geocoder.{Geocoder, Parameters, ResponseParser}
import doobie._
import doobie.implicits._
import geotrellis.proj4
import geotrellis.proj4.{CRS, Proj4Transform}
import geotrellis.proj4.io.wkt.Projection
import geotrellis.vector.Point
import geotrellis.vector.reproject.Reproject
import org.osgeo.proj4j.CoordinateReferenceSystem
import org.osgeo.proj4j.util.CRSCache

import scala.concurrent.Future
import scala.util.Success

trait Location {

  def ADDRESS: Address

  val xa = Transactor.fromDriverManager[IO](
    "com.microsoft.sqlserver.jdbc.SQLServerDriver",
    "jdbc:sqlserver://;servername=wvdb1devsql;databaseName=ABR;integratedSecurity=true;"
  )


  def fetchByAddress: Future[Option[gnafPID]] =
    sql"""WITH ADDRESS AS(select address_detail_pid, flat_type, flat_number, number_first,
         number_last, LTRIM(RTRIM(dbo.strip_spaces(ISNULL(CAST([STREET_NAME] AS CHAR),'') + ' ' + ISNULL(CAST([STREET_TYPE_CODE] AS CHAR),''))))
         AS street, locality_name, postcode, longitude, latitude from address_view where state_abbreviation = 'VIC')
         select address_detail_pid
         from ADDRESS
         where flat_number = ${this.ADDRESS.UNIT_NUMBER}
         AND number_first = ${this.ADDRESS.NUM_FIRST}
         AND street = ${this.ADDRESS.ST_NAME}
         AND locality_name = ${this.ADDRESS.SUBURB}
         AND postcode = ${this.ADDRESS.POSTCODE}
    """.query[gnafPID].option.transact(xa).unsafeToFuture()

  def isGNAF(add: Address = this.ADDRESS, gnaf: Map[gnafPID, Address] = gnafAddresses) = {
      gnaf.exists(_._2.unitAddress.equals(add.unitAddress))
  }

  def isclueGNAF = gnafAddresses.exists(_._2.clueAddress.equals(ADDRESS.clueAddress))

  def getGNAF = {
    if(isGNAF())
      gnafAddresses.find(_._2.unitAddress.equals(ADDRESS.unitAddress)).get._1
    //else if(isclueGNAF)
      //gnafAddresses.find(_._2.clueAddress.equals(ADDRESS.clueAddress)).get._1
    else gnafPID("NA")
  }

  // The factory object can be used to lazily create Geocoders
  val geo = Geocoder.create

  // If you need to use an API key because of limiting
  val geoWithKey = Geocoder.create("AIzaSyC8og_ZwJ38pNOFNwARZ86iAXzxnPq1y5Y")

  // Geocoders can be created with parameters that will be passed at every lookup
  val geoWithParams = Geocoder.create(Parameters(region = Some("au")))

  // And lastly if you need to manually create the Geocoder
  // that's supported as well.
  val customGeo = new Geocoder("https://maps.google.com/maps/api/geocode/", Some("AIzaSyC8og_ZwJ38pNOFNwARZ86iAXzxnPq1y5Y"), None, new ResponseParser)

  // Lookup a location with a formatted address string
  // Returns a Seq[Result]
  // Access the MapComponents geometry data to get the location
  def googleAPI = geoWithKey.lookup(ADDRESS.streetAddress).head.geometry.location
  def getGoogle = ADDRESS.copy(LATITUDE = Option(googleAPI.latitude), LONGITUDE = Option(googleAPI.longitude))

  def cwwProj = proj4.CRS.fromEpsgCode(28355)
  def googleProj = proj4.CRS.fromEpsgCode(4326)

  def tranformer = Proj4Transform(cwwProj, googleProj)

  def hasCoord = (ADDRESS.LATITUDE.getOrElse(0.0) != 0) & (ADDRESS.LONGITUDE.getOrElse(0.0) != 0)
  def coord = if(hasCoord) Point(ADDRESS.LONGITUDE.get, ADDRESS.LATITUDE.get) else Point(0, 0)
  def coordInDMA = coord.intersects(Docklands.bounds) | coord.intersects(Richmond.bounds)

  def coordTranformed = coord.reproject(tranformer)

  def addressTranformed = ADDRESS.copy(LONGITUDE = Option(coordTranformed.x), LATITUDE = Option(coordTranformed.y))


  def geocode = {
    if(ADDRESS.hasCoord & ADDRESS.coordInDMA)
      ADDRESS.addressTranformed
    else gnafData.gnafAddresses.getOrElse(getGNAF, getGoogle)
  }

  def UTM  = geocode.coord.reproject(googleProj, cwwProj)

  def inDMA = UTM.intersects(Docklands.bounds) | UTM.intersects(Richmond.bounds)

  def getclueGNAF = if(isclueGNAF) gnafAddresses.find(_._2.clueAddress.equals(ADDRESS.clueAddress)).get._1 else gnafPID("NA")

  def primarySecondary = if(isGNAF()) gnafPrimarySecondary(getGNAF).getOrElse("NA") else "NA"
  def isABR = ABRLocation.isDefinedAt(getGNAF)
  def getABR = if(isABR) ABRLocation(getGNAF) else businessLocation(0, 0, "NA")

  def isCLUE = clueMap.isDefinedAt(getclueGNAF)
  def getCLUE = if(isCLUE) businessLocation(0, 0, clueMap(getclueGNAF)) else businessLocation(0, 0, "NA")
}
