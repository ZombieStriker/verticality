package me.zombie_striker.verticality.event;

import me.zombie_striker.verticality.VerticalityCore;
import net.minestom.server.MinecraftServer;
import net.minestom.server.event.Event;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.GlobalEventHandler;

import java.util.HashSet;
import java.util.Set;

public class EventManager {

    private final Set<EventListener> listeners = new HashSet<>();
    private final VerticalityCore verticalityCore;

    public EventManager(VerticalityCore core) {
        this.verticalityCore = core;
    }

    public void init() {
        listeners.add(new PlayerLoginListener(verticalityCore));
        listeners.add(new BlockBreakListener());
        listeners.add(new PlayerInventoryItemCollectListener());
        listeners.add(new ChunkLoadListener(verticalityCore));
        listeners.add(new ChunkUnloadListener(verticalityCore));
        listeners.add(new ServerTickListener(verticalityCore));
        listeners.add(new ChatListener(verticalityCore));
        listeners.add(new PlayerCommandListener(verticalityCore)); // Register PlayerCommandListener
        listeners.add(new ServerListPingListener(verticalityCore));
    }

    public void registerAllEventListeners() {
        GlobalEventHandler geh = MinecraftServer.getGlobalEventHandler();
        for (Object listener : listeners) {
            if (listener instanceof EventListener<?>) {
                geh.addListener((EventListener<? extends Event>) listener);
            }
        }
    }
}
