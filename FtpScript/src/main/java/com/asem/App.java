package com.asem;

import com.asem.pojo.APIConfig;
import com.asem.pojo.Config;
import com.asem.pojo.FTPConfig;
import com.asem.pojo.LoggingConfig;
import org.apache.log4j.Logger;

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
import java.util.stream.Collectors;

/** Hello world! */
public class App {
  private static final Logger LOGGER = Logger.getLogger(App.class);
  private static final ExecutorService executorService = Executors.newFixedThreadPool(50);

  public static void main(String[] args) {
    try {
      List<Config> configs = getConfigs(args[0]);
      processAll(configs);
      executorService.shutdown();
      executorService.awaitTermination(10, TimeUnit.MINUTES);
      System.out.println("Finished successfully");
    } catch (IOException | InterruptedException e) {
      LOGGER.error("An error occurred: ", e);
    }
  }

  private static void processAll(List<Config> usersConfigs) throws IOException {
    for (Config config : usersConfigs) {
      processUser(config);
    }
  }

  private static void processUser(Config usersConfig) throws IOException {
    List<Path> filesToUpload = getFilesToUpload(usersConfig);
    if (!filesToUpload.isEmpty()) {
      for (Path f : filesToUpload) {
        executorService.submit(new FileUploader(usersConfig, f));
      }
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
      LoggingConfig loggingConfig = new LoggingConfig(config.getProperty("logging.urlLog"));
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