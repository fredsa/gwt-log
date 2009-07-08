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
package com.allen_sauer.gwt.log.clientserverdemo.client;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Client server logging demo.
 */
public class LogClientServerDemo implements EntryPoint {
  /**
   * Main entry point method.
   */
  public void onModuleLoad() {
    // set uncaught exception handler
    Log.setUncaughtExceptionHandler();

    // use deferred command to catch initialization exceptions
    DeferredCommand.addCommand(new Command() {
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
    ServiceDefTarget endpoint = (ServiceDefTarget) service;
    endpoint.setServiceEntryPoint(GWT.getModuleBaseURL() + "increment");

    AsyncCallback<Counter> callback = new AsyncCallback<Counter>() {
      public void onFailure(Throwable ex) {
        Log.fatal("onFailure", ex);
      }

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
  }
}
