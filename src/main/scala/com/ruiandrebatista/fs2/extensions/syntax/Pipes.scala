package com.ruiandrebatista.fs2.extensions.syntax

import com.ruiandrebatista.fs2.extensions.Pipes

import fs2._
import fs2.util.Async

trait PipesSyntaxExtensions {

  implicit class PipesAsyncSyntaxOps[F[_]: Async, O](s: Stream[F, O]) {
    def mapAsyncUnordered[O2](f: O => F[O2])(concurrencyLevel: Int) = s.through(Pipes.mapAsyncUnordered(f)(concurrencyLevel))
  }

}
