package de.theredend2000.hideandseek.role;

import org.bukkit.Bukkit;

public enum Role{

    Hider("§2Hider"),
    Spectator("§3Spectator"),
    Seeker("§4Seeker");


    public String name;
    private Role(String name2) {
        this.name = name2;
        //Delete
    }


    public String getName() {
        return name;
    }
}