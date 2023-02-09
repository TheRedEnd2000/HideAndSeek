package de.theredend2000.hideandseek.gamestates;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.countdowns.LobbyCountdown;
import de.theredend2000.hideandseek.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Collections;

public class LobbyState extends GameState{

    private Main plugin;
    private LobbyCountdown lobbyCountdown;

    public static final int MAX_PLAYER = 10;


    public LobbyState(Main plugin, GameStateManager gameStateManager) {
        this.plugin = plugin;
        lobbyCountdown = new LobbyCountdown(plugin,gameStateManager);
    }


    @Override
    public void start() {
        lobbyCountdown.startIdle();
    }

    @Override
    public void stop() {
        ArrayList<String> map = new ArrayList<>();
        map.addAll(plugin.yaml.getConfigurationSection("Arenas.").getKeys(false));
        for(int i = 0; i < map.size(); i++) {
            Collections.shuffle(map);
            plugin.getMapManager().playingMap = map.get(0);
            Bukkit.broadcastMessage("Map: "+map);
            map.clear();
        }
    }

    public LobbyCountdown getLobbyCountdown() {
        return lobbyCountdown;
    }
}
