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

import com.allen_sauer.gwt.log.client.ClientStackTraceElement;
import com.allen_sauer.gwt.log.client.WrappedClientThrowable;

/**
 * Representation of a client-side thrown exception, which can be re-instantiated on the server.
 */
@SuppressWarnings("serial")
public class UnwrappedClientThrowable extends Throwable {
  // CHECKSTYLE_JAVADOC_OFF

  public static UnwrappedClientThrowable getInstanceOrNull(WrappedClientThrowable wrapped) {
    return wrapped == null ? null : new UnwrappedClientThrowable(wrapped);
  }

  private String message;

  private String originalClassName;

  /**
   * Default constructor, required for RPC.
   */
  private UnwrappedClientThrowable() {
  }

  /**
   * Constructor used by {@link com.allen_sauer.gwt.log.client.RemoteLogger}.
   * 
   * @param wrapped the wrapped client-side exception
   */
  private UnwrappedClientThrowable(WrappedClientThrowable wrapped) {
    originalClassName = wrapped.getOriginalClassName();
    message = wrapped.getMessage();

    ClientStackTraceElement[] clientStackTrace = wrapped.getClientStackTrace();
    if (clientStackTrace != null) {
      StackTraceElement[] stackTrace = new StackTraceElement[clientStackTrace.length];
      for (int i = 0; i < clientStackTrace.length; i++) {
        stackTrace[i] = new StackTraceElement(clientStackTrace[i].getClassName(),
            clientStackTrace[i].getMethodName(), clientStackTrace[i].getFileName(),
            clientStackTrace[i].getLineNumber());
      }
      setStackTrace(stackTrace);
    }
  }

  /**
   * Method does nothing.
   * 
   * @return <code>this</code>
   */
  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }

  /**
   * Returns null.
   * 
   * @return null
   */
  @Override
  public Throwable getCause() {
    return null;
  }

  /**
   * Returns the same value as {@link #getMessage()}.
   * 
   * @return {@link Throwable#getMessage()} from the original exception
   */
  @Override
  public String getLocalizedMessage() {
    return getMessage();
  }

  /**
   * Returns the same value as {@link #getMessage()}.
   * 
   * @return {@link Throwable#getMessage()} from the original exception
   */
  @Override
  public String getMessage() {
    return message;
  }

  /**
   * Unimplemented.
   * 
   * @param cause unused parameter; an exception is thrown instead
   * @return nothing; an exception is thrown instead
   * 
   * @throws UnsupportedOperationException
   */
  @Override
  public synchronized Throwable initCause(Throwable cause) {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns a string representation of the original exception.
   * 
   * The result is a concatenation of:
   * <ul>
   * <li>the {@link Class#getName() class name} of the original exception</li>
   * <li> <code>": "</code> (a colon and a space)</li>
   * <li>the result of {@link #getMessage()}</li>
   * </ul>
   * 
   * If {@link #getMessage()} returns <code>null</code>, then just the {@link Class#getName() class
   * name} of the original exception is returned
   * 
   * @return a string representation of the original exception
   */
  @Override
  public String toString() {
    return originalClassName + (message != null ? ": " + message : "");
  }
}
