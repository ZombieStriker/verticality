package me.zombie_striker.verticality.entity.ai;

import me.zombie_striker.verticality.entity.customs.EntityNavMap;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.ai.EntityAIGroupBuilder;
import net.minestom.server.instance.Instance;

public class EntityAIManager {

    /**
     * Applies AI to the given entity by setting movement goals.
     *
     * @param entity   The entity to apply AI to.
     * @param instance The instance where the entity exists.
     */
    public void applyAI(EntityCreature entity, Instance instance) {
        EntityNavMap navMap = new EntityNavMap();
        // Adding points of interest - replace with actual coordinates as needed
        navMap.addPointOfInterest(new Pos(0, 0, 0)); // Example point
        navMap.addPointOfInterest(new Pos(10, 0, 10)); // Another example point

        // Creating a movement goal using the navMap
        NaturalAnimalMovementGoal movementGoal = new NaturalAnimalMovementGoal(entity, navMap, instance);
        new EntityAIGroupBuilder()
                .addGoalSelector(movementGoal)
                .build();
    }
}