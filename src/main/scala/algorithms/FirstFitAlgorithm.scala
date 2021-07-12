package algorithms

import scala.annotation.tailrec

object FirstFitAlgorithm extends BaseAlgorithm {
  val name = "First Fit Algorithm"

  def createChunks(elements: Seq[Int], maxChunkSize: Int): Vector[Vector[Int]] = {
    createChunksFirstFit(elements.sorted.toVector, maxChunkSize, Vector.empty)
  }

  @tailrec
  private def createChunksFirstFit(array: Vector[Int], chunkSize: Int, accumulator: Vector[Vector[Int]]): Vector[Vector[Int]] =
    (array, accumulator) match {
      case (Seq(), _) =>
        accumulator
      case (_, Seq()) =>
        createChunksFirstFit(array.tail, chunkSize, Vector(Vector(array.head)))
      case (_, _) =>
        if (accumulator.head.sum + array.head <= chunkSize) {
          createChunksFirstFit(array.tail, chunkSize, (array.head +: accumulator.head) +: accumulator.tail)
        } else {
          createChunksFirstFit(array.tail, chunkSize, Vector(array.head) +: accumulator)
        }
    }
}
