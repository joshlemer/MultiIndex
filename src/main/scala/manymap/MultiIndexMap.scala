package manymap

import scala.collection.Bag

/** Base trait of all MultiIndexMaps of any dimension */
trait MultiIndexMap[A] extends Iterable[A] {
  def multiSet: MultiSet[A]

  override def iterator: Iterator[A] = multiSet.toList.toIterator
}

trait MultiIndexMap1[A, B1] extends MultiIndexMap[A] {

  /** First function to index elements on */
  def f1: A => B1

  /** Get a bag of all elements that match on both indexes with b1 and b2 */
  def get(b1: B1): List[A]

  /** Get a list of all elements that match b1 on index 1 */
  def get1(b1: B1): List[A]

  /** Get a bag of all elements that match b1 on index 1 */
  def get1MultiSet(b1: B1): MultiSet[A]

  /** Append an element to these elements, add it to the indexes */
  def + (a: A): MultiIndexMap1[A, B1]

  /** Remove one instance of a from these elements */
  def - (a: A): MultiIndexMap1[A, B1]

  /** Append elements to these elements, add them to the indexes */
  def ++ (as: Iterable[A]): MultiIndexMap1[A, B1]

  /** Remove one instance of each element from these elements and indexes */
  def -- (as: Iterable[A]): MultiIndexMap1[A, B1]

  def withIndex[B2](f2: A => B2): MultiIndexMap2[A, B1, B2]

  def ==(that: MultiIndexMap1[A, B1]) = multiSet == that.multiSet && f1 == that.f1
}

trait MultiIndexMap2[A, B1, B2] extends MultiIndexMap[A] {

  /** First function to index elements on */
  def f1: A => B1

  /** Second function to index elements on */
  def f2: A => B2

  /** Get a List of all elements that match on both indexes with b1 and b2 */
  def get(b1: B1, b2: B2): List[A]

  /** Get a List of all elements that match on the first index */
  def get1(b1: B1): List[A]

  /** Get a bag of all elements that match b1 on index 1 */
  def get1MultiSet(b1: B1): MultiSet[A]

  def get2(b2: B2): List[A]

  /** Get a bag of all elements that match b2 on index 2 */
  def get2MultiSet(b2: B2): MultiSet[A]

  /** Append an element to these elements, add it to the indexes */
  def + (a: A): MultiIndexMap2[A, B1, B2]

  /** Remove one instance of a from these elements */
  def - (a: A): MultiIndexMap2[A, B1, B2]

  /** Append elements to these elements, add them to the indexes */
  def ++ (as: Iterable[A]): MultiIndexMap2[A, B1, B2]

  /** Remove one instance of each element from these elements and indexes */
  def -- (as: Iterable[A]): MultiIndexMap2[A, B1, B2]

  def withIndex[B3](f3: A => B3): MultiIndexMap3[A, B1, B2, B3]

  def ==(that: MultiIndexMap2[A, _, _]) = multiSet == that.multiSet && f1 == that.f1 && f2 == that.f2
}

trait MultiIndexMap3[A, B1, B2, B3] extends MultiIndexMap[A] {
  /** First function to index elements on */
  def f1: A => B1
  /** Second function to index elements on */
  def f2: A => B2
  /** Second function to index elements on */
  def f3: A => B3
  /** Get a bag of all elements that match on both indexes with b1 and b2 */
  def get(b1: B1, b2: B2, b3: B3): List[A]

  /** Get a List of all elements that match on the first index */
  def get1(b1: B1): List[A]

  /** Get a bag of all elements that match b1 on index 1 */
  def get1MultiSet(b1: B1): MultiSet[A]

  def get2(b2: B2): List[A]

  /** Get a bag of all elements that match b2 on index 2 */
  def get2MultiSet(b2: B2): MultiSet[A]

  def get3(b3: B3): List[A]

  /** Get a bag of all elements that match b2 on index 2 */
  def get3MultiSet(b3: B3): MultiSet[A]

  /** Append an element to these elements, add it to the indexes */
  def + (a: A): MultiIndexMap3[A, B1, B2, B3]

  /** Remove one instance of a from these elements */
  def - (a: A): MultiIndexMap3[A, B1, B2, B3]

  /** Append elements to these elements, add them to the indexes */
  def ++ (as: Iterable[A]): MultiIndexMap3[A, B1, B2, B3]

