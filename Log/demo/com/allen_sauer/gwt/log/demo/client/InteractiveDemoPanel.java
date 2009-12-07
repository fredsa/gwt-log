/*
 * Copyright 2009 Fred Sauer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.allen_sauer.gwt.log.demo.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;

import com.allen_sauer.gwt.log.client.DivLogger;
import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.client.LogRecord;

/**
 * Interactive demo panel used by {@link LogDemo}.
 */
public class InteractiveDemoPanel extends AbsolutePanel {
  /**
   * Example of class which fails static initialization due to purposefully broken JSNI code.
   */
  static class Broken {
    static final String broken = breakIt();

    /**
     * Throw a JavaScriptException using purposefully broken JSNI code.
     * 
     * @return nothing since an exception is thrown
     */
    private static native String breakIt()
    /*-{
      return i_do_not_exist();
    }-*/;
  }

  private static final String CSS_CURRENT = "current";
  private static final String CSS_NOOP = "noop";

  private static final int[] levels = {
      Log.LOG_LEVEL_TRACE, Log.LOG_LEVEL_DEBUG, Log.LOG_LEVEL_INFO, Log.LOG_LEVEL_WARN,
      Log.LOG_LEVEL_ERROR, Log.LOG_LEVEL_FATAL, Log.LOG_LEVEL_OFF,};

  private static final String[] levelTexts = {
      "TRACE", "DEBUG", "INFO", "WARN", "ERROR", "FATAL", "OFF",};

  private final Button clinitButtonFatal;
  private final Button jsniCatchButtonFatal;
  private final Button jsniNoCatchButtonFatal;
  private final Button jsTimeoutExceptionButtonFatal;
  private final Button messageButtons[] = new Button[levels.length - 1];
  private final Button npeButtonFatal;
  private final Button nullButtonDebug;

