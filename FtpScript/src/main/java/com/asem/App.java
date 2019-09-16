package com.asem;

import com.asem.pojo.APIConfig;
import com.asem.pojo.FTPConfig;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/** Hello world! */
public class App {
  private static final Logger LOGGER = Logger.getLogger(App.class.getName());
  // Paths config data
  private static Path toUploadDirectory;
  private static Path uploadedDirectory;
  private static FTPConfig ftpConfig;
  private static APIConfig apiConfig;

  public static void main(String[] args) throws IOException, InterruptedException {
    readConfig(args[0]);
    List<Path> filesToUpload = getFilesToUpload();
    if (!filesToUpload.isEmpty()) {
      ExecutorService executorService = Executors.newFixedThreadPool(filesToUpload.size());
      for (Path f : filesToUpload) {
        FileUploader uploader = new FileUploader(ftpConfig, apiConfig, f, uploadedDirectory);
        executorService.submit(uploader);
      }
      executorService.shutdown();
      executorService.awaitTermination(2, TimeUnit.MINUTES);
    }
    System.out.println("Finished successfully");
  }

  private static List<Path> getFilesToUpload() throws IOException {
    return Files.list(toUploadDirectory).filter(f -> Files.isRegularFile(f)).collect(Collectors.toList());
  }

  private static void readConfig(String pathToConfig) {
    Properties config = new Properties();
    try (BufferedReader reader =
                 new BufferedReader(
                         new FileReader(
                                 Paths.get(pathToConfig)
                                         .toFile()))) {
      config.load(reader);
      // load paths config
      toUploadDirectory = Paths.get(config.getProperty("local.ToUploadDirectory"));
      uploadedDirectory = Paths.get(config.getProperty("local.UploadedDirectory"));
      // load ftp config
      String ftpUsername = config.getProperty("ftp.username");
      String ftpPassword = config.getProperty("ftp.password");
      String ftpServerURL = config.getProperty("ftp.serverURL");
      int ftpServerPort = Integer.parseInt(config.getProperty("ftp.server.port"));
      String ftpDestinationDirectory = config.getProperty("ftp.destinationDirectory");
      ftpConfig = new FTPConfig(ftpUsername, ftpPassword, ftpServerURL, ftpServerPort, ftpDestinationDirectory);

      // TODO load api config

      // reading logging config
      System.setProperty("logging.urlLog", config.getProperty("logging.urlLog"));
      ;
    } catch (IOException e) {
      System.out.println("Can not load config.properties. Exiting. Reason: " + e.getMessage());
    }
  }
}