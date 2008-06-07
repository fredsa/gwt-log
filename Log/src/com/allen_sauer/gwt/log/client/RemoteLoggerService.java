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
package com.allen_sauer.gwt.log.client;

import com.google.gwt.user.client.rpc.RemoteService;

// CHECKSTYLE_JAVADOC_OFF
public interface RemoteLoggerService extends RemoteService {
  void debug(String message, WrappedClientThrowable ex);

  /**
   * @deprecated For internal gwt-log use only.
   */
  @Deprecated
  void diagnostic(String message, WrappedClientThrowable ex);

  void error(String message, WrappedClientThrowable ex);

  void fatal(String message, WrappedClientThrowable ex);

  void info(String message, WrappedClientThrowable ex);

  void trace(String message, WrappedClientThrowable ex);

  void warn(String message, WrappedClientThrowable ex);
}
