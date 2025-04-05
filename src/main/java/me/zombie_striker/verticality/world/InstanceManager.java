package me.zombie_striker.verticality.world;

import me.zombie_striker.verticality.VerticalGenerator;
import me.zombie_striker.verticality.VerticalityCore;
import net.minestom.server.MinecraftServer;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;
import net.minestom.server.instance.LightingChunk;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class InstanceManager {

    private final Set<UUID> chunkFullPopulateSet = new HashSet<>();
    private InstanceContainer container;
    private Instance mainInstance;

    public InstanceManager() {
    }

    public void init() {
        net.minestom.server.instance.InstanceManager manager = MinecraftServer.getInstanceManager();
        this.container = manager.createInstanceContainer();

        this.mainInstance = manager.createSharedInstance(container);

        mainInstance.setGenerator(new VerticalGenerator(VerticalityCore.WORLD_SEED));
        mainInstance.setChunkSupplier(LightingChunk::new);
        container.setChunkLoader(new VerticalityWorldLoader());
    }

    public Instance getMainInstance() {
        return mainInstance;
    }

    public InstanceContainer getContainer() {
        return container;
    }

    public void addChunkToPopulate(UUID chunkUuid) {
        this.chunkFullPopulateSet.add(chunkUuid);
    }

    public void removeChunkFromPopulateSet(UUID chunkUuid) {
        this.chunkFullPopulateSet.remove(chunkUuid);
    }

    public Set<UUID> getChunkFullPopulateSet() {
        return chunkFullPopulateSet;
    }
}
