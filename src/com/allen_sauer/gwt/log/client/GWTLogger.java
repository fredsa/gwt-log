package com.allen_sauer.gwt.log.client;

import com.google.gwt.core.client.GWT;

public class GWTLogger extends AbstractLogger {
  public boolean isSupported() {
    return !GWT.isScript();
  }

  public void log(int logLevel, String message) {
    GWT.log(message, null);
  }

  public void log(int logLevel, String message, Throwable throwable) {
    GWT.log(message, throwable);
  }
}
