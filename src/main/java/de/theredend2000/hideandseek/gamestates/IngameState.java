package de.theredend2000.hideandseek.gamestates;

import de.theredend2000.hideandseek.Main;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class IngameState extends GameState{
    private Main plugin;
    private ArrayList<Player> players;
    public IngameState(Main plugin, GameStateManager gameStateManager) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        teleportAllPlayers();
    }

    @Override
    public void stop() {

    }

    private void teleportAllPlayers(){
        plugin.getMapManager().load(plugin.getMapManager().getPlayingMap());
        players = plugin.getGamePlayer();
        for(int i = 0; i < players.size(); i++)
            players.get(i).teleport(plugin.getMapManager().getSpawnLocations()[i]);
    }
}
