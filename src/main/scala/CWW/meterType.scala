package CWW

case class meterType(MSIZE: Int,
                       MODEL: Option[String],
                       MANUFACTURER: Option[String],
                       AGE: Option[Int]) {

  val LoRaDM = this.MSIZE == 20

  val NBIoTDM = this.MSIZE == 20

  val meter = List(this.MSIZE.toString(), this.MANUFACTURER.getOrElse("UNKNOWN"), this.MODEL.getOrElse("UNKNOWN"))

  val meterTypeConversion: List[String] = meterTypeCleanse.meterTypesCleanse.getOrElse(meter, meter)

  def meterTypeCleanser = this.copy(MANUFACTURER = Option(meterTypeConversion(1)), MODEL = Option(meterTypeConversion(2)))

  val isTD8 = "TD8".r.findFirstIn(this.MODEL.getOrElse("UNKNOWN")).isDefined

  val isEmail = "EMAIL".r.findFirstIn(this.MANUFACTURER.getOrElse("UNKNOWN")).isDefined
}
