package me.zombie_striker.verticality.event;

import me.zombie_striker.verticality.VerticalityCore;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerCommandEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerCommandListener implements EventListener<PlayerCommandEvent> {
    private final VerticalityCore core;

    public PlayerCommandListener(VerticalityCore core) {
        this.core = core;
    }

    @Override
    public @NotNull Class<PlayerCommandEvent> eventType() {
        return PlayerCommandEvent.class;
    }

    @Override
    public @NotNull Result run(PlayerCommandEvent event) {
        String commandName = event.getCommand();
        core.log("Player " + event.getPlayer().getUsername() + " executed command: " + commandName);
        return Result.SUCCESS;
    }
}
