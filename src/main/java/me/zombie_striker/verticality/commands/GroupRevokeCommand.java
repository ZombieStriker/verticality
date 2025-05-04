package me.zombie_striker.verticality.commands;

import me.zombie_striker.verticality.VerticalityCore;
import me.zombie_striker.verticality.chat.ChatGroup;
import me.zombie_striker.verticality.chat.ChatGroupManager;
import me.zombie_striker.verticality.chat.ChatPermissions;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

import java.util.UUID;

public class GroupRevokeCommand extends Command {
    private final VerticalityCore core;

    public GroupRevokeCommand(VerticalityCore core) {
        super("group_revoke");
        this.core = core;

        var targetArg = ArgumentType.String("targetPlayerName");
        var groupArg = ArgumentType.String("groupName");

        addSyntax((sender, context) -> {
            if (sender instanceof Player player) {
                String targetPlayerName = context.get(targetArg);
                String groupName = context.get(groupArg);
                execute(player.getUuid(), targetPlayerName, groupName, player);
            }
        }, targetArg, groupArg);
    }

    public void execute(UUID playerId, String targetPlayerName, String groupName, Player sender) {
        UUID targetPlayerId = core.getInstanceManager().getPlayerByDisplayName(targetPlayerName);
        if (targetPlayerId == null) {
            sender.sendMessage("Target player " + targetPlayerName + " not found.");
            return;
        }

        ChatGroupManager chatGroupManager = core.getChatGroupManager();
        ChatGroup chatGroup = chatGroupManager.getGroupByName(groupName);
        if (chatGroup != null) {
            if (chatGroup.hasPermission(playerId, ChatPermissions.REVOKE)) {
                chatGroup.removeMember(targetPlayerId);
                sender.sendMessage("You revoked " + targetPlayerName + "'s membership from group " + chatGroup.getDisplayName());
            } else {
                sender.sendMessage("You do not have permission to revoke.");
            }
        } else {
            sender.sendMessage("Group " + groupName + " not found.");
        }
    }
}
