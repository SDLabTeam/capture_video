package com.asem;

import com.asem.pojo.APIConfig;
import com.asem.pojo.FTPConfig;
import com.asem.service.FTPService;
import com.asem.service.LoggingService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUploader implements Runnable {
  private FTPConfig ftpConfig;
  private APIConfig apiConfig;
  private Path fileToBeUploaded;
  private Path localUploadedDirectory;
  private FTPService ftpService;
  private LoggingService loggingService;

  public FileUploader(
      FTPConfig ftpConfig,
      APIConfig apiConfig,
      Path fileToBeUploaded,
      Path localUploadedDirectory) {
    this.ftpConfig = ftpConfig;
    this.apiConfig = apiConfig;
    this.fileToBeUploaded = fileToBeUploaded;
    this.localUploadedDirectory = localUploadedDirectory;
    this.ftpService = new FTPService(ftpConfig);
    this.loggingService = LoggingService.getInstance();
  }

  @Override
  public void run() {
    if (ftpService.sendFile(fileToBeUploaded, ftpConfig.getRemoteDirectory())) {
      moveFile();
      callToAPI();
      logInfo();
    }
  }

  private void callToAPI() {
    // TODO
  }

  private void moveFile() {
    try {
      Files.move(fileToBeUploaded, localUploadedDirectory.resolve(fileToBeUploaded.getFileName()));
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private String buildFileUrl(String serverFilePath) {
    return new StringBuilder("ftp://")
        .append(ftpConfig.getFtpServerURL())
        .append(":")
        .append(ftpConfig.getFtpServerPort())
        .append(serverFilePath)
        .toString();
  }

  private void logInfo() {
    loggingService.addInfo(buildFileUrl(ftpService.getLastUploadedFilePath()));
  }
}
