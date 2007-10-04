package com.allen_sauer.gwt.log.client;

import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;

public class LogUncaughtExceptionHandler implements UncaughtExceptionHandler {

  public void onUncaughtException(Throwable e) {
    Log.fatal("Uncaught Exception:", e);
  }
}
