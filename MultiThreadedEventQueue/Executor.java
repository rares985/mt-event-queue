import java.util.ArrayList;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.charset.Charset;
import java.io.IOException;
/**
 * Clasa ce reprezinta un thread worker.
 */
class Executor extends Thread {
    private EventQueue eq;
    public boolean available;
    public ArrayList<Integer> primeResults;
    public ArrayList<Integer> squareResults;
    public ArrayList<Integer> fiboResults;
    public ArrayList<Integer> factResults;

    public Executor(EventQueue EventQueue) {
        this.eq = EventQueue;
        primeResults = new ArrayList<Integer>();
        squareResults = new ArrayList<Integer>();
        fiboResults = new ArrayList<Integer>();
        factResults = new ArrayList<Integer>();
    }

   
    int execute(Event ev) {
        int res = 0;
        switch(ev.getType()) {
              case PRIME:
                res = Utils.primeLowerThan(ev.getValue());
                primeResults.add(res);
                break;
              case FACT:
                res = Utils.factLowerThan(ev.getValue());
                factResults.add(res);
                break;
              case SQUARE:
                res = Utils.squareLowerThan(ev.getValue());
                squareResults.add(res);
                break;
              case FIB:
                res = Utils.fiboLowerThan(ev.getValue());
                fiboResults.add(res);
                break;
              default:
                 break;
        }
        return res;
    }
    
	/* Get an event from the queue and execute it,
	or terminate the thread if there is no event left */    
    @Override
    public void run() {
      System.out.println("Worker " + this.getName() + " started");
      Event toExecute;
      while(true) {
        toExecute = eq.getEvent();
        if (toExecute == null) 
          break;
        execute(toExecute);
        System.out.println("Worker " + this.getName() + " executing " + toExecute);
      }
      System.out.println("Worker " + this.getName() + " ended");
    }
}





