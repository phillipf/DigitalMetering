import org.scalatest.FlatSpec

import CWW.readingRound.data

class readSpec extends FlatSpec {

  "A readingRound" should "be a list" in {
    assert(data(0).rounds == List("42001", "42011"))
  }

}
