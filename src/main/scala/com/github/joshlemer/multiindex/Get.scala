package com.github.joshlemer.multiindex

trait Get1[A, B1] {

  def index1: Index[A, B1]

  /** Get a list of all elements that match b1 on index 1 */
  def get1(b1: B1) = index1.getList(b1)

  /** Get a bag of all elements that match b1 on index 1 */
  def get1MultiSet(b1: B1): MultiSet[A] = index1(b1)
}
trait Get2[A, B1, B2] extends Get1[A, B1] {

  /** Get a list of all elements that match b2 on index 2 */
  def index2: Index[A, B2]

  /** Get a list of all elements that match b2 on index 2 */
  def get2(b2: B2) = index2.getList(b2)

  /** Get a bag of all elements that match b2 on index 2 */
  def get2MultiSet(b2: B2): MultiSet[A] = index2(b2)
}
trait Get3[A, B1, B2, B3] extends Get2[A, B1, B2] {

  /** Get a list of all elements that match b3 on index 3 */
  def index3: Index[A, B3]

  /** Get a list of all elements that match b3 on index 3 */
  def get3(b3: B3) = index3.getList(b3)

  /** Get a bag of all elements that match b2 on index 3 */
  def get3MultiSet(b3: B3): MultiSet[A] = index3(b3)
}
trait Get4[A, B1, B2, B3, B4] extends Get3[A, B1, B2, B3] {

  /** Get a list of all elements that match b2 on index 2 */
  def index4: Index[A, B4]

  /** Get a list of all elements that match b2 on index 2 */
  def get4(b4: B4) = index4.getList(b4)

  /** Get a bag of all elements that match b2 on index 2 */
  def get4MultiSet(b4: B4): MultiSet[A] = index4(b4)
}
