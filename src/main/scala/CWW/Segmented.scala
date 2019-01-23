package CWW

import GNAF.gnafPID

import scala.util.{Failure, Success}

import scala.concurrent.ExecutionContext.Implicits.global

trait Segmented {

  type G = Gentrack
  def values: List[G]
  def gnaf = values.map(_.ADDRESS.fetchByAddress)

  /*def result = gnaf foreach(_.onComplete {
    case Success(x) => Some(x)
    case Failure(y) => None
  })*/

}
