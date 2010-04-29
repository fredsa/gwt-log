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
 * Logger which sends output via RPC to the server where it can be logged and aggregated.
 */
public final class RemoteLogger implements Logger {
  // CHECKSTYLE_JAVADOC_OFF

  private static final RemoteLoggerConfig config = GWT.create(RemoteLoggerConfig.class);
  private static final int MESSAGE_QUEUEING_DELAY_MILLIS_BASELINE = 300;
  private static final int MESSAGE_QUEUEING_DELAY_MILLIS_MAX_QUEUED = 5 * 60 * 1000; // 5 mins
  private static int messageQueueingDelayMillis = MESSAGE_QUEUEING_DELAY_MILLIS_BASELINE;
  private static final String REMOTE_LOGGER_NAME = "Remote Logger";
  private final AsyncCallback<Void> callback;
  private boolean callInProgressOrScheduled = false;
  private Throwable failure;
  private final ArrayList<LogRecord> logMessageList = new ArrayList<LogRecord>();
  private final ArrayList<LogRecord> queuedMessageList = new ArrayList<LogRecord>();
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
    final ServiceDefTarget target = (ServiceDefTarget) service;
    String serviceEntryPointUrl = config.serviceEntryPointUrl();
    if (serviceEntryPointUrl != null) {
      target.setServiceEntryPoint(serviceEntryPointUrl);
    }

    callback = new AsyncCallback<Void>() {

      public void onFailure(Throwable ex) {
        String serviceEntryPoint = ((ServiceDefTarget) service).getServiceEntryPoint();
        if (messageQueueingDelayMillis > MESSAGE_QUEUEING_DELAY_MILLIS_MAX_QUEUED) {

          GWT.log(REMOTE_LOGGER_NAME
              + " has encountered too many failures while trying to contact servlet at "
              + serviceEntryPoint, ex);
          GWT.log(REMOTE_LOGGER_NAME + " has suspended with "
              + (logMessageList.size() + queuedMessageList.size())
              + " log message(s) not delivered", null);
          failure = ex;
          logMessageList.clear();
          queuedMessageList.clear();
        } else {
          GWT.log(REMOTE_LOGGER_NAME
              + " encountered possibly transient communication failure with servlet at "
              + serviceEntryPoint, ex);
          GWT.log(REMOTE_LOGGER_NAME + " will attempt redelivery of " + queuedMessageList.size()
              + " log message(s) in " + messageQueueingDelayMillis + "ms", null);
        }
        callInProgressOrScheduled = false;
        maybeTriggerRPC();

        // exponential back-off
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

  public void clear() {
  }

  public boolean isSupported() {
    return true;
  }

  public void log(LogRecord record) {
    if (failure != null) {
      return;
    }
    if (record.getLevel() == Log.LOG_LEVEL_OFF) {
      // don't forward gwt-log diagnostic messages
      return;
    }
    logMessageList.add(record);
    maybeTriggerRPC();
  }

  public void setCurrentLogLevel(int level) {
  }

  private void maybeTriggerRPC() {
    if (failure == null && !callInProgressOrScheduled
        && (!logMessageList.isEmpty() || !queuedMessageList.isEmpty())) {
      callInProgressOrScheduled = true;
      timer.schedule(messageQueueingDelayMillis);
    }
  }
}
