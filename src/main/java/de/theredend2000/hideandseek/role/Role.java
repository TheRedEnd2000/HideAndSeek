package de.theredend2000.hideandseek.role;

import org.bukkit.ChatColor;

public enum Role {

    SEEKER("Seeker", ChatColor.RED),
    HIDER("Hider", ChatColor.GREEN),
    SPECTATOR("Spectator",ChatColor.GRAY);

    private Role(String name, ChatColor chatColor){
        this.name = name;
        this.chatColor = chatColor;
    }

    private String name;
    private ChatColor chatColor;

    public String getName() {
        return name;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }
}
