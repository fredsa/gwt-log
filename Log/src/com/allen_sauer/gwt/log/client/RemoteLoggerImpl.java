/*
 * Copyright 2010 Fred Sauer Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in
 * writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.allen_sauer.gwt.log.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import com.allen_sauer.gwt.log.shared.LogRecord;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Logger which sends log records via GWT RPC to the server where it can be deobfuscated and logged.
 */
public final class RemoteLoggerImpl extends RemoteLogger {
  // CHECKSTYLE_JAVADOC_OFF

  private static final RemoteLoggerConfig config = GWT.create(RemoteLoggerConfig.class);
  private static final int MESSAGE_QUEUEING_DELAY_MILLIS_BASELINE = 250;

  /**
   * When RemoteLogger is enabled, output to other loggers may be delayed for up to {@value} ms.
   * This time delay must be kept fairly low, otherwise logging will appear to be entirely broken!
   */
  private static final int MESSAGE_QUEUEING_DELAY_MILLIS_MAX_QUEUED = 1000;
  private static int messageQueueingDelayMillis = MESSAGE_QUEUEING_DELAY_MILLIS_BASELINE;
  private static final String REMOTE_LOGGER_NAME = "Remote Logger";
  private final AsyncCallback<ArrayList<LogRecord>> callback;
  private boolean callInProgressOrScheduled = false;
  private Throwable failure;
  private final ArrayList<LogRecord> logRecordList = new ArrayList<LogRecord>();
  private final ArrayList<LogRecord> queuedLogRecordList = new ArrayList<LogRecord>();
  private final RemoteLoggerServiceAsync service;

  private final Timer timer = new Timer() {
    @Override
    public void run() {
      movePendingMessagesToQueue();
      service.log(queuedLogRecordList, callback);
    }

    // No need to synchronize on collection since JavaScript is single-threaded
    private void movePendingMessagesToQueue() {
      queuedLogRecordList.addAll(logRecordList);
      logRecordList.clear();
    }
  };

  public RemoteLoggerImpl() {
    if (!GWT.isClient()) {
      throw new UnsupportedOperationException();
    }
    service = (RemoteLoggerServiceAsync) GWT.create(RemoteLoggerService.class);
    final ServiceDefTarget target = (ServiceDefTarget) service;
    String serviceEntryPointUrl = config.serviceEntryPointUrl();
    if (serviceEntryPointUrl != null) {
      target.setServiceEntryPoint(serviceEntryPointUrl);
    }

    callback = new AsyncCallback<ArrayList<LogRecord>>() {

      @Override
      public void onFailure(Throwable ex) {
        String serviceEntryPoint = ((ServiceDefTarget) service).getServiceEntryPoint();
        if (messageQueueingDelayMillis > MESSAGE_QUEUEING_DELAY_MILLIS_MAX_QUEUED) {

          GWT.log(REMOTE_LOGGER_NAME
              + " has encountered too many failures while trying to contact servlet at "
              + serviceEntryPoint, ex);
          GWT.log(REMOTE_LOGGER_NAME + " has suspended with "
              + (logRecordList.size() + queuedLogRecordList.size())
              + " log message(s) not delivered", null);
          failure = ex;

          // log queued messages to other loggers before purging
          loggersLogToOthers(queuedLogRecordList);
          loggersLogToOthers(logRecordList);

          //purge
          logRecordList.clear();
          queuedLogRecordList.clear();
        } else {
          GWT.log(REMOTE_LOGGER_NAME
              + " encountered possibly transient communication failure with servlet at "
              + serviceEntryPoint, ex);
          GWT.log(REMOTE_LOGGER_NAME + " will attempt redelivery of " + queuedLogRecordList.size()
              + " log message(s) in " + messageQueueingDelayMillis + "ms", null);
        }
        callInProgressOrScheduled = false;
        maybeTriggerRPC();

        // exponential back-off
        messageQueueingDelayMillis += messageQueueingDelayMillis;
      }

      @Override
      public void onSuccess(ArrayList<LogRecord> deobfuscatedLogRecords) {
        if (deobfuscatedLogRecords != null) {
          loggersLogToOthers(deobfuscatedLogRecords);
        } else {
          // Server did not provide deobfuscated log records
          loggersLogToOthers(queuedLogRecordList);
        }
        queuedLogRecordList.clear();
        messageQueueingDelayMillis = MESSAGE_QUEUEING_DELAY_MILLIS_BASELINE;
        callInProgressOrScheduled = false;
        maybeTriggerRPC();
      }

      private void loggersLogToOthers(ArrayList<LogRecord> logRecords) {
        for (Iterator<LogRecord> iterator = logRecords.iterator(); iterator.hasNext();) {
          LogRecord record = iterator.next();
          RemoteLoggerImpl.super.loggersLog(record);
        }
      }
    };
  }

  @Override
  public void clear() {
  }

  @Override
  public boolean isSupported() {
    return true;
  }

  @Override
  public void log(LogRecord record) {
    if (failure != null) {
      // remote logger has been disabled; just pass on log messages to other loggers
      super.loggersLog(record);
      return;
    }
    if (record.getLevel() == Log.LOG_LEVEL_OFF) {
      // don't forward gwt-log diagnostic messages to the server; just pass on log messages to others
      super.loggersLog(record);
      return;
    }
    logRecordList.add(record);
    maybeTriggerRPC();
  }

  @Override
  public void loggersLog(LogRecord record) {
    log(record);
  }

  @Override
  public void setCurrentLogLevel(int level) {
  }

  private void maybeTriggerRPC() {
    if (failure == null && !callInProgressOrScheduled
        && (!logRecordList.isEmpty() || !queuedLogRecordList.isEmpty())) {
      callInProgressOrScheduled = true;
      timer.schedule(messageQueueingDelayMillis);
    }
  }
}
