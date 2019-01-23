package CLUE

import GNAF.gnafPID
import com.ning.http.client.{AsyncHttpClient, AsyncHttpClientConfig}
import com.socrata.soda2.consumer.http.HttpConsumer
import javax.net.ssl.SSLContext

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import com.socrata.future.ExecutionContextTimer.Implicits._
import play.api.libs.json.{Json, Writes}

import scala.concurrent.ExecutionContext.Implicits.global

//import spray.json._
//import CLUE.clueJsonProtocol._

import org.json4s._
import org.json4s.jackson.JsonMethods._

object clueData {

  val clientConfig = new AsyncHttpClientConfig.Builder().
    setSSLContext(SSLContext.getDefault). // Without this, ALL SSL certificates are treated as valid
    build()
  val client = new AsyncHttpClient(clientConfig)
  val service = new HttpConsumer(client, "data.melbourne.vic.gov.au")
  val future = service.query("q8hp-qgps").allRows()

  val buildingInfo = Await.result(future, Duration.Inf)
  implicit val formats = DefaultFormats
  val buildingJs = buildingInfo.toList
  buildingInfo.map(_.asJObject.fields)
  val clueData = buildingJs.map(j => parse(j.toString).extract[CLUEFeature].property).filter(_.gnafPID.GNAFPID != "NA")

  val clueMap: Map[gnafPID, String] = clueData map (x => x.gnafPID -> x.predominant_space_use) toMap

  def main(args: Array[String]): Unit = {

    /*implicit object PropertyRoleWrites extends Writes[clueProperty] {
      def writes(role: clueProperty) = role match {
        case address => Json.toJson("address")
        case gnafPID => Json.toJson("gnafPID")
        //case User => Json.toJson("User")
      }
    }*/
    import digitalMetering.Address
    implicit val PIDFormat = Json.format[gnafPID]
    implicit val AddressFormat = Json.format[Address]
    implicit val CLUEFormat = Json.format[clueProperty]

    val d = clueData
    //d.take(20) foreach println
    val json = Json.toJson(d(0))
    println(Json.prettyPrint(Json.toJson(d.head)))

    import au.id.jazzy.play.geojson._

    /*val json = Json.obj(
      "type" -> "Feature",
      "geometry" -> Json.obj(
        "type" -> "Point",
        "coordinates" -> Seq(151.2111, -33.86)
      ),
      "properties" -> Json.obj(
        "name" -> "Sydney"
      )
    )*/
    val geo = Json.fromJson[Feature[LatLng]](json)
    println(geo)



  }




}
