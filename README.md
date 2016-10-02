[![Build Status](https://travis-ci.org/joshlemer/MultiIndex.svg?branch=master)](https://travis-ci.org/joshlemer/MultiIndex)

# MultiIndex

This is a library to facilitate querying by multiple indexes on a collection in Scala. 

It is not currently released, but probably will be soon!

# Usage

In build.sbt:

```scala
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies ++= Seq(
  "com.github.joshlemer" %% "multiindex" % "0.0.1-SNAPSHOT"
)
```

# Examples

For example, maybe you have a `List[User]` and want to query by both name, and by id:

```scala
import com.github.joshlemer.multiindex._

case class User(name: String, id: Int)

val users: List[User] = List(User("Steve", 1), User("Josh", 2), User("Steve", 3), User("Mary", 4))

val usersByIdAndName = users.indexBy(_.name, _.id)

val usersNamedSteve: List[User] = usersByIdAndName.get1("Steve") // List(User("Steve", 1), User("Steve", 3))
val usersWithId4 = usersByIdAndName.get2(4) // List(User("Mary", 4))

val usersWithNameSteveAndId4 = usersByIdAndName.get("Steve", 4) // List()

```

The library also supports addition and subtraction

```scala
val added = usersByIdAndName + User("new user", 555) 
added.get1("new user") // List[User], contains the new user
added.get2(555) // List[User], contains the new user

val removed = added - User("removed", 556) // removes a single instance of this user
```

The library also supports adding indexes, yielding a higher-dimensioned index

```scala
val usersByIdAndNameAndNameLength = usersByIdAndName.withIndex(_.name.length)

usersByIdAndNameAndLength.get3(4) // List[Users], all uses with name of length 4

```


