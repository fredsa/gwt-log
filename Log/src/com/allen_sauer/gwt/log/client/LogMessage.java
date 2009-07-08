package com.allen_sauer.gwt.log.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LogMessage implements Serializable {

  public int level;
  private String message;
  private int messageSequence;
  private WrappedClientThrowable wrappedClientThrowable;

  // For GWT serialization
  @SuppressWarnings("unused")
  private LogMessage() {
  }

  public LogMessage(int messageSequence, int level, String message,
      WrappedClientThrowable wrappedClientThrowable) {
    this.messageSequence = messageSequence;
    this.level = level;
    this.message = message;
    this.wrappedClientThrowable = wrappedClientThrowable;
  }

  public int getLevel() {
    return level;
  }

  public String getMessage() {
    return message;
  }

  public int getMessageSequence() {
    return messageSequence;
  }

  public WrappedClientThrowable getWrappedClientThrowable() {
    return wrappedClientThrowable;
  }
}
