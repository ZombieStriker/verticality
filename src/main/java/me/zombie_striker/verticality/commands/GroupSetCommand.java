package me.zombie_striker.verticality.commands;

import me.zombie_striker.verticality.VerticalityCore;
import me.zombie_striker.verticality.chat.ChatGroup;
import me.zombie_striker.verticality.chat.ChatGroupManager;
import me.zombie_striker.verticality.chat.ChatPermissions;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

import java.util.UUID;

public class GroupSetCommand extends Command {
    private final VerticalityCore verticalityCore;

    public GroupSetCommand(VerticalityCore verticalityCore) {
        super("group_set");
        this.verticalityCore = verticalityCore;

        var targetArg = ArgumentType.String("targetPlayerName");
        var groupArg = ArgumentType.String("groupName");
        var permissionGroupArg = ArgumentType.String("permissionGroup");

        addSyntax((sender, context) -> {
            if (sender instanceof Player player) {
                String targetPlayerName = context.get(targetArg);
                String groupName = context.get(groupArg);
                String permissionGroup = context.get(permissionGroupArg);
                execute(player.getUuid(), targetPlayerName, groupName, permissionGroup, player);
            }
        }, targetArg, groupArg, permissionGroupArg);
    }

    public void execute(UUID playerId, String targetPlayerName, String groupName, String permissionGroup, Player sender) {
        UUID targetPlayerId = verticalityCore.getInstanceManager().getPlayerByDisplayName(targetPlayerName);
        if (targetPlayerId == null) {
            sender.sendMessage("Target player " + targetPlayerName + " not found.");
            return;
        }

        ChatGroupManager chatGroupManager = verticalityCore.getChatGroupManager();
        ChatGroup chatGroup = chatGroupManager.getGroupByName(groupName);
        if (chatGroup != null) {
            if (chatGroup.hasPermission(playerId, ChatPermissions.CONFIGURE_PERMISSIONS)) {
                // Create and assign a new profile to the target player
                chatGroup.addProfile(permissionGroup); // Adds a profile without predefined permissions

                chatGroup.setPlayerProfile(targetPlayerId, permissionGroup); // Assign profile to player
                sender.sendMessage("Profile " + permissionGroup + " assigned to " + targetPlayerName + " in " + groupName);
            } else {
                sender.sendMessage("You do not have permission to configure permissions.");
            }
        } else {
            sender.sendMessage("Group " + groupName + " not found.");
        }
    }
}
