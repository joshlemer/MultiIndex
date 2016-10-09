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
val indexed: MultiIndex1[Int, Int, Boolean, Boolean] = 
  MultiIndex(List(1,2,3))(_ + 1 * 11 / 2, _ < 0, _ % 2 == 0)
```

Third way - create an empty `MultiIndex` and add your collection to it
```scala
import multiindex.MultiIndex
val indexed: MultiIndex1[Int, Double] = MultiIndex.empty[Int, Double](_.toDouble) ++ List(1,2,3)
```

Notice that there isn't just a single MultiIndex type, but rather a different type for each number of dimensions (number of indexing functions). So for example `MultiIndex1[A, B1]`, `MultiIndex2[A, B1, B2]`, `MultiIndex3[A, B1, B2, B3]`, `MultiIndex4[A, B1, B2, B3, B4]`. The type of MultiIndex you get when you create one is determined by the number of indexing functions you pass in to the indexBy method, or to the Factory constructor.

### Lookups

To look up elements by an index, you can use the `getX` methods, where X is the index you want to query by. So for example

```scala
val multiIndex = List(1,1,1,2,3,4,5).indexBy(_.toString, _ + 1, _ > 3)

multiIndex.get1("4") // List(4)
multiIndex.get1("hello") // List()

multiIndex.get2(4) // List(3)
multiIndex.get2(8) // List()

multiIndex.get3(true) // List(4,5)
multiIndex.get3(false) // List(1,1,1,2,3)
```

Also exposed are `getXMultiSet` methods, where `X` is the index to query on. These methods return a `MultiSet[A]` rather than a `List[A]`, which is just a more compact representation of data. It is a Set data structure that can store duplicates.

```scala
// maps elements to their multiplicity in the set
multiindex.get3MultiSet(false) // MultiSet(1 -> 3, 2 -> 1, 3 -> 1) 
```

`MultiSet`s have the nice property that their unions and intersections are very fast to compute, and this is what you can use to make queries on multiple indexes at once. For example:

```scala

// get elements matching on BOTH index 1 and index 3
multiindex.get1MultiSet("1").intersect(multiindex.get3MultiSet(false)) 
// MultiSet(1 -> 3)

// get elements matching on EITHER index 2 or index 3
multiindex.get1MultiSet("2").union(multiindex.get3MultiSet(true)) 
// MultiSet(2 -> 1, 4 -> 1, 5 -> 1)
```

### Adding and removing

Adding and removing elements is very straight forward

```scala
val added = multiIndex + 9 + 10 + 11 // adds 9, 10, 11 to the index
added.get1("10") // List(10)

// each addition or removal only adds or removes 1 instance the element from the MultiIndex
val removed = multiIndex - 1 - 1 - 2

// or equivalently
val added = multiIndex ++ List(9, 10, 11)
val removed = multiIndex -- List(1, 1, 2)

// It's also very vast to add MultSets to a MultiIndex
val ms: MultiSet[Int] = ???
multiIndex ++ added
```


