package manymap

import scala.collection.immutable.{HashedBagConfiguration, MapLike, Bag}

import utils._

object ManyMap {

  implicit class PimpedSeq[A1, A2, B](seq: Seq[B]) {
    def asManyMap(f1: B => A1, f2: B => A2): ManyMap2[A1, A2, B] = {
      apply(seq.map(e => ((f1(e), f2(e)), e)): _*)
    }
  }


  def apply[A1, A2, B](elems: ((A1, A2), B)*) = {
    val underlying = elems.toMap
    val (index1, index2) = elems.foldLeft(
      (Map.empty[A1, Bag[B]].withDefaultValue(Bag.empty[B]), Map.empty[A2, Bag[B]].withDefaultValue(Bag.empty[B])))
      { case ((map1, map2), ((a1, a2), b)) =>
        (map1.updated(a1, map1(a1) + b), map2.updated(a2, map2(a2) + b))
      }

    new ManyMap2(underlying, index1, index2)
  }
  def apply[A1, A2, A3, B](elems: ((A1, A2, A3), B)*) = new ManyMap3(elems)
  def apply[A1, A2, A3, A4, B](elems: ((A1, A2, A3, A4), B)*) = new ManyMap4(elems)
}

private[manymap] object utils {
  def addToMap[A, B](map: Map[A, List[B]], a: A, b: B): Map[A, List[B]] =
    map + (a -> (b :: map.getOrElse(a, Nil)))

  implicit def config[T]: HashedBagConfiguration[T] = Bag.configuration.compact[T]
}

class ManyMap1[A1, +B](elems: Seq[(A1, B)]) extends Map[A1, B] with MapLike[A1, B, ManyMap1[A1, B]] {
  val projection1 = elems.toMap
  def get(a1: A1): Option[B] = projection1.get(a1)
  def +[B1 >: B](kvp: (A1, B1)): ManyMap1[A1, B1] = new ManyMap1(elems :+ kvp)
  def -(key: A1) = new ManyMap1(elems.filter(_._1 != key))
  def iterator: Iterator[(A1, B)] = projection1.toIterator

  override def empty: ManyMap1[A1, B] = new ManyMap1[A1,B](Nil)

}


class ManyMap2[A1, A2, B](
  val underlying: Map[(A1, A2), B],
  _index1: Map[A1, Bag[B]],
  _index2: Map[A2, Bag[B]]
  ) extends Map[(A1, A2), B] with MapLike[(A1, A2), B, ManyMap2[A1, A2, B]] {

  val index1 = _index1.withDefaultValue(Bag.empty[B])
  val index2 = _index2.withDefaultValue(Bag.empty[B])

  def get(a: (A1, A2)): Option[B] = underlying.get(a)

  def +[B1 >: B](kvp: ((A1, A2), B1)): ManyMap2[A1, A2, B1] = kvp match { case (key @ (a1, a2), b) =>
    if(underlying.get(key).contains(b)) {
      this.asInstanceOf[ManyMap2[A1, A2, B1]]
    } else if (underlying.contains(key)) {
      (this - key) + kvp
    } else {
      new ManyMap2(underlying + kvp,
        (index1 ++ Map(a1 -> (Bag(b) ++ index1(a1)))).asInstanceOf[Map[A1, Bag[B1]]],
        (index2 ++ Map(a2 -> (Bag(b) ++ index2(a2)))).asInstanceOf[Map[A2, Bag[B1]]])
    }
  }

  def -(key: (A1, A2)): ManyMap2[A1, A2, B] = key match { case (a1, a2) =>
    underlying.get(key) match {
      case Some(b) =>
        val (bag1, bag2) = (index1(a1) - b, index2(a2) - b)
        new ManyMap2(underlying - key, index1 + (a1 -> bag1), index2 + (a2 -> bag2))
      case None => this
    }
  }
  def iterator: Iterator[((A1, A2), B)] = underlying.toIterator

  def get1(a1: A1): Set[B] = index1(a1).toSet
  def get2(a2: A2): Set[B] = index2(a2).toSet

  def bag1(a1: A1): Bag[B] = index1(a1)
  def bag2(a2: A2): Bag[B] = index2(a2)

  def ++(that: ManyMap2[A1, A2, B]): ManyMap2[A1, A2, B] = {
    def i[A](thisI: Map[A, Bag[B]], thatI: Map[A, Bag[B]]) =
      thatI.foldLeft(thisI) { case (index, (a1, bBag)) => index + (a1 -> (bBag ++ index(a1)))}

    new ManyMap2(underlying ++ that.underlying, i(index1, that.index1), i(index2, that.index2))
  }

  override def mapValues[B1](f: B => B1): ManyMap2[A1, A2, B1] =
    new ManyMap2(underlying.mapValues(f) , index1.mapValues(_.map(f)), index2.mapValues(_.map(f)))

  def updated(key: (A1, A2), value: B): ManyMap2[A1, A2, B] =
    this + ((key, value))

  override def empty: ManyMap2[A1, A2, B] = new ManyMap2[A1, A2, B](Map.empty, Map.empty, Map.empty)
}

