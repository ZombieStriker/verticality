
package me.zombie_striker.verticality;

import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator;
import net.minestom.server.coordinate.Point;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.block.Block;
import net.minestom.server.instance.generator.GenerationUnit;
import net.minestom.server.instance.generator.Generator;
import net.minestom.server.instance.generator.UnitModifier;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class VerticalGenerator implements Generator {

    private final int seed;

    private final FastSimplexNoiseGenerator noiseGenerator;

    public VerticalGenerator(int seed) {
        this.seed = seed;
        this.noiseGenerator = FastSimplexNoiseGenerator.newBuilder().build();
    }

    @Override
    public void generate(@NotNull GenerationUnit generationUnit) {
        Point start = generationUnit.absoluteStart();
        Point end = generationUnit.absoluteEnd();
        generateTerrain(start, end, generationUnit);
    }

    public float getNoise(int seed, float x, float y, float wavelength) {
        return (float) ((((noiseGenerator.evaluateNoise(x / wavelength, y / wavelength, seed)))));
    }

    public float getBetterNoise(int seed, float x, float y, float wavelength, int recursiveLayers) {
        float noise = 0;
        float val = 1.0f;
        float val2 = 1.0f;
        for (int i = 0; i < recursiveLayers; i++) {
            noise += val * getNoise(seed, x, y, wavelength / val2);
            val /= 2;
            val2 *= 2;
        }
        return noise;
    }

    public float getRangeFromRecursive(int recursiveLayers) {
        float result = 0;
        float val = 1.0f;
        for (int i = 0; i < recursiveLayers; i++) {
            result += val;
            val /= 2;
        }
        return result;
    }

    public void generateTerrain(Point start, Point end, GenerationUnit generationUnit) {
        UnitModifier mod = generationUnit.modifier();

        mod.fillHeight(-64, -63, Block.BEDROCK);

        for (float x = start.blockX(); x < end.blockX(); x++) {
            for (float z = start.blockZ(); z < end.blockZ(); z++) {
                generateXZ(mod, x, z);
            }
        }
    }

    public void generateXZ(UnitModifier mod, float x, float z) {
        float noise = getBetterNoise(seed, x, z, 1300f, 8);
        float range = getRangeFromRecursive(8);

        // Create a moisture noise for biome variation
        float moistureNoise = getBetterNoise(seed + 123, x, z, 2000f, 6);
        float moistureRange = getRangeFromRecursive(6);
        float normalizedMoisture = (moistureNoise / moistureRange);

        float noisePower = 4;

        float normalized = (noise / range);

        double yheight = 50f;

        yheight += Math.min(250, Math.pow((normalized + 0.9f), noisePower) * 14);

        // Determine biome based on height and moisture
        BiomeType biome = getBiomeType(yheight, normalizedMoisture);

        // Base terrain
        mod.fill(new Pos(x, -63, z), new Pos(x + 1, yheight, z + 1), Block.STONE);

        if (yheight < 67) {
            // Ocean/Beach
            if (yheight < 64) {
                mod.fill(new Pos(x, yheight, z), new Pos(x + 1, 64, z + 1), Block.WATER);
            }
            mod.fill(new Pos(x, yheight - 3, z), new Pos(x + 1, yheight, z + 1), Block.SAND);
        } else {
            // Apply biome-specific blocks
            applyBiomeBlocks(mod, x, z, yheight, biome, normalizedMoisture);

            // Tree generation based on biome
            generateVegetation(mod, x, z, yheight, biome, normalized);
        }
    }

    private BiomeType getBiomeType(double height, float moisture) {
        if (height > 120) {
            // Mountain tops
            return BiomeType.SNOWY_MOUNTAINS;
        } else if (height > 90) {
            // Mid-mountains
            return moisture < -0.2 ? BiomeType.ROCKY_MOUNTAINS : BiomeType.FOREST;
        } else if (height > 75) {
            // Lower areas
            return moisture > 0.3 ? BiomeType.SWAMP : (moisture < -0.3 ? BiomeType.PLAINS : BiomeType.FOREST);
        } else {
            // Near water
            return BiomeType.BEACH;
        }
    }

    private void applyBiomeBlocks(UnitModifier mod, float x, float z, double height, BiomeType biome, float moisture) {
        switch (biome) {
            case SNOWY_MOUNTAINS:
                mod.fill(new Pos(x, height - 3, z), new Pos(x + 1, height, z + 1), Block.STONE);
                mod.setBlock(new Pos(x, height - 1, z), Block.SNOW_BLOCK);
                mod.setBlock(new Pos(x, height, z), Block.SNOW);
                break;

            case ROCKY_MOUNTAINS:
                mod.fill(new Pos(x, height - 3, z), new Pos(x + 1, height, z + 1), Block.STONE);
                mod.setBlock(new Pos(x, height - 1, z),
                        ThreadLocalRandom.current().nextFloat() > 0.4 ? Block.STONE : Block.ANDESITE);
                break;

            case SWAMP:
                mod.fill(new Pos(x, height - 3, z), new Pos(x + 1, height - 1, z + 1), Block.DIRT);
                mod.setBlock(new Pos(x, height - 1, z), Block.GRASS_BLOCK);
                // Sometimes add mud or clay
                if (ThreadLocalRandom.current().nextFloat() > 0.7) {
                    mod.setBlock(new Pos(x, height - 1, z), Block.CLAY);
                }
                break;

            case PLAINS:
                mod.fill(new Pos(x, height - 3, z), new Pos(x + 1, height - 1, z + 1), Block.DIRT);
                mod.setBlock(new Pos(x, height - 1, z), Block.GRASS_BLOCK);
                break;

            case FOREST:
                mod.fill(new Pos(x, height - 3, z), new Pos(x + 1, height - 1, z + 1), Block.DIRT);
                mod.setBlock(new Pos(x, height - 1, z),
                        ThreadLocalRandom.current().nextFloat() > 0.1 ? Block.GRASS_BLOCK : Block.COARSE_DIRT);
                break;

            case BEACH:
                mod.fill(new Pos(x, height - 3, z), new Pos(x + 1, height, z + 1), Block.SAND);
                break;
        }
    }

    private void generateVegetation(UnitModifier mod, float x, float z, double height, BiomeType biome, float normalized) {
        float vegetationChance = ThreadLocalRandom.current().nextFloat();

        switch (biome) {
            case FOREST:
                // Dense tree coverage
                if (vegetationChance > 0.92) {
                    mod.setBlock(new Pos(x, height, z),
                            ThreadLocalRandom.current().nextFloat() > 0.3 ? Block.OAK_SAPLING : Block.BIRCH_SAPLING);
                } else if (vegetationChance > 0.8) {
                    mod.setBlock(new Pos(x, height, z), Block.GRASS_BLOCK);
                }
                break;

            case PLAINS:
                // Sparse trees, mostly grass
                if (vegetationChance > 0.98) {
                    mod.setBlock(new Pos(x, height, z), Block.OAK_SAPLING);
                } else if (vegetationChance > 0.7) {
                    mod.setBlock(new Pos(x, height, z), Block.GRASS_BLOCK);
                }
                break;

            case SWAMP:
                // Very sparse trees, lily pads near water
                if (vegetationChance > 0.95) {
                    mod.setBlock(new Pos(x, height, z), Block.OAK_SAPLING);
                } else if (vegetationChance > 0.9) {
                    mod.setBlock(new Pos(x, height, z), Block.LILY_PAD);
                }
                break;

            case SNOWY_MOUNTAINS:
                // No vegetation
                break;

            case ROCKY_MOUNTAINS:
                // Very rare vegetation
                if (vegetationChance > 0.98) {
                    mod.setBlock(new Pos(x, height, z), Block.GRASS_BLOCK);
                }
                break;
        }
    }

    // Enum to represent different biome types
    private enum BiomeType {
        SNOWY_MOUNTAINS,
        ROCKY_MOUNTAINS,
        FOREST,
        PLAINS,
        SWAMP,
        BEACH
    }
}
