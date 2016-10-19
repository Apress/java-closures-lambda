import java.util.*;
import java.util.concurrent.*;

public class Listing4 {


  protected ThreadFactory createThreadFactory() {
    return r -> {
      Thread t = new Thread(r);
      t.setPriority(Thread.MAX_PRIORITY);
      return t;
    };
  }

  protected RejectedExecutionHandler createdRejectedExecutionHandler() {
    return (rejected, executor) -> {
      BlockingQueue<Runnable> fullQueue = executor.getQueue();

      // Wait for the timeout
      try {
        boolean submitted = fullQueue.offer(rejected, 1L, TimeUnit.SECONDS);
        if (submitted) return;
      } catch (InterruptedException e) {
        // Fall through
      }

      // If we get here, the queue is well and truly full
      // First, execute our work
      rejected.run();

      // Next, execute another piece of work to be nice
      // (This also slows down the producer thread and might break a deadlock.)
      Runnable otherWork = fullQueue.poll();
      if (otherWork != null) otherWork.run();
    };
  }

  public ThreadPoolExecutor createThreadPoolExecutor() {
    int processorCount = Math.max(1, Runtime.getRuntime().availableProcessors());
    int corePoolSize = processorCount;
    int maxPoolSize = processorCount * 2 + 1;
    long threadTimeoutMag = 1L;
    TimeUnit threadTimeoutUnit = TimeUnit.SECONDS;
    BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(processorCount * 4 + 1);
    ThreadFactory threadFactory = createThreadFactory();
    Objects.requireNonNull(threadFactory, "thread factory for thread pool executor");
    RejectedExecutionHandler rejectHandler = createdRejectedExecutionHandler();
    Objects.requireNonNull(rejectHandler, "rejected execution handler for thread pool executor");

    return new ThreadPoolExecutor(
        corePoolSize,
        maxPoolSize,
        threadTimeoutMag, threadTimeoutUnit,
        queue,
        threadFactory,
        rejectHandler
    );
  }

}

