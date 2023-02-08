package de.theredend2000.hideandseek;

import de.theredend2000.hideandseek.gamejoin.JoinGameEvent;
import de.theredend2000.hideandseek.gamestates.GameStateManager;
import de.theredend2000.hideandseek.gamejoin.JoinGameManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class Main extends JavaPlugin {

    public static final String PREFIX = " §e[§4Hide§5and§2Seek§e] §r";

    private JoinGameManager joinGameManager;
    private GameStateManager gameStateManager;
    private ArrayList<Player> gamePlayer;


    @Override
    public void onEnable() {
        saveDefaultConfig();

        gamePlayer = new ArrayList<>();
        initManagers();
        initListeners();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initManagers(){
        joinGameManager = new JoinGameManager(this);
        gameStateManager = new GameStateManager(this);
    }

    private void initListeners(){
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new JoinGameEvent(this),this);
    }

    public JoinGameManager getJoinGameManager() {
        return joinGameManager;
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public ArrayList<Player> getGamePlayer() {
        return gamePlayer;
    }
}
