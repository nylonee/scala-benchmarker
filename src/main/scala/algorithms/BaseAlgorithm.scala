package algorithms

trait BaseAlgorithm {
  def name: String

  def createChunks(elements: Seq[Int], maxChunkSize: Int): Seq[Seq[Int]]
}
