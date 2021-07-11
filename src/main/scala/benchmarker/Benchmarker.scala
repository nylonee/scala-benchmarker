package benchmarker

import cats.effect.{Clock, Sync}
import cats.syntax.all._
import monix.eval.Task

import scala.concurrent.duration.MILLISECONDS

class Benchmarker {

  def measure[A](n: Int)(fa: Task[A])
                      (implicit clock: Clock[Task]): Task[(Seq[A], Long)] =
    Task
      .sequence((1 to n).map(_ => measureBase(fa)))
      .map { results =>
        val answers = results.map(_._1)
        val averageTimeTaken = results.map(_._2).sum / results.map(_._2).size

        (answers, averageTimeTaken)
      }

  private def measureBase[F[_], A](fa: F[A])
                      (implicit F: Sync[F], clock: Clock[F]): F[(A, Long)] = {

    for {
      start  <- clock.monotonic(MILLISECONDS)
      result <- fa
      finish <- clock.monotonic(MILLISECONDS)
    } yield (result, finish - start)
  }
}
