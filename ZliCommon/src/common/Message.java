package common;

import java.io.Serializable;

import Enums.Task;
/**
 * This class is the "Message" we send back and forth between the client and the server
 * we create messages by passing  a String in order to know which action to perform
 * and we send Object as the Data we pass, arrays,Strings and etc...
 * @author Eliav shabat
 */
public class Message implements Serializable {
  private static final long serialVersionUID = 1L;
  private Task task;
  private Object object;
  
  public Message(Task task, Object object) {
    this.task = task;
    this.object = object;
  }
  
  public Task getTask() {
    return this.task;
  }
  
  public void setTask(Task task) {
    this.task = task;
  }
  
  public Object getObject() {
    return this.object;
  }
  
  public void setObject(Object object) {
    this.object = object;
  }
}
