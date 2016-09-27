package manymap

import scala.collection.Bag

trait MultiIndexMap1[A, B1] {
  /** Get a bag of all elements that match on both indexes with b1 and b2 */
  def get(b1: B1): Bag[A]

  def get1(b1: B1): List[A]

  /** Get a bag of all elements that match b1 on index 1 */
  def get1Bag(b1: B1): Bag[A]

  /** Append an element to these elements, add it to the indexes */
  def + (a: A): MultiIndexMap1[A, B1]

  /** Remove one instance of a from these elements */
  def - (a: A): MultiIndexMap1[A, B1]

  def withIndex[B2](f2: A => B2): MultiIndexMap2[A, B1, B2]
}

trait MultiIndexMap2[A, B1, B2] {
  /** Get a bag of all elements that match on both indexes with b1 and b2 */
  def get(b1: B1, b2: B2): Bag[A]

  def get1(b1: B1): List[A]

  /** Get a bag of all elements that match b1 on index 1 */
  def get1Bag(b1: B1): Bag[A]

  def get2(b2: B2): List[A]

  /** Get a bag of all elements that match b2 on index 2 */
  def get2Bag(b2: B2): Bag[A]

  /** Append an element to these elements, add it to the indexes */
  def + (a: A): MultiIndexMap2[A, B1, B2]

  /** Remove one instance of a from these elements */
  def - (a: A): MultiIndexMap2[A, B1, B2]
}

trait MultiIndexMap3[A, B1, B2, B3] {
  /** Get a bag of all elements that match on both indexes with b1 and b2 */
  def get(b1: B1, b2: B2, b3: B3): Bag[A]

  def get1(b1: B1): List[A]

  /** Get a bag of all elements that match b1 on index 1 */
  def get1Bag(b1: B1): Bag[A]

  def get2(b2: B2): List[A]

  /** Get a bag of all elements that match b2 on index 2 */
  def get2Bag(b2: B2): Bag[A]

  def get3(b3: B3): List[A]

  /** Get a bag of all elements that match b2 on index 2 */
  def get3Bag(b3: B3): Bag[A]

  /** Append an element to these elements, add it to the indexes */
  def + (a: A): MultiIndexMap3[A, B1, B2, B3]

  /** Remove one instance of a from these elements */
  def - (a: A): MultiIndexMap3[A, B1, B2, B3]
}

trait MultiIndexMap4[A, B1, B2, B3, B4] {
  /** Get a bag of all elements that match on both indexes with b1 and b2 */
  def get(b1: B1, b2: B2, b3: B3, b4: B4): Bag[A]

  def get1(b1: B1): List[A]

  /** Get a bag of all elements that match b1 on index 1 */
  def get1Bag(b1: B1): Bag[A]

  def get2(b2: B2): List[A]

  /** Get a bag of all elements that match b2 on index 2 */
  def get2Bag(b2: B2): Bag[A]

  def get3(b3: B3): List[A]

  /** Get a bag of all elements that match b2 on index 2 */
  def get3Bag(b3: B2): Bag[A]

  def get4(b4: B4): List[A]

  /** Get a bag of all elements that match b2 on index 2 */
  def get4Bag(b4: B4): Bag[A]

  /** Append an element to these elements, add it to the indexes */
  def + (a: A): MultiIndexMap4[A, B1, B2, B3, B4]

  /** Remove one instance of a from these elements */
  def - (a: A): MultiIndexMap4[A, B1, B2, B3, B4]
}

private[manymap] object Utils {
  implicit def config[A] = Bag.configuration.compact[A]

  def add[A, BN](a: A, bn: BN, index: Map[BN, Bag[A]]): Map[BN, Bag[A]] =
    index + (bn -> (index.getOrElse(bn, Bag.empty[A]) + a))

  def remove[A, BN](a: A, bn: BN, index: Map[BN, Bag[A]]): Map[BN, Bag[A]] = {
    index.get(bn) match {
      case Some(bnBag) =>
        val removedBag = bnBag - a
        if (removedBag.isEmpty) index - bn
        else index + (bn -> removedBag)
      case None => index
    }
  }

  def getList[A, BN](index: Map[BN, Bag[A]], bn: BN) = index.get(bn).map(_.toList).getOrElse(Nil)

  def getBag[A, BN](index: Map[BN, Bag[A]], bn: BN) = index.getOrElse(bn, Bag.empty[A])

  def makeIndex[A, BN](bag: Bag[A], fn: A => BN) = bag.groupBy(fn).mapValues(Bag.empty[A] ++ _)
}

import Utils._

class MultiIndexMap1Impl[A, B1] private[manymap] (
  val bag: Bag[A],
  f1: A => B1,
  val index1: Map[B1, Bag[A]]) extends MultiIndexMap1[A, B1] {

  def get(b1: B1) = getBag(index1, b1)

  def get1(b1: B1) = getList(index1, b1)

  def get1Bag(b1: B1) = getBag(index1, b1)

  def + (a: A) = new MultiIndexMap1Impl(bag + a, f1, add(a, f1(a), index1))

  def - (a: A) = new MultiIndexMap1Impl(bag - a, f1, remove(a, f1(a), index1))

  def withIndex[B2](f2: A => B2) = new MultiIndexMap2Impl(bag, f1, index1, f2, makeIndex(bag, f2))
}

class MultiIndexMap2Impl[A, B1, B2] private[manymap] (
  val bag: Bag[A],
  f1: A => B1,
  val index1: Map[B1, Bag[A]],
  f2: A => B2,
  val index2: Map[B2, Bag[A]]) extends MultiIndexMap2[A, B1, B2] {

  def get(b1: B1, b2: B2) = get1Bag(b1).intersect(get2Bag(b2))

  def get1(b1: B1) = index1.get(b1).map(_.toList).getOrElse(Nil)
  def get1Bag(b1: B1) = index1.getOrElse(b1, Bag.empty)

  def get2(b2: B2) = index2.get(b2).map(_.toList).getOrElse(Nil)
  def get2Bag(b2: B2) = index2.getOrElse(b2, Bag.empty)

  def + (a: A) = new MultiIndexMap2Impl(bag + a, f1, add(a, f1(a), index1), f2, add(a, f2(a), index2))

  def - (a: A) = new MultiIndexMap2Impl(bag - a, f1, add(a, f1(a), index1), f2, add(a, f2(a), index2))

}

object MultiIndexMapObj {
  implicit class IterableOps[A](iterable: Iterable[A]) {
    def asMultiIndexMap[B1, B2](f1: A => B1, f2: A => B2): MultiIndexMap2[A, B1, B2] = {
      val bag = Bag.empty[A] ++ iterable
      new MultiIndexMap2Impl(bag, f1, makeIndex(bag, f1), f2, makeIndex(bag, f2))
    }
  }
}

