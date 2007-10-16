package com.allen_sauer.gwt.log.demo.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

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
     return i_do_not_exist();
    }-*/;
  }

  private static final String DEMO_MAIN_PANEL = "demo-main-panel";

  public void onModuleLoad() {
    // set uncaught exception handler
    GWT.setUncaughtExceptionHandler(new LogUncaughtExceptionHandler());

    // use a deferred command so that the handler catches onModuleLoad2()
    // exceptions
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        onModuleLoad2();
      }
    });
  }

  private native void jsniCatch()
  /*-{
    try {
      my_non_existant_variable.my_non_existant_method();
    } catch(e) {
      @com.allen_sauer.gwt.log.client.Log::error(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)("Caught JSNI Exception", e);
    }
  }-*/;

  private native void jsniNoCatch()
  /*-{
    my_non_existant_variable.my_non_existant_method();
  }-*/;

  private void onModuleLoad2() {
    RootPanel mainPanel = RootPanel.get(DEMO_MAIN_PANEL);
    DOM.setInnerHTML(mainPanel.getElement(), "");
    
    // Hosted mode 1st time: ExceptionInInitializerError
    // Hosted mode Nth time: NoClassDefFoundError
    // Web mode: JavaScriptException
    Button clinitButton = new Button("test static (class) initialization failure");
    mainPanel.add(clinitButton);
    clinitButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        new Broken();
      }
    });

    // JSNI tests

    mainPanel.add(new HTML("<BR>"));

    Button jsniCatchButton = new Button("JSNI with try/catch");
    mainPanel.add(jsniCatchButton);
    jsniCatchButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        jsniCatch();
      }
    });

    Button jsniNoCatchButton = new Button("JSNI without try/catch");
    mainPanel.add(jsniNoCatchButton);
    jsniNoCatchButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        jsniNoCatch();
      }
    });

    mainPanel.add(new HTML("<BR>"));

    Button debugButton = new Button("Log a 'DEBUG' message");
    mainPanel.add(debugButton);
    debugButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        Log.debug("This is a 'DEBUG' test message");
      }
    });

    Button infoButton = new Button("Log a 'INFO' message");
    mainPanel.add(infoButton);
    infoButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        Log.info("This is a 'INFO' test message");
      }
    });

    Button warnButton = new Button("Log a 'WARN' message");
    mainPanel.add(warnButton);
    warnButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        Log.warn("This is a 'WARN' test message");
      }
    });

    Button errorButton = new Button("Log a 'ERROR' message");
    mainPanel.add(errorButton);
    errorButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        Log.error("This is a 'ERROR' test message");
      }
    });

    Button fatalButton = new Button("Log a 'FATAL' message");
    mainPanel.add(fatalButton);
    fatalButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        Log.fatal("This is a 'FATAL' test message");
      }
    });

    mainPanel.add(new HTML("<BR>"));

    Button clearButton = new Button("clear()");
    mainPanel.add(clearButton);
    clearButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        Log.clear();
      }
    });
  }
}
