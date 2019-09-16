package com.asem.pojo;

public class FTPConfig {
    private final String ftpUsername;
    private final String ftpPassword;
    private final String ftpServerURL;
    private final int ftpServerPort;
    private final String remoteDirectory;

    public FTPConfig(String ftpUsername, String ftpPassword, String ftpServerURL, int ftpServerPort, String remoteDirectory) {
        this.ftpUsername = ftpUsername;
        this.ftpPassword = ftpPassword;
        this.ftpServerURL = ftpServerURL;
        this.ftpServerPort = ftpServerPort;
        this.remoteDirectory = remoteDirectory;
    }

    public String getFtpUsername() {
        return ftpUsername;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public String getFtpServerURL() {
        return ftpServerURL;
    }

    public int getFtpServerPort() {
        return ftpServerPort;
    }

    public String getRemoteDirectory() {
        return remoteDirectory;
    }

    @Override
    public String toString() {
        return "FTPConfig{" +
                "ftpUsername='" + ftpUsername + '\'' +
                ", ftpPassword='" + ftpPassword + '\'' +
                ", ftpServerURL='" + ftpServerURL + '\'' +
                ", ftpServerPort=" + ftpServerPort +
                ", remoteDirectory='" + remoteDirectory + '\'' +
                '}';
    }
}