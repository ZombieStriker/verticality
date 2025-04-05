package me.zombie_striker.verticality.event;

import me.zombie_striker.verticality.Verticality;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.instance.InstanceChunkUnloadEvent;
import org.jetbrains.annotations.NotNull;

public class ChunkUnloadListener implements EventListener<InstanceChunkUnloadEvent> {
    @Override
    public @NotNull Class<InstanceChunkUnloadEvent> eventType() {
        return InstanceChunkUnloadEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull InstanceChunkUnloadEvent event) {
        Verticality.getVerticalityCore().getInstanceManager().removeChunkFromPopulateSet(event.getChunk().getIdentifier());
        return Result.SUCCESS;
    }
}
