package com.uploader.service;

import org.apache.log4j.Logger;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerService {
  private static final Logger LOGGER = Logger.getLogger(LoggerService.class);
  private Writer out;

  public LoggerService(String pathToLogFile) {
    File outFile = new File(pathToLogFile);
    try {
      outFile.createNewFile();
      out = new BufferedWriter(new FileWriter(outFile, true));
    } catch (IOException e) {
      LOGGER.error("Error: ", e);
    }
  }

  public synchronized void addInfo(String fileUrl) {
    if (out != null) { // out can be null. See constructor
      try {
        out.append(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(LocalDateTime.now()))
            .append(" - ")
            .append(fileUrl)
            .append(System.lineSeparator());
        out.flush();
      } catch (IOException e) {
        LOGGER.error("Error: ", e);
      }
    }
  }
}
