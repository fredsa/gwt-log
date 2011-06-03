/*
 * Copyright 2008 Fred Sauer
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
package com.allen_sauer.gwt.log.clientserverdemo.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

import com.allen_sauer.gwt.log.client.Log;

/**
 * Client server logging demo.
 */
public class LogClientServerDemo implements EntryPoint {
  /**
   * Main entry point method.
   */
  @Override
  public void onModuleLoad() {
    // set uncaught exception handler
    Log.setUncaughtExceptionHandler();

    // use deferred command to catch initialization exceptions in onModuleLoad2
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        onModuleLoad2();
      }
    });
  }

  /**
   * Deferred initialization.
   */
  public void onModuleLoad2() {
    IncrementServiceAsync service = (IncrementServiceAsync) GWT.create(IncrementService.class);

    AsyncCallback<Counter> callback = new AsyncCallback<Counter>() {
      @Override
      public void onFailure(Throwable ex) {
        Log.fatal("onFailure", ex);
      }

      @Override
      public void onSuccess(Counter counter) {
        Log.debug("onSuccess result counter is at " + counter.getCount());
      }
    };

    // execute the service
    Counter counter = new Counter();
    counter.increment();
    service.increment(counter, callback);

    new Timer() {
      @Override
      public void run() {
        Log.debug("*********************************************");
        Log.fatal("fatal - should be visible");
        Log.setCurrentLogLevel(Log.LOG_LEVEL_OFF);
        Log.fatal("fatal - should be filtered out");
        Log.setCurrentLogLevel(Log.LOG_LEVEL_OFF); // should still output client side
      }
    }.schedule(2000);

    new Timer() {
      @Override
      public void run() {
        Log.setCurrentLogLevel(Log.LOG_LEVEL_FATAL);
        Log.fatal("Here it comes...");
        try {
          throw new NullPointerException("catch me if you can");
        } catch (Exception e) {
          throw new RuntimeException("log me on the server if you will", e);
        }
      }
    }.schedule(4000);
  }
}
