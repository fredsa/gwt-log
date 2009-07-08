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
package com.allen_sauer.gwt.log.server;

import javax.servlet.http.HttpServletRequest;

import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.client.LogMessage;
import com.allen_sauer.gwt.log.client.RemoteLoggerService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

// CHECKSTYLE_JAVADOC_OFF

@SuppressWarnings("serial")
public class RemoteLoggerServiceImpl extends RemoteServiceServlet implements RemoteLoggerService {

  @SuppressWarnings("deprecation")
  private void diagnostic(Throwable throwable, String message) {
    Log.diagnostic(message, throwable);
  }

  public final void log(LogMessage[] logMessages) {
    for (int i = 0; i < logMessages.length; i++) {
      try {
        Throwable throwable = UnwrappedClientThrowable.getInstanceOrNull(logMessages[i].getWrappedClientThrowable());
        HttpServletRequest request = getThreadLocalRequest();
        String message = "[" + request.getRemoteAddr() + " " + logMessages[i].getMessageSequence()
            + "] " + logMessages[i].getMessage();
        switch (logMessages[i].level) {
          case Log.LOG_LEVEL_TRACE:
            Log.trace(message, throwable);
            break;
          case Log.LOG_LEVEL_DEBUG:
            Log.debug(message, throwable);
            break;
          case Log.LOG_LEVEL_INFO:
            Log.info(message, throwable);
            break;
          case Log.LOG_LEVEL_WARN:
            Log.warn(message, throwable);
            break;
          case Log.LOG_LEVEL_ERROR:
            Log.error(message, throwable);
            break;
          case Log.LOG_LEVEL_FATAL:
            Log.fatal(message, throwable);
            break;
          default:
            diagnostic(throwable, message);
        }
      } catch (RuntimeException e) {
        System.err.println("Failed to log message due to " + e.toString());
        e.printStackTrace();
      }
    }
  }

}
