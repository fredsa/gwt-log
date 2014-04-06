package com.allen_sauer.gwt.log;

public class LogSLF4JTest extends LogTest {
  public static void main(String[] args) {
    System.setProperty("gwt-log.RemoteLogger", "SLF4J");
    testApi();
  }
}
