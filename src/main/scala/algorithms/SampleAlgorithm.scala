package algorithms

import scala.annotation.tailrec

object SampleAlgorithm extends BaseAlgorithm {
  val name = "Sample Chunking Algorithm"

  def createChunks(elements: Seq[Int], maxChunkSize: Int): Vector[Vector[Int]] = {
    createChunksAcc(elements.sorted.toVector, maxChunkSize, 0, Vector.empty, Vector.empty)
  }

  @tailrec
  private def createChunksAcc(array: Vector[Int], maxChunkSize: Int, currentChunkSize: Int, incompleteChunk: Vector[Int], completeChunks: Vector[Vector[Int]]): Vector[Vector[Int]] = {
    (array, incompleteChunk) match {
      case (Seq(), _) =>
        incompleteChunk +: completeChunks
      case (_, Seq()) =>
        createChunksAcc(array.dropRight(1), maxChunkSize, currentChunkSize + array.last, incompleteChunk :+ array.last, completeChunks)
      case (_, _) =>
        if (array.head + currentChunkSize > maxChunkSize) {
          createChunksAcc(array, maxChunkSize, 0, Vector.empty, incompleteChunk +: completeChunks)
        } else {
          createChunksAcc(array.tail, maxChunkSize, currentChunkSize + array.head, array.head +: incompleteChunk, completeChunks)
        }
    }
  }
}
