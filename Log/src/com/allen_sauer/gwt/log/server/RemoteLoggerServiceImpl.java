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
package com.allen_sauer.gwt.log.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.client.RemoteLoggerService;

// CHECKSTYLE_JAVADOC_OFF

public class RemoteLoggerServiceImpl extends RemoteServiceServlet implements RemoteLoggerService {
  public final void debug(String message, Throwable ex) {
    try {
      Log.debug(message, ex);
    } catch (RuntimeException e) {
      System.err.println("Failed to log message due to " + e.toString());
      e.printStackTrace();
    }
  }

  /**
   * @deprecated For internal gwt-log use only.
   */
  @Deprecated
  public final void diagnostic(String message, Throwable ex) {
    try {
      Log.diagnostic(message, ex);
    } catch (RuntimeException e) {
      System.err.println("Failed to log message due to " + e.toString());
      e.printStackTrace();
    }
  }

  public final void error(String message, Throwable ex) {
    try {
      Log.error(message, ex);
    } catch (RuntimeException e) {
      System.err.println("Failed to log message due to " + e.toString());
      e.printStackTrace();
    }
  }

  public final void fatal(String message, Throwable ex) {
    try {
      Log.fatal(message, ex);
    } catch (RuntimeException e) {
      System.err.println("Failed to log message due to " + e.toString());
      e.printStackTrace();
    }
  }

  public final void info(String message, Throwable ex) {
    try {
      Log.info(message, ex);
    } catch (RuntimeException e) {
      System.err.println("Failed to log message due to " + e.toString());
      e.printStackTrace();
    }
  }

  public final void warn(String message, Throwable ex) {
    try {
      Log.warn(message, ex);
    } catch (RuntimeException e) {
      System.err.println("Failed to log message due to " + e.toString());
      e.printStackTrace();
    }
  }
}
