package me.zombie_striker.verticality.world;

import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.IChunkLoader;
import net.minestom.server.instance.Instance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VerticalityWorldLoader implements IChunkLoader {
    @Override
    public @Nullable Chunk loadChunk(@NotNull Instance instance, int x, int z) {
        return null;
    }

    @Override
    public void saveChunk(@NotNull Chunk chunk) {

    }

}
