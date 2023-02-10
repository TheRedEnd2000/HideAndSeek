package de.theredend2000.hideandseek;

import de.theredend2000.hideandseek.gamejoin.JoinGameEvent;
import de.theredend2000.hideandseek.gamestates.GameStateManager;
import de.theredend2000.hideandseek.gamejoin.JoinGameManager;
import de.theredend2000.hideandseek.menus.*;
import de.theredend2000.hideandseek.voting.Map;
import de.theredend2000.hideandseek.voting.Voting;
import de.theredend2000.hideandseek.voting.VotingListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public final class Main extends JavaPlugin {

    public static final String PREFIX = " §e[§4Hide§5and§2Seek§e] §r";

    public YamlConfiguration yaml;
    public File data = new File(getDataFolder(), "database.yml");

    private JoinGameManager joinGameManager;
    private GameStateManager gameStateManager;
    private CommandMessagesManager commandMessagesManager;
    private MenuManager mainMenuManager;
    private ArrayList<Player> gamePlayer;
    private ArrayList<Map> maps;
    private Voting voting;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.yaml = YamlConfiguration.loadConfiguration(this.data);
        this.saveData();

        gamePlayer = new ArrayList<>();
        initVoting();
        initManagers();
        initListeners();
        initCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void saveData() {
        try {
            this.yaml.save(this.data);
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }

    public void initVoting() {
        if(!yaml.contains("Arenas")){
            yaml.set("Arenas.Test.Builder","TestBuilder");
            saveData();
        }
        maps = new ArrayList<>();
        maps.clear();
            for (String current : yaml.getConfigurationSection("Arenas.").getKeys(false)) {
                Map map = new Map(this, current);
                if (map.playable())
                    maps.add(map);
            }
            if (maps.size() >= Voting.MAP_AMOUNT) {
                voting = new Voting(this, maps);
                Bukkit.broadcastMessage(String.valueOf(Voting.MAP_AMOUNT+maps.size()));
            }else {
                voting = null;
            }
    }

    private void initManagers(){
        joinGameManager = new JoinGameManager(this);
        gameStateManager = new GameStateManager(this);
        commandMessagesManager = new CommandMessagesManager(this);
        mainMenuManager = new MenuManager(this);
    }

    private void initListeners(){
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new JoinGameEvent(this),this);
        pluginManager.registerEvents(new VotingListener(this),this);
        pluginManager.registerEvents(new MenuListener(this),this);
    }

    private void initCommands(){
        getCommand("hideandseek").setExecutor(new MainCommand(this));
        getCommand("hideandseekadmin").setExecutor(new AdminCommand(this));
    }

    public JoinGameManager getJoinGameManager() {
        return joinGameManager;
    }

    public GameStateManager getGameStateManager() {
        return gameStateManager;
    }

    public CommandMessagesManager getCommandMessagesManager() {
        return commandMessagesManager;
    }

    public MenuManager getMenuManager() {
        return mainMenuManager;
    }

    public ArrayList<Player> getGamePlayer() {
        return gamePlayer;
    }

    public Voting getVoting() {
        return voting;
    }
}
