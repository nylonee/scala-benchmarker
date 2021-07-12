package algorithms

import scala.annotation.tailrec
import scala.collection.SortedMap

object BestFitAlgorithm extends BaseAlgorithm {
  val name = "Best Fit Algorithm"

  def createChunks(elements: Seq[Int], maxChunkSize: Int): Vector[Vector[Int]] = {
    createChunksBestFit(elements.toVector, maxChunkSize, SortedMap.empty)
  }

  @tailrec
  private def createChunksBestFit(array: Vector[Int], chunkSize: Int, accumulator: SortedMap[Int, Vector[Vector[Int]]]): Vector[Vector[Int]] =
    (array, accumulator) match {
      case (Seq(), _) =>
        accumulator.values.toVector.flatten

      case (_, Seq()) =>
        createChunksBestFit(array.tail, chunkSize, SortedMap((chunkSize - array.head) -> Vector(Vector(array.head))))

      case (_, _) =>
        val newAccumulator: SortedMap[Int, Vector[Vector[Int]]] = accumulator.rangeFrom(array.head).headOption match {
          case None =>
            val newLocation = chunkSize - array.head
            val existingValuesAtNewLocation = accumulator.getOrElse(newLocation, Vector.empty)
            accumulator + (newLocation -> (Vector(Vector(array.head)) ++ existingValuesAtNewLocation))

          case Some((oldLocation, vectors)) =>
            val newLocation = oldLocation - array.head
            val updatedAccumulator = accumulator + (oldLocation -> vectors.drop(1))
            updatedAccumulator + (newLocation ->
              (updatedAccumulator.getOrElse(newLocation, Vector.empty) ++
                Vector(vectors.headOption.getOrElse(Vector.empty) ++ Vector(array.head))))
        }

        createChunksBestFit(array.tail, chunkSize, newAccumulator)
    }
}
