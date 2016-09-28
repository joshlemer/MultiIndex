# ManyMap

This is a POC library to facilitate querying by multiple indexes on a collection in Scala. 

It is not currently released, but probably will be soon!

For example, maybe you have a `List[User]` and want to query by both name, and by id:

```scala
import manymap._

case class User(name: String, id: Int)

val users: List[User] = ???

val usersByIdAndName = users.indexBy(_.name, _.id)

val usersNamedSteve: List[User] = usersByIdAndName.get1("Steve")
val usersWithId4 = usersByIdAndName.get2(4)

val usersWithNameSteveAndId4 = usersByIdAndName.get("Steve", 4)

```

The library also supports addition and subtraction

```scala
val added = usersByIdAndName + User(555, "new user") 
added.get1("new user") // List[User], contains the new user
added.get2(555) // List[User], contains the new user

val removed = added - User(556, "removed") // removes a single instance of this user
```

The library also supports adding indexes, yielding a higher-dimensioned index

```scala
val usersByIdAndNameAndNameLength = usersByIdAndName.withIndex(_.name.length)

usersByIdAndNameAndLength.get3(4) // List[Users], all uses with name of length 4

```