class ManyMap3[A1, A2, A3, +B](elems: Seq[((A1, A2, A3), B)]) extends Map[(A1, A2, A3), B] with MapLike[(A1, A2, A3), B, ManyMap3[A1, A2, A3, B]] {
  lazy val elemsMap = elems.toMap

  lazy val (p1, p2, p3) = elemsMap.foldLeft((Map.empty[A1, List[B]], Map.empty[A2, List[B]], Map.empty[A3, List[B]])){
    case ((map1, map2, map3), ((a1, a2, a3), b)) => (addToMap(map1, a1, b), addToMap(map2, a2, b), addToMap(map3, a3, b))
  }

  def get(a: (A1, A2, A3)): Option[B] = elemsMap.get(a)
  def +[B1 >: B](kvp: ((A1, A2, A3), B1)): ManyMap3[A1, A2, A3, B1] = new ManyMap3(elems :+ kvp)
  def -(key: (A1, A2, A3)) = new ManyMap3(elems.filter(_._1 != key))
  def iterator: Iterator[((A1, A2, A3), B)] = elemsMap.toIterator

  def get1(a1: A1): Option[List[B]] = p1.get(a1)
  def get2(a2: A2): Option[List[B]] = p2.get(a2)
  def get3(a3: A3): Option[List[B]] = p3.get(a3)

  override def empty: ManyMap3[A1, A2, A3, B] = new ManyMap3[A1, A2, A3, B](Nil)
}

class ManyMap4[A1, A2, A3, A4, +B](elems: Seq[((A1, A2, A3, A4), B)]) extends Map[(A1, A2, A3, A4), B] with MapLike[(A1, A2, A3, A4), B, ManyMap4[A1, A2, A3, A4, B]] {
  lazy val elemsMap = elems.toMap

  lazy val (p1, p2, p3, p4) = elemsMap.foldLeft((Map.empty[A1, List[B]], Map.empty[A2, List[B]], Map.empty[A3, List[B]], Map.empty[A4, List[B]])){
    case ((map1, map2, map3, map4), ((a1, a2, a3, a4), b)) => (addToMap(map1, a1, b), addToMap(map2, a2, b), addToMap(map3, a3, b), addToMap(map4, a4, b))
  }

  def get(a: (A1, A2, A3, A4)): Option[B] = elemsMap.get(a)
  def +[B1 >: B](kvp: ((A1, A2, A3, A4), B1)): ManyMap4[A1, A2, A3, A4, B1] = new ManyMap4(elems :+ kvp)
  def -(key: (A1, A2, A3, A4)) = new ManyMap4(elems.filter(_._1 != key))
  def iterator: Iterator[((A1, A2, A3, A4), B)] = elemsMap.toIterator

  def get1(a1: A1): Option[List[B]] = p1.get(a1)
  def get2(a2: A2): Option[List[B]] = p2.get(a2)
  def get3(a3: A3): Option[List[B]] = p3.get(a3)
  def get4(a4: A4): Option[List[B]] = p4.get(a4)

  override def empty: ManyMap4[A1, A2, A3, A4, B] = new ManyMap4[A1, A2, A3, A4, B](Nil)
}
