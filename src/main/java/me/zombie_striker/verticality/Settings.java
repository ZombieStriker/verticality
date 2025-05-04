package me.zombie_striker.verticality;

public class Settings {
    private String motd = "Welcome to the server!"; // Default MOTD
    private int maxPlayerCount = 20; // Default max player count
    private int logSaveInterval = 5; // Log saving interval in minutes
    private String defaultWorldFolder = "world"; // Default world folder
    private String defaultChatGroup = "default"; // Default chat group name
    private String chatFormat = "[{group}] {player}: {message}"; // Updated chat format

    // Getters and Setters
    public String getMotd() {
        return motd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public int getMaxPlayerCount() {
        return maxPlayerCount;
    }

    public void setMaxPlayerCount(int maxPlayerCount) {
        this.maxPlayerCount = maxPlayerCount;
    }

    public int getLogSaveInterval() {
        return logSaveInterval;
    }

    public void setLogSaveInterval(int logSaveInterval) {
        this.logSaveInterval = logSaveInterval;
    }

    public String getDefaultWorldFolder() {
        return defaultWorldFolder;
    }

    public void setDefaultWorldFolder(String defaultWorldFolder) {
        this.defaultWorldFolder = defaultWorldFolder;
    }

    public String getDefaultChatGroup() {
        return defaultChatGroup;
    }

    public void setDefaultChatGroup(String defaultChatGroup) {
        this.defaultChatGroup = defaultChatGroup;
    }

    public String getChatFormat() {
        return chatFormat;
    }

    public void setChatFormat(String chatFormat) {
        this.chatFormat = chatFormat;
    }
}
