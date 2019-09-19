package com.uploader.pojo;

import java.util.Objects;

public class FTPConfig {
  private final String ftpUsername;
  private final String ftpPassword;
  private final String ftpServerURL;
  private final int ftpServerPort;
  private final String remoteDirectory;

  public FTPConfig(
      String ftpUsername,
      String ftpPassword,
      String ftpServerURL,
      int ftpServerPort,
      String remoteDirectory) {
    this.ftpUsername = ftpUsername;
    this.ftpPassword = ftpPassword;
    this.ftpServerURL = ftpServerURL;
    this.ftpServerPort = ftpServerPort;
    this.remoteDirectory = remoteDirectory;
  }

  public String getFtpUsername() {
    return ftpUsername;
  }

  public String getFtpPassword() {
    return ftpPassword;
  }

  public String getFtpServerURL() {
    return ftpServerURL;
  }

  public int getFtpServerPort() {
    return ftpServerPort;
  }

  public String getRemoteDirectory() {
    return remoteDirectory;
  }

  @Override
  public String toString() {
    return "FTPConfig{"
        + "ftpUsername='"
        + ftpUsername
        + '\''
        + ", ftpPassword='"
        + ftpPassword
        + '\''
        + ", ftpServerURL='"
        + ftpServerURL
        + '\''
        + ", ftpServerPort="
        + ftpServerPort
        + ", remoteDirectory='"
        + remoteDirectory
        + '\''
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    FTPConfig ftpConfig = (FTPConfig) o;
    return ftpServerPort == ftpConfig.ftpServerPort &&
            Objects.equals(ftpUsername, ftpConfig.ftpUsername) &&
            Objects.equals(ftpPassword, ftpConfig.ftpPassword) &&
            Objects.equals(ftpServerURL, ftpConfig.ftpServerURL) &&
            Objects.equals(remoteDirectory, ftpConfig.remoteDirectory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ftpUsername, ftpPassword, ftpServerURL, ftpServerPort, remoteDirectory);
  }
}
