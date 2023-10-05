import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;


/*
 * Class which represents an event generator thread. It reads data from a file which
 * is given as parameter in its constructor, creates and event from this data, and then
 * adds it to an event queue
 */
public class Generator extends Thread {
  
  EventQueue eq; /* the event queue in which it adds */
  FileParser file; /* the file where the data is read from */
  int eventCount;
  
  
  public Generator(EventQueue eq, String filePath,int eventCount) {
    this.eq = eq;
    file = new FileParser(filePath);
    this.eventCount = eventCount;
    file.open();
  }


  @Override
  public void run() {
    // System.out.println("Generator " + this.getName() + " started");
    Event newEvent;
    
    String lineRead = file.parseNextLine();
    int timeToWait = 0;
    EventType eventType;
    int value;

    
    for(int i = 0; i < eventCount; i ++) {
      String[] splitLine = lineRead.split(","); /* tokenize the line that was read */
      
      timeToWait = Integer.parseInt(splitLine[0]);
      try {
        Thread.sleep(timeToWait);
      }catch(InterruptedException ex) {
        ex.printStackTrace();
      }
      eventType = EventType.valueOf(splitLine[1]);
      value = Integer.parseInt(splitLine[2]);
      newEvent = new Event(eventType,value); /* create a new event */
      eq.addEvent(newEvent);
      lineRead = file.parseNextLine(); /* read the next line */
    }
    eq.nFinishedGenerators++; /* the generator finished its work so it's now waiting */
    file.close();
    // System.out.println("Generator " + this.getName() + " ended");
  }
}
