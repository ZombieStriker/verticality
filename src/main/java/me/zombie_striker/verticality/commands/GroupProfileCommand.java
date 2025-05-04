package me.zombie_striker.verticality.commands;

import me.zombie_striker.verticality.VerticalityCore;
import me.zombie_striker.verticality.chat.ChatGroup;
import me.zombie_striker.verticality.chat.ChatGroupManager;
import me.zombie_striker.verticality.chat.ChatGroupPermProfile;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;

import java.util.UUID;

public class GroupProfileCommand extends Command {
    private final VerticalityCore verticalityCore;

    public GroupProfileCommand(VerticalityCore verticalityCore) {
        super("group_profile");
        this.verticalityCore = verticalityCore;

        var actionArg = ArgumentType.String("action");
        var profileNameArg = ArgumentType.String("profileName");
        var groupArg = ArgumentType.String("groupName");

        addSyntax((sender, context) -> {
            if (sender instanceof Player player) {
                String action = context.get(actionArg);
                String profileName = context.get(profileNameArg);
                String groupName = context.get(groupArg);
                execute(player.getUuid(), action, profileName, groupName, player);
            }
        }, actionArg, profileNameArg, groupArg);
    }

    public void execute(UUID playerId, String action, String profileName, String groupName, Player sender) {
        ChatGroupManager chatGroupManager = verticalityCore.getChatGroupManager();
        ChatGroup chatGroup = chatGroupManager.getGroupByName(groupName);

        if (chatGroup == null) {
            sender.sendMessage("Group " + groupName + " not found.");
            return;
        }

        switch (action.toLowerCase()) {
            case "create":
                ChatGroupPermProfile newProfile = new ChatGroupPermProfile(profileName);
                chatGroup.getProfiles().put(profileName, newProfile);
                sender.sendMessage("Profile " + profileName + " created in group " + groupName);
                break;

            case "delete":
                if (chatGroup.getProfiles().remove(profileName) != null) {
                    sender.sendMessage("Profile " + profileName + " deleted from group " + groupName);
                } else {
                    sender.sendMessage("Profile " + profileName + " not found in group " + groupName + ".");
                }
                break;
            case "set":
                sender.sendMessage("Permissions set for profile " + profileName + " in group " + groupName);
                break;

            default:
                sender.sendMessage("Unknown action: " + action);
                break;
        }
    }
}
