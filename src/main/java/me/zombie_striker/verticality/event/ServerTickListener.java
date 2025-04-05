package me.zombie_striker.verticality.event;

import me.zombie_striker.verticality.Verticality;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.instance.InstanceTickEvent;
import net.minestom.server.instance.Chunk;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;

public class ServerTickListener implements EventListener<InstanceTickEvent> {
    @Override
    public @NotNull Class<InstanceTickEvent> eventType() {
        return InstanceTickEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull InstanceTickEvent event) {
        Collection<Chunk> col = event.getInstance().getChunks();
        if (col.isEmpty()) {
            return Result.SUCCESS;
        }

        Chunk[] chunks = col.toArray(new Chunk[col.size()]);
        Chunk randomChunk = chunks[ThreadLocalRandom.current().nextInt(chunks.length)];

        if (Verticality.getVerticalityCore().getInstanceManager().getChunkFullPopulateSet().contains(randomChunk.getIdentifier())) {

        }
        return Result.SUCCESS;
    }
}
