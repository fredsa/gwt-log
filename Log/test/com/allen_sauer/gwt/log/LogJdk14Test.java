package com.allen_sauer.gwt.log;


public class LogJdk14Test extends LogTest {
  public static void main(String[] args) {
    System.setProperty("gwt-log.RemoteLogger", "JDK14");
    testApi();
  }
}
