import benchmarker.Benchmarker
import algorithms.{BaseAlgorithm, KovidBestFitAlgorithm, KovidFirstFitAlgorithm, NihalAlgorithm}
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.util.Random

object BinProblemBenchmarked extends Benchmarker {

  def main(args: Array[String]): Unit = {
    // Initialize the testing
    val testArray = List.range(1, 10000000).map { _ => Random.nextInt(1000) }
    val maxChunkSize = 900
    val runsPerTest = 2
    val algorithmsToTest: Seq[BaseAlgorithm] = Seq(NihalAlgorithm, KovidBestFitAlgorithm, KovidFirstFitAlgorithm)

    println("Starting benchmarking suite...")

    algorithmsToTest.foreach { algorithm =>
      // Run the testing
      val (answers, averageTimeTaken) = runCreateChunks(algorithm)(runsPerTest, testArray, maxChunkSize).runSyncUnsafe()

      // Make sure the result is accurate
      testResults(answers, testArray, maxChunkSize)

      // Aggregate the result
      val chunks = answers.map(_.size).sum / answers.length

      // Print the result
      println(s"On average, running ${algorithm.name} took $averageTimeTaken milliseconds to produce $chunks chunks")
    }
  }

  private def runCreateChunks(algorithm: BaseAlgorithm)(n: Int, elements: Seq[Int], chunkSize: Int) = measure(n) {
    Task(algorithm.createChunks(elements, chunkSize))
  }

  private def testResults(results: Seq[Seq[Seq[Int]]], elements: Seq[Int], maxChunkSize: Int) = results.map { result =>
    // Test 1: The sum of each of the chunks should be less than or equal to chunkSize, unless there's only one element
    result.foreach { chunk =>
      if (chunk.length != 1)
        assert(chunk.sum <= maxChunkSize)
    }

    // Test 2: There should be less chunks than there are elements
    assert(result.length < elements.length)

    // Test 3: There should be no missing or new elements
    assert(result.flatten.sorted == elements.sorted)
  }
}
