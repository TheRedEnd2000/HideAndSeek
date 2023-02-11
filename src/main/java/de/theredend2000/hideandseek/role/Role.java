package de.theredend2000.hideandseek.role;

import org.bukkit.Bukkit;

public enum Role{

    Hider("Hider"),
    Seeker("Seeker");


    final String name;
    private Role(String name2) {
        this.name = name2;
    }


    public String getName() {
        return name;
    }
}