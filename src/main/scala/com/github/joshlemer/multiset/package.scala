package com.github.joshlemer

package object multiset {
  implicit class MultiSetIterableOps[A](iterable: Iterable[A]) {
    def toMultiSet = MultiSet.empty[A] ++ iterable
  }
}
