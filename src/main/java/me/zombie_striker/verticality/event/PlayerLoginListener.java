package me.zombie_striker.verticality.event;

import me.zombie_striker.verticality.Verticality;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.AsyncPlayerConfigurationEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerLoginListener implements EventListener<AsyncPlayerConfigurationEvent> {

    @Override
    public @NotNull Class<AsyncPlayerConfigurationEvent> eventType() {
        return AsyncPlayerConfigurationEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull AsyncPlayerConfigurationEvent event) {
        final Player player = event.getPlayer();
        event.setSpawningInstance(Verticality.getVerticalityCore().getInstanceManager().getMainInstance());
        player.setRespawnPoint(new Pos(0, 100, 0));
        return Result.SUCCESS;
    }
}
