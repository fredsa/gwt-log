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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

/**
 * Logger which sends output via RPC to the server where it
 * can be logged and aggregated.
 */
public final class RemoteLogger extends AbstractLogger {
  // CHECKSTYLE_JAVADOC_OFF

  private static final int MESSAGE_QUEUEING_DELAY_MILLIS_BASELINE = 300;
  private static final int MESSAGE_QUEUEING_DELAY_MILLIS_MAX_QUEUED = 5 * 60 * 1000; // 5 mins
  private static int messageQueueingDelayMillis = MESSAGE_QUEUEING_DELAY_MILLIS_BASELINE;
  private static final String REMOTE_LOGGER_NAME = "Remote Logger";
  private final AsyncCallback<Void> callback;
  private boolean callInProgressOrScheduled = false;
  private Throwable failure;
  private final ArrayList<LogMessage> logMessageList = new ArrayList<LogMessage>();
  private int messageSequence = 1;

  private final ArrayList<LogMessage> queuedMessageList = new ArrayList<LogMessage>();

  private final RemoteLoggerServiceAsync service;

  private final Timer timer = new Timer() {
    @Override
    public void run() {
      movePendingMessagesToQueue();
      service.log(queuedMessageList, callback);
    }

    // No need to synchronize on collection since JavaScript is single-threaded
    private void movePendingMessagesToQueue() {
      queuedMessageList.addAll(logMessageList);
      logMessageList.clear();
    }
  };

  public RemoteLogger() {
    if (!GWT.isClient()) {
      throw new UnsupportedOperationException();
    }
    service = (RemoteLoggerServiceAsync) GWT.create(RemoteLoggerService.class);
    callback = new AsyncCallback<Void>() {

      public void onFailure(Throwable ex) {
        String serviceEntryPoint = ((ServiceDefTarget) service).getServiceEntryPoint();
        if (messageQueueingDelayMillis > MESSAGE_QUEUEING_DELAY_MILLIS_MAX_QUEUED) {

          GWT.log(REMOTE_LOGGER_NAME
              + " has encountered too many failures while trying to contact servlet at "
              + serviceEntryPoint, ex);
          GWT.log(REMOTE_LOGGER_NAME + " has suspended with "
              + (logMessageList.size() + queuedMessageList.size())
              + " log message(s) not delivered"
              , null);
          failure = ex;
          logMessageList.clear();
          queuedMessageList.clear();
        } else {
          GWT.log(REMOTE_LOGGER_NAME
              + " encountered possibly transient communication failure with servlet at "
              + serviceEntryPoint, ex);
          GWT.log(REMOTE_LOGGER_NAME + " will attempt redelivery of "
              + queuedMessageList.size() + " log message(s) in "
              + messageQueueingDelayMillis
              + "ms",
              null);
        }
        callInProgressOrScheduled = false;
        maybeTriggerRPC();

        // exponential backoff
        messageQueueingDelayMillis += messageQueueingDelayMillis;
      }

      public void onSuccess(Void result) {
        messageQueueingDelayMillis = MESSAGE_QUEUEING_DELAY_MILLIS_BASELINE;
        queuedMessageList.clear();
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

  @Override
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
    if (failure == null && !callInProgressOrScheduled
        && (!logMessageList.isEmpty() || !queuedMessageList.isEmpty())) {
      callInProgressOrScheduled = true;
      timer.schedule(messageQueueingDelayMillis);
    }
  }

  private String removeTrailingLineSeparator(String message) {
    return message.replaceAll("(\r\n|\r|\n)$", "");
  }

  private void sendLogMessage(int logLevel, String message, Throwable throwable) {
    if (failure != null) {
      return;
    }
    logMessageList.add(new LogMessage(messageSequence++, logLevel,
        removeTrailingLineSeparator(message),
        WrappedClientThrowable.getInstanceOrNull(throwable)));
    maybeTriggerRPC();
  }
}
