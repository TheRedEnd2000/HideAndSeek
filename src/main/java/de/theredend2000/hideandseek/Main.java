package de.theredend2000.hideandseek;

import de.theredend2000.hideandseek.arenas.Arena;
import de.theredend2000.hideandseek.arenas.ArenaManager;
import de.theredend2000.hideandseek.commands.ArenaCommands;
import de.theredend2000.hideandseek.countdowns.ArenaDurationCountdown;
import de.theredend2000.hideandseek.countdowns.ArenaEndCountdown;
import de.theredend2000.hideandseek.countdowns.ArenaWaitingCountdown;
import de.theredend2000.hideandseek.game.GameManager;
import de.theredend2000.hideandseek.listeners.GameListeners;
import de.theredend2000.hideandseek.messages.MessageManager;
import de.theredend2000.hideandseek.placeholder.PlaceholderExtension;
import de.theredend2000.hideandseek.role.RoleManager;
import de.theredend2000.hideandseek.signs.SignAdmin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.mineacademy.fo.plugin.SimplePlugin;

import java.io.*;
import java.util.HashMap;

public final class Main extends SimplePlugin {
    private static Main plugin;
    public static final String PREFIX = "§f[§aHide§eAnd§2Seek§f] §r";
    private HashMap<String, Arena> arenaManagerHashMap;
    private ArenaManager arenaManager;
    private MessageManager messageManager;
    private ArenaWaitingCountdown arenaWaitingCountdown;
    private ArenaDurationCountdown arenaDurationCountdown;
    private ArenaEndCountdown arenaEndCountdown;
    private GameManager gameManager;
    private RoleManager roleManager;
    private FileConfiguration signs;
    private File signsFile;

    @Override
    public void onPluginStart() {
        plugin = this;
        //Metrics metrics = new Metrics(this,	19494);
        Bukkit.getServer().getScheduler().runTask(Main.getPlugin(), () -> {
            Bukkit.getConsoleSender().sendMessage("\n");
            Bukkit.getConsoleSender().sendMessage("§6§lHIDE AND SEEK by XMC-Plugins v" + getDescription().getVersion());
            Bukkit.getConsoleSender().sendMessage("\n");
            initConfiguration();
            initManagers();
            initCommands();
            initListeners();
            initExtras();
            //initBannedWorlds();
            arenaManager.loadAllArena();
            checkPlaceholderAPI();
        });
    }

    @Override
    protected void onPluginStop() {
        // Plugin shutdown logic
    }

    private void checkPlaceholderAPI(){
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX+"§aDuels detected PlaceholderAPI, enabling placeholders.");
            new PlaceholderExtension().register();
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX+"§2§lAll placeholders successfully enabled.");
        }
    }

    private void initCommands(){
        getCommand("hideandseek").setExecutor(new ArenaCommands(this));
        //getCommand("hideandseek").setExecutor(new GameCommands(this));
    }

    private void initListeners(){
        PluginManager pluginManager = Bukkit.getPluginManager();
       pluginManager.registerEvents(new GameListeners(this),this);
    }

    private void initManagers(){
        messageManager = new MessageManager();
        arenaManager = new ArenaManager();
        gameManager = new GameManager();
        roleManager = new RoleManager(this);
    }

    private void initConfiguration(){
        this.saveDefaultConfig();
        registerSigns();
        SignAdmin signs = new SignAdmin(this);
        signs.actualizarSigns();
        //checkUpdatePath();
    }
    private void initExtras(){
        arenaManagerHashMap = new HashMap<>();
        arenaWaitingCountdown = new ArenaWaitingCountdown();
        arenaDurationCountdown = new ArenaDurationCountdown();
        arenaEndCountdown = new ArenaEndCountdown();
    }


    public void registerSigns(){
        signsFile = new File(this.getDataFolder(), "signs.yml");
        if(!signsFile.exists()){
            this.getSigns().options().copyDefaults(true);
            saveSigns();
        }
    }
    public void saveSigns() {
        try {
            signs.save(signsFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getSigns() {
        if (signs == null) {
            reloadSigns();
        }
        return signs;
    }

    public void reloadSigns() {
        if (signs == null) {
            signsFile = new File(getDataFolder(), "signs.yml");
        }
        signs = YamlConfiguration.loadConfiguration(signsFile);

        Reader defConfigStream;
        try {
            defConfigStream = new InputStreamReader(this.getResource("signs.yml"), "UTF8");
            if (defConfigStream != null) {
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                signs.setDefaults(defConfig);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }

    public HashMap<String, Arena> getArenaManagerHashMap() {
        return arenaManagerHashMap;
    }

    public static Main getPlugin() {
        return plugin;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public ArenaWaitingCountdown getArenaWaitingCountdown() {
        return arenaWaitingCountdown;
    }

    public ArenaEndCountdown getArenaEndCountdown() {
        return arenaEndCountdown;
    }

    public ArenaDurationCountdown getArenaDurationCountdown() {
        return arenaDurationCountdown;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public RoleManager getRoleManager() {
        return roleManager;
    }
}
