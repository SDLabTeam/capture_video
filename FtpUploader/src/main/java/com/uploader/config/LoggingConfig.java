package com.uploader.config;

public class LoggingConfig {
  private String logFilePath;

  public LoggingConfig(String logFilePath) {
    this.logFilePath = logFilePath;
  }

  public String getLogFilePath() {
    return logFilePath;
  }

  public void setLogFilePath(String logFilePath) {
    this.logFilePath = logFilePath;
  }
}
