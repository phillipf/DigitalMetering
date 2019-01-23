package CLUE



case class rootCollection(items: List[CLUEFeature]) extends IndexedSeq[CLUEFeature] {
  def apply(index: Int) = items(index)

  def length = items.length
}
