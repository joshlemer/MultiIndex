package manymap

import scala.collection.Bag

trait MultiIndexMap[A, B1, B2] {
  /** Get a bag of all elements that match on both indexes with b1 and b2 */
  def get(b1: B1, b2: B2): Bag[A]

  def get1(b1: B1): List[A]

  /** Get a bag of all elements that match b1 on index 1 */
  def get1Bag(b1: B1): Bag[A]

  def get2(b2: B2): List[A]

  /** Get a bag of all elements that match b2 on index 2 */
  def get2Bag(b2: B2): Bag[A]

  /** Append an element to these elements, add it to the indexes */
  def + (a: A): MultiIndexMap[A, B1, B2]

  def - (a: A): MultiIndexMap[A, B1, B2]
}

private[manymap] object Implicits {
  implicit def config[A] = Bag.configuration.compact[A]
}

import Implicits._

class MultiIndexMapImpl[A, B1, B2] private[manymap] (
  val bag: Bag[A],
  f1: A => B1,
  val index1: Map[B1, Bag[A]],
  f2: A => B2,
  val index2: Map[B2, Bag[A]]) extends MultiIndexMap[A, B1, B2] {

  def get(b1: B1, b2: B2) = get1Bag(b1).intersect(get2Bag(b2))

  def get1(b1: B1) = index1.get(b1).map(_.toList).getOrElse(Nil)
  def get1Bag(b1: B1) = index1.getOrElse(b1, Bag.empty)

  def get2(b2: B2) = index2.get(b2).map(_.toList).getOrElse(Nil)
  def get2Bag(b2: B2) = index2.getOrElse(b2, Bag.empty)

  private[manymap] def add[BN](a: A, bn: BN, index: Map[BN, Bag[A]]): Map[BN, Bag[A]] =
    index + (bn -> (index.getOrElse(bn, Bag.empty[A]) + a))

  private[manymap] def remove[BN](a: A, bn: BN, index: Map[BN, Bag[A]]): Map[BN, Bag[A]] = {
    index.get(bn) match {
      case Some(bnBag) =>
        val removedBag = bnBag - a
        if (removedBag.isEmpty) index - bn
        else index + (bn -> removedBag)
      case None => index
    }
  }

  def + (a: A) = new MultiIndexMapImpl(bag + a, f1, add(a, f1(a), index1), f2, add(a, f2(a), index2))

  def - (a: A) = new MultiIndexMapImpl(bag - a, f1, add(a, f1(a), index1), f2, add(a, f2(a), index2))

}

object MultiIndexMapObj {


  implicit class IterableOps[A](iterable: Iterable[A]) {
    def asMultiIndexMap[B1, B2](f1: A => B1, f2: A => B2): MultiIndexMap[A, B1, B2] = {
      val bag = Bag.empty[A] ++ iterable
      new MultiIndexMapImpl(bag, f1, bag.groupBy(f1).mapValues(Bag.empty[A] ++ _), f2, bag.groupBy(f2).mapValues(Bag.empty[A] ++ _))
    }
  }
}



object Ex extends App {
  import MultiIndexMapObj._

  case class User(name: String, sex: Char)

  val users = List(
    User("Josh", 'M'),
    User("Joanne", 'F'),
    User("Shivani", 'F'),
    User("Steve", 'M'),
    User("Josh", 'M'),
    User("Hillary", 'F'),
    User("Robert", 'M'),
    User("Josh", 'M')
  )

  val mim = users.asMultiIndexMap(_.name, _.sex)

  println(mim.get1("Josh")) // List(User(1,Josh,M), User(1,Josh,M), User(1,Josh,M))
  println(mim.get1("Shivani")) // List(User(1,Shivani,F))
  println(mim.get1("fake")) // List()

  println(mim.get2('M')) // List(User(1,Steve,M), User(1,Robert,M), User(1,Josh,M), User(1,Josh,M), User(1,Josh,M))
  println(mim.get2('F')) // List(User(1,Joanne,F), User(1,Shivani,F), User(1,Hillary,F))
  println(mim.get2('W')) // List()

  println(mim.get1Bag("Josh")) // Bag(User(1,Josh,M), User(1,Josh,M), User(1,Josh,M))
  println(mim.get1Bag("Shivani")) // Bag(User(1,Shivani,F))
  println(mim.get1Bag("fake")) // Bag()

  println(mim.get2Bag('M')) // Bag(User(1,Steve,M); User(1,Robert,M); User(1,Josh,M), User(1,Josh,M), User(1,Josh,M))
  println(mim.get2Bag('F')) // Bag(User(1,Joanne,F); User(1,Shivani,F); User(1,Hillary,F))
  println(mim.get2Bag('W')) // Bag()

  println(mim.get("Josh", 'M'))
  println()





  val words = List("hello", ", ", "this", "is a", "list of", "words", ".", "yes, ", "words")

  val wordsMim = words.asMultiIndexMap(identity, _.length)

  println(wordsMim.get1("hello"))
  println(wordsMim.get2(5))

}

