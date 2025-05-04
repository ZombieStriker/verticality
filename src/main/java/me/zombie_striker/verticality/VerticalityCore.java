package me.zombie_striker.verticality;

import me.zombie_striker.verticality.chat.ChatGroupManager;
import me.zombie_striker.verticality.commands.CommandManager;
import me.zombie_striker.verticality.entity.ai.EntityAIManager;
import me.zombie_striker.verticality.event.EventManager;
import me.zombie_striker.verticality.files.LoggingFileManager;
import me.zombie_striker.verticality.files.MinecraftServerSettingsManager;
import me.zombie_striker.verticality.world.InstanceManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;
import net.minestom.server.timer.TaskSchedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class VerticalityCore {

    public static final int WORLD_SEED = 11545;
    private final Logger LOGGER = LoggerFactory.getLogger("Verticality");

    private final InstanceManager instanceManager = new InstanceManager(this);
    private final EventManager eventManager = new EventManager(this);
    private final EntityAIManager aiManager = new EntityAIManager();
    private final CommandManager commandManager = new CommandManager(this); // Pass this
    private final ChatGroupManager chatGroupManager = new ChatGroupManager(this); // Pass this

    private final LoggingFileManager loggingFileManager;
    private final MinecraftServerSettingsManager settingsManager; // Added field for settings manager
    private final ScheduledExecutorService loggingService;

    protected VerticalityCore() {
        this.loggingFileManager = new LoggingFileManager("logs/server.log");
        this.loggingService = Executors.newSingleThreadScheduledExecutor();
        this.settingsManager = new MinecraftServerSettingsManager(); // Initialize settings manager
    }

    public void init() {
        MojangAuth.init();
        instanceManager.init();
        eventManager.init();
        commandManager.init();
        startLogging(); // Start the logging service
    }

    private void startLogging() {
        loggingService.scheduleAtFixedRate(loggingFileManager::writeLogToFile, 5, 5, TimeUnit.MINUTES);
    }

    public void start(MinecraftServer server) {
        eventManager.registerAllEventListeners();
        log("Server started.");
        MinecraftServer.getSchedulerManager().scheduleTask(() -> {
            loggingFileManager.writeLogToFile();
            return TaskSchedule.minutes(5);
        }, TaskSchedule.minutes(5));
        server.start("0.0.0.0", 25565);
    }

    public void stop() {
        loggingService.shutdown(); // Shutdown the logging service
        loggingFileManager.writeLogToFile(); // Flush remaining logs on shutdown
    }

    public EntityAIManager getAiManager() {
        return aiManager;
    }

    public InstanceManager getInstanceManager() {
        return instanceManager;
    }

    public ChatGroupManager getChatGroupManager() {
        return chatGroupManager;
    }

    public MinecraftServerSettingsManager getSettingsManager() { // Getter for settings manager
        return settingsManager;
    }

    public void log(String message) {
        LOGGER.info(message);
        loggingFileManager.log(message);
    }

    public void log(String provider, String message) {
        LOGGER.info("[{}]: {}", provider, message);
    }
}
