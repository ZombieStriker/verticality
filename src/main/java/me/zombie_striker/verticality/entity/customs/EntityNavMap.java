package me.zombie_striker.verticality.entity.customs;

import net.minestom.server.coordinate.Pos;

import java.util.ArrayList;
import java.util.List;

public class EntityNavMap {
    private final List<Pos> pointsOfInterest = new ArrayList<>();

    /**
     * Adds a point of interest to the navigation map.
     *
     * @param point The point to add.
     */
    public void addPointOfInterest(Pos point) {
        pointsOfInterest.add(point);
    }

    /**
     * Gets a random point of interest for navigation.
     *
     * @return A random point or null if no points are available.
     */
    public Pos getRandomPoint() {
        if (pointsOfInterest.isEmpty()) {
            return null;
        }
        return pointsOfInterest.get((int) (Math.random() * pointsOfInterest.size()));
    }
}
