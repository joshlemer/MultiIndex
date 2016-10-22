package com.github.joshlemer.multiindex

import org.scalatest.{FlatSpec, Matchers}

class MultiIndex1Spec extends FlatSpec with Matchers with MultiIndex1Behaviors {

  val f: Int => Int = _ / 4
  val g: Int => String = _.toString

  val multiIndex1ToInt = ((1 to 1000) ++ (1 to 100) ).indexBy(f)
  val multiIndex1ToString = ((1 to 100) ++ (1 to 10000)).indexBy(g)

  val keysToTest = 100

  behavior of "MultiIndex1"

  "get1(b1)" should "return a list containing a, if f(a) == b1" in {
    multiIndex1ToInt.foreach { i =>
      multiIndex1ToInt.get1(f(i)) should contain (i)
    }
    multiIndex1ToString.foreach { i =>
      multiIndex1ToString.get1(g(i)) should contain (i)
    }
  }

  "get1(b1)" should "return a list containing `a` as many times as a is in the MultiIndex, if f(a) == b1" in {
    multiIndex1ToInt.take(keysToTest).foreach { i =>
      multiIndex1ToInt.get1(f(i)).count(_ == i) should be (multiIndex1ToInt.multiSet(i))
    }
    multiIndex1ToString.take(keysToTest).foreach { i =>
      multiIndex1ToString.get1(g(i)).count(_ == i) should be (multiIndex1ToString.multiSet(i))
    }
  }

  "get1MultiSet(b1)" should "return a MultiSet containing `a` as many times as `a` is in the MultiIndex, if f(a) == b1" in {
    multiIndex1ToInt.take(keysToTest).foreach { i =>
      multiIndex1ToInt.get1MultiSet(f(i))(i) should be(multiIndex1ToInt.multiSet(i))
      multiIndex1ToInt.get1MultiSet(f(i))(i) should be(multiIndex1ToInt.count(_ == i))
    }
    multiIndex1ToString.take(keysToTest).foreach { i =>
      multiIndex1ToString.get1MultiSet(g(i))(i) should be(multiIndex1ToString.multiSet(i))
      multiIndex1ToString.get1MultiSet(g(i))(i) should be(multiIndex1ToString.count(_ == i))
    }
  }

  "count(_ == a)" should "equal multiSet(a)" in {
    multiIndex1ToInt.take(keysToTest).foreach { i =>
      multiIndex1ToInt.count(_ == i) should be (multiIndex1ToInt.multiSet(i))
    }
    multiIndex1ToString.take(keysToTest).foreach { i =>
      multiIndex1ToString.count(_ == i) should be (multiIndex1ToString.multiSet(i))
    }
  }

  "take(n)" should "contain n elements if multiIndex is larger than n" in {
    multiIndex1ToInt.take(keysToTest).size should equal (keysToTest)
    multiIndex1ToString.take(keysToTest).size should equal (keysToTest)
  }

  "take(n)" should "equal itself if there are less than n elements" in {
    (multiIndex1ToInt == multiIndex1ToInt.take(multiIndex1ToInt.size + 100)) should be (true)
    (multiIndex1ToString.take(multiIndex1ToString.size + 100) == multiIndex1ToString) should be (true)
  }

  "take(n) failing" should "equal itself if there are less than n elements" in {

    val a = multiIndex1ToInt.take(multiIndex1ToInt.size + 100)
    val b = multiIndex1ToInt

    info("a == b: " + (a == b).toString)
    info("a.toString == b.toString: " + (a.toString == b.toString).toString)
    info("a equals b: " + (a equals b).toString)

    a should equal (b)
    a should be (b)
  }

  "An empty MultiIndex1" should behave like emptyBehavior(MultiIndex.empty[Int, Int](identity))

  "A non-empty MultiIndex1" should behave like nonEmptyBehavior(multiIndex1ToInt)

  "A MultiIndex1" should behave like plusBehavior(44, multiIndex1ToInt)
  it should behave like minusBehavior(44, multiIndex1ToInt)
  it should behave like concatenateBehavior(1 to 10000, multiIndex1ToInt)
  it should behave like behaviorOfGet(1 to 10, multiIndex1ToInt)

}

trait MultiIndex1Behaviors {
  this: FlatSpec with Matchers =>

  def emptyBehavior[A, B1](multiIndex: => MultiIndex1[A, B1]): Unit = {
    it should "be empty" in {
      multiIndex.isEmpty should be (true)
    }

    it should "not be non-empty" in {
      multiIndex.nonEmpty should be (false)
    }

    it should "have size 0" in {
      multiIndex.size should be (0)
    }

    it should "have an empty multiSet" in {
      multiIndex.multiSet.isEmpty should be (true)
      multiIndex.multiSet.nonEmpty should be (false)
    }

    it should "throw NoSuchElementException if .head is called" in {
      intercept[NoSuchElementException] {
        multiIndex.head
      }
    }

    it should "throw UnsupportedOperationException if .tail is called" in {
      intercept[UnsupportedOperationException] {
        multiIndex.tail
      }
    }
  }

