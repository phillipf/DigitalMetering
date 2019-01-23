package vicMaps

case class rootCollection(items: List[planningFeature]) extends IndexedSeq[planningFeature] {
  def apply(index: Int) = items(index)

  def length = items.length
}


