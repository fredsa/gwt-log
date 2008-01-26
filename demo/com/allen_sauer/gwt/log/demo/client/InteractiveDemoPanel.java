/*
 * Copyright 2008 Fred Sauer
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.allen_sauer.gwt.log.demo.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.client.DivLogger;

public class InteractiveDemoPanel extends AbsolutePanel {
  static class Broken {
    static final String broken = breakIt();

    private static native String breakIt()
    /*-{
     return i_do_not_exist();
    }-*/;
  }

  private static final String CSS_CURRENT = "current";
  private static final String CSS_NOOP = "noop";

  private static final int[] levels = {
      Log.LOG_LEVEL_DEBUG, Log.LOG_LEVEL_INFO, Log.LOG_LEVEL_WARN, Log.LOG_LEVEL_ERROR,
      Log.LOG_LEVEL_FATAL, Log.LOG_LEVEL_OFF,};

  private static final String[] levelTexts = {"DEBUG", "INFO", "WARN", "ERROR", "FATAL", "OFF",};

  private Button clinitButtonFatal;
  private Button npeButtonFatal;
  private HTML currentLogLevelLabel = new HTML();
  private Button jsniCatchButtonFatal;
  private Button jsniNoCatchButtonFatal;
  private Button levelButtons[] = new Button[levels.length];
  private HTML lowestLogLevelLabel = new HTML();
  private Button messageButtons[] = new Button[levels.length - 1];

  public InteractiveDemoPanel() {
    add(new HTML("Log a message:"));

    for (int i = 0; i < levels.length - 1; i++) {
      final int level = levels[i];
      final String levelString = levelTexts[i];
      messageButtons[i] = new Button("Log." + levelString.toLowerCase() + "(...)");
      add(messageButtons[i]);
      messageButtons[i].addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          String msg = "This is a '" + levelString + "' test message";
          switch (level) {
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
    jsniCatchButtonFatal.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        jsniCatch();
      }
    });

    jsniNoCatchButtonFatal = new Button("JSNI without try/catch");
    add(jsniNoCatchButtonFatal);
    jsniNoCatchButtonFatal.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        jsniNoCatch();
      }
    });

    clinitButtonFatal = new Button("static (class) initialization failure");
    add(clinitButtonFatal);
    clinitButtonFatal.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        new Broken();
      }
    });

    npeButtonFatal = new Button("NullPointerException");
    add(npeButtonFatal);
    npeButtonFatal.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        throw new NullPointerException();
      }
    });

    add(new HTML("<BR>"));
    add(new HTML("Clear log output (on supported destinations):"));

    Button clearButton = new Button("clear()");
    add(clearButton);
    clearButton.addClickListener(new ClickListener() {
      public void onClick(Widget sender) {
        Log.clear();
      }
    });

    add(new HTML("<BR>"));
    add(new HTML("Set runtime log level to:"));

    for (int i = 0; i < levels.length; i++) {
      final int level = levels[i];
      levelButtons[i] = new Button(levelTexts[i]);
      levelButtons[i].addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
          Log.setCurrentLogLevel(level);
          updateLogLevelLabels();
        }
      });
      add(levelButtons[i]);
    }

    add(new HTML("<BR>"));
    add(new HTML("Change the <code>log_level</code> URL parameter to:"));

    for (int i = 0; i < levels.length; i++) {
      final int level = levels[i];
      final String url = getPageURL() + "?log_level=" + levelTexts[i];
      Button button = new Button(levelTexts[i]);
      button.addClickListener(new ClickListener() {
        public void onClick(Widget sender) {
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

    add(currentLogLevelLabel);
    add(lowestLogLevelLabel);
    updateLogLevelLabels();

    if (Log.isLoggingEnabled()) {
      initDivLogger();
    }
  }

  private native String getPageURL()
  /*-{
    return $wnd.location.href.replace(/[\\?#].*$/, "");
  }-*/;

  private void initDivLogger() {
    final DivLogger divLogger = (DivLogger) Log.getLogger(DivLogger.class);
    divLogger.moveTo(10, 10);
    new Timer() {
      public void run() {
        if (!divLogger.isVisible()) {
          divLogger.info(
              "This is the draggable 'DivLogger' panel, just one of the available loggers.", null);
          divLogger.info("Click on the various buttons to send test messages or trap exceptions.",
              null);
        }
      }
    }.schedule(3000);
  }

  private native void jsniCatch()
  /*-{
    try {
      my_non_existant_variable.my_non_existant_method();
    } catch(e) {
      @com.allen_sauer.gwt.log.client.Log::fatal(Ljava/lang/String;Lcom/google/gwt/core/client/JavaScriptObject;)("Caught JSNI Exception", e);
    }
  }-*/;

  private native void jsniNoCatch()
  /*-{
    my_non_existant_variable.my_non_existant_method();
  }-*/;

  private native void setLocation(String url)
  /*-{
    $wnd.location = url;
  }-*/;

  private void styleLevelButton(Button button, int buttonLogLevel, String levelText) {
    if (buttonLogLevel < Log.getLowestLogLevel()) {
      button.addStyleDependentName(CSS_NOOP);
      button.setTitle("(Compile time log level prevents this runtime level)");
    } else {
      if (buttonLogLevel == Log.getCurrentLogLevel()) {
        button.addStyleDependentName(CSS_CURRENT);
        button.setTitle("(Already at this runtime log level)");
      } else {
        button.removeStyleDependentName(CSS_CURRENT);
        button.setTitle("Set runtime log level to '" + levelText + "'");
      }
    }
  }

  private void styleMessageButton(Button button, int buttonLogLevel, String levelText) {
    if (buttonLogLevel < Log.getCurrentLogLevel()) {
      button.addStyleDependentName(CSS_NOOP);
      button.setTitle("Currently has no effect");
    } else {
      button.removeStyleDependentName(CSS_NOOP);
      button.setTitle("Send a '" + levelText + "' message");
    }
  }

  private void updateLogLevelLabels() {
    currentLogLevelLabel.setHTML("Current (runtime) log level = <code>"
        + Log.getCurrentLogLevelString() + "</code>");
    lowestLogLevelLabel.setHTML("Current lowest possible (compile time) log level = <code>"
        + Log.getLowestLogLevelString() + "</code>");
    for (int i = 0; i < messageButtons.length; i++) {
      styleMessageButton(messageButtons[i], levels[i], levelTexts[i]);
    }
    for (int i = 0; i < levelButtons.length; i++) {
      styleLevelButton(levelButtons[i], levels[i], levelTexts[i]);
    }
    styleMessageButton(clinitButtonFatal, Log.LOG_LEVEL_FATAL, "FATAL");
    styleMessageButton(jsniNoCatchButtonFatal, Log.LOG_LEVEL_FATAL, "FATAL");
    styleMessageButton(jsniCatchButtonFatal, Log.LOG_LEVEL_FATAL, "FATAL");
  }

}
