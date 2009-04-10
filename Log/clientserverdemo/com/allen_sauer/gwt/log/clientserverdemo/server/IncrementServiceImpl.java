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
package com.allen_sauer.gwt.log.clientserverdemo.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.clientserverdemo.client.Counter;
import com.allen_sauer.gwt.log.clientserverdemo.client.IncrementService;

/**
 * Implementation of increment service.
 */
@SuppressWarnings("serial")
public class IncrementServiceImpl extends RemoteServiceServlet implements IncrementService {
  /**
   * Increment the provided counter by one, and log progress along the way.
   *
   * @param counter the counter to increment
   * @return the updated counter
   */
  public Counter increment(Counter counter) {
    try {
      Log.debug("Counter has getCount() '" + counter.getCount() + "'");

      Log.debug("Calling counter.increment()...");
      counter.increment();
      Log.debug("...called counter.increment()");

      Log.debug("Counter has getCount() '" + counter.getCount() + "'");
      return counter;
    } catch (Throwable ex) {
      ex.printStackTrace();
      return null;
    }
  }
}
