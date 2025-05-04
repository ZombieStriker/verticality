package me.zombie_striker.verticality.chat;

import me.zombie_striker.verticality.VerticalityCore;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatGroupManager {
    private final VerticalityCore verticalityCore;
    private final Map<UUID, ChatGroup> groups;
    private final Map<UUID, UUID> playerChatGroupMap;

    public ChatGroupManager(VerticalityCore verticalityCore) {
        this.verticalityCore = verticalityCore;
        this.groups = new HashMap<>();
        this.playerChatGroupMap = new HashMap<>();
    }

    public ChatGroup createChatGroup(String groupName) {
        if (getGroupByName(groupName) != null) {
            return null; // Ensure group names are unique
        }

        UUID groupId = UUID.randomUUID();
        ChatGroup chatGroup = new ChatGroup(groupId, groupName);
        groups.put(groupId, chatGroup);
        return chatGroup;
    }

    public void addPlayerToGroup(UUID playerId, UUID groupId, String profileName) {
        ChatGroup chatGroup = groups.get(groupId);
        if (chatGroup != null && chatGroup.getProfiles().containsKey(profileName)) {
            chatGroup.addMember(playerId, profileName); // Only allow existing profiles
            playerChatGroupMap.put(playerId, groupId); // Track which group the player is in
        }
    }

    // Set the player's active chat group directly
    public void setPlayerActiveGroup(UUID playerId, UUID groupId) {
        playerChatGroupMap.put(playerId, groupId); // Update the player's default chat group
    }

    public void processChat(UUID playerId, String message) {
        UUID groupId = playerChatGroupMap.get(playerId);
        ChatGroup chatGroup = groups.get(groupId);
        if (chatGroup != null) {
            for (UUID recipientId : chatGroup.getMembers()) {
                sendMessage(recipientId, message);
            }
        }
    }

    public void processChat(UUID playerId, Component message) {
        UUID groupId = playerChatGroupMap.get(playerId);
        ChatGroup chatGroup = groups.get(groupId);
        if (chatGroup != null) {
            for (UUID recipientId : chatGroup.getMembers()) {
                sendMessage(recipientId, message);
            }
        }
    }

    private void sendMessage(UUID recipientId, String message) {
        Player recipient = verticalityCore.getInstanceManager().getPlayerByUUID(recipientId); // Get recipient player
        if (recipient != null) {
            recipient.sendMessage(message); // Send the message directly
        }
    }

    // New sendMessage method for Component type
    private void sendMessage(UUID recipientId, Component message) {
        Player recipient = verticalityCore.getInstanceManager().getPlayerByUUID(recipientId); // Get recipient player
        if (recipient != null) {
            recipient.sendMessage(message); // Send the message directly
        }
    }

    public ChatGroup getGroupById(UUID groupId) {
        return groups.get(groupId);
    }

    public ChatGroup getGroupByName(String name) {
        return groups.values().stream()
                .filter(group -> group.getDisplayName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void inviteToGroup(UUID targetPlayerId, String groupName, String profileName) {
        ChatGroup chatGroup = getGroupByName(groupName);
        if (chatGroup != null && targetPlayerId != null && chatGroup.getProfiles().containsKey(profileName)) {
            addPlayerToGroup(targetPlayerId, chatGroup.getId(), profileName);
        }
    }

    public Map<UUID, UUID> getPlayerChatGroupMap() {
        return playerChatGroupMap;
    }
}
