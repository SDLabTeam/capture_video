package com.uploader.pojo;

import java.nio.file.Path;

public class Config {
  private APIConfig apiConfig;
  private FTPConfig ftpConfig;
  private LoggingConfig loggingConfig;
  private Path toUploadDirectoryPath;
  private Path uploadedDirectoryPath;

  public Config(
      APIConfig apiConfig,
      FTPConfig ftpConfig,
      LoggingConfig loggingConfig,
      Path toUploadDirectoryPath,
      Path uploadedDirectoryPath) {
    this.apiConfig = apiConfig;
    this.ftpConfig = ftpConfig;
    this.loggingConfig = loggingConfig;
    this.toUploadDirectoryPath = toUploadDirectoryPath;
    this.uploadedDirectoryPath = uploadedDirectoryPath;
  }

  public APIConfig getApiConfig() {
    return apiConfig;
  }

  public void setApiConfig(APIConfig apiConfig) {
    this.apiConfig = apiConfig;
  }

  public FTPConfig getFtpConfig() {
    return ftpConfig;
  }

  public void setFtpConfig(FTPConfig ftpConfig) {
    this.ftpConfig = ftpConfig;
  }

  public LoggingConfig getLoggingConfig() {
    return loggingConfig;
  }

  public void setLoggingConfig(LoggingConfig loggingConfig) {
    this.loggingConfig = loggingConfig;
  }

  public Path getToUploadDirectoryPath() {
    return toUploadDirectoryPath;
  }

  public void setToUploadDirectoryPath(Path toUploadDirectoryPath) {
    this.toUploadDirectoryPath = toUploadDirectoryPath;
  }

  public Path getUploadedDirectoryPath() {
    return uploadedDirectoryPath;
  }

  public void setUploadedDirectoryPath(Path uploadedDirectoryPath) {
    this.uploadedDirectoryPath = uploadedDirectoryPath;
  }
}
