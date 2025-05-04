package me.zombie_striker.verticality.commands;

import me.zombie_striker.verticality.VerticalityCore;
import me.zombie_striker.verticality.chat.ChatGroup;
import me.zombie_striker.verticality.chat.ChatGroupManager;
import me.zombie_striker.verticality.chat.ChatPermissions;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

import java.util.UUID;

public class GroupInviteCommand extends Command {
    private final VerticalityCore verticalityCore;

    public GroupInviteCommand(VerticalityCore verticalityCore) {
        super("group_invite");
        this.verticalityCore = verticalityCore;

        var targetArg = ArgumentType.String("targetPlayerName");
        var groupArg = ArgumentType.String("groupName");
        var profileArg = ArgumentType.String("profileName");

        addSyntax((sender, context) -> {
            if (sender instanceof Player player) {
                String targetPlayerName = context.get(targetArg);
                String groupName = context.get(groupArg);
                String profileName = context.get(profileArg);
                execute(player.getUuid(), targetPlayerName, groupName, profileName, player);
            }
        }, targetArg, groupArg, profileArg);
    }

    public void execute(UUID playerId, String targetPlayerName, String groupName, String profileName, Player sender) {
        UUID targetPlayerId = verticalityCore.getInstanceManager().getPlayerByDisplayName(targetPlayerName);
        if (targetPlayerId == null) {
            sender.sendMessage("Target player " + targetPlayerName + " not found.");
            return;
        }

        ChatGroupManager chatGroupManager = verticalityCore.getChatGroupManager();
        ChatGroup chatGroup = chatGroupManager.getGroupByName(groupName);
        if (chatGroup != null) {
            // Check for Invite permission
            if (!chatGroup.hasPermission(playerId, ChatPermissions.INVITE)) {
                sender.sendMessage("You do not have permission to invite players to this group.");
                return;
            }

            if (chatGroup.getProfiles().containsKey(profileName)) {
                chatGroupManager.inviteToGroup(targetPlayerId, groupName, profileName);
                sender.sendMessage("You invited " + targetPlayerName + " to group " + groupName + " with profile " + profileName + ".");
                Player targetPlayer = verticalityCore.getInstanceManager().getPlayerByUUID(targetPlayerId);
                if (targetPlayer != null) {
                    targetPlayer.sendMessage("You have been invited to join " + groupName + " group with profile " + profileName + "!");
                }
            } else {
                sender.sendMessage("Profile " + profileName + " does not exist in group " + groupName + ".");
            }
        } else {
            sender.sendMessage("Group " + groupName + " not found.");
        }
    }
}
