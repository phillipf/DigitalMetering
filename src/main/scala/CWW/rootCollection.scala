package CWW

case class rootCollection(items: List[DMAFeature]) extends IndexedSeq[DMAFeature] {
  def apply(index: Int) = items(index)

  def length = items.length
}

