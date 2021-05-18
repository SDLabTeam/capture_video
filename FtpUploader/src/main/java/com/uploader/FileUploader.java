package com.uploader;

import com.uploader.config.APIConfig;
import com.uploader.config.Config;
import com.uploader.config.FTPConfig;
import com.uploader.service.EmailService;
import com.uploader.service.FTPService;
import com.uploader.service.LoggerService;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CountDownLatch;

public class FileUploader implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(FileUploader.class);
    private FTPConfig ftpConfig;
    private APIConfig apiConfig;
    private Path fileToBeUploaded;
    private Path localUploadedDirectory;
    private FTPService ftpService;
    private LoggerService loggerService;
    private EmailService emailService;
    private CountDownLatch latch;

    public FileUploader(Config config, Path fileToBeUploaded, CountDownLatch latch) {
        this.ftpConfig = config.getFtpConfig();
        this.apiConfig = config.getApiConfig();
        this.fileToBeUploaded = fileToBeUploaded;
        this.localUploadedDirectory = config.getUploadedDirectoryPath();
        this.ftpService = new FTPService(ftpConfig);
        this.loggerService = new LoggerService(config.getLoggingConfig().getLogFilePath());
        this.emailService = new EmailService(config.getEmailConfig());
        this.latch = latch;
    }

    @Override
    public void run() {
        try {
            if (ftpService.sendFile(fileToBeUploaded, ftpConfig.getRemoteDirectory())) {
                moveFile();
                callToAPI();
                logInfo();
            }
        } finally {
            latch.countDown();
        }
    }

    private void callToAPI() {
        emailService.sendEmail(ftpConfig.buildFileUrl(ftpService.getLastUploadedFilePath()));
    }

    private void moveFile() {
        try {
            Files.move(fileToBeUploaded, localUploadedDirectory.resolve(fileToBeUploaded.getFileName()));
        } catch (IOException e) {
            LOGGER.error("Error: ", e);
        }
    }

    private void logInfo() {
        loggerService.addInfo(ftpConfig.buildFileUrl(ftpService.getLastUploadedFilePath()));
    }

}
