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
package com.allen_sauer.gwt.log.client;

import com.google.gwt.core.client.GWT;

import java.util.Date;

public abstract class AbstractLogger implements Logger {
  public void clear() {
    info("======================" + new Date() + "======================", null);
  }

  public void debug(String message, Throwable throwable) {
    log(Log.LOG_LEVEL_DEBUG, message, throwable);
  }

  public void error(String message, Throwable throwable) {
    log(Log.LOG_LEVEL_ERROR, message, throwable);
  }

  public void fatal(String message, Throwable throwable) {
    log(Log.LOG_LEVEL_FATAL, message, throwable);
  }

  public void info(String message, Throwable throwable) {
    log(Log.LOG_LEVEL_INFO, message, throwable);
  }

  public abstract void log(int logLevel, String message);

  public void log(int logLevel, String message, Throwable throwable) {
    String text = formatText(message);
    if (throwable != null) {
      text += formatText("\n");
      while (throwable != null) {
        text += formatThrowable(throwable);
        throwable = throwable.getCause();
        if (throwable != null) {
          text += formatText("Caused by: ");
        }
      }
    }
    logRaw(logLevel, text);
  }

  public final void warn(String message) {
    warn(message, null);
  }

  public void warn(String message, Throwable throwable) {
    log(Log.LOG_LEVEL_WARN, message, throwable);
  }

  String formatText(String text) {
    return text;
  }

  String formatThrowable(Throwable throwable) {
    String text = "";
    text += GWT.getTypeName(throwable) + ":\n" + throwable.getMessage() + "\n";
    StackTraceElement[] stackTraceElements = throwable.getStackTrace();
    for (int i = 0; i < stackTraceElements.length; i++) {
      text += "\tat " + stackTraceElements[i] + "\n";
    }
    return text;
  }

  void logRaw(int logLevel, String text) {
    log(logLevel, text);
  }
}
