package me.zombie_striker.verticality.commands;

import me.zombie_striker.verticality.VerticalityCore;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

public class CreateGroupCommand extends Command {
    private final VerticalityCore verticalityCore;

    public CreateGroupCommand(VerticalityCore verticalityCore) {
        super("create_group");
        this.verticalityCore = verticalityCore;

        addSyntax((sender, context) -> {
            if (sender instanceof Player player) {
                String groupName = context.get("name");
                execute(player.getUsername(), groupName);
            }
        }, ArgumentType.String("name"));
    }

    public void execute(String username, String groupName) {

    }
}
