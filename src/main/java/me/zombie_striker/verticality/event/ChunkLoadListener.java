package me.zombie_striker.verticality.event;

import me.zombie_striker.verticality.VerticalityCore;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.instance.InstanceChunkLoadEvent;
import org.jetbrains.annotations.NotNull;

public class ChunkLoadListener implements EventListener<InstanceChunkLoadEvent> {

    private final VerticalityCore verticalityCore;

    public ChunkLoadListener(VerticalityCore core) {
        this.verticalityCore = core;
    }
    @Override
    public @NotNull Class<InstanceChunkLoadEvent> eventType() {
        return InstanceChunkLoadEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull InstanceChunkLoadEvent event) {
        verticalityCore.getInstanceManager().addChunkToPopulate(event.getChunk().getIdentifier());
        return Result.SUCCESS;
    }
}
