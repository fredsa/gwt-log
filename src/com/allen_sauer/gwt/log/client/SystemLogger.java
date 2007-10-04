package com.allen_sauer.gwt.log.client;

import com.google.gwt.core.client.GWT;

public class SystemLogger extends AbstractLogger {
  public boolean isSupported() {
    return !GWT.isScript();
  }

  public void log(int logLevel, String message) {
    if (logLevel >= Log.LOG_LEVEL_ERROR) {
      System.err.println(message);
    } else {
      System.out.println(message);
    }
  }
}
