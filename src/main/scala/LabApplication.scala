import java.lang.Runtime
import java.util.concurrent.{ForkJoinPool, RecursiveTask}
package de.othr.dco {

  object LabApplication extends App {

    /*
   Instantiate your fork-join-pool here,
   start task(s) and
   inform about the result (from) here
   */
    val start = 0
    val end = 24
    val inclusiveRange: List[Int] = (start to end).toList
    println(inclusiveRange.mkString("Array(", ", ", ")"))

    val numberOfProcessors = Runtime.getRuntime.availableProcessors()

    val pool: ForkJoinPool = new ForkJoinPool(numberOfProcessors)

    val recursiveTask = new RecursiveImmutable(inclusiveRange)

    val startTime = System.currentTimeMillis()

    // Execute the task
    val result = pool.submit(recursiveTask)

    val endTime = System.currentTimeMillis()

    println(endTime - startTime)

    println(result.get().mkString("Array(", ", ", ")"))
    pool.shutdown()

  }

//  class RecursiveMutable(array: Array[Int], start: Int, end: Int) extends RecursiveAction {
//    override def compute(): Unit = {
//      if (end - start <= 10) {
//        for (i <- start to end) {
//          array(i) = array(i) * 2
//        }
//      }
//      else {
//        val mid = (end + start) / 2
//        val left = new RecursiveMutable(array, start, mid)
//        val right = new RecursiveMutable(array, mid, end)
//        left.fork()
//        right.compute()
//
//        left.join()
//      }
//    }
//  }

  class RecursiveImmutable(list: List[Int]) extends RecursiveTask[List[Int]] {
    override def compute(): List[Int] = {
      if(list.length <= 10)
        list.map(i => i * 2)
      else {
        val (leftList, rightList) = list.splitAt(list.length / 2)
        val left = new RecursiveImmutable(leftList)
        val right = new RecursiveImmutable(rightList)
        left.fork()
        right.fork()

        val leftResult = left.join()
        val rightResult = right.join()
        leftResult ++ rightResult
      }
    }
  }

}