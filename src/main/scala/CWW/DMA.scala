package CWW

import digitalMetering.Rollout
import digitalMetering.Address

trait DMA {

  type G = Gentrack
  type R = Rollout
  type S = DMASpatial
  def values: List[G]
  def ageSorted: List[G]
  def nonPulse = values.filter(x =>
    !x.hasExistingDevice & x.METER_TYPE.MSIZE <= 20 & (x.METER_TYPE.isEmail | x.METER_TYPE.isTD8)
  )
  def pf: PartialFunction[DMA, List[R]]
  def Rollout = Richmond.pf orElse Docklands.pf
  def toRollout = Rollout(this)

  def toSpatial: Map[String, (Address, String, Boolean)] = toRollout.map(x => x.MMETERNO -> (x.geocode, x.REPLACEMENT, x.inDMA)).toMap

}


