
public class Event {
  
  private EventType eventType; /* the type of the event */
  private int value; /* the value that will be sent to the worker */
  
  public Event(EventType type, int number) {
    eventType = type;
    this.value = number;
  }
  
  public EventType getType() {
    return eventType;
  }
  
  public int getValue() {
    return value;
  }
  
  @Override
  public String toString() {
    return new String(eventType.name() + " " +  Integer.toString(value));
  }

}
