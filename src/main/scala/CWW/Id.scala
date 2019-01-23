package CWW

trait Id {

  def MMETERNO: String
  def INSTALLNO: Int

  val MASTERID = this.INSTALLNO.toString.drop(2).toInt
  val isEmis = Emis.emisMap.isDefinedAt(this.MASTERID)
  val getEmis = if(isEmis) Emis.emisMap(this.MASTERID) else (0, "NA")

}
