package manymap

import scala.collection.Bag

trait MultiIndexMap[A] extends Iterable[A] {
  def bag: Bag[A]

  def iterator: Iterator[A] = bag.toIterator
}

trait MultiIndexMap1[A, B1] extends MultiIndexMap[A] with Iterable[A] {

  def f1: A => B1

  def bag: Bag[A]

  /** Get a bag of all elements that match on both indexes with b1 and b2 */
  def get(b1: B1): Bag[A]

  def get1(b1: B1): List[A]

  /** Get a bag of all elements that match b1 on index 1 */
  def get1Bag(b1: B1): Bag[A]

  /** Append an element to these elements, add it to the indexes */
  def + (a: A): MultiIndexMap1[A, B1]

  /** Remove one instance of a from these elements */
  def - (a: A): MultiIndexMap1[A, B1]

  def ++ (as: Iterable[A]): MultiIndexMap1[A, B1]

  def -- (as: Iterable[A]): MultiIndexMap1[A, B1]

  def withIndex[B2](f2: A => B2): MultiIndexMap2[A, B1, B2]

  def ==(that: MultiIndexMap1[A, B1]) = bag == that.bag && f1 == that.f1
}

trait MultiIndexMap2[A, B1, B2] extends MultiIndexMap[A] {

  def f1: A => B1

  def f2: A => B2

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

  def ++ (as: Iterable[A]): MultiIndexMap2[A, B1, B2]

  def -- (as: Iterable[A]): MultiIndexMap2[A, B1, B2]

  def withIndex[B3](f3: A => B3): MultiIndexMap3[A, B1, B2, B3]

  def ==(that: MultiIndexMap2[A, _, _]) = bag == that.bag && f1 == that.f1 && f2 == that.f2
}

trait MultiIndexMap3[A, B1, B2, B3] extends MultiIndexMap[A] {
  def f1: A => B1
  def f2: A => B2
  def f3: A => B3
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

  def ++ (as: Iterable[A]): MultiIndexMap3[A, B1, B2, B3]

  def -- (as: Iterable[A]): MultiIndexMap3[A, B1, B2, B3]

  def withIndex[B4](f4: A => B4): MultiIndexMap4[A, B1, B2, B3, B4]

  def ==(that: MultiIndexMap3[A, _, _, _]) = bag == that.bag && f1 == that.f1 && f2 == that.f2 && f3 == that.f3
}

trait MultiIndexMap4[A, B1, B2, B3, B4] extends MultiIndexMap[A] {
  def f1: A => B1
  def f2: A => B2
  def f3: A => B3
  def f4: A => B4

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
  def get3Bag(b3: B3): Bag[A]

  def get4(b4: B4): List[A]

  /** Get a bag of all elements that match b2 on index 2 */
  def get4Bag(b4: B4): Bag[A]

  /** Append an element to these elements, add it to the indexes */
  def + (a: A): MultiIndexMap4[A, B1, B2, B3, B4]

  /** Remove one instance of a from these elements */
  def - (a: A): MultiIndexMap4[A, B1, B2, B3, B4]

  def ++ (as: Iterable[A]): MultiIndexMap4[A, B1, B2, B3, B4]

  def -- (as: Iterable[A]): MultiIndexMap4[A, B1, B2, B3, B4]

  def ==(that: MultiIndexMap4[A, _, _, _, _]) = bag == that.bag && f1 == that.f1 && f2 == that.f2 && f3 == that.f3
}

