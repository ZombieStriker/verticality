package me.zombie_striker.verticality;

import de.articdive.jnoise.generators.noisegen.opensimplex.FastSimplexNoiseGenerator;
import me.zombie_striker.verticality.util.FastPowUtil;
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
    private final FastPowUtil fastPowUtil;

    public VerticalGenerator(int seed) {
        this.seed = seed;
        this.noiseGenerator = FastSimplexNoiseGenerator.newBuilder().build();
        this.fastPowUtil = new FastPowUtil(1200, 100, -1, 2, 4, 4);
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

        float noisePower = 4;

        float normalized = (noise / range);

        double yheight = 50f;

        yheight += Math.min(250, fastPowUtil.getOutput((normalized + 0.9f), noisePower) * 14);

        mod.fill(new Pos(x, -63, z), new Pos(x + 1, yheight, z + 1), Block.STONE);

        if (yheight < 67) {
            if (yheight < 64) {
                mod.fill(new Pos(x, yheight, z), new Pos(x + 1, 64, z + 1), Block.WATER);
            }
            mod.fill(new Pos(x, yheight - 3, z), new Pos(x + 1, yheight, z + 1), Block.SAND);
        } else {
            mod.fill(new Pos(x, yheight - 3, z), new Pos(x + 1, yheight, z + 1), Block.DIRT);
            mod.setBlock(new Pos(x, yheight - 1, z), Block.GRASS_BLOCK);


            float noiseTrees = getBetterNoise(seed, x, z, 1020f, 4);
            float rangeTress = getRangeFromRecursive(4);

            float normalizedTrees = (noiseTrees / rangeTress);

            if (normalizedTrees > -0.5f) {
                normalizedTrees = (normalized + 0.5f) / (1.5f);

                boolean shouldPlantTree = ThreadLocalRandom.current().nextInt((int) (((1.0f - (normalizedTrees)) * 100)) + 1) == 0;
                if (shouldPlantTree) {
                    mod.setBlock(new Pos(x, yheight, z), Block.OAK_SAPLING);
                }
            }
        }
    }
}
