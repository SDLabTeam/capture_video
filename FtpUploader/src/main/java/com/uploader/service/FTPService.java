package com.uploader.service;

import com.uploader.pojo.FTPConfig;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class FTPService {
  private static final Logger LOGGER = Logger.getLogger(FTPService.class);
  private FTPConfig ftpConfig;
  private String lastUploadedFilePath;

  public FTPService(FTPConfig ftpConfig) {
    this.ftpConfig = ftpConfig;
  }

  public boolean sendFile(Path localFilePath, String remoteDirectoryPath) {
    String remotePath = getRemotePath(remoteDirectoryPath, localFilePath);
    LOGGER.info("Start uploading file to ftp path " + remotePath);
    FTPClient ftpClient = null;
    try (InputStream inputStream =
        new BufferedInputStream(new FileInputStream(localFilePath.toFile()))) {
      ftpClient = prepareFtpClient();
      boolean resultFlag = ftpClient.storeFile(remotePath, inputStream);
      if (resultFlag) {
        lastUploadedFilePath = remotePath;
        LOGGER.info("Uploading file " + remotePath + " finished successfully");
      } else {
        LOGGER.info("Uploading file " + remotePath + " finished UNsuccessfully");
      }
      return resultFlag;
    } catch (IOException e) {
      LOGGER.error("Error has happened: ", e);
      return false;
    } finally {
      try {
        if (ftpClient != null && ftpClient.isConnected()) {
          ftpClient.logout();
          ftpClient.disconnect();
        }
      } catch (IOException e) {
        LOGGER.error("Error: ", e);
      }
    }
  }

  private FTPClient prepareFtpClient() throws IOException {
    FTPClient ftpClient = new FTPClient();
    ftpClient.connect(ftpConfig.getFtpServerURL(), ftpConfig.getFtpServerPort());
    ftpClient.login(ftpConfig.getFtpUsername(), ftpConfig.getFtpPassword());
    ftpClient.enterLocalPassiveMode();
    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
    return ftpClient;
  }

  private String getRemotePath(String remoteDirectoryPath, Path localFilePath) {
    String result;
    if (remoteDirectoryPath.endsWith("/")) {
      result = remoteDirectoryPath + localFilePath.getFileName().toString();
    } else {
      result = remoteDirectoryPath + "/" + localFilePath.getFileName().toString();
    }
    return result.startsWith("/") ? result : "/" + result;
  }

  public String getLastUploadedFilePath() {
    return lastUploadedFilePath;
  }
}
