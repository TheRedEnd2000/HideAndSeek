package de.theredend2000.hideandseek.voting;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.gamestates.LobbyState;
import de.theredend2000.hideandseek.util.ConfigLocationUtil;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class Map {

    private Main plugin;
    private String name;
    private String builder;
    private String players;
    private Location[] spawnLocations = new Location[LobbyState.MAX_PLAYER];
    private Location spectatorLocation;

    private int votes;

    public Map(Main plugin, String name) {
        this.plugin = plugin;
        this.name = name;

        if(exists())
            builder = plugin.yaml.getString("Arenas."+name+".Builder");
    }

    public void create(String builder) {
        this.builder = builder;
        plugin.yaml.set("Arenas."+ name+".Builder", builder);
        plugin.saveData();
    }

    public void load() {
        for(int i = 0; i < spawnLocations.length; i++)
            spawnLocations[i] = new ConfigLocationUtil(plugin, "Arenas."+name+"."+ (i + 1)).loadLocation();
        spectatorLocation = new ConfigLocationUtil(plugin, "Arenas."+name+".Spectator").loadLocation();
    }

    public boolean exists() {
        return (plugin.yaml.getString("Arenas." + name + ".Builder") != null);
    }

    public boolean playable() {
        if(!plugin.yaml.contains("Builder")) return false;
        for(int i = 1; i < LobbyState.MAX_PLAYER+ 1; i++) {
            if(!plugin.yaml.contains(Integer.toString(i))) return false;
        }
        return true;
    }

    public void setSpawnLocation(int spawnNumber, Location location) {
        spawnLocations[spawnNumber - 1] = location;
        new ConfigLocationUtil(plugin,location, "Arenas."+name+"."+spawnNumber).saveLocation();
    }

    public void setSpectatorLocation(Location location) {
        spectatorLocation = location;
        new ConfigLocationUtil(plugin,location,"Arenas."+name+".Spectator").saveLocation();
    }

    public void addVote() {
        votes++;
    }

    public void removeVote() {
        votes--;
    }

    public String getName() {
        return name;
    }

    public String getBuilder() {
        return builder;
    }

    public Location[] getSpawnLocations() {
        return spawnLocations;
    }

    public Location getSpectatorLocation() {
        return spectatorLocation;
    }

    public int getVotes() {
        return votes;
    }
}
