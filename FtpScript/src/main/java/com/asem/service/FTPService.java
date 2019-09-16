package com.asem.service;

import com.asem.pojo.FTPConfig;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class FTPService {
  private FTPConfig ftpConfig;
  private String lastUploadedFilePath;

  public FTPService(FTPConfig ftpConfig) {
    this.ftpConfig = ftpConfig;
  }

  public boolean sendFile(Path localFilePath, String remoteDirectoryPath) {
    String remotePath = getRemotePath(remoteDirectoryPath, localFilePath);
    FTPClient ftpClient = null;
    try (InputStream inputStream =
        new BufferedInputStream(new FileInputStream(localFilePath.toFile()))) {
      ftpClient = prepareFtpClient();
      boolean resultFlag = ftpClient.storeFile(remotePath, inputStream);
      if (resultFlag) {
        lastUploadedFilePath = remotePath;
      }
      return resultFlag;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    } finally {
      try {
        if (ftpClient != null && ftpClient.isConnected()) {
          ftpClient.logout();
          ftpClient.disconnect();
        }
      } catch (IOException e) {
        e.printStackTrace();
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