  /** Remove one instance of each element from these elements and indexes */
  def -- (as: Iterable[A]): MultiIndexMap3[A, B1, B2, B3]

  def withIndex[B4](f4: A => B4): MultiIndexMap4[A, B1, B2, B3, B4]

  def ==(that: MultiIndexMap3[A, _, _, _]) = multiSet == that.multiSet && f1 == that.f1 && f2 == that.f2 && f3 == that.f3
}

trait MultiIndexMap4[A, B1, B2, B3, B4] extends MultiIndexMap[A] {
  /** First function to index elements on */
  def f1: A => B1
  /** Second function to index elements on */
  def f2: A => B2
  /** Second function to index elements on */
  def f3: A => B3
  /** Fourth function to index elements on */
  def f4: A => B4

  /** Get a bag of all elements that match on both indexes with b1 and b2 */
  def get(b1: B1, b2: B2, b3: B3, b4: B4): List[A]

  /** Get a List of all elements that match on the first index */
  def get1(b1: B1): List[A]

  /** Get a bag of all elements that match b1 on index 1 */
  def get1MultiSet(b1: B1): MultiSet[A]

  def get2(b2: B2): List[A]

  /** Get a bag of all elements that match b2 on index 2 */
  def get2MultiSet(b2: B2): MultiSet[A]

  def get3(b3: B3): List[A]

  /** Get a bag of all elements that match b2 on index 2 */
  def get3MultiSet(b3: B3): MultiSet[A]

  def get4(b4: B4): List[A]

  /** Get a bag of all elements that match b2 on index 2 */
  def get4MultiSet(b4: B4): MultiSet[A]

  /** Append an element to these elements, add it to the indexes */
  def + (a: A): MultiIndexMap4[A, B1, B2, B3, B4]

  /** Remove one instance of a from these elements */
  def - (a: A): MultiIndexMap4[A, B1, B2, B3, B4]

  /** Append elements to these elements, add them to the indexes */
  def ++ (as: Iterable[A]): MultiIndexMap4[A, B1, B2, B3, B4]

  /** Remove one instance of each element from these elements and indexes */
  def -- (as: Iterable[A]): MultiIndexMap4[A, B1, B2, B3, B4]

  def ==(that: MultiIndexMap4[A, _, _, _, _]) = multiSet == that.multiSet && f1 == that.f1 && f2 == that.f2 && f3 == that.f3
}

object MultiSet {
  def apply[A](iterable: Iterable[A]) = new MultiSet[A](Map.empty) ++ iterable

  def empty[A] = apply[A](Nil)
}

class MultiSet[A](inner: Map[A, Int]) {
  private lazy val _inner = inner.withDefaultValue(0)

  def apply(a: A): Int = _inner(a)

  def +(a: A, multiplicity: Int = 1) = if(multiplicity < 1) this else new MultiSet(inner + (a -> (_inner(a) + multiplicity)))
  def -(a: A, multiplicity: Int = 1) = if(multiplicity < 1) this else inner.get(a) match {
    case Some(0) | None => this
    case Some(few) if few <= multiplicity => new MultiSet(inner - a)
    case Some(many) => new MultiSet(_inner + (a -> (_inner(a) - multiplicity)))
  }

  def ++(as: Iterable[A]) = as.foldLeft(this){ case (ms, a) => ms + a}
  def --(as: Iterable[A]) = as.foldLeft(this){ case (ms, a) => ms - a}

  def intersect(that: MultiSet[A]): MultiSet[A] = {
    val (small, big) = if(size < that.size) (this, that) else (that, this)
    small._inner.foldLeft(MultiSet.empty[A]){ case (ms, (a, int)) => ms + (a, small(a).min(big(a)))}
  }

  def toList: List[A] = inner.flatMap{ case(k, v) => List.fill(v)(k) }.toList

  def isEmpty: Boolean = inner.isEmpty

  val size = _inner.size
}

object JIndex{
  def apply[A, B](es: Iterable[A], f: A => B) = new JIndex[A, B](Map.empty) ++ (es, f)
}

class JIndex[A, B](elems: Map[B, MultiSet[A]]) {

  val _elems = elems.withDefaultValue(MultiSet.empty[A])

  def apply(b: B) = _elems(b)

  def + (a: A, b: B): JIndex[A, B] = new JIndex(_elems + (b -> (_elems(b) + a )))

