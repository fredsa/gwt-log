package com.allen_sauer.gwt.log.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LogMessage implements Serializable {

  public int level;
  private String message;
  private WrappedClientThrowable wrappedClientThrowable;

  public LogMessage(int level, String message, WrappedClientThrowable wrappedClientThrowable) {
    this.level = level;
    this.message = message;
    this.wrappedClientThrowable = wrappedClientThrowable;
  }

  // For GWT serialization
  @SuppressWarnings("unused")
  private LogMessage() {
  }

  public int getLevel() {
    return level;
  }

  public String getMessage() {
    return message;
  }

  public WrappedClientThrowable getWrappedClientThrowable() {
    return wrappedClientThrowable;
  }
}
