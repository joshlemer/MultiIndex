package com.github.joshlemer.multiindex

import com.github.joshlemer.multiset.MultiSet

import scala.collection.{mutable, IterableLike}

trait MultiIndex1[A, B1] extends MultiIndex1Like[A, B1, MultiIndex1[A, B1]] {

  def empty(f1: A => B1) = MultiIndex.empty(f1)

  def ==(that: MultiIndex1[A, B1]) =  f1 == that.f1 && multiSet == that.multiSet

  def equals(that: MultiIndex1[A, B1]) =  f1 == that.f1 && multiSet == that.multiSet
}

trait MultiIndex1Like[A, B1, +This <: MultiIndex1Like[A, B1, This] with MultiIndex1[A, B1]] extends IterableLike[A, This] with MultiIndex[A] {

  def empty(f1: A => B1): This

  /** First function to index elements on */
  def f1: A => B1

  /** Get a bag of all elements that match on both indexes with b1 and b2 */
  def get(b1: B1): List[A]

  /** Get a list of all elements that match b1 on index 1 */
  def get1(b1: B1): List[A]

  /** Get a bag of all elements that match b1 on index 1 */
  def get1MultiSet(b1: B1): MultiSet[A]

  /** Append an element to these elements, add it to the indexes */
  def + (a: A): MultiIndex1[A, B1]

  /** Remove one instance of a from these elements */
  def - (a: A): MultiIndex1[A, B1]

  /** Append elements to these elements, add them to the indexes */
  def ++ (as: Iterable[A]): MultiIndex1[A, B1]

  /** Remove one instance of each element from these elements and indexes */
  def -- (as: Iterable[A]): MultiIndex1[A, B1]

  def withIndex[B2](f2: A => B2): MultiIndex2[A, B1, B2]

  override protected[this] def newBuilder: mutable.Builder[A, This] = new MultiIndexMap1Builder[A, B1, This](empty(f1))
}
class MultiIndexMap1Builder[A, B1, Coll <: MultiIndex1[A, B1] with MultiIndex1Like[A, B1, Coll]](empty: Coll) extends mutable.Builder[A, Coll] {
  protected var elems: Coll = empty
  def +=(x: A): this.type = {
    elems = (elems + x).asInstanceOf[Coll]
    this
  }
  def clear() { elems = empty }
  def result(): Coll = elems
}

class MultiIndex1Impl[A, B1] private[multiindex] (
  val multiSet: MultiSet[A],
  val f1: A => B1,
  val index1: Index[A, B1]) extends MultiIndex1[A, B1] {

  def get(b1: B1) = get1(b1)

  def get1(b1: B1) = index1.getList(b1)//getList(index1, b1)

  def get1MultiSet(b1: B1) = index1(b1)

  def + (a: A) = new MultiIndex1Impl(multiSet + a, f1, index1 + a)

  def - (a: A) = new MultiIndex1Impl(multiSet - a, f1, index1 - a)

  def ++ (as: Iterable[A]) = new MultiIndex1Impl(multiSet ++ as, f1, index1 ++ as)

  /** Remove one instance of each element from these elements and indexes */
  def -- (as: Iterable[A]) = new MultiIndex1Impl(multiSet -- as, f1, index1 -- as)

  override def filter(p: A => Boolean) = new MultiIndex1Impl(multiSet.filter(p), f1, index1.filter(p))

  def withIndex[B2](f2: A => B2) = new MultiIndex2Impl(multiSet, f1, index1, f2, Index.fromMultiSet(multiSet)(f2))
}