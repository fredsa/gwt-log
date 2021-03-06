/*
 * Copyright 2010 Fred Sauer
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

import com.allen_sauer.gwt.log.shared.LogRecord;

import java.util.ArrayList;

/**
 * Logger which sends log records via GWT RPC to the server where it can be deobfuscated and logged.
 */
public final class RemoteLoggerImpl extends RemoteLogger {
  // CHECKSTYLE_JAVADOC_OFF

  private static final RemoteLoggerConfig config = GWT.create(RemoteLoggerConfig.class);

  /**
   * Delay after first log message is received before firing RPC.
   */
  private static final int MESSAGE_QUEUEING_DELAY_MILLIS = 100;

  private static final String REMOTE_LOGGER_NAME = "Remote Logger";

  private final Timer batchDeliveryTimer = new Timer() {
    @Override
    public void run() {
      service.log(logRecordList, callback);
      logRecordList.clear();
    }
  };

  private final AsyncCallback<ArrayList<LogRecord>> callback;

  private boolean callInProgressOrScheduled = false;

  private Throwable failure;

  private final ArrayList<LogRecord> logRecordList = new ArrayList<LogRecord>();

  private final RemoteLoggerServiceAsync service;

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
        GWT.log(REMOTE_LOGGER_NAME + " has failed to contact servlet at " + serviceEntryPoint, ex);
        GWT.log(REMOTE_LOGGER_NAME + " has suspended with " + logRecordList.size()
            + " log message(s) not delivered", null);
        failure = ex;
        callInProgressOrScheduled = false;
      }

      @Override
      public void onSuccess(ArrayList<LogRecord> deobfuscatedLogRecords) {
        if (GWT.isProdMode() && deobfuscatedLogRecords != null) {
          for (LogRecord record : deobfuscatedLogRecords) {
            if (record.getThrowable() != null) {
              RemoteLoggerImpl.super.loggersLog(record);
            }
          }
        }
        callInProgressOrScheduled = false;
        maybeTriggerRPC();
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
    super.loggersLog(record);
    if (failure != null) {
      // remote logger has been disabled
      return;
    }
    if (record.getLevel() == Log.LOG_LEVEL_OFF) {
      // don't forward gwt-log diagnostic messages to the server
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
    if (failure == null && !callInProgressOrScheduled && !logRecordList.isEmpty()) {
      // allow a few log messages to accumulate before firing RPC
      batchDeliveryTimer.schedule(MESSAGE_QUEUEING_DELAY_MILLIS);
      callInProgressOrScheduled = true;
    }
  }
}
