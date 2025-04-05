package me.zombie_striker.verticality.event;

import me.zombie_striker.verticality.Verticality;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.instance.InstanceChunkLoadEvent;
import org.jetbrains.annotations.NotNull;

public class ChunkLoadListener implements EventListener<InstanceChunkLoadEvent> {
    @Override
    public @NotNull Class<InstanceChunkLoadEvent> eventType() {
        return InstanceChunkLoadEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull InstanceChunkLoadEvent event) {
        Verticality.getVerticalityCore().getInstanceManager().addChunkToPopulate(event.getChunk().getIdentifier());
        return Result.SUCCESS;
    }
}
