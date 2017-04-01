package com.ruiandrebatista.fs2.extensions

import org.specs2._
import org.specs2.concurrent.ExecutionEnv
import org.scalacheck._

import fs2._
import fs2.util.Attempt

import com.ruiandrebatista.fs2.extensions.syntax._

class MapAsyncSpec(implicit executionEnv: ExecutionEnv) extends mutable.Specification with ScalaCheck {
  implicit val strategy = Strategy.fromCachedDaemonPool("fs2")
  val concurrencyGen = Arbitrary(Gen.choose(1, 10))

  def expectLog[A](stream: Stream[Task, A], expected: Attempt[Vector[A]]) = {
    stream.runLog.attempt.unsafeRunAsyncFuture must beEqualTo(expected).await
  }

  def expectLogUnordered[A](stream: Stream[Task, A], expected: Attempt[Vector[A]]) = {
    stream.runLog.attempt.unsafeRunAsyncFuture.map(_.right.map(_.toSet)) must beEqualTo(expected.right.map(_.toSet)).await
  }

  "mapAsync combinator" should {
    "Preserve order in simple streams" in prop { (v: Vector[Int], concurrency: Int) =>
      (v.nonEmpty && concurrency > 0) ==> {
        val s = Stream.emits[Task, Int](v)
          .mapAsync(i => Task.delay(i))(concurrency)
        expectLog(s, Right(v))
      }
    }.setArbitrary2(concurrencyGen)

    "fail with failed streams" in {
      object Exc extends Exception("Failed")
      val s = Stream[Task, Int](1)
        .mapAsync(i => Task.delay(i))(10)
        .flatMap { _ => Stream.fail(Exc) }
      expectLog(s, Left(Exc))

      val s2 = Stream[Task, Int](1)
        .flatMap { _: Int => Stream.fail(Exc) }
        .mapAsync((i: Int) => Task.delay(i))(10)
      expectLog(s, Left(Exc))
    }
  }

  "mapUnorderedAsync combinator" should {
    "map all values in simple streams" in prop { (v: Vector[Int], concurrency: Int) =>
      (v.nonEmpty && concurrency > 0) ==> {
        val s = Stream.emits[Task, Int](v)
          .mapAsyncUnordered(i => Task.delay(i))(concurrency)
        expectLogUnordered(s, Right(v))
      }
    }.setArbitrary2(concurrencyGen)
  }
}
