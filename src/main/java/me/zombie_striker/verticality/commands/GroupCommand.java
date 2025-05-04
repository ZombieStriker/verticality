package me.zombie_striker.verticality.commands;

import me.zombie_striker.verticality.VerticalityCore;
import me.zombie_striker.verticality.chat.ChatGroup;
import me.zombie_striker.verticality.chat.ChatGroupManager;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

import java.util.UUID;

public class GroupCommand extends Command {
    private final VerticalityCore verticalityCore;

    public GroupCommand(VerticalityCore verticalityCore) {
        super("group");
        this.verticalityCore = verticalityCore;

        var groupArg = ArgumentType.String("groupName");
        addSyntax((sender, context) -> {
            if (sender instanceof Player player) {
                String groupName = context.get(groupArg);
                execute(player.getUuid(), groupName, player);
            }
        }, groupArg);
    }

    public void execute(UUID playerId, String groupName, Player sender) {
        if (groupName == null || groupName.isBlank()) {
            sender.sendMessage("Group name cannot be empty.");
            return;
        }

        ChatGroupManager chatGroupManager = verticalityCore.getChatGroupManager();
        ChatGroup chatGroup = chatGroupManager.getGroupByName(groupName);

        if (chatGroup != null) {
            // Check if the player is already a member of the chat group
            if (chatGroup.getMembers().contains(playerId)) {
                // Set the player's default active chat group
                chatGroupManager.setPlayerActiveGroup(playerId, chatGroup.getId());
                sender.sendMessage("You have set your default chat group to " + chatGroup.getDisplayName() + ".");
            } else {
                sender.sendMessage("You are not a member of the chat group " + groupName + ".");
            }
        } else {
            sender.sendMessage("Chat group " + groupName + " not found.");
        }
    }
}
