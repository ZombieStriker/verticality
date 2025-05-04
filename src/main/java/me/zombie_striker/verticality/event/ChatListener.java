package me.zombie_striker.verticality.event;

import me.zombie_striker.verticality.VerticalityCore;
import me.zombie_striker.verticality.chat.ChatGroup;
import me.zombie_striker.verticality.chat.ChatGroupManager;
import me.zombie_striker.verticality.chat.ChatPermissions;
import me.zombie_striker.verticality.util.VComponentBuilder;
import net.kyori.adventure.text.Component;
import net.minestom.server.entity.Player;
import net.minestom.server.event.EventListener;
import net.minestom.server.event.player.PlayerChatEvent;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Listener to handle player chat messages.
 */
public class ChatListener implements EventListener<PlayerChatEvent> {

    private final ChatGroupManager chatGroupManager;
    private final VerticalityCore core;

    public ChatListener(VerticalityCore verticalityCore) {
        this.chatGroupManager = verticalityCore.getChatGroupManager();
        this.core = verticalityCore;
    }

    @Override
    public @NotNull Class<PlayerChatEvent> eventType() {
        return PlayerChatEvent.class;
    }

    @Override
    public @NotNull Result run(PlayerChatEvent event) {
        String message = event.getRawMessage();
        Player sender = event.getPlayer();
        UUID playerId = sender.getUuid();

        event.setCancelled(true);

        UUID groupId = chatGroupManager.getPlayerChatGroupMap().get(playerId);
        ChatGroup chatGroup = chatGroupManager.getGroupById(groupId);
        String chatGroupDisplayName = chatGroup != null ? chatGroup.getDisplayName() : "General"; // Default

        // Check if muted
        if (chatGroup != null && chatGroup.hasPermission(playerId, ChatPermissions.MUTED)) {
            sender.sendMessage("You are muted and cannot send messages.");
            return Result.SUCCESS;
        }

        String chatFormat = core.getSettingsManager().getSettings().getChatFormat();
        Component chatMessageComponent = VComponentBuilder.buildChatMessage(chatFormat, chatGroupDisplayName, sender.getUsername(), message);

        chatGroupManager.processChat(playerId, chatMessageComponent);
        return Result.SUCCESS;
    }
}
