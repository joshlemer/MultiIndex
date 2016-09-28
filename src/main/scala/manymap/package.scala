import scala.collection.Bag

package object manymap {
  import Utils._
  implicit class IterableOps[A](iterable: Iterable[A]) {
    def indexBy[B1](f1: A => B1): MultiIndexMap1[A, B1] = new MultiIndexMap1Impl(MultiSet(iterable), f1, JIndex(iterable, f1))

    def indexBy[B1, B2](f1: A => B1, f2: A => B2): MultiIndexMap2[A, B1, B2] = {
      val bag = Bag.empty[A] ++ iterable
      new MultiIndexMap2Impl(bag, f1, makeIndex(bag, f1), f2, makeIndex(bag, f2))
    }
    def indexBy[B1, B2, B3](f1: A => B1, f2: A => B2, f3: A => B3): MultiIndexMap3[A, B1, B2, B3] = {
      val bag = Bag.empty[A] ++ iterable
      new MultiIndexMap3Impl(bag, f1, makeIndex(bag, f1), f2, makeIndex(bag, f2), f3, makeIndex(bag, f3))
    }
    def indexBy[B1, B2, B3, B4](f1: A => B1, f2: A => B2, f3: A => B3, f4: A => B4): MultiIndexMap4[A, B1, B2, B3, B4] = {
      val bag = Bag.empty[A] ++ iterable
      new MultiIndexMap4Impl(bag, f1, makeIndex(bag, f1), f2, makeIndex(bag, f2), f3, makeIndex(bag, f3), f4, makeIndex(bag, f4))
    }
  }
}
