package de.theredend2000.hideandseek.role;

import org.bukkit.ChatColor;

public enum Role{

    Hider("Hider"),
    Seeker("Seeker");


    private Role(String name) {
        this.name = name;
    }

    private final String name;


    public String getName() {
        return name;
    }
}