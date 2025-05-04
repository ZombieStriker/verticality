package me.zombie_striker.verticality.event;

import me.zombie_striker.verticality.VerticalityCore;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.EntityType;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.instance.InstanceTickEvent;
import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ServerTickListener implements EventListener<InstanceTickEvent> {
    private static final int MAX_TREES_PER_TICK = 30;

    private final VerticalityCore verticalityCore;

    public ServerTickListener(VerticalityCore core) {
        this.verticalityCore = core;
    }

    @Override
    public @NotNull Class<InstanceTickEvent> eventType() {
        return InstanceTickEvent.class;
    }

    @Override
    public @NotNull Result run(@NotNull InstanceTickEvent event) {
        Collection<Chunk> chunks = event.getInstance().getChunks();
        if (chunks.isEmpty()) {
            return Result.SUCCESS;
        }

        int r = ThreadLocalRandom.current().nextInt(chunks.size());
        Chunk randomChunk = chunks.toArray(new Chunk[0])[r];

        if (verticalityCore.getInstanceManager().getChunkFullPopulateSet().contains(randomChunk.getIdentifier())) {
            checkAllSaplingsInChunk(event.getInstance(), randomChunk);
            spawnEntitiesInChunk(event.getInstance(), randomChunk);
            verticalityCore.getInstanceManager().removeChunkFromPopulateSet(randomChunk.getIdentifier());
        }
        return Result.SUCCESS;
    }

    private void checkAllSaplingsInChunk(Instance instance, Chunk chunk) {
        if (!chunk.isLoaded()) {
            return;
        }

        Random random = ThreadLocalRandom.current();
        int chunkX = chunk.getChunkX() * 16;
        int chunkZ = chunk.getChunkZ() * 16;
        int treesGrown = 0;

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = chunkX + x;
                int worldZ = chunkZ + z;
                int y = findHighestBlock(instance, worldX, worldZ);

                if (y > 0) {
                    Block block = instance.getBlock(worldX, y, worldZ);

                    if (block == Block.OAK_SAPLING) {
                        instance.setBlock(worldX, y, worldZ, Block.AIR);
                        generateTree(instance, worldX, y, worldZ, random);
                        treesGrown++;

                        if (treesGrown >= MAX_TREES_PER_TICK) {
                            return;
                        }
                    }
                }
            }
        }
    }

    private int findHighestBlock(Instance instance, int x, int z) {
        if (!instance.isChunkLoaded(x >> 4, z >> 4)) {
            return 0;
        }

        for (int y = 255; y > 0; y--) {
            if (instance.getBlock(x, y, z) != Block.AIR) {
                return y;
            }
        }
        return 0;
    }

    private void generateTree(Instance instance, int x, int y, int z, Random random) {
        int height = random.nextInt(3) + 4;

        for (int i = 0; i < height; i++) {
            instance.setBlock(x, y + i, z, Block.OAK_LOG);
        }

        int leafStartY = y + height - 3;
        for (int ly = 0; ly < 4; ly++) {
            int radius = ly == 3 ? 1 : 2;
            for (int lx = -radius; lx <= radius; lx++) {
                for (int lz = -radius; lz <= radius; lz++) {
                    if ((Math.abs(lx) == 2 && Math.abs(lz) == 2) && random.nextBoolean()) {
                        continue;
                    }
                    instance.setBlock(x + lx, leafStartY + ly, z + lz, Block.OAK_LEAVES);
                }
            }
        }
    }

    private void spawnEntitiesInChunk(Instance instance, Chunk chunk) {
        if (!chunk.isLoaded() || shouldSkipAnimalSpawn()) {
            return;
        }

        Random random = ThreadLocalRandom.current();
        int chunkX = chunk.getChunkX() * 16;
        int chunkZ = chunk.getChunkZ() * 16;
        int spawnAttempts = random.nextInt(49) + 2;

        for (int i = 0; i < spawnAttempts; i++) {
            int x = chunkX + random.nextInt(16);
            int z = chunkZ + random.nextInt(16);
            int y = findHighestBlock(instance, x, z);

            if (canSpawnAnimal(instance, x, y, z)) {
                spawnAnimalGroup(instance, x, y, z, random);
            }
        }
    }

    /**
     * Determines if an animal spawn attempt should be skipped.
     *
     * @return true if the spawn should be skipped, false otherwise.
     */
    private boolean shouldSkipAnimalSpawn() {
        return ThreadLocalRandom.current().nextInt(20) != 0; // 1 in 20 chance
    }

    /**
     * Checks if an animal can be spawned at the given coordinates.
     *
     * @return true if the animal can be spawned, false otherwise.
     */
    private boolean canSpawnAnimal(Instance instance, int x, int y, int z) {
        return y > 64 && instance.getBlock(x, y + 1, z) == Block.AIR &&
                (instance.getBlock(x, y, z) == Block.GRASS_BLOCK ||
                        instance.getBlock(x, y, z) == Block.DIRT);
    }

    /**
     * Spawns a group of animals in the given position.
     */
    private void spawnAnimalGroup(Instance instance, int x, int y, int z, Random random) {
        EntityType entityType = getRandomAnimalType(random);
        spawnAnimal(instance, x, y, z, entityType); // Spawn the first animal

        int groupSize = random.nextInt(3) + 1; // Random group size
        for (int i = 0; i < groupSize; i++) {
            spawnAnimalAtOffset(instance, x, y, z, random, entityType);
        }
    }

    /**
     * Spawns an individual animal at the specified coordinates.
     */
    private void spawnAnimal(Instance instance, int x, int y, int z, EntityType entityType) {
        EntityCreature entity = new EntityCreature(entityType);
        verticalityCore.getAiManager().applyAI(entity, instance); // Pass the instance to applyAI
        entity.setInstance(instance, new Pos(x + 0.5, y + 1, z + 0.5));
    }

    /**
     * Spawns an animal at a random offset from the original coordinates.
     */
    private void spawnAnimalAtOffset(Instance instance, int x, int y, int z, Random random, EntityType entityType) {
        float offsetX = random.nextFloat() * 6 - 3;
        float offsetZ = random.nextFloat() * 6 - 3;
        int newX = x + Math.round(offsetX);
        int newZ = z + Math.round(offsetZ);

        spawnAnimal(instance, newX, y, newZ, entityType);
    }

    /**
     * Randomly selects an animal type to spawn.
     */
    private EntityType getRandomAnimalType(Random random) {
        return switch (random.nextInt(4)) {
            case 0 -> EntityType.PIG;
            case 1 -> EntityType.CHICKEN;
            case 2 -> EntityType.SHEEP;
            case 3 -> EntityType.COW;
            default -> EntityType.COW; // Fallback, should never reach this
        };
    }
}
