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
package com.allen_sauer.gwt.log.demo.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.RootPanel;

import com.allen_sauer.gwt.log.client.Log;

/**
 * Demonstrate Log capabilities.
 */
public class LogDemo implements EntryPoint {
  private static final String DEMO_MAIN_PANEL = "demo-main-panel";

  /**
   * Main entry point method.
   */
  public void onModuleLoad() {
    // set uncaught exception handler
    Log.setUncaughtExceptionHandler();

    // use deferred command to catch initialization exceptions in onModuleLoad2
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      public void execute() {
        onModuleLoad2();
      }
    });
  }

  /**
   * Deferred initialization method, used by {@link #onModuleLoad()}.
   */
  private void onModuleLoad2() {
    RootPanel mainPanel = RootPanel.get(DEMO_MAIN_PANEL);
    DOM.setInnerHTML(mainPanel.getElement(), "");
    mainPanel.add(new InteractiveDemoPanel());
  }
}
