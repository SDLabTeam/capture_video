package com.asem.service;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggingService {
    private static Writer out;
    private LoggingService(){
        String outFilePath = System.getProperty("logging.urlLog");
        File outFile = new File(outFilePath);
        try {
            outFile.createNewFile();
            out = new BufferedWriter(new FileWriter(outFile, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static LoggingService instance;

    public static LoggingService getInstance() {
        if (instance == null) {
            synchronized (LoggingService.class) {
                if (instance == null) {
                    instance = new LoggingService();
                }
            }
        }
        return instance;
    }

    public synchronized void addInfo(String fileUrl) {
        if (out != null) { // out can be null. See constructor
            try {
                out.append(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss").format(LocalDateTime.now())).append(" - " + fileUrl).append(System.lineSeparator());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
