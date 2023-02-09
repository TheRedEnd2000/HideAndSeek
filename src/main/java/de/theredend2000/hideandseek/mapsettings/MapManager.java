package de.theredend2000.hideandseek.mapsettings;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.gamestates.LobbyState;
import de.theredend2000.hideandseek.util.ConfigLocationUtil;
import org.bukkit.Location;

public class MapManager {

    private Main plugin;
    private Location[] spawnLocations = new Location[LobbyState.MAX_PLAYER];
    private Location spectatorLocation;
    public String playingMap;

    public MapManager(Main plugin){
        this.plugin = plugin;
    }

    public void load(String name) {
        for(int i = 0; i < spawnLocations.length; i++)
            spawnLocations[i] = new ConfigLocationUtil(plugin, "Arenas."+name+"."+ (i + 1)).loadLocation();
        spectatorLocation = new ConfigLocationUtil(plugin, "Arenas."+name+".Spectator").loadLocation();
    }

    public void setSpawnLocation(int spawnNumber, Location location, String name) {
        spawnLocations[spawnNumber - 1] = location;
        new ConfigLocationUtil(plugin,location, "Arenas."+name+"."+spawnNumber).saveLocation();
    }

    public void setSpectatorLocation(Location location, String name) {
        spectatorLocation = location;
        new ConfigLocationUtil(plugin,location,"Arenas."+name+".Spectator").saveLocation();
    }

    public Location[] getSpawnLocations() {
        return spawnLocations;
    }

    public Location getSpectatorLocation() {
        return spectatorLocation;
    }

    public String getPlayingMap() {
        return playingMap;
    }
}
