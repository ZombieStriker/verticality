package me.zombie_striker.verticality.world;

import net.minestom.server.instance.Instance;
import net.minestom.server.instance.LightingChunk;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class VChunk extends LightingChunk {


    private boolean initPostGen = false;

    public VChunk(@NotNull Instance instance, int chunkX, int chunkZ) {
        super(instance, chunkX, chunkZ);
        Set<BlockStruct> blockDataAll = new HashSet<>();
    }

    public void setInitPostGen(boolean b) {
        this.initPostGen = b;
    }

    public boolean isPostGen() {
        return initPostGen;
    }
}
