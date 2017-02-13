package com.ruiandrebatista.fs2.extensions

import fs2._
import fs2.util.Async
import fs2.util.syntax._


object Pipes {

  /**
   * Maps a stream's elements assynchronously with no order preserved
   *
   *  @param f function to map elements
   * @param concurrencyLevel howmany operations run in parallel
   */
  def mapAsyncUnordered[F[_]: Async, I, O](f: I => F[O])(concurrencyLevel: Int): Pipe[F, I, O] = { s: Stream[F, I] =>
    def mkInner(elem: I) = Stream.eval(f(elem))
    concurrent.join(concurrencyLevel)(s.map(mkInner(_)))
  }

  /**
    * Maps over a stream with an effectfull function in an assynchronous way
    * Concorrency level controls howmany requests are running at the same time.
    *  Note: Order is preserved.
    * 
    */
  def mapAsync[F[_]: Async, I, O](f: I => F[O])(concurrencyLevel: Int): Pipe[F, I, O] = {s =>
    for {
      killSignal <- Stream.eval(async.signalOf[F, Boolean](false))
      runningQueue <- Stream.eval(async.mutable.Queue.bounded[F, Option[F[O]]](concurrencyLevel))
      producer = s.evalMap (i => f(i).start)
      .noneTerminate
      .evalMap(runningQueue.enqueue1)
      .interruptWhen(killSignal)
      consumer = runningQueue.dequeue.unNoneTerminate
      .evalMap(identity)
      .onFinalize(killSignal.set(true))
      o <- (producer mergeDrainL consumer)
    } yield o
  }
}
