package me.zombie_striker.verticality.files;

import me.zombie_striker.verticality.Settings;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MinecraftServerSettingsManager {
    private static final String SETTINGS_FILE = "settings.json";
    private Settings settings;

    public MinecraftServerSettingsManager() {
        settings = new Settings();
        loadSettings();
    }

    public Settings getSettings() {
        return settings;
    }

    /**
     * Load settings from the JSON file
     */
    private void loadSettings() {
        File file = new File(SETTINGS_FILE);
        if (!file.exists()) {
            createDefaultSettingsFile(file);
        } else {
            try {
                String content = new String(Files.readAllBytes(Paths.get(SETTINGS_FILE)));
                JSONObject json = new JSONObject(content);

                settings.setMotd(json.optString("motd", settings.getMotd()));
                settings.setMaxPlayerCount(json.optInt("maxPlayerCount", settings.getMaxPlayerCount()));
                settings.setLogSaveInterval(json.optInt("logSaveInterval", settings.getLogSaveInterval()));
                settings.setDefaultWorldFolder(json.optString("defaultWorldFolder", settings.getDefaultWorldFolder()));
                settings.setDefaultChatGroup(json.optString("defaultChatGroup", settings.getDefaultChatGroup()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Create the default settings JSON file
     */
    private void createDefaultSettingsFile(File file) {
        try {
            JSONObject json = new JSONObject();
            json.put("motd", settings.getMotd());
            json.put("maxPlayerCount", settings.getMaxPlayerCount());
            json.put("logSaveInterval", settings.getLogSaveInterval());
            json.put("defaultWorldFolder", settings.getDefaultWorldFolder());
            json.put("defaultChatGroup", settings.getDefaultChatGroup());

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(json.toString(4)); // Pretty print with 4 spaces
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the current settings to the JSON file
     */
    public void saveSettings() {
        JSONObject json = new JSONObject();
        json.put("motd", settings.getMotd());
        json.put("maxPlayerCount", settings.getMaxPlayerCount());
        json.put("logSaveInterval", settings.getLogSaveInterval());
        json.put("defaultWorldFolder", settings.getDefaultWorldFolder());
        json.put("defaultChatGroup", settings.getDefaultChatGroup());

        try (FileWriter writer = new FileWriter(SETTINGS_FILE)) {
            writer.write(json.toString(4)); // Pretty print with 4 spaces
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
