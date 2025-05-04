package me.zombie_striker.verticality.files;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LoggingFileManager {
    private final List<String> logMessages = new ArrayList<>();
    private final String logFilePath;

    public LoggingFileManager(String logFilePath) {
        this.logFilePath = logFilePath;
        try {
            Files.createDirectories(Paths.get(logFilePath).getParent());
            Files.createFile(Paths.get(logFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void log(String message) {
        logMessages.add(getTimestamp() + " - " + message);
    }

    public synchronized void writeLogToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
            for (String logMsg : logMessages) {
                writer.write(logMsg);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logMessages.clear(); // Clear the list after writing.
        }
    }

    private String getTimestamp() {
        return java.time.LocalDateTime.now().toString();
    }
}
