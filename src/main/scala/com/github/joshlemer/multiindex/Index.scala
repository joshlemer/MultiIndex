package com.github.joshlemer.multiindex

object Index{
  def apply[A, B](f: A => B, es: Iterable[A] = Iterable.empty[A]) = empty[A, B](f) ++ es

  def empty[A, B](f: A => B) = new Index[A, B](Map.empty, f)
}

class Index[A, B](elems: Map[B, MultiSet[A]], val f: A => B) {

  val _elems = elems.withDefaultValue(MultiSet.empty[A])

  def apply(b: B) = _elems(b)

  def + (a: A, multiplicity: Int = 1): Index[A, B] = {
    val b = f(a)
    new Index(_elems + (b -> (_elems(b) + a)), f)
  }

  def - (a: A, multiplicity: Int = 1): Index[A, B] = {
    val b = f(a)
    val newElems = _elems(b) match {
      case empty if empty.isEmpty => _elems
      case nonEmpty =>
        val removed = nonEmpty - (a, multiplicity)
        if (removed.isEmpty) _elems - b
        else _elems + (b -> removed)
    }
    new Index(newElems, f)
  }

  def ++ (as: Iterable[A]): Index[A, B] = as.foldLeft(this){ case (ind, a) => ind + a }

  def ++ (as: MultiSet[A]): Index[A, B] = as.distinct.foldLeft(this) { case (ind, a) => ind + (a, as(a))}

  def -- (as: Iterable[A]): Index[A, B] = as.foldLeft(this){ case (ind, a) => ind - a }

  def -- (as: MultiSet[A]): Index[A, B] = as.distinct.foldLeft(this) { case (ind, a) => ind - (a, as(a))}

  def filter(p: A => Boolean) = new Index(elems.mapValues(_.filter(p)).filterNot{ case (_, ms) => ms.isEmpty}, f)

  def getList(b: B): List[A] = _elems(b).toList

  def toList: List[A] = _elems.keys.flatMap(getList).toList
}