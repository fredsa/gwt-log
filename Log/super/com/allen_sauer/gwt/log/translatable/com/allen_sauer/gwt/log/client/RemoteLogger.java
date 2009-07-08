/*
 * Copyright 2009 Fred Sauer
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

import java.util.ArrayList;

import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.client.LogMessage;
import com.allen_sauer.gwt.log.client.RemoteLoggerServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Logger which sends output via RPC to the server where it
 * can be logged and aggregated.
 * 
 * TODO optimize RPC round trips; at a minimum try to combine multiple messages at once
 */
public final class RemoteLogger extends AbstractLogger {
  // CHECKSTYLE_JAVADOC_OFF

  private static final int MESSAGE_QUEUEING_DELAY_MILLIS = 300;
  private final AsyncCallback<Void> callback;
  private boolean callInProgressOrScheduled = false;
  private RuntimeException failure;
  private ArrayList<LogMessage> logMessageList = new ArrayList<LogMessage>();
  private int messageSequence = 1;

  private final RemoteLoggerServiceAsync service;

  private Timer timer = new Timer() {
    @Override
    public void run() {
      LogMessage[] logMessages = logMessageList.toArray(new LogMessage[] {});
      logMessageList.clear();
      service.log(logMessages, callback);
    }
  };

  public RemoteLogger() {
    service = (RemoteLoggerServiceAsync) GWT.create(RemoteLoggerService.class);
    final ServiceDefTarget target = (ServiceDefTarget) service;
    target.setServiceEntryPoint(GWT.getModuleBaseURL() + "gwt-log");

    callback = new AsyncCallback<Void>() {
      public void onFailure(Throwable ex) {
        failure = new RuntimeException(
            "Remote logging will be suspended due to communication failure with "
                + service.getClass().getName() + " at " + target.getServiceEntryPoint(), ex);
        callInProgressOrScheduled = false;
      }

      public void onSuccess(Void result) {
        callInProgressOrScheduled = false;
        maybeTriggerRPC();
      }
    };
  }

  @Override
  public void clear() {
    // do nothing on the server
  }

  @Override
  public void debug(String message, Throwable throwable) {
    sendLogMessage(Log.LOG_LEVEL_DEBUG, message, throwable);
  }

  @Override
  public void diagnostic(String message, Throwable throwable) {
    sendLogMessage(Log.LOG_LEVEL_OFF, message, throwable);
  }

  @Override
  public void error(String message, Throwable throwable) {
    sendLogMessage(Log.LOG_LEVEL_ERROR, message, throwable);
  }

  @Override
  public void fatal(String message, Throwable throwable) {
    sendLogMessage(Log.LOG_LEVEL_FATAL, message, throwable);
  }

  @Override
  public void info(String message, Throwable throwable) {
    sendLogMessage(Log.LOG_LEVEL_INFO, message, throwable);
  }

  public boolean isSupported() {
    return true;
  }

  @Override
  public void trace(String message, Throwable throwable) {
    sendLogMessage(Log.LOG_LEVEL_TRACE, message, throwable);
  }

  @Override
  public void warn(String message, Throwable throwable) {
    sendLogMessage(Log.LOG_LEVEL_WARN, message, throwable);
  }

  @Override
  void log(int logLevel, String message) {
    assert false;
    // Method never called
  }

  private void maybeTriggerRPC() {
    if (failure == null && !callInProgressOrScheduled && !logMessageList.isEmpty()) {
      callInProgressOrScheduled = true;
      timer.schedule(MESSAGE_QUEUEING_DELAY_MILLIS);
    }
  }

  private String removeTrailingLineSeparator(String message) {
    return message.replaceAll("(\r\n|\r|\n)$", "");
  }

  private void sendLogMessage(int logLevel, String message, Throwable throwable) {
    if (failure != null) {
      throw failure;
    }
    logMessageList.add(new LogMessage(messageSequence++, logLevel, removeTrailingLineSeparator(message),
        WrappedClientThrowable.getInstanceOrNull(throwable)));
    maybeTriggerRPC();
  }
}
