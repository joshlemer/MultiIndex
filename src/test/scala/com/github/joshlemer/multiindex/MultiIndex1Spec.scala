package com.github.joshlemer.multiindex

import org.scalatest.{FlatSpec, Matchers}

class MultiIndex1Spec extends FlatSpec with Matchers {

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

//  "take(n) failing" should "equal itself if there are less than n elements" in {
//
//    val a = multiIndex1ToInt.take(multiIndex1ToInt.size + 100)
//    val b = multiIndex1ToInt
//
//    println(a == b)
//    println(a equals b)
//
//    a should equal (b)
//    a should be (b)
//  }

}
