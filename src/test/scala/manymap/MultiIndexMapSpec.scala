package manymap

import org.scalatest.{Matchers, FlatSpec}

class MultiIndexMapSpec extends FlatSpec with Matchers {

  behavior of "MultiIndexMap"


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
    User("Josh", 'M'),
    User("Aboozipadoo from planet pluto", '?')
  )

  val mim = users.asMultiIndexMap(_.name, _.sex)

  "get1" should "get many elements when there are many matches on index1" in {
    mim.get1("Josh").length should be (3)
  }

  "get1" should "get a single element when exactly one match is found" in {
    mim.get1("Shivani").length should be (1)
  }

  "get1" should "return empty sequence when no elements match" in {
    mim.get1("fake").length should be (0)
  }

  "get2" should "get many elements when there are many matches on index2" in {
    mim.get2('F').length should be (3)
  }

  "get2" should "get a single element when exactly one match is found" in {
    mim.get2('?').length should be (1)
  }

  "get2" should "return empty sequence when no elements match" in {
    mim.get2('8').length should be (0)
  }
}
