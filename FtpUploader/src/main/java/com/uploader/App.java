package com.uploader;

import com.uploader.pojo.APIConfig;
import com.uploader.pojo.Config;
import com.uploader.pojo.FTPConfig;
import com.uploader.pojo.LoggingConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;


public class App {
  private static final Logger LOGGER = Logger.getLogger(App.class);
  private static final ExecutorService executorService = Executors.newFixedThreadPool(50);
  private static final String UPLOADED_LOGGING_FILENAME = "uploaded.txt";
  private static CountDownLatch latch;

  public static void main(String[] args) {
    if (args.length != 1) {
      showHelp();
      System.exit(-1);
    }
    try {
      System.out.println("Starting upload files....");
      List<Pair<Config, Path>> tasks = getTasks(getConfigs(args[0]));
      latch = new CountDownLatch(tasks.size());
      processAllTasksConcurrently(tasks);
      executorService.shutdown();
      System.out.println("Awaiting for uploading all files");
      latch.await();
      System.out.println("Finished successfully");
    } catch (IOException | InterruptedException e) {
      LOGGER.error("An error occurred: ", e);
    }
  }

  private static List<Pair<Config, Path>> getTasks(List<Config> configs) throws IOException {
    ArrayList<Pair<Config, Path>> result = new ArrayList<>();
    for (Config config : configs) {
      for (Path path : getFilesToUpload(config)) {
        result.add(Pair.of(config, path));
      }
    }
    return result;
  }

  private static void showHelp() {
    System.out.println("Invalid argumets count");
    System.out.println("Usage");
    System.out.println("\t <path to user folders>");
  }

  private static void processAllTasksConcurrently(List<Pair<Config, Path>> tasks) {
    for (Pair<Config, Path> task : tasks) {
      executorService.submit(new FileUploader(task.getKey(), task.getValue(), latch));
    }
  }

  private static List<Path> getFilesToUpload(Config config) throws IOException {
    return Files.list(config.getToUploadDirectoryPath())
        .filter(f -> Files.isRegularFile(f))
        .collect(Collectors.toList());
  }

  private static Config readConfig(Path pathToConfig) {
    Properties config = new Properties();
    try (BufferedReader reader = new BufferedReader(new FileReader(pathToConfig.toFile()))) {
      config.load(reader);
      // load paths config
      Path toUploadDirectory = Paths.get(config.getProperty("local.ToUploadDirectory"));
      Path uploadedDirectory = Paths.get(config.getProperty("local.UploadedDirectory"));
      // load ftp config
      String ftpUsername = config.getProperty("ftp.username");
      String ftpPassword = config.getProperty("ftp.password");
      String ftpServerURL = config.getProperty("ftp.serverURL");
      int ftpServerPort = Integer.parseInt(config.getProperty("ftp.server.port"));
      String ftpDestinationDirectory = config.getProperty("ftp.destinationDirectory");
      FTPConfig ftpConfig =
          new FTPConfig(
              ftpUsername, ftpPassword, ftpServerURL, ftpServerPort, ftpDestinationDirectory);

      // TODO load api config
      APIConfig apiConfig = new APIConfig();
      // Logging config
      LoggingConfig loggingConfig =
          new LoggingConfig(pathToConfig.getParent().resolve(UPLOADED_LOGGING_FILENAME).toString());
      return new Config(apiConfig, ftpConfig, loggingConfig, toUploadDirectory, uploadedDirectory);
    } catch (IOException e) {
      // this method is used in stream.map, that is why we can not throw checked exception here
      throw new RuntimeException(e);
    }
  }

  private static List<Config> getConfigs(String usersRootPath) throws IOException {
    return Files.list(Paths.get(usersRootPath))
        .filter(f -> Files.isDirectory(f))
        .map(p -> p.resolve(Paths.get("config.properties")))
        .map(App::readConfig)
        .collect(Collectors.toList());
  }
}