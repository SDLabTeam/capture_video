package com.uploader.config;

public class EmailConfig {
    private String username = "kievdaode@gmail.com";
    private String password = "Taichi1976";

    private String fromEmail = "kievdaode@gmail.com";

    /**
     * @param toEmail could be coma separated email string
     */
    private String toEmail;

    public EmailConfig(String username, String password, String fromEmail, String toEmail) {
        this.username = username;
        this.password = password;
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFromEmail() {
        return fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }
}
