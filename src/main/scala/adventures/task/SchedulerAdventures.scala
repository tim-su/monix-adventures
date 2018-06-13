package adventures.task

import java.util.concurrent.TimeUnit

import monix.execution.Scheduler.{global => scheduler}

object SchedulerAdventures {

  def main(args: Array[String]): Unit = {

    var count: Int = 0
    scheduler.scheduleAtFixedRate(1, 2, TimeUnit.SECONDS,
      new Thread(() => {
        println(s"Fixed delay task ${count} executed.")
        count = count + 1
      })
    )
    while (true) {
    }

  }

}
