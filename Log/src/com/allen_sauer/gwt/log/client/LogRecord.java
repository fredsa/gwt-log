package com.allen_sauer.gwt.log.client;

import com.google.gwt.core.client.GWT;

import java.io.Serializable;

@SuppressWarnings("serial")
public class LogRecord implements Serializable {

  static final LogMessageFormatter FORMATTER =
      (LogMessageFormatter) (GWT.isClient() ? GWT.create(LogMessageFormatter.class)
      : null);
  private static int gloablRecordSequence;
  public int level;
  private String category;
  private String message;
  private int recordSequence;
  private transient Throwable throwable;

  private WrappedClientThrowable wrappedClientThrowable;

  // c category
  // C FQCN
  // d date
  // F filename
  // l location
  // L line number
  // m message
  // M method name
  // n line separator
  // p priority
  // r number of milliseconds since construction of layout
  // t thread name
  // x NDC (nested diagnostic context)
  // X MDC (mapped diagnostic context)
  // % percent

  public LogRecord(String category, int level, String message,
      Throwable throwable) {
    this.category = category;
    this.throwable = throwable;
    recordSequence = ++gloablRecordSequence;
    this.level = level;
    this.message = message;
    wrappedClientThrowable = WrappedClientThrowable.getInstanceOrNull(throwable);
  }

  // For GWT serialization
  @SuppressWarnings("unused")
  private LogRecord() {
  }

  public String getFormattedMessage() {
    return FORMATTER.format(getLevelText(), message);
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

  public Throwable getThrowable() {
    assert GWT.isClient() : "Method may only be invoked in GWT client code";
    return throwable;
  }

  public WrappedClientThrowable getWrappedClientThrowable() {
    return wrappedClientThrowable;
  }

  private String getLevelText() {
    return LogUtil.levelToString(level);
  }
}
