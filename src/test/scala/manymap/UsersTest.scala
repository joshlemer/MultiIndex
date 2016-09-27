package manymap

object UsersTest extends App{

  trait Person

  protected case class User(id: Long, name: String, sex: Char) extends Person

  case class Admin(id: Int, priviledges: String) extends Person

  import ManyMap._

  val josh = User(1L, "josh", 'm')

  val users = List(
    User(1L, "josh", 'm'),
    User(2L, "shiv", 'f'),
    User(3L, "joanne", 'f'),
    User(5L, "yoel", 'm'),
    User(6L, "josh", 'm'),
    User(1L, "josh", 'm')
  )

  val admin = Admin(55, "priv's")

  val m1 = users.asManyMap(_.id, _.name)

  val m2 = m1 + ((111L, "admin") -> admin)

  val m3 = m2 + ((44L, "jojojojo") -> User(44L, "jojojo", 'r'))

  println("here!")

  trait A
  trait B extends A
  trait C extends B

  val b = ManyMap((1, 1) -> new B{})
  val a = b + ((2, 2) -> new A {})

 println("hiiii")



}
