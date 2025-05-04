package me.zombie_striker.verticality.entity.ai;

import me.zombie_striker.verticality.entity.customs.EntityNavMap;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.GoalSelector;
import net.minestom.server.instance.Instance;

import java.util.Random;

public class NaturalAnimalMovementGoal extends GoalSelector {
    private static final double WANDER_DISTANCE = 10.0;
    private static final double CLOSE_ENOUGH_DISTANCE = 1.0;
    private static final int WANDER_CHANCE = 5;
    private final EntityCreature entity;
    private final EntityNavMap navMap;
    private final Random random = new Random();
    private final Instance instance; // Instance for block checks
    private Pos targetPoint;

    public NaturalAnimalMovementGoal(EntityCreature entity, EntityNavMap navMap, Instance instance) {
        super(entity);
        this.entity = entity;
        this.navMap = navMap;
        this.instance = instance; // Assign instance
    }

    @Override
    public boolean shouldStart() {
        return random.nextInt(WANDER_CHANCE) == 0;
    }

    @Override
    public void start() {
        setNewTargetPoint();
    }

    @Override
    public void tick(long l) {
    }

    @Override
    public boolean shouldEnd() {
        return isCloseToTargetPoint();
    }

    @Override
    public void end() {
        targetPoint = null;
    }

    private void setNewTargetPoint() {
        int entityX = (int) entity.getPosition().x();
        int entityZ = (int) entity.getPosition().z();
        double randomX = entityX + (random.nextDouble() * 2 - 1) * WANDER_DISTANCE;
        double randomZ = entityZ + (random.nextDouble() * 2 - 1) * WANDER_DISTANCE;

        if (isWalkable(randomX, randomZ)) {
            targetPoint = new Pos(randomX, entity.getPosition().y(), randomZ);
            navMap.addPointOfInterest(targetPoint);
            entity.getNavigator().setPathTo(targetPoint);
        }
    }

    private boolean isCloseToTargetPoint() {
        return targetPoint != null && entity.getPosition().distanceSquared(targetPoint) < CLOSE_ENOUGH_DISTANCE * CLOSE_ENOUGH_DISTANCE;
    }

    /**
     * Checks if the specified position is walkable.
     */
    private boolean isWalkable(double x, double z) {
        int worldY = findGroundHeight(x, z);
        if (worldY < 1) return false; // Too low to be valid

        return !instance.getBlock((int) x, worldY, (int) z).isSolid(); // Ensure it's not a solid block
    }

    /**
     * Finds the ground height for given coordinates.
     */
    private int findGroundHeight(double x, double z) {
        for (int y = 255; y >= 0; y--) {
            if (!instance.getBlock((int) x, y, (int) z).isAir()) {
                return y; // Found the highest solid block
            }
        }
        return -1; // No ground found
    }
}
