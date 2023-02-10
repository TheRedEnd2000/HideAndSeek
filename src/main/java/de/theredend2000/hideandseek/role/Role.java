package de.theredend2000.hideandseek.role;

import org.bukkit.ChatColor;

public enum Role{

    Hider("Unschuldiger", ChatColor.GREEN),
    Seeker("Detektive", ChatColor.RED);


    private Role(String name, ChatColor chatColor) {
        this.name = name;
        this.chatColor = chatColor;
    }

    private final String name;
    private ChatColor chatColor;


    public String getName() {
        return name;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }
}