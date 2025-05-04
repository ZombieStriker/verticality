package me.zombie_striker.verticality.world;

import net.minestom.server.instance.Chunk;
import net.minestom.server.instance.IChunkLoader;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class VerticalityWorldLoader implements IChunkLoader {
    private final String worldName;
    private final Path worldPath;

    public VerticalityWorldLoader(String worldName) {
        this.worldName = worldName;
        this.worldPath = Paths.get("worlds", worldName, "regions");
        createDirectories();
    }

    private void createDirectories() {
        try {
            Files.createDirectories(worldPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public @Nullable Chunk loadChunk(@NotNull Instance instance, int x, int z) {
        // Load chunk logic (implement as needed)
        return null;
    }

    @Override
    public void saveChunk(@NotNull Chunk chunk) {
        int chunkX = chunk.getChunkX();
        int chunkZ = chunk.getChunkZ();
        String regionFileName = "r." + (chunkX >> 5) + "." + (chunkZ >> 5) + ".mca"; // Region file naming
        Path regionFilePath = worldPath.resolve(regionFileName);

        try (RandomAccessFile file = new RandomAccessFile(regionFilePath.toFile(), "rw")) {
            int offset = calculateChunkOffset(chunkX, chunkZ);
            file.seek(offset);
            writeChunkData(file, chunk);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int calculateChunkOffset(int chunkX, int chunkZ) {
        // Assuming a fixed-size chunk record of 4 bytes
        // Total chunks per region = 32 * 32
        int regionIndex = (chunkX & 31) + (chunkZ & 31) * 32;
        return regionIndex * 4; // Chunk data offset in the file
    }

    private void writeChunkData(RandomAccessFile file, Chunk chunk) throws IOException {
        // Here serialize the chunk's block data
        // Example structure: Write chunk coordinates and block data
        // `writeInt` for header or metadata, followed by serialized block data
        file.writeInt(chunk.getChunkX());
        file.writeInt(chunk.getChunkZ());

        // Serialize block data as needed
        Map<BlockPosition, Block> blockData = getBlockDataFromChunk(chunk);
        for (Map.Entry<BlockPosition, Block> entry : blockData.entrySet()) {
            BlockPosition position = entry.getKey();
            Block block = entry.getValue();

            // Write position and block type (example)
            file.writeInt(position.getX());
            file.writeInt(position.getY());
            file.writeInt(position.getZ());
            file.writeInt(block.getId()); // Assuming you have a method to get block ID
        }
    }

    private Map<BlockPosition, Block> getBlockDataFromChunk(Chunk chunk) {
        Map<BlockPosition, Block> blockData = new HashMap<>();
        // Collect the block data from the chunk and return it as a map
        // Example: iterate through chunk blocks and add block data
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 256; y++) { // Change as needed for world height
                for (int z = 0; z < 16; z++) {
                    Block block = chunk.getBlock(x, y, z);
                    if (block != Block.AIR) { // Avoid saving air blocks
                        blockData.put(new BlockPosition(x, y, z), block);
                    }
                }
            }
        }
        return blockData;
    }

    private static class BlockPosition {
        private final int x, y, z;

        BlockPosition(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getZ() {
            return z;
        }
    }
}