private[manymap] object Utils {
  implicit def config[A] = Bag.configuration.compact[A]

  def add[A, BN](a: A, bn: BN, index: Map[BN, Bag[A]]): Map[BN, Bag[A]] =
    index + (bn -> (index.getOrElse(bn, Bag.empty[A]) + a))

  def addMany[A, BN](abs: Iterable[A], f: A => BN, index: Map[BN, Bag[A]]): Map[BN, Bag[A]] =
    abs.foldLeft(index){ case (ind, a) => add(a, f(a), ind )}

  def remove[A, BN](a: A, bn: BN, index: Map[BN, Bag[A]]): Map[BN, Bag[A]] = {
    index.get(bn) match {
      case Some(bnBag) =>
        val removedBag = bnBag - a
        if (removedBag.isEmpty) index - bn
        else index + (bn -> removedBag)
      case None => index
    }
  }

  def removeMany[A, BN](as: Iterable[A], f: A => BN, index: Map[BN, Bag[A]]): Map[BN, Bag[A]] =
    as.foldLeft(index){ case (ind, a) => remove(a, f(a), ind)}

  def getList[A, BN](index: Map[BN, Bag[A]], bn: BN) = index.get(bn).map(_.toList).getOrElse(Nil)

  def getBag[A, BN](index: Map[BN, Bag[A]], bn: BN) = index.getOrElse(bn, Bag.empty[A])

  def makeIndex[A, BN](bag: Bag[A], fn: A => BN) = bag.groupBy(fn).mapValues(Bag.empty[A] ++ _)
}

import Utils._

class MultiIndexMap1Impl[A, B1] private[manymap] (
  val bag: Bag[A],
  val f1: A => B1,
  index1: Map[B1, Bag[A]]) extends MultiIndexMap1[A, B1] {

  def get(b1: B1) = getBag(index1, b1)

  def get1(b1: B1) = getList(index1, b1)

  def get1Bag(b1: B1) = getBag(index1, b1)

  def + (a: A) = new MultiIndexMap1Impl(bag + a, f1, add(a, f1(a), index1))

  def - (a: A) = new MultiIndexMap1Impl(bag - a, f1, remove(a, f1(a), index1))

  def ++ (as: Iterable[A]) = new MultiIndexMap1Impl(bag ++ as, f1, addMany(as, f1, index1))

  def -- (as: Iterable[A]) = new MultiIndexMap1Impl(bag -- as, f1, removeMany(as, f1, index1))

  def withIndex[B2](f2: A => B2) = new MultiIndexMap2Impl(bag, f1, index1, f2, makeIndex(bag, f2))
}

class MultiIndexMap2Impl[A, B1, B2] private[manymap] (
  val bag: Bag[A],
  val f1: A => B1,
  val index1: Map[B1, Bag[A]],
  val f2: A => B2,
  val index2: Map[B2, Bag[A]]) extends MultiIndexMap2[A, B1, B2] {

  def get(b1: B1, b2: B2) = get1Bag(b1).intersect(get2Bag(b2))

  def get1(b1: B1) = index1.get(b1).map(_.toList).getOrElse(Nil)
  def get1Bag(b1: B1) = index1.getOrElse(b1, Bag.empty)

  def get2(b2: B2) = index2.get(b2).map(_.toList).getOrElse(Nil)
  def get2Bag(b2: B2) = index2.getOrElse(b2, Bag.empty)

  def + (a: A) = new MultiIndexMap2Impl(bag + a, f1, add(a, f1(a), index1), f2, add(a, f2(a), index2))

  def - (a: A) = new MultiIndexMap2Impl(bag - a, f1, add(a, f1(a), index1), f2, add(a, f2(a), index2))

  def ++ (as: Iterable[A]) = new MultiIndexMap2Impl(bag ++ as, f1, addMany(as, f1, index1), f2, addMany(as, f2, index2))

  def -- (as: Iterable[A]) = new MultiIndexMap2Impl(bag -- as, f1, removeMany(as, f1, index1), f2, removeMany(as, f2, index2))

  def withIndex[B3](f3: A => B3) = new MultiIndexMap3Impl(bag, f1, index1, f2, index2, f3, makeIndex(bag, f3))
}

