package me.zombie_striker.verticality;

import me.zombie_striker.verticality.event.EventManager;
import me.zombie_striker.verticality.world.InstanceManager;
import net.minestom.server.MinecraftServer;
import net.minestom.server.extras.MojangAuth;

public class VerticalityCore {

    public static int WORLD_SEED = 11545;

    private final InstanceManager instanceManager = new InstanceManager();
    private final EventManager eventManager = new EventManager();


    public VerticalityCore() {

    }

    public void init() {
        MojangAuth.init();
        instanceManager.init();
        eventManager.init();
    }

    public void start(MinecraftServer server) {
        eventManager.registerAllEventListeners();
        Verticality.log("Server started.");
        server.start("0.0.0.0", 25565);

    }

    public InstanceManager getInstanceManager() {
        return instanceManager;
    }
}
