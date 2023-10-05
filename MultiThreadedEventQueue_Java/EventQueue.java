import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class which implements an <code>EventQueue</code> based on the
 * "replicated workers" model. The tasks contained in its work
 * pool are objects of type <code>Event</code>
 *
 */
public class EventQueue {
    private int poolSize;
    private int nThreads; /* the number of threads */
    private int nWorkers;
    private int nGenerators;
    private int nWaitingWorkers = 0; /* the number of threads which are waiting */
    public int nWaitingGenerators = 0;
    public int nFinishedGenerators = 0;
    public boolean endGet = false; /* if the problem is solved */
    public boolean endAdd = false; 
    
    Queue<Event> tasks;

    /**
     * Constructor. Creates an object of type <code> EventQueue</code>.
     * @param nThreads - number of threads (both worker and generator)
     */
    public EventQueue(int nThreads,int nWorkers,int nGenerators, int poolSize) {
        this.nThreads = nThreads;
        this.nWorkers = nWorkers;
        this.nGenerators = nGenerators;
        this.poolSize = poolSize;
        tasks = new LinkedBlockingQueue<Event>(poolSize);
    }

    /**
     * Function which tries to obtain an <code>Event</code> object
     * from the <code>EventQueue</code>. If there are no available tasks,
     * the thread is locked until a task can be obtained or until the problem is solved.
     * @return A task from the pool, or null if the problem is solved.
     */
    public synchronized Event getEvent() {
      if (tasks.size() == 0) {
          nWaitingWorkers++;
          if (nWaitingWorkers >= nWorkers && nFinishedGenerators >= nGenerators) { /* if all workers wait and all generators finished and there are no tasks, problem is over */
            endGet = true;
            notifyAll();
            return null;
          } else { /* some worker is not waiting */
            while(!endGet && tasks.size() == 0) /* problem not solved, but no tasks are available */
              try {
                this.wait();
              }catch(InterruptedException e) {
                e.printStackTrace();
              }
            if (endGet)
              return null;
            nWaitingWorkers--; /* queue is not empty anymore */
          }
      }
      return tasks.remove();
    }

    synchronized boolean addEvent(Event ev) {
      if (tasks.size() == poolSize) { /* daca queue-ul este full */
        nWaitingGenerators++; /* un generator asteapta */
        while(tasks.size() == poolSize) {
          try {
            this.wait();
          } catch(InterruptedException e) {
            e.printStackTrace();
          }
        }
        nWaitingGenerators--;
      }
      tasks.add(ev);
      this.notify();
      return true;
    }
    // /**
    //  * Function which adds a task to the EventQueue.
    //  * @param ev - The task which will be added
    //  */
    // synchronized boolean addEvent(Event ev) {
    //  if (tasks.size() == 0 && nWaitingGenerators == nGenerators) {
    //   endGet = true;
    //   notifyAll();
    //   return false;
    //  }
    //  if (tasks.size() == poolSize) {
    //    nWaitingGenerators++;
    //    while (tasks.size() == poolSize) {
    //      try{
    //        this.wait();
    //      }catch(InterruptedException exc) {
    //        exc.printStackTrace();
    //      }
    //    }
    //    nWaitingGenerators--; /* queue is not full anymore */
    //  }
    //  tasks.add(ev);
    //  this.notify();
    //  return true;
    // }
    
    synchronized boolean isFull() {
      return (tasks.size() == poolSize);
    }


}


