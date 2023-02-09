package de.theredend2000.hideandseek;

import de.theredend2000.hideandseek.gamejoin.JoinGameEvent;
import de.theredend2000.hideandseek.gamestates.GameStateManager;
import de.theredend2000.hideandseek.gamejoin.JoinGameManager;
import de.theredend2000.hideandseek.menus.CommandMessagesManager;
import de.theredend2000.hideandseek.menus.MainCommand;
import de.theredend2000.hideandseek.menus.MainMenuManager;
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
    private MainMenuManager mainMenuManager;
    private ArrayList<Player> gamePlayer;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.yaml = YamlConfiguration.loadConfiguration(this.data);
        this.saveData();

        gamePlayer = new ArrayList<>();
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


    private void initManagers(){
        joinGameManager = new JoinGameManager(this);
        gameStateManager = new GameStateManager(this);
        commandMessagesManager = new CommandMessagesManager(this);
        mainMenuManager = new MainMenuManager(this);
    }

    private void initListeners(){
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new JoinGameEvent(this),this);
    }

    private void initCommands(){
        getCommand("hideandseek").setExecutor(new MainCommand(this));
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

    public MainMenuManager getMainMenuManager() {
        return mainMenuManager;
    }

    public ArrayList<Player> getGamePlayer() {
        return gamePlayer;
    }

}
