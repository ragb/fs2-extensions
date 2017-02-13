package com.ruiandrebatista.fs2.extensions

import org.scalatest._
import org.scalatest.prop._
import org.scalatest.concurrent.ScalaFutures

import fs2._

import com.ruiandrebatista.fs2.extensions.syntax._

class MapAsyncSpec extends AsyncFlatSpec with Matchers {
  implicit val strategy = Strategy.fromCachedDaemonPool("fs2")
  "mapAsync combinator" should "Preserve order in simple streams" in {
    val v = (1 to 100).toVector

    val s = Stream.emits[Task, Int](v)
      .mapAsync(i => Task.delay(i))(10)
    s.runLog.unsafeRunAsyncFuture.map { _ shouldBe v }

  }

  it should "fail with failed streams" in {
    object Exc extends Exception("Failed")
    val s = Stream[Task, Int](1)
      .mapAsync(i => Task.delay(i))(10)
      .flatMap { _ => Stream.fail(Exc) }
    s.runLog.attempt.unsafeRunAsyncFuture.map { _ shouldBe Left(Exc) }

    val s2 = Stream[Task, Int](1)
      .flatMap { _: Int => Stream.fail(Exc) }
      .mapAsync((i: Int) => Task.delay(i))(10)
    s2.runLog.attempt.unsafeRunAsyncFuture.map { _ shouldBe Left(Exc) }

  }
}
