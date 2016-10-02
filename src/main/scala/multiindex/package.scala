
package object multiindex {
  implicit class IterableOps[A](iterable: Iterable[A]) {
    val sequence = iterable.toSeq

    def indexBy[B1](f1: A => B1): MultiIndex1[A, B1] = new MultiIndex1Impl(MultiSet(sequence), f1, Index(f1, sequence))
    def indexBy[B1, B2](f1: A => B1, f2: A => B2): MultiIndex2[A, B1, B2] = indexBy(f1).withIndex(f2)
    def indexBy[B1, B2, B3](f1: A => B1, f2: A => B2, f3: A => B3): MultiIndex3[A, B1, B2, B3] = indexBy(f1, f2).withIndex(f3)
    def indexBy[B1, B2, B3, B4](f1: A => B1, f2: A => B2, f3: A => B3, f4: A => B4): MultiIndex4[A, B1, B2, B3, B4] = indexBy(f1, f2, f3).withIndex(f4)
  }
}
