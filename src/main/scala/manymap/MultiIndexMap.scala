package manymap

/** Base trait of all MultiIndexMaps of any dimension */
trait MultiIndexMap[A] extends Iterable[A] {
  def multiSet: MultiSet[A]

  override def iterator: Iterator[A] = multiSet.toList.toIterator
}

object MultiIndexMap {
  class MultiIndexMapFactory[A](iterable: Iterable[A]) {
    def apply[B1](f1: A => B1) = iterable.indexBy(f1)
    def apply[B1, B2](f1: A => B1, f2: A => B2) = iterable.indexBy(f1, f2)
    def apply[B1, B2, B3](f1: A => B1, f2: A => B2, f3: A => B3) = iterable.indexBy(f1, f2, f3)
    def apply[B1, B2, B3, B4](f1: A => B1, f2: A => B2, f3: A => B3, f4: A => B4) = iterable.indexBy(f1, f2, f3, f4)
  }
  def apply[A](iterable: Iterable[A]) = new MultiIndexMapFactory(iterable)

  def empty[A, B1](f1: A => B1): MultiIndexMap1[A, B1] = apply[A](Iterable.empty)(f1)
  def empty[A, B1, B2](f1: A => B1, f2: A => B2): MultiIndexMap2[A, B1, B2] = empty(f1).withIndex(f2)
  def empty[A, B1, B2, B3](f1: A => B1, f2: A => B2, f3: A => B3): MultiIndexMap3[A, B1, B2, B3] = empty(f1, f2).withIndex(f3)
  def empty[A, B1, B2, B3, B4](f1: A => B1, f2: A => B2, f3: A => B3, f4: A => B4): MultiIndexMap4[A, B1, B2, B3, B4] = empty(f1, f2, f3).withIndex(f4)
}