class MultiIndexMap3Impl[A, B1, B2, B3] private[manymap] (
  val bag: Bag[A],
  val f1: A => B1,
  index1: Map[B1, Bag[A]],
  val f2: A => B2,
  index2: Map[B2, Bag[A]],
  val f3: A => B3,
  index3: Map[B3, Bag[A]]
  ) extends MultiIndexMap3[A, B1, B2, B3] {

  def get(b1: B1, b2: B2, b3: B3) = get1Bag(b1).intersect(get2Bag(b2)).intersect(get3Bag(b3))

  def get1(b1: B1) = getList(index1, b1)
  def get1Bag(b1: B1) = getBag(index1, b1)

  def get2(b2: B2) = getList(index2, b2)
  def get2Bag(b2: B2) = getBag(index2, b2)

  def get3(b3: B3) = getList(index3, b3)
  def get3Bag(b3: B3) = getBag(index3, b3)

  def + (a: A) = new MultiIndexMap3Impl(bag + a, f1, add(a, f1(a), index1), f2, add(a, f2(a), index2), f3, add(a, f3(a), index3))

  def - (a: A) = new MultiIndexMap3Impl(bag - a, f1, remove(a, f1(a), index1), f2, remove(a, f2(a), index2), f3, remove(a, f3(a), index3))

  def ++ (as: Iterable[A]) =
    new MultiIndexMap3Impl(bag ++ as, f1, addMany(as, f1, index1), f2, addMany(as, f2, index2), f3, addMany(as, f3, index3))

  def -- (as: Iterable[A]) =
    new MultiIndexMap3Impl(bag -- as, f1, removeMany(as, f1, index1), f2, removeMany(as, f2, index2), f3, removeMany(as, f3, index3))

  def withIndex[B4](f4: A => B4) = new MultiIndexMap4Impl(bag, f1, index1, f2, index2, f3, index3, f4, makeIndex(bag, f4))
}

class MultiIndexMap4Impl[A, B1, B2, B3, B4] private[manymap] (
  val bag: Bag[A],
  val f1: A => B1,
  index1: Map[B1, Bag[A]],
  val f2: A => B2,
  index2: Map[B2, Bag[A]],
  val f3: A => B3,
  index3: Map[B3, Bag[A]],
  val f4: A => B4,
  index4: Map[B4, Bag[A]]
  ) extends MultiIndexMap4[A, B1, B2, B3, B4] {

  def get(b1: B1, b2: B2, b3: B3, b4: B4) = get1Bag(b1).intersect(get2Bag(b2)).intersect(get3Bag(b3)).intersect(get4Bag(b4))

  def get1(b1: B1) = getList(index1, b1)
  def get1Bag(b1: B1) = getBag(index1, b1)

  def get2(b2: B2) = getList(index2, b2)
  def get2Bag(b2: B2) = getBag(index2, b2)

  def get3(b3: B3) = getList(index3, b3)
  def get3Bag(b3: B3) = getBag(index3, b3)

  def get4(b4: B4) = getList(index4, b4)
  def get4Bag(b4: B4) = getBag(index4, b4)

  def + (a: A) = new MultiIndexMap4Impl(bag + a, f1, add(a, f1(a), index1), f2, add(a, f2(a), index2), f3, add(a, f3(a), index3), f4, add(a, f4(a), index4))

  def - (a: A) = new MultiIndexMap4Impl(bag - a, f1, remove(a, f1(a), index1), f2, remove(a, f2(a), index2), f3, remove(a, f3(a), index3), f4, remove(a, f4(a), index4))

  def ++ (as: Iterable[A]) =
    new MultiIndexMap4Impl(bag ++ as, f1, addMany(as, f1, index1), f2, addMany(as, f2, index2), f3, addMany(as, f3, index3), f4, addMany(as, f4, index4))

  def -- (as: Iterable[A]) =
    new MultiIndexMap4Impl(bag -- as, f1, removeMany(as, f1, index1), f2, removeMany(as, f2, index2), f3, removeMany(as, f3, index3), f4, addMany(as, f4, index4))
}

object MultiIndexMap {
  class MultiIndexMapFactory[A](iterable: Iterable[A]) {
    def apply[B1](f1: A => B1) = iterable.indexBy(f1)
    def apply[B1, B2](f1: A => B1, f2: A => B2) = iterable.indexBy(f1, f2)
    def apply[B1, B2, B3](f1: A => B1, f2: A => B2, f3: A => B3) = iterable.indexBy(f1, f2, f3)
    def apply[B1, B2, B3, B4](f1: A => B1, f2: A => B2, f3: A => B3, f4: A => B4) = iterable.indexBy(f1, f2, f3, f4)
  }
  def apply[A, B1](iterable: Iterable[A]) = new MultiIndexMapFactory(iterable)
}

