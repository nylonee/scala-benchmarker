# Scala Algorithm Benchmarker

Benchmarks scala algorithms in FP style using Monix and Cats

## Quick Overview
The benchmarker library introduces a function called measure:

```
def measure[A](n: Int)(fa: Task[A])(implicit clock: Clock[Task]): Task[(Seq[A], Long)]
```
In English:

> The function measure will benchmark any function wrapped in a monix Task. The function will run n times, and output all results, plus the average time taken to complete the processing. By default, the Clock is provided implicitly, but this can be overridden in testing to emulate time passing faster or slower.

This can be applied to any function as follows:
```
import monix.eval.Task
import scala.benchmarker.Benchmarker.measure

def functionToBenchmark: Int = 1

val (answers, timeTaken) = measure(5) { // Run `functionToBenchmark` 5 times, and output the average time taken
Task(functionToBenchmark)
}

println(s"functionToBenchmark took $timeTaken milliseconds")

// All five outputted answers must be correct
answers.foreach(answer => assert(answer == 1))
```

## Demo Usage
* Clone the project
* Run BinProblemBenchmarked.scala using your favourite compiler/IDE

## Usage, writing your own algorithms
When writing your own algorithms that you'd like to benchmark, the setup is straightforward. Consider the following structure
```
YourNewAlgorithmRunner.scala
algorithms/
├─ BaseAlgorithm.scala
├─ YourAlgorithm.scala
benchmarker/
├─ Benchmarker.scala
```
The files that you will need to modify are: `YourNewAlgorithmRunner.scala`, `BaseAlgorithm.scala` and `YourAlgorithm.scala`. Let's start looking at what each of these files do

### BaseAlgorithm.scala
This defines the trait type that all algorithms will follow. For example, to define a sorting algorithm that takes a sequence of integers, and outputs a sequence of integers:
```
package algorithms

trait BaseAlgorithm {
    def name: String
    
    def sort(elements: Seq[Int]): Seq[Int]
}
```
The reason this trait is needed for the benchmarker is to abstract over multiple implementations of the same algorithm. This allows us to loop through all the implementations and run nifty benchmarking stuff on each one of them.

### YourAlgorithm.scala
This is where the meat of your algorithm goes. Since it is extending BaseAlgorithm, you must override `name` and `sort()`. For example:
```
package algorithms

object YourAlgorithm extends BaseAlgorithm {
    override val name = "My First Algorithm"
    
    override def sort(elements: Seq[Int]): Seq[Int] =
        elements.sorted // Use the built-in Scala sorting algorithm
}
```

### YourNewAlgorithmRunner.scala
Last but not least, the algorithm runner will apply the benchmarking methods and print the results nicely for you to compare algorithms:
```
import benchmarker.Benchmarker
import algorithms._
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global

import scala.util.Random

object YourNewAlgorithmRunner extends Benchmarker {

    def main(args: Array[String]): Unit = {
    
        // Make a test array for sorting
        val testArray = List.range(1, 10000000).map { _ => Random.nextInt(1000) }
        
        val runsPerTest = 2 // The time for each run gets averaged to provide a more accurate benchmark
        
        val algorithmsToTest: Seq[BaseAlgorithm] = Seq(
          YourAlgorithm // If you write more algorithms, update this val to include the new ones
        )
    
        algorithmsToTest.foreach { algorithm =>
        
          // Run the testing
          val (answers, averageTimeTaken) = runBenchmarker(algorithm)(runsPerTest, testArray).runSyncUnsafe()
    
          // Print the result
          println(s"On average, running ${algorithm.name} took $averageTimeTaken milliseconds")
        }
    }

    private def runBenchmarker(algorithm: BaseAlgorithm)(n: Int, elements: Seq[Int]) = measure(n) {
        Task(algorithm.sort(elements))
    }
}
```