  def nonEmptyBehavior[A, B1](multiIndex: => MultiIndex1[A, B1]): Unit = {
    it should "not be empty" in {
      multiIndex.isEmpty should be (false)
    }

    it should "be non-empty" in {
      multiIndex.nonEmpty should be (true)
    }

    it should "have size > 0" in {
      multiIndex.size should be > 0
    }

    it should "have an non-empty multiSet" in {
      multiIndex.multiSet.nonEmpty should be (true)
      multiIndex.multiSet.isEmpty should be (false)
    }

    it should "have a head" in {
      multiIndex.head
    }

    it should "have a tail" in {
      multiIndex.tail
    }

    it should "contain its head" in {
      multiIndex.contains(multiIndex.head) should be (true)
    }
  }

  def plusBehavior[A, B1](a: A, multiIndex: => MultiIndex1[A, B1]): Unit = {

    it should "contain an inserted element" in {
      (multiIndex + a).contains(a) should be (true)
    }

    it should "incriment in size when inserted" in {
      (multiIndex + a).size should be (multiIndex.size + 1)
    }

    it should "find an inserted element after insertion" in {
      (multiIndex + a).get1(multiIndex.f1(a)) should contain (a)
    }

    it should "add `a` to lookup list of f(a) and retain all others" in {
      (multiIndex + a).get1(multiIndex.f1(a)) should contain theSameElementsAs (a :: multiIndex.get1(multiIndex.f1(a)))
    }

    it should "add duplicate elements" in {
      (multiIndex + a + a).size should be (multiIndex.size + 2)
    }
  }

  def minusBehavior[A, B1](a: A, multiIndex: => MultiIndex1[A, B1]): Unit = {

    it should "not increase in size when an element is removed" in {
      (multiIndex - a).size should be <= multiIndex.size
    }

    it should "decrease in size by 1 if the element was in the multiIndex" in {
      if(multiIndex.contains(a)) (multiIndex - a).size should be (multiIndex.size - 1)
    }

    it should "retain the same size if the element is not in the multiIndex" in {
      if(!multiIndex.contains(a)) (multiIndex - a).size should be (multiIndex.size)
    }

    it should "remove an instance of a removed element from the lookup" in {
      val beforeCount = multiIndex.get1(multiIndex.f1(a)).count(_ == a)
      val removed = multiIndex - a
      val removedCount = removed.get1(removed.f1(a)).count(_ == a)

      removedCount should be ((beforeCount - 1).max(0))
    }
  }

  def concatenateBehavior[A, B1](as: Iterable[A], multiIndex: => MultiIndex1[A, B1]): Unit = {
    val seq = as.toSeq
    val concatenated = multiIndex ++ seq

    it should "increase in size by the length of the concatenation when concatenated" in {
      concatenated.size should be (multiIndex.size + seq.size)
    }

    it should "contain all concatenated elements" in {
      seq.foreach(a => concatenated.contains(a) should be (true))
    }

    it should "return all concatenated elements in lookups" in {
      seq.foreach(a => concatenated.get1(concatenated.f1(a)) should contain (a))
    }

    it should "be the same MultiIndex when concatenated with an empty Iterable" in {
      (multiIndex ++ Iterable.empty[A] == multiIndex) should be(true)
    }
  }

  def removeBehavior[A, B1](as: Iterable[A], multiIndex: => MultiIndex1[A, B1]): Unit = {
    val seq = as.toSeq
    val removed = multiIndex -- seq

    it should "decrease in size by the length of the removal when removed from" in {
      removed.size should be ((multiIndex.size - seq.size).max(0))
    }

    it should "return fewer of each removed element" in {
      seq.foreach(a => removed.multiSet(a) should be < multiIndex.multiSet(a))
    }

    it should "remain the same multiIndex when empty Iterable is removed" in {
      (multiIndex -- Iterable.empty == multiIndex) should be (true)
    }
  }

  def behaviorOfGet[A, B1](as: Iterable[A], multiIndex: MultiIndex1[A, B1]): Unit = {
    it should "return elements which all map to the f1 lookup value in get" in {
      multiIndex.multiSet.distinct.foreach(a =>
        multiIndex.get(multiIndex.f1(a)).count(_ == a) should be (multiIndex.multiSet(a))
      )
    }
    it should "return elements which all map to the f1 lookup value" in {
      multiIndex.multiSet.distinct.foreach(a =>
        multiIndex.get1(multiIndex.f1(a)).count(_ == a) should be (multiIndex.multiSet(a))
      )
    }
    it should "return equivalent lists and multiSets in get1 and get1MultiSet" in {
      (multiIndex.multiSet.distinct ++ as).foreach { a =>
        multiIndex.get1(multiIndex.f1(a)) should contain theSameElementsAs multiIndex.get1MultiSet(multiIndex.f1(a))
      }
    }
  }
}
