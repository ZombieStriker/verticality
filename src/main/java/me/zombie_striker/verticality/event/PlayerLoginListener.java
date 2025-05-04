package me.zombie_striker.verticality.event;

import me.zombie_striker.verticality.VerticalityCore;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerLoginListener implements EventListener<AsyncPlayerConfigurationEvent> {

    private final VerticalityCore verticalityCore;

    public PlayerLoginListener(VerticalityCore core) {
        this.verticalityCore = core;
    }

    @Override
    public @NotNull Class<AsyncPlayerConfigurationEvent> eventType() {
        return AsyncPlayerConfigurationEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull AsyncPlayerConfigurationEvent event) {
        final Player player = event.getPlayer();
        event.setSpawningInstance(verticalityCore.getInstanceManager().getMainInstance());
        player.setRespawnPoint(new Pos(0, 100, 0));
        return Result.SUCCESS;
    }
}
