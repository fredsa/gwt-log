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
package com.mycompany.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;

import com.allen_sauer.gwt.log.client.Log;

/**
 * Illustrative example.
 */
public class MyApplication implements EntryPoint {
  /**
   * This field gets compiled out when <code>log_level=OFF</code>, or any <code>log_level</code>
   * higher than <code>DEBUG</code>.
   */
  private long startTimeMillis;

  /**
   * Note, we defer all application initialization code to {@link #onModuleLoad2()} so that the
   * UncaughtExceptionHandler can catch any unexpected exceptions.
   */
  @Override
  public void onModuleLoad() {
    /*
     * Install an UncaughtExceptionHandler which will produce <code>FATAL</code> log messages
     */
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
   * Deferred initialization method, used by {@link #onModuleLoad()}.
   */
  private void onModuleLoad2() {
    /*
     * Use a <code>if (Log.isDebugEnabled()) {...}</code> guard to ensure that
     * <code>System.currentTimeMillis()</code> is compiled out when <code>log_level=OFF</code>, or
     * any <code>log_level</code> higher than <code>DEBUG</code>.
     */
    if (Log.isDebugEnabled()) {
      startTimeMillis = System.currentTimeMillis();
    }

    /*
     * No guards necessary. Code will be compiled out when <code>log_level=OFF</code>
     */
    Log.debug("This is a 'DEBUG' test message");
    Log.info("This is a 'INFO' test message");
    Log.warn("This is a 'WARN' test message");
    Log.error("This is a 'ERROR' test message");
    Log.fatal("This is a 'FATAL' test message");

    Log.fatal("This is what an exception might look like", new RuntimeException("2 + 2 = 5"));

    Log.debug("foo.bar.baz", "Using logging categories", (Exception) null);

    /*
     * Again, we need a guard here, otherwise <code>log_level=OFF</code> would still produce the
     * following useless JavaScript: <pre> var durationSeconds, endTimeMillis; endTimeMillis =
     * currentTimeMillis_0(); durationSeconds = (endTimeMillis - this$static.startTimeMillis) /
     * 1000.0; </pre>
     */
    if (Log.isDebugEnabled()) {
      long endTimeMillis = System.currentTimeMillis();
      float durationSeconds = (endTimeMillis - startTimeMillis) / 1000F;
      Log.debug("Duration: " + durationSeconds + " seconds");
    }
  }
}