  def - (a: A, b: B): JIndex[A, B] = {
    val newElems = _elems(b) match {
      case empty if empty.isEmpty => _elems
      case nonEmpty =>
        nonEmpty(a) match {
          case 0 => _elems
          case 1 =>
            val removed = nonEmpty - a
            if (removed.isEmpty) _elems - b
            else _elems + (b -> removed)
        }
    }
    new JIndex(newElems)
  }

  def ++ (as: Iterable[A], f: A => B): JIndex[A, B] = as.foldLeft(this){ case (ind, a) => ind + (a, f(a)) }

  def -- (as: Iterable[A], f: A => B): JIndex[A, B] = as.foldLeft(this){ case (ind, a) => ind - (a, f(a))}

  def getList(b: B): List[A] = _elems(b).toList

  def toList: List[A] = _elems.keys.flatMap(getList).toList
}

class MultiIndexMap1Impl[A, B1] private[manymap] (
  val multiSet: MultiSet[A],
  val f1: A => B1,
  val index1: JIndex[A, B1]) extends MultiIndexMap1[A, B1] {

  def get(b1: B1) = get1(b1)

  def get1(b1: B1) = index1.getList(b1)//getList(index1, b1)

  def get1MultiSet(b1: B1) = index1(b1)

  def + (a: A) = new MultiIndexMap1Impl(multiSet + a, f1, index1 + (a, f1(a)))

  def - (a: A) = new MultiIndexMap1Impl(multiSet - a, f1, index1 - (a, f1(a)))

//  def ++ (as: Iterable[A]) = new MultiIndexMap1Impl(bag ++ as, f1, index1, index1Exp ++ (as, f1))
  def ++ (as: Iterable[A]) = new MultiIndexMap1Impl(multiSet ++ as, f1, index1 ++ (as, f1))

  /** Remove one instance of each element from these elements and indexes */
  def -- (as: Iterable[A]) = new MultiIndexMap1Impl(multiSet -- as, f1, index1 -- (as, f1))

  def withIndex[B2](f2: A => B2) = new MultiIndexMap2Impl(multiSet, f1, index1, f2, JIndex(multiSet.toList, f2))
}

class MultiIndexMap2Impl[A, B1, B2] private[manymap] (
  val multiSet: MultiSet[A],
  val f1: A => B1,
  val index1: JIndex[A, B1],
  val f2: A => B2,
  val index2: JIndex[A, B2]) extends MultiIndexMap2[A, B1, B2] {

  def get(b1: B1, b2: B2) = get1MultiSet(b1).intersect(get2MultiSet(b2)).toList

  def get1(b1: B1) = index1.getList(b1)
  def get1MultiSet(b1: B1) = index1(b1)

  def get2(b2: B2) = index2.getList(b2)
  def get2MultiSet(b2: B2) = index2(b2)

  def + (a: A) = new MultiIndexMap2Impl(multiSet + a, f1, index1 + (a, f1(a)), f2, index2 + (a, f2(a)))

  def - (a: A) = new MultiIndexMap2Impl(multiSet - a, f1, index1 - (a, f1(a)), f2, index2 - (a, f2(a)))

//  def ++ (as: Iterable[A]) = new MultiIndexMap1Impl(bag ++ as, f1, index1, index1Exp ++ (as, f1))
  def ++ (as: Iterable[A]) = new MultiIndexMap2Impl(multiSet ++ as, f1, index1 ++ (as, f1), f2, index2 ++ (as, f2))

  /** Remove one instance of each element from these elements and indexes */
  def -- (as: Iterable[A]) = new MultiIndexMap2Impl(multiSet -- as, f1, index1 -- (as, f1), f2, index2 ++ (as, f2))

  def withIndex[B3](f3: A => B3) = new MultiIndexMap3Impl(multiSet, f1, index1, f2, index2, f3, JIndex(multiSet.toList, f3))
}

