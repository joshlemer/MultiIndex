package com.github.joshlemer.multiindex

/** Base trait of all MultiIndexMaps of any dimension */
trait MultiIndex[A] extends Iterable[A] {
  def multiSet: MultiSet[A]

  override def iterator: Iterator[A] = multiSet.toList.toIterator
}

object MultiIndex {
  class MultiIndexMapFactory[A](iterable: Iterable[A]) {
    def apply[B1](f1: A => B1) = iterable.indexBy(f1)
    def apply[B1, B2](f1: A => B1, f2: A => B2) = iterable.indexBy(f1, f2)
    def apply[B1, B2, B3](f1: A => B1, f2: A => B2, f3: A => B3) = iterable.indexBy(f1, f2, f3)
    def apply[B1, B2, B3, B4](f1: A => B1, f2: A => B2, f3: A => B3, f4: A => B4) = iterable.indexBy(f1, f2, f3, f4)
  }
  def apply[A](iterable: Iterable[A]) = new MultiIndexMapFactory(iterable)

  def empty[A, B1](f1: A => B1): MultiIndex1[A, B1] = apply[A](Iterable.empty)(f1)
  def empty[A, B1, B2](f1: A => B1, f2: A => B2): MultiIndex2[A, B1, B2] = empty(f1).withIndex(f2)
  def empty[A, B1, B2, B3](f1: A => B1, f2: A => B2, f3: A => B3): MultiIndex3[A, B1, B2, B3] = empty(f1, f2).withIndex(f3)
  def empty[A, B1, B2, B3, B4](f1: A => B1, f2: A => B2, f3: A => B3, f4: A => B4): MultiIndex4[A, B1, B2, B3, B4] = empty(f1, f2, f3).withIndex(f4)
}
