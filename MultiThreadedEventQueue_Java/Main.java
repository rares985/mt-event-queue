import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {
  public static void main(String[] args) {
    
    
    if (args.length < 2) {
      System.out.println("Usage: java queueSize eventsPerFile file1, file2 ...");
      return;
    }
    
    int nGenerators = args.length - 2; /* the number of generator threads, one per file */
    int nWorkers = 2; /* number of workers, fixed to 2 */
    int nThreads = nGenerators + nWorkers; /* total number of threads */
    
    int queueSize = Integer.parseInt(args[0]); /* the size of the event queue */
    int eventsPerFile = Integer.parseInt(args[1]); /* the number of events per file */
    
    String outputFileFib = "FIB.out";
    String outputFileSq = "SQUARE.out";
    String outputFilePrime = "PRIME.out";
    String outputFileFact = "FACT.out";
    
    ArrayList<String> primeResults = new ArrayList<String>();
    ArrayList<String> squareResults = new ArrayList<String>();
    ArrayList<String> fiboResults = new ArrayList<String>();
    ArrayList<String> factResults = new ArrayList<String>();
    

    /* Create the event queue, which is shared among all threads */
    EventQueue eventQueue = new EventQueue(nThreads,nWorkers,nGenerators,queueSize);
    

    /* Create the thread pool */
    Executor[] workers = new Executor[nWorkers];
    Generator[] generators = new Generator[nGenerators];
    
    for(int i = 2; i < args.length; i ++) {
        generators[i-2] = new Generator(eventQueue,args[i],eventsPerFile);
    }
    for(int i = 0; i < nWorkers; i ++) {
      workers[i] = new Executor(eventQueue);
    }
    


    /* start the threads */
    for(int i = 0; i < nGenerators; i++) {
      generators[i].start();
    }
    
    for(int i = 0; i < nWorkers; i++) {
      workers[i].start();
    }
    

    /* wait for them to die */
    for(int i = 0; i < nGenerators; i++) {
      try {
        generators[i].join();
      }catch(InterruptedException ex) {
        ex.printStackTrace();
      }
    }
    
    for(int i = 0; i < nWorkers; i ++) {
      try {
        workers[i].join();
      }catch(InterruptedException ex) {
        ex.printStackTrace();
      }
    }

    class StringComparator implements Comparator<String> {
      @Override
      public int compare(String s1, String s2) {
        return Integer.parseInt(s1) - Integer.parseInt(s2);
      }
    };
    
    
    /* Join all data structures from the workers together and sort them
       individually, then print the output to the corresponding file */
    for(int i = 0; i < nWorkers; i ++) {

      for(int j = 0; j < workers[i].primeResults.size();j++) {
        primeResults.add(Integer.toString(workers[i].primeResults.get(j)));
      }

      Collections.sort(primeResults, new StringComparator());
      
      try {
        Files.write(Paths.get(outputFilePrime),primeResults,Charset.forName("UTF-8"));
      }catch(IOException ex) {
        ex.printStackTrace();
      }
      
      
      for(int j = 0; j < workers[i].squareResults.size();j++) {
        squareResults.add(Integer.toString(workers[i].squareResults.get(j)));
      }

      Collections.sort(squareResults, new StringComparator());
      
      try {
        Files.write(Paths.get(outputFileSq),squareResults,Charset.forName("UTF-8"));
      }catch(IOException ex) {
        ex.printStackTrace();
      }
      

      for(int j = 0; j < workers[i].fiboResults.size();j++) {
        fiboResults.add(Integer.toString(workers[i].fiboResults.get(j)));
      }
      
      Collections.sort(fiboResults,new StringComparator());
      
      try {
        Files.write(Paths.get(outputFileFib),fiboResults,Charset.forName("UTF-8"));
      }catch(IOException ex) {
        ex.printStackTrace();
      }
      
      
      for(int j = 0; j < workers[i].factResults.size();j++) {
        factResults.add(Integer.toString(workers[i].factResults.get(j)));
      }
      
      Collections.sort(factResults, new StringComparator());
      
      try {
        Files.write(Paths.get(outputFileFact),factResults,Charset.forName("UTF-8"));
      }catch(IOException ex) {
        ex.printStackTrace();
      }
    }
  }
}
