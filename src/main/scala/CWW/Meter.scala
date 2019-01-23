package CWW

import ABR.lucene.luceneBusiness

import scala.util.Try

trait Meter extends Location with Id {

  def MMETERNO: String
  def INSTALLNO: Int
  def DEVICENO: Option[Int]
  def IMETNETWORK: Option[Int]
  def NETWK_FLAG: String
  def METER_TYPE: meterType
  def BILLSEQ: String
  def METER_LOCATION: String

  val isTemetraFuture = if(this.IMETNETWORK.isDefined) Temetra.temetraFutureNetworks.contains(this.IMETNETWORK.get) else false

  val isCyble = this.DEVICENO.nonEmpty

  val collectionMethod: Option[String] = {
    Try(Temetra.temetraMap(this.MMETERNO).head.COLLECTIONMETHOD).toOption
  }

  val isTemetra: Boolean = {
    if(collectionMethod.isEmpty) false
    else if(collectionMethod.contains("Manual Read")) false
    else true
  }

  val existingDevice =
    if(isTemetra) Some("TEMETRA")
    else if(isCyble)
      if(isTemetraFuture) Some("TEMETRA-FUTURE")
      else Some("CYBLE")
    else None

  val hasExistingDevice = existingDevice.isDefined

  def typeCleanse = List(this.METER_TYPE.MSIZE.toString(), this.METER_TYPE.MANUFACTURER.getOrElse("UNKNOWN"), this.METER_TYPE.MODEL.getOrElse("UNKNOWN"))

  def meterTypeConversion: List[String] = meterTypeCleanse.meterTypesCleanse.getOrElse(typeCleanse, typeCleanse)

  def meterTypeCleanser = meterType(this.METER_TYPE.MSIZE, Option(meterTypeConversion(1)), Option(meterTypeConversion(2)), this.METER_TYPE.AGE)

  //def ageSorted(meters: List[Meter]) = meters.sortBy(meter => -meter.METER_TYPE.MSIZE)
}




