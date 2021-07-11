package algorithms

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object KovidBestFitAlgorithm extends BaseAlgorithm {
  val name = "Kovid's Best Fit Algorithm"

  def createChunks(elements: Seq[Int], maxChunkSize: Int): Vector[Vector[Int]] = {
    createChunksBestFit(elements, maxChunkSize)
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
  private def createChunksBestFit(array: Seq[Int], chunkSize: Int): Vector[Vector[Int]] = {
    // Sanity Check
    if (array.isEmpty) { return Vector.empty }
    // Build priority queue, add first item
    val heap: mutable.PriorityQueue[Bin] = mutable.PriorityQueue()(Ordering.by(Bin.binOrder))
    heap += Bin.fromItem(array(0), chunkSize)
    // Add items to highest priority bin
    for (int <- array.tail) {
      if (heap.head.canAccomodate(int)) {
        val top = heap.dequeue()
        top.add(int)
        heap += top
      } else {
        heap += Bin.fromItem(int, chunkSize)
      }
    }
    return heap.map(bin => bin.toVector).toVector
  }
}
