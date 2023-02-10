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

    }

    public LobbyCountdown getLobbyCountdown() {
        return lobbyCountdown;
    }
}
