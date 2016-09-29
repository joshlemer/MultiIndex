
package object manymap {
  implicit class IterableOps[A](iterable: Iterable[A]) {
    val sequence = iterable.toSeq
    def indexBy[B1](f1: A => B1): MultiIndexMap1[A, B1] = new MultiIndexMap1Impl(MultiSet(sequence), f1, JIndex(sequence, f1))

    def indexBy[B1, B2](f1: A => B1, f2: A => B2): MultiIndexMap2[A, B1, B2] =
      new MultiIndexMap2Impl(MultiSet(sequence), f1, JIndex(sequence, f1), f2, JIndex(sequence, f2))
    def indexBy[B1, B2, B3](f1: A => B1, f2: A => B2, f3: A => B3): MultiIndexMap3[A, B1, B2, B3] =
      new MultiIndexMap3Impl(MultiSet(sequence), f1, JIndex(sequence, f1), f2, JIndex(sequence, f2), f3, JIndex(sequence, f3))
    def indexBy[B1, B2, B3, B4](f1: A => B1, f2: A => B2, f3: A => B3, f4: A => B4): MultiIndexMap4[A, B1, B2, B3, B4] =
      new MultiIndexMap4Impl(MultiSet(sequence), f1, JIndex(sequence, f1), f2, JIndex(sequence, f2), f3, JIndex(sequence, f3), f4, JIndex(sequence, f4))
  }
}
