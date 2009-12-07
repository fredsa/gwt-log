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
package com.allen_sauer.gwt.log.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import com.allen_sauer.gwt.log.client.Log;
import com.allen_sauer.gwt.log.client.LogRecord;
import com.allen_sauer.gwt.log.client.RemoteLoggerService;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

// CHECKSTYLE_JAVADOC_OFF

@SuppressWarnings("serial")
public class RemoteLoggerServiceImpl extends RemoteServiceServlet implements RemoteLoggerService {

  public final void log(ArrayList<LogRecord> logRecords) {
    for (Iterator<LogRecord> iterator = logRecords.iterator(); iterator.hasNext();) {
      LogRecord record = iterator.next();
      try {
        HttpServletRequest request = getThreadLocalRequest();
        record.set("remoteAddr", request.getRemoteAddr());
        Log.log(record);
      } catch (RuntimeException e) {
        System.err.println("Failed to log message due to " + e.toString());
        e.printStackTrace();
      }
    }
  }

}
