package manymap

import scala.collection.immutable.MapLike

object ManyMap {
  def apply[A1, A2, B](elems: ((A1, A2), B)*) = new ManyMap2(elems)
  def apply[A1, A2, A3, B](elems: ((A1, A2, A3), B)*) = new ManyMap3(elems)
  def apply[A1, A2, A3, A4, B](elems: ((A1, A2, A3, A4), B)*) = new ManyMap4(elems)
}

private[manymap] object utils {
  def addToMap[A, B](map: Map[A, List[B]], a: A, b: B): Map[A, List[B]] =
    map + (a -> (b :: map.getOrElse(a, Nil)))
}

class ManyMap1[A1, +B](elems: Seq[(A1, B)]) extends Map[A1, B] with MapLike[A1, B, ManyMap1[A1, B]] {
  val projection1 = elems.toMap
  def get(a1: A1): Option[B] = projection1.get(a1)
  def +[B1 >: B](kvp: (A1, B1)): ManyMap1[A1, B1] = new ManyMap1(elems :+ kvp)
  def -(key: A1) = new ManyMap1(elems.filter(_._1 != key))
  def iterator: Iterator[(A1, B)] = projection1.toIterator

  override def empty: ManyMap1[A1, B] = new ManyMap1[A1,B](Nil)

}

import utils._

class ManyMap2[A1, A2, +B](private val elems: Seq[((A1, A2), B)]) extends Map[(A1, A2), B] with MapLike[(A1, A2), B, ManyMap2[A1, A2, B]] {
  lazy val elemsMap = elems.toMap
  lazy val (projection1, projection2) = elemsMap.foldLeft((Map.empty[A1, B], Map.empty[A2, B])){
    case ((map1, map2), ((a1, a2), b)) => (map1 + (a1 -> b), map2 + (a2 -> b))
  }

  lazy val (p1, p2) = elemsMap.foldLeft((Map.empty[A1, List[B]], Map.empty[A2, List[B]])){
    case ((map1, map2), ((a1, a2), b)) => (addToMap(map1, a1, b), addToMap(map2, a2, b))
  }

  def get(a: (A1, A2)): Option[B] = elemsMap.get(a)
  def +[B1 >: B](kvp: ((A1, A2), B1)): ManyMap2[A1, A2, B1] = new ManyMap2(elems :+ kvp)
  def -(key: (A1, A2)) = new ManyMap2(elems.filter(_._1 != key))
  def iterator: Iterator[((A1, A2), B)] = elemsMap.toIterator

  def get1(a1: A1): Option[List[B]] = p1.get(a1)
  def get2(a2: A2): Option[List[B]] = p2.get(a2)

  def ++[B1 >: B](that: ManyMap2[A1, A2, B1]): ManyMap2[A1, A2, B1] = new ManyMap2(elems ++ that.elems)

  override def mapValues[B1](f: B => B1): ManyMap2[A1, A2, B1] = new ManyMap2(elems.map{ case (k, v) => (k, f(v)) })

  override def updated [B1 >: B](key: (A1, A2), value: B1): ManyMap2[A1, A2, B1] =
    new ManyMap2(elemsMap.updated(key, value).toSeq)

  override def empty: ManyMap2[A1, A2, B] = new ManyMap2[A1, A2, B](Nil)
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
