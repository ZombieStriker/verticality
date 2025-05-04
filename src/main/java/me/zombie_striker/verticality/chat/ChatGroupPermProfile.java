package me.zombie_striker.verticality.chat;

import java.util.EnumSet;

public class ChatGroupPermProfile {
    private final String name; // Profile name
    private final EnumSet<ChatPermissions> permissions; // Set of permissions for this profile

    public ChatGroupPermProfile(String name) {
        this.name = name;
        this.permissions = EnumSet.noneOf(ChatPermissions.class);
    }

    // Add permission to the profile
    public void addPermission(ChatPermissions permission) {
        permissions.add(permission);
    }

    // Add multiple permissions to the profile
    public void addPermissions(ChatPermissions... permissions) {
        for (ChatPermissions permission : permissions) {
            this.permissions.add(permission);
        }
    }

    // Remove permission from the profile
    public void removePermission(ChatPermissions permission) {
        permissions.remove(permission);
    }

    // Remove multiple permissions from the profile
    public void removePermissions(ChatPermissions... permissions) {
        for (ChatPermissions permission : permissions) {
            this.permissions.remove(permission);
        }
    }

    // Check if the profile has a specific permission
    public boolean hasPermission(ChatPermissions permission) {
        return permissions.contains(permission);
    }

    public String getName() {
        return name;
    }
}
