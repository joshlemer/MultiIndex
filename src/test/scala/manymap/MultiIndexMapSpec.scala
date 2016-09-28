package manymap

import org.scalatest.{Matchers, FlatSpec}

class MultiIndexMapSpec extends FlatSpec with Matchers {

  behavior of "MultiIndexMap"


  case class User(name: String, sex: Char)

  val users = List(
    User("Josh", 'M'),
    User("Joanne", 'F'),
    User("Shivani", 'F'),
    User("Steve", 'M'),
    User("Josh", 'M'),
    User("Hillary", 'F'),
    User("Robert", 'M'),
    User("Josh", 'M'),
    User("Aboozipadoo from planet pluto", '?')
  )

  val mim1 = users.indexBy(_.name)

  println((mim1 ++ mim1) == mim1 ++ users)


  println(mim1)

  val mim2: MultiIndexMap2[User, String, Char] = users.indexBy(_.name, _.sex)

  val mim3: MultiIndexMap3[User, String, Char, Int] = mim2.withIndex(_.name.length)

  "get1" should "get many elements when there are many matches on index1" in {
    mim2.get1("Josh").length should be (3)
  }

  "get1" should "get a single element when exactly one match is found" in {
    mim2.get1("Shivani").length should be (1)
  }

  "get1" should "return empty sequence when no elements match" in {
    mim2.get1("fake").length should be (0)
  }

  "get2" should "get many elements when there are many matches on index2" in {
    mim2.get2('F').length should be (3)
  }

  "get2" should "get a single element when exactly one match is found" in {
    mim2.get2('?').length should be (1)
  }

  "get2" should "return empty sequence when no elements match" in {
    mim2.get2('8').length should be (0)
  }

  "get3" should "return all elements matching index2" in {
    mim3.get3(6).length should be (2)
    mim3.get3(6).foreach(_.name.length should be (6))
  }

}
