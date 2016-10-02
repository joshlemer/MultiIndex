package com.github.joshlemer.multiindex

import org.scalacheck.Gen
import org.scalacheck.Arbitrary._

case class User(id: Int, name: String, sex: Char)

object Generators {

  implicit val userGen: Gen[User] = for {
    id <- arbitrary[Int]
    name <- arbitrary[String]
    sex <- Gen.oneOf('M', 'F', '?')
  } yield User(id, name, sex)

  implicit def multiIndex1Gen[A, B1](f1: A => B1)(implicit aGen: Gen[A]): Gen[MultiIndex1[A, B1]] =
    for (list <- Gen.listOf(aGen)) yield MultiIndex(list)(f1)

  implicit def multiIndex2Gen[A, B1, B2](f1: A => B1, f2: A => B2)(implicit aGen: Gen[A]): Gen[MultiIndex2[A, B1, B2]] =
    for (list <- Gen.listOf(aGen)) yield MultiIndex(list)(f1, f2)

  implicit def multiIndex3Gen[A, B1, B2, B3](f1: A => B1, f2: A => B2, f3: A => B3)(implicit aGen: Gen[A]): Gen[MultiIndex3[A, B1, B2, B3]] =
    for (list <- Gen.listOf(aGen)) yield MultiIndex(list)(f1, f2, f3)

  implicit def multiIndex4Gen[A, B1, B2, B3, B4](f1: A => B1, f2: A => B2, f3: A => B3, f4: A => B4)(implicit aGen: Gen[A]): Gen[MultiIndex4[A, B1, B2, B3, B4]] =
    for (list <- Gen.listOf(aGen)) yield MultiIndex(list)(f1, f2, f3, f4)

}
