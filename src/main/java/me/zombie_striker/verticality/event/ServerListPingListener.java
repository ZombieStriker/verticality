package me.zombie_striker.verticality.event;

import me.zombie_striker.verticality.VerticalityCore;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.server.ServerListPingEvent;
import org.jetbrains.annotations.NotNull;

public class ServerListPingListener implements EventListener<ServerListPingEvent> {
    private final VerticalityCore core;

    public ServerListPingListener(VerticalityCore core) {
        this.core = core;
    }

    @Override
    public @NotNull Class<ServerListPingEvent> eventType() {
        return ServerListPingEvent.class;
    }

    @Override
    public Result run(ServerListPingEvent event) {
        // Set the MOTD
        event.getResponseData().setDescription(core.getSettingsManager().getSettings().getMotd());
        // Set the max players
        event.getResponseData().setOnline(core.getInstanceManager().getTotalPlayerCount());
        // Set the current online players
        event.getResponseData().setMaxPlayer(core.getSettingsManager().getSettings().getMaxPlayerCount());
        // Optionally set the server icon (Assuming you have logic to handle server icons)
        // event.setServerIcon(loadIconMethod());
        return Result.SUCCESS;
    }
}