package multiindex

import scala.collection.{mutable, IterableLike}

trait MultiIndex2[A, B1, B2] extends MultiIndex2Like[A, B1, B2, MultiIndex2[A, B1, B2]] {

  def empty(f1: A => B1, f2: A => B2) = MultiIndex.empty(f1, f2)

  def ==(that: MultiIndex2[A, B1, B2]) = f1 == that.f1 && f2 == that.f2 && multiSet == that.multiSet
}

trait MultiIndex2Like[A, B1, B2, +This <: MultiIndex2Like[A, B1, B2, This] with MultiIndex2[A, B1, B2]] extends IterableLike[A, This] with MultiIndex[A] {

  def empty(f1: A => B1, f2: A => B2): This

  /** First function to index elements on */
  def f1: A => B1

  /** Second function to index elements on */
  def f2: A => B2

  /** Get a bag of all elements that match on both indexes with b1 and b2 */
  def get(b1: B1, b2: B2): List[A]

  /** Get a list of all elements that match b1 on index 1 */
  def get1(b1: B1): List[A]

  /** Get a bag of all elements that match b1 on index 1 */
  def get1MultiSet(b1: B1): MultiSet[A]

  /** Get a list of all elements that match b2 on index 2 */
  def get2(b2: B2): List[A]

  /** Get a bag of all elements that match b2 on index 2 */
  def get2MultiSet(b2: B2): MultiSet[A]

  /** Append an element to these elements, add it to the indexes */
  def + (a: A): MultiIndex2[A, B1, B2]

  /** Remove one instance of a from these elements */
  def - (a: A): MultiIndex2[A, B1, B2]

  /** Append elements to these elements, add them to the indexes */
  def ++ (as: Iterable[A]): MultiIndex2[A, B1, B2]

  /** Remove one instance of each element from these elements and indexes */
  def -- (as: Iterable[A]): MultiIndex2[A, B1, B2]

  def withIndex[B3](f2: A => B3): MultiIndex3[A, B1, B2, B3]

  override protected[this] def newBuilder: mutable.Builder[A, This] = new MultiIndexMap2Builder[A, B1, B2, This](empty(f1, f2))
}
class MultiIndexMap2Builder[A, B1, B2, Coll <: MultiIndex2[A, B1, B2] with MultiIndex2Like[A, B1, B2, Coll]](empty: Coll) extends mutable.Builder[A, Coll] {
  protected var elems: Coll = empty
  def +=(x: A): this.type = {
    elems = (elems + x).asInstanceOf[Coll]
    this
  }
  def clear() { elems = empty }
  val result: Coll = elems
}

class MultiIndex2Impl[A, B1, B2] private[multiindex] (
  val multiSet: MultiSet[A],
  val f1: A => B1,
  val index1: Index[A, B1],
  val f2: A => B2,
  val index2: Index[A, B2]) extends MultiIndex2[A, B1, B2] {

  def get(b1: B1, b2: B2) = get1(b1).intersect(get2(b2))

  def get1(b1: B1) = index1.getList(b1)

  def get1MultiSet(b1: B1) = index1(b1)

  def get2(b2: B2) = index2.getList(b2)

  def get2MultiSet(b2: B2) = index2(b2)

  def + (a: A) = new MultiIndex2Impl(multiSet + a, f1, index1 + a, f2, index2 + a)

  def - (a: A) = new MultiIndex2Impl(multiSet - a, f1, index1 - a, f2, index2 - a)

  def ++ (as: Iterable[A]) = new MultiIndex2Impl(multiSet ++ as, f1, index1 ++ as, f2, index2 ++ as)

  /** Remove one instance of each element from these elements and indexes */
  def -- (as: Iterable[A]) = new MultiIndex2Impl(multiSet -- as, f1, index1 -- as, f2, index2 -- as)

  override def filter(p: A => Boolean) = new MultiIndex2Impl(multiSet.filter(p), f1, index1.filter(p), f2, index2.filter(p))

  def withIndex[B3](f3: A => B3) = new MultiIndex3Impl(multiSet, f1, index1, f2, index2, f3, Index(f3, multiSet))
}