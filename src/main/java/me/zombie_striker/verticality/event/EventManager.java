package me.zombie_striker.verticality.event;

import net.minestom.server.MinecraftServer;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.GlobalEventHandler;

import java.util.HashSet;
import java.util.Set;

public class EventManager {

    private final Set<EventListener> listeners = new HashSet<>();

    public EventManager() {

    }

    public void init() {
        listeners.add(new PlayerLoginListener());
    }

    public void registerAllEventListeners() {
        GlobalEventHandler geh = MinecraftServer.getGlobalEventHandler();
        for (EventListener listener : listeners) {
            geh.addListener(listener);
        }
    }
}
