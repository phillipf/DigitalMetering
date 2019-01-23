
import org.scalatest.FlatSpec
import java.io.File

import scala.io.Source

class parseSpec extends FlatSpec {

  def getListOfFiles(dir: File, extensions: List[String]): List[File] = {
    dir.listFiles.filter(_.isFile).toList.filter { file =>
      extensions.exists(file.getName.endsWith(_))
    }
  }

  val okFileExtensions = List("json")

  val files = getListOfFiles(new File("C:/Users/farrelp1/Documents/DigitalMetering/data/Nb-IoT Payloads_imei-863703032742533_19-10-2018"), okFileExtensions)

  val json = files.map(f => Source.fromFile(f.getPath()).getLines().toList)

  /*"A readingRound" should "be a list" in {
    assert(data(0).rounds == List("42001", "42011"))
  }*/

}
