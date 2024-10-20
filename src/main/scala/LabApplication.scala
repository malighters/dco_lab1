import java.lang.Runtime
import java.util.concurrent.ForkJoinPool
package de.othr.dco {

  import java.util.concurrent.RecursiveAction

  object LabApplication extends App {

    /*
   Instantiate your fork-join-pool here,
   start task(s) and
   inform about the result (from) here
   */
    val start = 0
    val end = 24
    val inclusiveRange: Array[Int] = (start to end).toArray
    println(inclusiveRange.mkString("Array(", ", ", ")"))

    val numberOfProcessors = Runtime.getRuntime.availableProcessors()

    val pool: ForkJoinPool = new ForkJoinPool(4)

    val recursiveTask = new RecursiveMutable(inclusiveRange, start, end)

    val startTime = System.currentTimeMillis()

    // Execute the task
    val result = pool.invoke(recursiveTask)

    val endTime = System.currentTimeMillis()

    println(endTime - startTime)

    println(inclusiveRange.mkString("Array(", ", ", ")"))
    pool.shutdown()

  }

  class RecursiveMutable(array: Array[Int], start: Int, end: Int) extends RecursiveAction {
    override def compute(): Unit = {
      if (end - start <= 10) {
        for (i <- start to end) {
          array(i) = array(i) * 2
        }
      }
      else {
        val mid = (end + start) / 2
        val left = new RecursiveMutable(array, start, mid)
        val right = new RecursiveMutable(array, mid, end)
        left.fork()
        right.compute()

        left.join()
      }
    }

  }

}