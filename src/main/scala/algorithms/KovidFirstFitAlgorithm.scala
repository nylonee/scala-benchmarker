package algorithms

import scala.collection.mutable.ListBuffer

object KovidFirstFitAlgorithm extends BaseAlgorithm {
  val name = "Kovid's First Fit Algorithm"

  def createChunks(elements: Seq[Int], maxChunkSize: Int): Vector[Vector[Int]] = {
    createChunksFirstFitAscending(elements, maxChunkSize)
  }

  private class Bin(capacity: Int) {
    var currWeight = 0
    var buffer = new ListBuffer[Int]()
    def remaining = capacity - currWeight
    // Add a new integer to Bin
    def add(int: Int): Unit = {
      buffer += int
      currWeight += int
    }
    // Check if we can accomodate new integer
    def canAccomodate(int: Int): Boolean = int <= remaining
    // Convenience func to vectorize buffer
    def toVector = buffer.toVector
  }

  private object Bin {
    // Instantiate a bin with an item
    def fromItem(int: Int, capacity: Int): Bin = {
      val bin = new Bin(capacity)
      bin.add(int)
      return bin
    }
    def binOrder(b: Bin) = b.remaining
  }

  private def createChunksFirstFit(array: Seq[Int], chunkSize: Int): Vector[Vector[Int]] = {
    // Sanity Check
    if (array.isEmpty) { return Vector.empty }
    // FirstFit algorithm
    var bins = List(new Bin(chunkSize))
    for (int <- array) {
      val curr = bins.head
      if (curr.canAccomodate(int)) {
        curr.add(int)
      } else {
        bins = Bin.fromItem(int, chunkSize) :: bins
      }
    }
    return bins.map(bin => bin.toVector).toVector
  }

  private def createChunksFirstFitAscending(array: Seq[Int], chunkSize: Int): Vector[Vector[Int]] = {
    return createChunksFirstFit(array.sorted, chunkSize)
  }
}
