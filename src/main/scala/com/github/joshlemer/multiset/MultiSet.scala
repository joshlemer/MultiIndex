package com.github.joshlemer.multiset

object MultiSet {
  def apply[A](as: A*) = as.toMultiSet

  def empty[A] = new MultiSet[A](Map.empty)
}

private[multiset] object utils {
  def smallBig[A](left: MultiSet[A], right: MultiSet[A]) = if(left.size < right.size) (left, right) else (right, left)
}

class MultiSet[A](inner: Map[A, Int]) extends Iterable[A] {
  import utils._
  private lazy val _inner = inner.withDefaultValue(0)

  def apply(a: A): Int = _inner(a)

  def +(a: A, multiplicity: Int = 1) = if(multiplicity < 1) this else new MultiSet(inner + (a -> (_inner(a) + multiplicity)))
  def -(a: A, multiplicity: Int = 1) = if(multiplicity < 1) this else inner.get(a) match {
    case Some(0) | None => this
    case Some(few) if few <= multiplicity => new MultiSet(inner - a)
    case Some(many) => new MultiSet(_inner + (a -> (_inner(a) - multiplicity)))
  }

  def ++(as: Iterable[A]) = as.foldLeft(this){ case (ms, a) => ms + a }

  def ++(that: MultiSet[A]) = {
    val (small, big) = smallBig(this, that)
    small._inner.foldLeft(big) { case (ms, (a, multiplicity)) => ms + (a, multiplicity)}
  }

  def --(as: Iterable[A]) = as.foldLeft(this){ case (ms, a) => ms - a }
  
  def intersect(that: MultiSet[A]): MultiSet[A] = {
    val (small, big) = smallBig(this, that)
    small._inner.foldLeft(MultiSet.empty[A]){ case (ms, (a, int)) => ms + (a, small(a).min(big(a)))}
  }

  def union(that: MultiSet[A]): MultiSet[A] = {
    val (small, big) = smallBig(this, that)
    small._inner.foldLeft(big) { case (ms, (a, int)) => ms + (a, int) }
  }

  def iterator: Iterator[A] = inner.flatMap{ case(k, v) => Vector.fill(v)(k) }.toIterator

  def distinct: Set[A] = inner.keySet

  def toMap = inner

  override def filter(p: A => Boolean) = new MultiSet(inner.filterKeys(p))

  def ==(that: MultiSet[A]) = inner.forall{ case (a, i) => that._inner(a) == i } && that._inner.forall{ case (a, i) => _inner(a) == i }
}