package me.zombie_striker.verticality.commands.util;

import net.minestom.server.entity.Player;
import net.minestom.server.instance.Instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Utility class to parse Minecraft command selector arguments.
 */
public class SelectorCommandUtil {
    private final Random random = new Random();

    /**
     * Parses the command selector from the given input.
     *
     * @param input  The command selector input (e.g., @p, @r, @a).
     * @param sender The player who sent the command.
     * @return A list of players matching the selector.
     */
    public List<Player> parseSelector(String input, Player sender) {
        List<Player> players = new ArrayList<>();
        Instance instance = sender.getInstance();

        switch (input) {
            case "@p":
                // Nearest player logic
                players.add(findNearestPlayer(sender, instance));
                break;
            case "@r":
                // Random player
                players.add(getRandomPlayer(instance));
                break;
            case "@a":
                // All players in the instance
                players.addAll(instance.getPlayers());
                break;
            default:
                // Handle coordinates and other formats here if necessary
                break;
        }

        return players;
    }

    private Player findNearestPlayer(Player sender, Instance instance) {
        // Logic to find the nearest player to the sender
        return instance.getPlayers().stream()
                .filter(player -> !player.equals(sender))
                .min((p1, p2) -> Double.compare(sender.getDistance(p1), sender.getDistance(p2)))
                .orElse(null);
    }

    private Player getRandomPlayer(Instance instance) {
        Set<Player> players = instance.getPlayers();
        return players.isEmpty() ? null : players.toArray(new Player[0])[random.nextInt(players.size())];
    }
}
