package com.allen_sauer.gwt.log.client;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LogRecord implements Serializable {

  public int level;
  private String message;
  private int recordSequence;
  private WrappedClientThrowable wrappedClientThrowable;

  public LogRecord(int recordSequence, int level, String message,
      WrappedClientThrowable wrappedClientThrowable) {
    this.recordSequence = recordSequence;
    this.level = level;
    this.message = message;
    this.wrappedClientThrowable = wrappedClientThrowable;
  }

  // For GWT serialization
  @SuppressWarnings("unused")
  private LogRecord() {
  }

  public int getLevel() {
    return level;
  }

  public String getMessage() {
    return message;
  }

  public int getRecordSequence() {
    return recordSequence;
  }

  public WrappedClientThrowable getWrappedClientThrowable() {
    return wrappedClientThrowable;
  }
}
