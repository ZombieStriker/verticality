package me.zombie_striker.verticality.world;

import me.zombie_striker.verticality.VerticalGenerator;
import me.zombie_striker.verticality.VerticalityCore;
import net.minestom.server.MinecraftServer;
import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;
import net.minestom.server.instance.InstanceContainer;

import java.util.*;

public class InstanceManager {

    private final Set<UUID> chunkFullPopulateSet = new HashSet<>();
    private final Map<UUID, Instance> playerInstances = new HashMap<>();
    private InstanceContainer container;
    private Instance mainInstance;

    private final VerticalityCore verticalityCore;

    public InstanceManager(VerticalityCore core) {
        this.verticalityCore = core;
    }

    public void init() {
        net.minestom.server.instance.InstanceManager manager = MinecraftServer.getInstanceManager();
        this.container = manager.createInstanceContainer();

        this.mainInstance = manager.createSharedInstance(container);

        VerticalGenerator generator = new VerticalGenerator(VerticalityCore.WORLD_SEED);

        mainInstance.setGenerator(generator);
        mainInstance.setChunkSupplier(VChunk::new);
        container.setChunkLoader(new VerticalityWorldLoader(verticalityCore.getSettingsManager().getSettings().getDefaultWorldFolder()));
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

    public int getTotalPlayerCount() {
        return this.mainInstance.getPlayers().size();
    }

    public Set<Player> getTotalPlayers() {
        Set<Player> players = new HashSet<>();
        players.addAll(this.mainInstance.getPlayers());
        return players;
    }

    public UUID getPlayerByDisplayName(String displayName) {
        for (UUID playerId : playerInstances.keySet()) {
            Player player = null;
            for (Instance i : this.playerInstances.values()) {
                for (Player p : i.getPlayers()) {
                    if (p.getUsername().equalsIgnoreCase(displayName)) {
                        player = p;
                        break;
                    }
                }
                if (player != null) break;
            }
            if (player != null && player.getUsername().equalsIgnoreCase(displayName)) {
                return playerId; // Match found, return UUID
            }
        }
        return null; // Player not found
    }

    /**
     * Retrieves the Player associated with the given UUID.
     *
     * @param playerId the UUID of the player to retrieve
     * @return The Player object if found; null otherwise.
     */
    public Player getPlayerByUUID(UUID playerId) {
        Instance instance = playerInstances.get(playerId);
        if (instance != null) {
            return instance.getPlayers().stream()
                    .filter(player -> player.getUuid().equals(playerId))
                    .findFirst()
                    .orElse(null); // Return null if not found
        }
        return null; // Player not found
    }
}
