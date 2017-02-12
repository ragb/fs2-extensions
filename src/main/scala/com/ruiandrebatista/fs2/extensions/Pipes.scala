package com.ruiandrebatista.fs2.extensions

import fs2._
import fs2.util.Async
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
}
