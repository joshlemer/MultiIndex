package manymap

/** Base trait of all MultiIndexMaps of any dimension */
trait MultiIndexMap[A] extends Iterable[A] {
  def multiSet: MultiSet[A]

  override def iterator: Iterator[A] = multiSet.toList.toIterator
}

object MultiSet {
  def apply[A](iterable: Iterable[A]) = new MultiSet[A](Map.empty) ++ iterable

  def empty[A] = apply[A](Nil)
}

class MultiSet[A](inner: Map[A, Int]) extends Iterable[A] {
  private lazy val _inner = inner.withDefaultValue(0)

  def apply(a: A): Int = _inner(a)

  def +(a: A, multiplicity: Int = 1) = if(multiplicity < 1) this else new MultiSet(inner + (a -> (_inner(a) + multiplicity)))
  def -(a: A, multiplicity: Int = 1) = if(multiplicity < 1) this else inner.get(a) match {
    case Some(0) | None => this
    case Some(few) if few <= multiplicity => new MultiSet(inner - a)
    case Some(many) => new MultiSet(_inner + (a -> (_inner(a) - multiplicity)))
  }

  def ++(as: Iterable[A]) = as.foldLeft(this){ case (ms, a) => ms + a }
  def --(as: Iterable[A]) = as.foldLeft(this){ case (ms, a) => ms - a }

  def intersect(that: MultiSet[A]): MultiSet[A] = {
    val (small, big) = if(size < that.size) (this, that) else (that, this)
    small._inner.foldLeft(MultiSet.empty[A]){ case (ms, (a, int)) => ms + (a, small(a).min(big(a)))}
  }

  def union(that: MultiSet[A]): MultiSet[A] = {
    val (small, big) = if(size < that.size) (this, that) else (that, this)
    small._inner.foldLeft(big) { case (ms, (a, int)) => ms + (a, int) }
  }

  def iterator: Iterator[A] = inner.flatMap{ case(k, v) => Vector.fill(v)(k) }.toIterator

  def distinct: Set[A] = inner.keySet

  override def filter(p: A => Boolean) = new MultiSet(inner.filterKeys(p))
}

object JIndex{
  def apply[A, B](f: A => B, es: Iterable[A] = Iterable.empty[A]) = empty[A, B](f) ++ es

  def empty[A, B](f: A => B) = new JIndex[A, B](Map.empty, f)
}

class JIndex[A, B](elems: Map[B, MultiSet[A]], f: A => B) {

  val _elems = elems.withDefaultValue(MultiSet.empty[A])

  def apply(b: B) = _elems(b)

  def + (a: A, multiplicity: Int = 1): JIndex[A, B] = {
    val b = f(a)
    new JIndex(_elems + (b -> (_elems(b) + a)), f)
  }

  def - (a: A, multiplicity: Int = 1): JIndex[A, B] = {
    val b = f(a)
    val newElems = _elems(b) match {
      case empty if empty.isEmpty => _elems
      case nonEmpty =>
        val removed = nonEmpty - (a, multiplicity)
        if (removed.isEmpty) _elems - b
        else _elems + (b -> removed)
    }
    new JIndex(newElems, f)
  }

  def ++ (as: Iterable[A]): JIndex[A, B] = as.foldLeft(this){ case (ind, a) => ind + a }

  def ++ (as: MultiSet[A]): JIndex[A, B] = as.distinct.foldLeft(this) { case (ind, a) => ind + (a, as(a))}

  def -- (as: Iterable[A]): JIndex[A, B] = as.foldLeft(this){ case (ind, a) => ind - a }

  def -- (as: MultiSet[A]): JIndex[A, B] = as.distinct.foldLeft(this) { case (ind, a) => ind - (a, as(a))}

  def filter(p: A => Boolean) = new JIndex(elems.mapValues(_.filter(p)).filterNot{ case (_, ms) => ms.isEmpty}, f)

  def getList(b: B): List[A] = _elems(b).toList

  def toList: List[A] = _elems.keys.flatMap(getList).toList
}

object MultiIndexMap {
  class MultiIndexMapFactory[A](iterable: Iterable[A]) {
    def apply[B1](f1: A => B1) = iterable.indexBy(f1)
    def apply[B1, B2](f1: A => B1, f2: A => B2) = iterable.indexBy(f1, f2)
    def apply[B1, B2, B3](f1: A => B1, f2: A => B2, f3: A => B3) = iterable.indexBy(f1, f2, f3)
    def apply[B1, B2, B3, B4](f1: A => B1, f2: A => B2, f3: A => B3, f4: A => B4) = iterable.indexBy(f1, f2, f3, f4)
  }
  def apply[A](iterable: Iterable[A]) = new MultiIndexMapFactory(iterable)

  def empty[A, B1](f1: A => B1): MultiIndexMap1[A, B1] = apply[A](Iterable.empty)(f1)
  def empty[A, B1, B2](f1: A => B1, f2: A => B2): MultiIndexMap2[A, B1, B2] = empty(f1).withIndex(f2)
  def empty[A, B1, B2, B3](f1: A => B1, f2: A => B2, f3: A => B3): MultiIndexMap3[A, B1, B2, B3] = empty(f1, f2).withIndex(f3)
  def empty[A, B1, B2, B3, B4](f1: A => B1, f2: A => B2, f3: A => B3, f4: A => B4): MultiIndexMap4[A, B1, B2, B3, B4] = empty(f1, f2, f3).withIndex(f4)
}

object Foo extends App {
  println((1 to 10).indexBy(_ / 3).filter(_ % 3 != 0).get1(1))

  def time[R](block: => R): R = {
    val t0 = System.currentTimeMillis()
    val result = block    // call-by-name
    val t1 = System.currentTimeMillis()
    println("Elapsed time: " + (t1 - t0) + "ms")
    result
  }
}
