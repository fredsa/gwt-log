/*
 * Copyright 2010 Fred Sauer
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

/**
 * Representation of a client-side thrown exception, which can be re-instantiated on the server.
 */
@SuppressWarnings("serial")
public class UnwrappedClientThrowable extends Throwable {
  // CHECKSTYLE_JAVADOC_OFF

  public static UnwrappedClientThrowable getInstanceOrNull(WrappedClientThrowable wrapped) {
    return wrapped == null ? null : new UnwrappedClientThrowable(wrapped);
  }

  private UnwrappedClientThrowable cause;

  private String message;

  private String originalToString;

  /**
   * Default constructor, required for RPC.
   */
  private UnwrappedClientThrowable() {
  }

  /**
   * Constructor used by {@link com.allen_sauer.gwt.log.client.RemoteLoggerImpl}.
   * 
   * @param wrapped the wrapped client-side exception
   */
  private UnwrappedClientThrowable(WrappedClientThrowable wrapped) {
    originalToString = wrapped.getOriginalToString();
    message = wrapped.getMessage();

    StackTraceElement[] clientStackTrace = wrapped.getClientStackTrace();
    if (clientStackTrace != null) {
      StackTraceElement[] stackTrace = new StackTraceElement[clientStackTrace.length];
      for (int i = 0; i < clientStackTrace.length; i++) {
        stackTrace[i] = new StackTraceElement(clientStackTrace[i].getClassName(),
            clientStackTrace[i].getMethodName(), clientStackTrace[i].getFileName(),
            clientStackTrace[i].getLineNumber());
      }
      setStackTrace(stackTrace);
    }
    WrappedClientThrowable wrappedcause = wrapped.getCause();
    if (wrappedcause != null) {
      this.cause = new UnwrappedClientThrowable(wrappedcause);
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
   * Returns an {@link UnwrappedClientThrowable}.
   * 
   * @return the {@link UnwrappedClientThrowable}
   */
  @Override
  public UnwrappedClientThrowable getCause() {
    return cause;
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
   * Returns the result of calling toString() on the original exception.
   * 
   * @return the result of calling toString() on the original exception
   */
  @Override
  public String toString() {
    return originalToString;
  }
}
