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
package com.allen_sauer.gwt.log.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * Representation of a client-side thrown exception, which can be serialized via RPC.
 */
@SuppressWarnings("serial")
public class WrappedClientThrowable implements Serializable, IsSerializable {
  // CHECKSTYLE_JAVADOC_OFF

  public static WrappedClientThrowable getInstanceOrNull(Throwable ex) {
    return ex == null ? null : new WrappedClientThrowable(ex);
  }

  private WrappedClientThrowable cause;
  private StackTraceElement[] clientStackTrace;
  private String message;
  private String originalToString;

  /**
   * Private default constructor for RPC serialization.
   */
  private WrappedClientThrowable() {
  }

  private WrappedClientThrowable(Throwable ex) {
    if (ex != null) {
      originalToString = ex.toString();
      message = ex.getMessage();
      StackTraceElement[] stackTrace = ex.getStackTrace();
      clientStackTrace = new StackTraceElement[stackTrace.length];
      for (int i = 0; i < stackTrace.length; i++) {
        clientStackTrace[i] = new StackTraceElement(stackTrace[i].getClassName(),
            stackTrace[i].getMethodName(), stackTrace[i].getFileName(),
            stackTrace[i].getLineNumber());
      }
      setCause(getInstanceOrNull(ex.getCause()));
    }
  }

  public WrappedClientThrowable getCause() {
    return cause;
  }

  public StackTraceElement[] getClientStackTrace() {
    return clientStackTrace;
  }

  public String getMessage() {
    return message;
  }

  public String getOriginalToString() {
    return originalToString;
  }

  /**
   * Replace the current client stack trace with a new one. This is used in the deobfuscation
   * process.
   *
   * @param clientStackTrace the new client stack trace
   */
  public void setClientStackTrace(StackTraceElement[] clientStackTrace) {
    this.clientStackTrace = clientStackTrace;
  }

  private void setCause(WrappedClientThrowable cause) {
    this.cause = cause;
  }
}
