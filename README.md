[![Build Status](https://travis-ci.org/joshlemer/MultiIndex.svg?branch=master)](https://travis-ci.org/joshlemer/MultiIndex)

# MultiIndex

This is a library to facilitate querying by multiple indexes on a collection in Scala. 

# Installation

## SBT

In build.sbt:

```scala
libraryDependencies ++= Seq(
  "com.github.joshlemer" %% "multiindex" % "0.1.0"
)
```

## Maven

```xml
<dependency>
    <groupId>com.github.joshlemer</groupId>
    <artifactId>multiindex_2.11</artifactId>
    <version>0.1.0</version>
</dependency>
```

# Motivation

It is a very common occurrence in programming to have a collection of elements, from which you want to find only the elements with some property. 

For example, maybe you are writing forum software that deals with Comments:
```scala
case class Comment(userId: UserId, threadId: Int, createdAt: DateTime, body: String)
```

So you have a `List[User]`, but want to quickly be able to retrieve all the comments for a particular user. To support this you might create a `Map[UserId, List[Comment]]` by calling `comments.groupBy(_.userId)`. Now you have fast lookup of comments by userId. This works well enough for this simple case, but now what happens when you want to access your data in more than one way? Maybe you also want to quickly find comments in a given thread. So you can again use `comments.groupBy(_.threadId)` to have fast lookups. But now you have to manually keep track of multiple different mappings, and it's not so easy to insert or remove comments.

Instead, you can use `MultiIndex` to easily index your collection on multiple indexes. The example above would simply be:

```scala
import com.joshlemer.multiindex._

val comments: List[Comment] = ???

val commentsByUserAndThread = comments.indexBy(_.userId, _.threadId) // MultiIndex2[User, UserId, Int]

def commentsByUser(userId: UserId): List[Comment] = comments.get1(userId)
def commentsByThread(threadId: Int): List[Comment] = comments.get2(thread)

def postComment(comment: Comment): MultiIndex2[User, UserId, Int] = commentsByUserAndThread + comment
def deleteComment(comment: Comment): MultiIndex2[User, UserId, Int] = commentsByUserAndThread - comment
```

I've tried to make the library simple to use, fast, light-weight, and dependency-free.

# Usage in detail

### Creating a MultiIndex

First way - convert any `Iterable` to a MultiIndex by importing an implicit class:
```scala
import multiindex._
val indexed: MultiIndex1[Int, String] = List(1,2,3).indexBy(_.toString)
```

Second way - convert any `Iterable` to a MultiIndex using the `MultiIndex` companion object
```scala
import multiindex.MultiIndex
val indexed: MultiIndex1[Int, Int, Boolean, Boolean] = MultiIndex(List(1,2,3))(_ + 1 * 11 / 2, _ < 0, _ % 2 == 0)
```

Third way - create an empty `MultiIndex` and add your collection to it
```scala
import multiindex.MultiIndex
val indexed: MultiIndex1[Int, Double] = MultiIndex.empty[Int, Double](_.toDouble) ++ List(1,2,3)
```

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


