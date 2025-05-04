package me.zombie_striker.verticality.commands;

import me.zombie_striker.verticality.VerticalityCore;
import net.minestom.server.MinecraftServer;
import net.minestom.server.command.builder.Command;

import java.util.HashMap;

public class CommandManager {
    private final VerticalityCore verticalityCore;
    private final HashMap<String, Command> commands = new HashMap<>();

    public CommandManager(VerticalityCore verticalityCore) {
        this.verticalityCore = verticalityCore;
    }

    public void init() {
        registerAllCommands();
    }

    /**
     * Registers all commands.
     */
    private void registerAllCommands() {
        registerCommand("op", new OPCommand());
        registerCommand("groupinvite", new GroupInviteCommand(verticalityCore));
        registerCommand("grouprevoke", new GroupRevokeCommand(verticalityCore));
        registerCommand("group", new GroupCommand(verticalityCore));
        registerCommand("creategroup", new CreateGroupCommand(verticalityCore));
        registerCommand("group_profile", new GroupProfileCommand(verticalityCore));  // Register the GroupProfileCommand
        registerCommand("group_set", new GroupSetCommand(verticalityCore));  // Register the GroupSetCommand
        // Add any additional commands here as necessary
    }

    public void registerCommand(String commandName, Command command) {
        commands.put(commandName, command);
        MinecraftServer.getCommandManager().register(command);
    }
}
