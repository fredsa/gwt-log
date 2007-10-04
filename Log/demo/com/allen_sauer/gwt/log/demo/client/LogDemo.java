package com.allen_sauer.gwt.log.demo.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.client.LogUncaughtExceptionHandler;

/**
 * Demonstrate Log capabilities.
 */
public class LogDemo implements EntryPoint {
  static class Broken {
    static final String broken = breakIt();

    private static native String breakIt()
    /*-{
     return a.b.c();
    }-*/;
  }

  public void onModuleLoad() {
    // set uncaught exception handler
    GWT.setUncaughtExceptionHandler(new LogUncaughtExceptionHandler());

    // use deferred command to catch initialization exceptions
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        onModuleLoad2();
      }
    });
  }

  public void onModuleLoad2() {
    Log.fatal("test fatal message", new Exception("dummy test exception"));
    Log.error("test error message", new Exception("dummy test exception"));
    Log.warn("test warning message");
    Log.info("test informational message");
    Log.debug("test debug <message>");
    jsni();
    new Broken();
  }

  private native void jsni()
  /*-{
    try {
      LogDemo_non_existant_variable.LogDemo_non_existant_method();
    } catch(e) {
      @com.allen_sauer.gwt.log.client.Log::error(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)("Caught JSNI Exception", e);
    }
  }-*/;
}