class MultiIndexMap3Impl[A, B1, B2, B3] private[manymap] (
  val multiSet: MultiSet[A],
  val f1: A => B1,
  val index1: JIndex[A, B1],
  val f2: A => B2,
  val index2: JIndex[A, B2],
  val f3: A => B3,
  val index3: JIndex[A, B3]) extends MultiIndexMap3[A, B1, B2, B3] {

  def get(b1: B1, b2: B2, b3: B3) = get1MultiSet(b1).intersect(get2MultiSet(b2)).intersect(get3MultiSet(b3)).toList

  def get1(b1: B1) = index1.getList(b1)
  def get1MultiSet(b1: B1) = index1(b1)

  def get2(b2: B2) = index2.getList(b2)
  def get2MultiSet(b2: B2) = index2(b2)

  def get3(b3: B3) = index3.getList(b3)
  def get3MultiSet(b3: B3) = index3(b3)

  def + (a: A) = new MultiIndexMap3Impl(multiSet + a, f1, index1 + (a, f1(a)), f2, index2 + (a, f2(a)), f3, index3 + (a, f3(a)))

  def - (a: A) = new MultiIndexMap3Impl(multiSet - a, f1, index1 - (a, f1(a)), f2, index2 - (a, f2(a)), f3, index3 - (a, f3(a)))

  //  def ++ (as: Iterable[A]) = new MultiIndexMap1Impl(bag ++ as, f1, index1, index1Exp ++ (as, f1))
  def ++ (as: Iterable[A]) = new MultiIndexMap3Impl(multiSet ++ as, f1, index1 ++ (as, f1), f2, index2 ++ (as, f2), f3, index3 ++ (as, f3))

  /** Remove one instance of each element from these elements and indexes */
  def -- (as: Iterable[A]) = new MultiIndexMap3Impl(multiSet -- as, f1, index1 -- (as, f1), f2, index2 ++ (as, f2), f3, index3 ++ (as, f3))

  def withIndex[B4](f4: A => B4) = new MultiIndexMap4Impl(multiSet, f1, index1, f2, index2, f3, index3, f4, JIndex(multiSet.toList, f4))
}

class MultiIndexMap4Impl[A, B1, B2, B3, B4] private[manymap] (
  val multiSet: MultiSet[A],
  val f1: A => B1,
  val index1: JIndex[A, B1],
  val f2: A => B2,
  val index2: JIndex[A, B2],
  val f3: A => B3,
  val index3: JIndex[A, B3],
  val f4: A => B4,
  val index4: JIndex[A, B4]) extends MultiIndexMap4[A, B1, B2, B3, B4] {

  def get(b1: B1, b2: B2, b3: B3, b4: B4) =
    get1MultiSet(b1).intersect(get2MultiSet(b2)).intersect(get3MultiSet(b3)).intersect(get4MultiSet(b4)).toList

  def get1(b1: B1) = index1.getList(b1)
  def get1MultiSet(b1: B1) = index1(b1)

  def get2(b2: B2) = index2.getList(b2)
  def get2MultiSet(b2: B2) = index2(b2)

  def get3(b3: B3) = index3.getList(b3)
  def get3MultiSet(b3: B3) = index3(b3)

  def get4(b4: B4) = index4.getList(b4)
  def get4MultiSet(b4: B4) = index4(b4)

  def + (a: A) = new MultiIndexMap4Impl(multiSet + a, f1, index1 + (a, f1(a)), f2, index2 + (a, f2(a)), f3, index3 + (a, f3(a)), f4, index4 + (a, f4(a)))

  def - (a: A) = new MultiIndexMap4Impl(multiSet - a, f1, index1 - (a, f1(a)), f2, index2 - (a, f2(a)), f3, index3 - (a, f3(a)), f4, index4 - (a, f4(a)))

  //  def ++ (as: Iterable[A]) = new MultiIndexMap1Impl(bag ++ as, f1, index1, index1Exp ++ (as, f1))
  def ++ (as: Iterable[A]) = new MultiIndexMap4Impl(multiSet ++ as, f1, index1 ++ (as, f1), f2, index2 ++ (as, f2), f3, index3 ++ (as, f3), f4, index4 ++ (as, f4))

  /** Remove one instance of each element from these elements and indexes */
  def -- (as: Iterable[A]) = new MultiIndexMap4Impl(multiSet -- as, f1, index1 -- (as, f1), f2, index2 ++ (as, f2), f3, index3 ++ (as, f3), f4, index4 ++ (as, f4))
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

object Foo extends App {

  def time[R](block: => R): R = {
    val t0 = System.currentTimeMillis()
    val result = block    // call-by-name
    val t1 = System.currentTimeMillis()
    println("Elapsed time: " + (t1 - t0) + "ms")
    result
  }

  val x = time((1 to 1000000).indexBy(_ / 4))
  println(time(x.get1(1000)))
  println(x.get1(100000))
}
