package adventures.task

import monix.eval.Task

import scala.concurrent.duration.FiniteDuration

object TaskAdventures {

  /**
    * 1.  Create a task which returns 43
    *
    * See https://monix.io/docs/2x/eval/task.html#simple-builders for ways to construct Tasks
    */
  def immediatelyExecutingTask(): Task[Int] = {
    Task(43)
  }

  /**
    * 2.	Create a Task which when executed logs “hello world” (using `logger`)
    */
  def helloWorld(logger: String => Unit): Task[Unit] = {
   Task {
     logger("hello world")
   }
  }

  /**
    * 3.	Create a Task which always fails
    */
  def alwaysFailingTask(): Task[Unit] = {
    Task {
      throw new Exception("Always fail!")
    }
  }

  /**
    * 4.	There is 1 remote service which will return you a task that provides the current temperature in celcius.
    *
    */
  def getCurrentTempInF(currentTemp: () => Task[Int]): Task[Int] = {
    def cToF(c: Int) = c * 9 / 5 + 32

    currentTemp().map(cToF(_))
  }

  /**
    * 5.	There is 1 remote service which will return you a task that provides the current temperature in celcius.
    * The conversion is complex so we have decided to refactor it out to another remote microservice.
    * Make use of both of these services to return the current temperature in farenheit.
    */
  def getCurrentTempInFAgain(currentTemp: () => Task[Int], convertor: Int => Task[Int]): Task[Int] = {
    for {
      cTemp <- currentTemp()
      fTemp <- convertor(cTemp)
    } yield fTemp
  }

  /**
    * 6. Computing the complexity of a string is a very expensive op.  Implement this function so that complexity
    * of the calculation will be done in parallel.  Sum the returned results to
    * provide the overall complexity for all Strings.  (try using operations from monix)
    *
    * Also, check out the spec for this to see how the Monix TestScheduler can be used to simulate the passage of time
    * in tests.
    */
  def calculateStringComplexityInParallel(strings: List[String], complexity: String => Task[Int]): Task[Int] = {
    val aggregate: Task[List[Int]] = Task.gather(strings.map(each => Task(each.size))).map(_.toList)
    aggregate.flatMap[Int](list => Task(list.sum))
  }

  /**
    * 6.b. As above, but try to implement the parallel processing using the monix Applicative instance for Task
    * and the cats `sequence` function. (if you haven't heard of cats / sequence skip this).
    *
    * The following imports will help.
    * import monix.eval.Task.nondeterminism // look at the documentation / implementation for this
    * import monix.cats._
    * import cats.implicits._
    */
  def calculateStringComplexityInParallelAgain(strings: List[String], complexity: String => Task[Int]): Task[Int] = {
    ???
  }

  /**
    * 7.	Write a function which given a Task, will reattempt that task after a specified delay for a maximum number of
    * attempts if the supplied Task fails.
    */
  def retryOnFailure[T](t: Task[T], maxRetries: Int, delay: FiniteDuration): Task[T] = {
    t.delayExecution(delay).onErrorRestart(maxRetries)
  }

}
