package com.allen_sauer.gwt.log;

public class LogLog4jTest extends LogTest {
  public static void main(String[] args) {
    System.setProperty("gwt-log.RemoteLogger", "LOG4J");
    testApi();
  }
}