  /**
   * Default constructor.
   */
  public InteractiveDemoPanel() {
    add(new HTML("Log a message:"));

    for (int i = 0; i < levels.length - 1; i++) {
      final int level = levels[i];
      final String levelString = levelTexts[i];
      messageButtons[i] = new Button("Log." + levelString.toLowerCase() + "(...)");
      add(messageButtons[i]);
      messageButtons[i].addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          String msg = "This is a '" + levelString + "' test message";
          switch (level) {
            case Log.LOG_LEVEL_TRACE:
                      Log.trace(msg);
              break;
            case Log.LOG_LEVEL_DEBUG:
                      Log.debug(msg);
              break;
            case Log.LOG_LEVEL_INFO:
                      Log.info(msg);
              break;
            case Log.LOG_LEVEL_WARN:
                      Log.warn(msg);
              break;
            case Log.LOG_LEVEL_ERROR:
                      Log.error(msg);
              break;
            case Log.LOG_LEVEL_FATAL:
                      Log.fatal(msg);
              break;
          }
        }
      });
    }

    add(new HTML("<BR>"));
    add(new HTML("Catch some exceptions:"));

    jsniCatchButtonFatal = new Button("JSNI with try/catch");
    add(jsniCatchButtonFatal);
    jsniCatchButtonFatal.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        jsniCatch();
      }
    });

    jsniNoCatchButtonFatal = new Button("JSNI without try/catch");
    add(jsniNoCatchButtonFatal);
    jsniNoCatchButtonFatal.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        jsniNoCatch();
      }
    });

    clinitButtonFatal = new Button("static (class) initialization failure");
    add(clinitButtonFatal);
    clinitButtonFatal.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        new Broken();
      }
    });

    add(new HTML("<BR>"));
    npeButtonFatal = new Button("NullPointerException");
    add(npeButtonFatal);
    npeButtonFatal.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        throw new NullPointerException();
      }
    });

    jsTimeoutExceptionButtonFatal = new Button("JavaScript setTimeout() exception [FF/IE only]");
    add(jsTimeoutExceptionButtonFatal);
    jsTimeoutExceptionButtonFatal.addClickHandler(new ClickHandler() {
      public native void onClick(ClickEvent event)
            /*-{
    setTimeout(function() {
    my_non_existant_variable.my_non_existant_method();
    }, 1);
  }-*/;
    });

    nullButtonDebug = new Button("Log.debug(null)");
    add(nullButtonDebug);
    nullButtonDebug.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        Log.debug(null);
      }
    });

    add(new HTML("<BR>"));
    add(new HTML("Clear log output (on supported destinations):"));

    Button clearButton = new Button("clear()");
    add(clearButton);
    clearButton.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        Log.clear();
      }
    });

    // add(new HTML("<BR>"));
    // add(new HTML("Set runtime log level to:"));
    //
    // for (int i = 0; i < levels.length; i++) {
    // final int level = levels[i];
    // levelButtons[i] = new Button(levelTexts[i]);
    // levelButtons[i].addClickHandler(new ClickHandler() {
    // public void onClick(ClickEvent event) {
    // Log.setCurrentLogLevel(level);
    // updateLogLevelLabels();
    // }
    // });
    // add(levelButtons[i]);
    // }

    add(new HTML("<BR>"));
    add(new HTML("Change the compile time <code>log_level</code> URL parameter to:"));

    for (int i = 0; i < levels.length; i++) {
      final int level = levels[i];
      final String url = makePageURL(levelTexts[i]);
      Button button = new Button(levelTexts[i]);
      button.addClickHandler(new ClickHandler() {
        public void onClick(ClickEvent event) {
          setLocation(url);
        }
      });
      button.setTitle("Switch to '" + levelTexts[i] + "' compile time log level");
      add(button);
      if (level == Log.getLowestLogLevel()) {
        button.addStyleDependentName(CSS_CURRENT);
      }
    }

    add(new HTML("<BR>"));

    updateLogLevelLabels();

    if (Log.isLoggingEnabled()) {
      initDivLogger();
    }
  }

  /**
   * Initialize the location and contents of the DivLogger after a short delay.
   */
  private void initDivLogger() {
    final DivLogger divLogger = Log.getLogger(DivLogger.class);
    divLogger.moveTo(10, 10);
    new Timer() {
      @Override
      public void run() {
        if (!divLogger.isVisible()) {
          LogRecord record = new LogRecord("gwt-log", Log.LOG_LEVEL_OFF,
              "This is the draggable 'DivLogger' panel, just one of the available loggers.\n"
              + "The above buttons control the current (runtime) logging level.\n"
              + "Use the other buttons on this page to send test messages or trap exceptions."
              , null);
          divLogger.log(record);
        }
      }
    }.schedule(3000);
  }

  /**
   * Try/catch a JavaScript exception/error and log it directly from JSNI.
   */
  private native void jsniCatch()
  /*-{
    try {
      my_non_existant_variable.my_non_existant_method();
    } catch(e) {
      @com.allen_sauer.gwt.log.client.Log::fatal(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)("Caught JSNI Exception", e);
    }
  }-*/;

  /**
   * Throw a JavaScriptException using purposefully broken JSNI code.
   */
  private native void jsniNoCatch()
  /*-{
    my_non_existant_variable.my_non_existant_method();
  }-*/;

  /**
   * @return the URL of the current page
   */
  private native String makePageURL(String logLevelText)
  /*-{
    var url = $wnd.location.href.replace(/([\\?&]log_level=[^&]*)/, "");
    return url + (url.indexOf('?') == -1 ? "?" : "&") + "log_level=" + logLevelText;
  }-*/;

  /**
   * Set the current window location.
   * 
   * @param url the new URL location
   */
  private native void setLocation(String url)
  /*-{
    $wnd.location = url;
  }-*/;

  // /**
  // * Update the style on a button specific to a certain log level.
  // *
  // * @param button the button to style
  // * @param buttonLogLevel the log level associated with this button's action
  // * @param levelText the text representation of the log level to use in the
  // button title
  // */
  // private void styleLevelButton(Button button, int buttonLogLevel, String
  // levelText) {
  // if (buttonLogLevel < Log.getLowestLogLevel()) {
  // button.addStyleDependentName(CSS_NOOP);
  // button.setTitle("(Compile time log level prevents this runtime level)");
  // } else {
  // if (buttonLogLevel == Log.getCurrentLogLevel()) {
  // button.addStyleDependentName(CSS_CURRENT);
  // button.setTitle("(Already at this runtime log level)");
  // } else {
  // button.removeStyleDependentName(CSS_CURRENT);
  // button.setTitle("Set runtime log level to '" + levelText + "'");
  // }
  // }
  // }

  /**
   * Update titles on message generating buttons.
   * 
   * @param button the button to style
   * @param buttonLogLevel the log level associated with the button's message
   * @param levelText the text representation of the log level to use in the button title
   */
  private void styleMessageButton(Button button, int buttonLogLevel, String levelText) {
    if (buttonLogLevel < Log.getCurrentLogLevel()) {
      button.addStyleDependentName(CSS_NOOP);
      button.setTitle("Currently has no effect");
    } else {
      button.removeStyleDependentName(CSS_NOOP);
      button.setTitle("Send a '" + levelText + "' message");
    }
  }

  /**
   * Update log level related dynamic text.
   */
  private void updateLogLevelLabels() {
    for (int i = 0; i < messageButtons.length; i++) {
      styleMessageButton(messageButtons[i], levels[i], levelTexts[i]);
    }
    // for (int i = 0; i < levelButtons.length; i++) {
    // styleLevelButton(levelButtons[i], levels[i], levelTexts[i]);
    // }
    styleMessageButton(clinitButtonFatal, Log.LOG_LEVEL_FATAL, "FATAL");
    styleMessageButton(jsniNoCatchButtonFatal, Log.LOG_LEVEL_FATAL, "FATAL");
    styleMessageButton(jsniCatchButtonFatal, Log.LOG_LEVEL_FATAL, "FATAL");
  }

}
