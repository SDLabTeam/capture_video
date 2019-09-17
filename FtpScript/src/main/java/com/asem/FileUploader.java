package com.asem;

import com.asem.pojo.APIConfig;
import com.asem.pojo.Config;
import com.asem.pojo.FTPConfig;
import com.asem.service.FTPService;
import com.asem.service.LoggingService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUploader implements Runnable {
  private static final Logger LOGGER = Logger.getLogger(FileUploader.class);
  private FTPConfig ftpConfig;
  private APIConfig apiConfig;
  private Path fileToBeUploaded;
  private Path localUploadedDirectory;
  private FTPService ftpService;
  private LoggingService loggingService;

  public FileUploader(Config config, Path fileToBeUploaded) {
    this.ftpConfig = config.getFtpConfig();
    this.apiConfig = config.getApiConfig();
    this.fileToBeUploaded = fileToBeUploaded;
    this.localUploadedDirectory = config.getUploadedDirectoryPath();
    this.ftpService = new FTPService(ftpConfig);
    this.loggingService = new LoggingService(config.getLoggingConfig().getLogFilePath());
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
      LOGGER.error("Error: ", e);
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
