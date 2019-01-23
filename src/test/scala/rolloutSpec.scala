import org.scalatest.FlatSpec

import scala.util.Random

import digitalMetering.Rollout._

class rolloutSpec extends FlatSpec{



  "An Itron TD8 20mm meter" should "be replaced with a LoRa Digital Meter" in {

    val test = plan.filter(m => {
        m.existingDevice.isEmpty &
        m.METER_TYPE.MODEL == "TD8" &
        m.METER_TYPE.MANUFACTURER == "Itron" &
        m.METER_TYPE.MSIZE == 20
    })

    val random = new Random
    val randomMeter = random.nextInt(test.size)

    assert(test(randomMeter).REPLACEMENT == "DM-L")
  }

  "An Email 20mm meter" should "be replaced with a LoRa Digital Meter" in {

    val test = plan.filter(m => {
        m.existingDevice.isEmpty &
        m.METER_TYPE.MANUFACTURER == "EMAIL" &
        m.METER_TYPE.MSIZE == 20
    })

    val random = new Random
    val randomMeter = random.nextInt(test.size)

    assert(test(randomMeter).REPLACEMENT == "DM-L")
  }
}
