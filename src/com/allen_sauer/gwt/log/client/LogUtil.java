package com.allen_sauer.gwt.log.client;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;

class LogUtil {
  static JavaScriptException convertJavaScriptObjectToException(
      JavaScriptObject e) {
     return new JavaScriptException(javaScriptExceptionName(e),
        javaScriptExceptionDescription(e));
  }

  private static native String javaScriptExceptionDescription(JavaScriptObject e)
  /*-{
    try {
     return e.message;
   } catch(ex) {
     return "[e has no message]";
   }
  }-*/;

  private static native String javaScriptExceptionName(JavaScriptObject e)
  /*-{
    try {
     return e.name;
   } catch(ex) {
     return "[e has no name]";
   }
  }-*/;
}
