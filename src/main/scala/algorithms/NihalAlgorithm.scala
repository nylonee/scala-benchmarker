package algorithms

import scala.annotation.tailrec

object NihalAlgorithm extends BaseAlgorithm {
  val name = "Nihal's Algorithm"

  def createChunks(elements: Seq[Int], maxChunkSize: Int): Vector[Vector[Int]] = {
    @tailrec
    def createChunksAcc(array: Vector[Int], currentChunkSize: Int, incompleteChunk: Vector[Int], completeChunks: Vector[Vector[Int]]): Vector[Vector[Int]] = {
      (array, incompleteChunk) match {
        case (Vector(), _) =>
          incompleteChunk +: completeChunks
        case (_, Vector()) =>
          createChunksAcc(array.dropRight(1), currentChunkSize + array.last, incompleteChunk :+ array.last, completeChunks)
        case (_, _) =>
          if (array.head + currentChunkSize > maxChunkSize) {
            createChunksAcc(array, 0, Vector.empty, incompleteChunk +: completeChunks)
          } else {
            createChunksAcc(array.tail, currentChunkSize + array.head, array.head +: incompleteChunk, completeChunks)
          }
      }
    }

    createChunksAcc(elements.sorted.toVector, 0, Vector.empty, Vector.empty)
  }
}
