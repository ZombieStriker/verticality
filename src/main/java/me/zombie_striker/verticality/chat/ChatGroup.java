package me.zombie_striker.verticality.chat;

import java.util.*;

public class ChatGroup {
    private final UUID id;  // Unique identifier for the chat group
    private final String displayName; // Name of the chat group
    private final Set<UUID> members;  // Set of player UUIDs in the group
    private final Map<String, ChatGroupPermProfile> profiles; // Profiles for the chat group (by name)
    private final Map<UUID, String> playerProfiles; // Mapping of player UUIDs to profile names

    // Constructor
    public ChatGroup(UUID id, String displayName) {
        this.id = id;
        this.displayName = displayName;
        this.members = new HashSet<>();
        this.profiles = new HashMap<>();
        this.playerProfiles = new HashMap<>(); // Initialize the player profiles map

        // Initialize default profiles
        initializeDefaultProfiles();
    }

    /**
     * Initializes the default profiles upon creating a chat group.
     */
    private void initializeDefaultProfiles() {
        // Create the Owner profile with all permissions
        ChatGroupPermProfile ownerProfile = new ChatGroupPermProfile("Owner");
        ownerProfile.addPermissions(ChatPermissions.values()); // Assign all permissions
        profiles.put(ownerProfile.getName(), ownerProfile); // Store profile by name

        // Create the Admin profile with limited permissions
        ChatGroupPermProfile adminProfile = new ChatGroupPermProfile("Admin");
        adminProfile.addPermissions(ChatPermissions.INVITE, ChatPermissions.REVOKE);
        profiles.put(adminProfile.getName(), adminProfile); // Store profile by name

        // Create the Member profile (no specific permissions defined; just a basic profile)
        ChatGroupPermProfile memberProfile = new ChatGroupPermProfile("Member");
        profiles.put(memberProfile.getName(), memberProfile); // Store profile by name
    }

    // Add a member to the group with an existing profile
    public void addMember(UUID playerId, String profileName) {
        if (profiles.containsKey(profileName)) {
            members.add(playerId);
            playerProfiles.put(playerId, profileName); // Store the player's profile
        }
    }

    // Remove a member from the group
    public void removeMember(UUID playerId) {
        members.remove(playerId);
        playerProfiles.remove(playerId); // Remove profile record upon member removal
    }

    // Check if a player has the required permission through their profile
    public boolean hasPermission(UUID playerId, ChatPermissions permission) {
        String profileName = playerProfiles.getOrDefault(playerId, "Member"); // Get player's profile name or default to Member
        ChatGroupPermProfile profile = profiles.get(profileName);
        return profile != null && profile.hasPermission(permission);
    }

    /**
     * Creates and adds a new profile to the chat group.
     *
     * @param profileName The name of the profile to add.
     * @param permissions The permissions to assign to the new profile.
     */
    public void addProfile(String profileName, ChatPermissions... permissions) {
        ChatGroupPermProfile newProfile = new ChatGroupPermProfile(profileName);
        newProfile.addPermissions(permissions);
        profiles.put(profileName, newProfile);
    }

    /**
     * Assigns a profile to a player in the chat group.
     *
     * @param playerId    The UUID of the player.
     * @param profileName The name of the profile to assign.
     */
    public void setPlayerProfile(UUID playerId, String profileName) {
        if (profiles.containsKey(profileName)) {
            playerProfiles.put(playerId, profileName); // Logic to store the player's assigned profile
        } else {
            throw new IllegalArgumentException("Profile does not exist.");
        }
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Set<UUID> getMembers() {
        return new HashSet<>(members); // Return a copy to prevent external modification
    }

    public Map<String, ChatGroupPermProfile> getProfiles() {
        return new HashMap<>(profiles); // Return a copy to prevent external modification
    }
}
