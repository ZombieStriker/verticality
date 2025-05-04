package me.zombie_striker.verticality.commands;

import net.minestom.server.command.builder.Command;
import net.minestom.server.entity.Player;

public class OPCommand extends Command {

    public OPCommand() {
        super("op");
        setDefaultExecutor((sender, context) -> {
            if (sender instanceof Player player) {
                if (player.getPermissionLevel() < 4) {
                    player.setPermissionLevel(4);
                    player.sendMessage("You have been OP'd!");
                } else {
                    player.sendMessage("You are already OP!");
                }
            }
        });
    }
}
