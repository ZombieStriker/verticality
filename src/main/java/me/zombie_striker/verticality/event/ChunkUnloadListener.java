package me.zombie_striker.verticality.event;

import me.zombie_striker.verticality.VerticalityCore;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.instance.InstanceChunkUnloadEvent;
import org.jetbrains.annotations.NotNull;

public class ChunkUnloadListener implements EventListener<InstanceChunkUnloadEvent> {
    private final VerticalityCore core;

    public ChunkUnloadListener(VerticalityCore verticalityCore) {
        this.core = verticalityCore;
    }

    @Override
    public @NotNull Class<InstanceChunkUnloadEvent> eventType() {
        return InstanceChunkUnloadEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull InstanceChunkUnloadEvent event) {
        core.getInstanceManager().removeChunkFromPopulateSet(event.getChunk().getIdentifier());
        return Result.SUCCESS;
    }
}
