package com.asem;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

public class FTPClientTest {

  private FTPClient ftpClient = new FTPClient();

  @Before
  public void prepareFtpClient() throws IOException {
    // preparing ftp client
    ftpClient.connect("localhost", 21);
    ftpClient.login("asem", "123456789");
    ftpClient.enterLocalPassiveMode();
    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
  }

  @Test
  public void testConcurrentUploading() throws IOException {
    long before = System.currentTimeMillis();
    uploadFilesConcurrently(getTestFiles());
    long after = System.currentTimeMillis();
    System.out.print("Total time: " + (after - before));
  }

  private List<Path> getTestFiles() throws IOException {
    Path testFilesToUploadDir = Paths.get("src/test/resources/testFilesToUpload");
    return Files.list(testFilesToUploadDir).collect(Collectors.toList());
  }

  private void uploadFilesConcurrently(List<Path> files) {
    ArrayList<Thread> threads = new ArrayList<>(files.size());
    for (Path f : files) {
      Thread t = new Thread(() -> uploadFile(f));
      t.start();
      threads.add(t);
    }
    for (Thread t : threads) {
      try {
        t.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  private void uploadFile(Path path) {
    try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(path.toFile()))) {
      synchronized (this) {
        ftpClient.storeFile(path.getFileName().toString(), in);
      }
    } catch (IOException e) {
      Throwable cause = e;
      while (e.getCause() != null) {
        cause = e.getCause();
      }
      cause.printStackTrace();
    }
  }
}
