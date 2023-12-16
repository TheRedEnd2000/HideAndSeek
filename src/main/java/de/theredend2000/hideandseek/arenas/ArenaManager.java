package de.theredend2000.hideandseek.arenas;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.game.GameManager;
import de.theredend2000.hideandseek.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArenaManager {

    private FileConfiguration arenaYaml;
    private File arenaFile;
    private final Main plugin;

    public ArenaManager() {
        plugin = Main.getPlugin();
        arenaFile = new File(Main.getPlugin().getDataFolder(), "arenas.yml");
        arenaYaml = YamlConfiguration.loadConfiguration(arenaFile);
    }

    public FileConfiguration getArenaYaml() {
        return arenaYaml;
    }

    public void saveArenaYaml() {
        try {
            arenaYaml.save(arenaFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAllArenas() {
        for (Arena arena : Main.getPlugin().getArenaManagerHashMap().values()) {
            arenaYaml.set("Arenas." + arena.getName() + ".lobbyLocation", arena.getLobbySpawn());
            arenaYaml.set("Arenas." + arena.getName() + ".specLocation", arena.getSpectatorSpawn());
            arenaYaml.set("Arenas." + arena.getName() + ".pos1",arena.getPos1());
            arenaYaml.set("Arenas." + arena.getName() + ".pos2",arena.getPos2());
            arenaYaml.set("Arenas." + arena.getName() + ".seekerWait",arena.getSeekerWait());
            arenaYaml.set("Arenas." + arena.getName() + ".seekerRelease",arena.getSeekerRelease());
            arenaYaml.set("Arenas." + arena.getName() + ".minPlayers",arena.getMinPlayers());
            arenaYaml.set("Arenas." + arena.getName() + ".maxPlayers",arena.getMaxPlayers());
            for(int i = 1; arena.getSpawns().length <= arena.getMaxPlayers(); i++) {
                arenaYaml.set("Arenas." + arena.getName() + ".spawns." + i, arena.getSpawns()[i]);
                Bukkit.broadcastMessage("set spawn "+i);
            }
            try {
                arenaYaml.save(arenaFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        saveArenaYaml();
    }

    public void loadAllArena() {
        if (!arenaYaml.contains("Arenas.")) {
            Bukkit.getConsoleSender().sendMessage("§cNo arenas found in the configuration file.");
            return;
        }

        ConfigurationSection arenasSection = arenaYaml.getConfigurationSection("Arenas.");
        if (arenasSection == null) {
            Bukkit.getConsoleSender().sendMessage("§c'Arenas' section not found in the configuration file.");
            return;
        }

        for (String arenaName : arenasSection.getKeys(false)) {
            String arenaPath = "Arenas." + arenaName;

            if (!arenaYaml.contains(arenaPath)) {
                Bukkit.getConsoleSender().sendMessage("§cArena data not found for arena: " + arenaName);
                continue;
            }

            boolean enabled = arenaYaml.getBoolean(arenaPath + ".enabled");

            if (!enabled) {
                Bukkit.getConsoleSender().sendMessage("§cInvalid location data for arena: " + arenaName);
                continue;
            }
            Location lobby = arenaYaml.getLocation(arenaPath + ".lobbyLocation");
            Location spec = arenaYaml.getLocation(arenaPath + ".specLocation");
            int maxPlayers = arenaYaml.getInt(arenaPath + ".maxPlayers");
            int minPlayers = arenaYaml.getInt(arenaPath + ".minPlayers");
            Location[] spawns = new Location[maxPlayers+1];
            for(int i = 1; i <= maxPlayers; i++)
                spawns[i] = arenaYaml.getLocation(arenaPath + ".spawns."+i);
            Location pos1 = arenaYaml.getLocation(arenaPath + ".pos1");
            Location pos2 = arenaYaml.getLocation(arenaPath + ".pos2");
            Location seekerWait =  arenaYaml.getLocation(arenaPath + ".seekerWait");
            Location seekerRelease =  arenaYaml.getLocation(arenaPath + ".seekerRelease");
            Arena arena = new Arena(arenaName, true,new ArrayList<>(),new ArrayList<>(),spawns,lobby,spec,pos1,pos2,seekerWait,seekerRelease,GameState.WAITING,minPlayers,maxPlayers);
            Main.getPlugin().getArenaManagerHashMap().put(arenaName, arena);
        }
    }

    /*public void updateArenas(){
        for (Arena arena : Main.getPlugin().getArenaManagerHashMap().values()) {
            arenaYaml.set("Arenas." + arena.getName() + ".name", arena.getName());
            arenaYaml.set("Arenas." + arena.getName() + ".endLocation", arena.getEndSpawn());
            arenaYaml.set("Arenas." + arena.getName() + ".lobbyLocation", arena.getLobbySpawn());
            arenaYaml.set("Arenas." + arena.getName() + ".spawn1", arena.getSpawn1());
            arenaYaml.set("Arenas." + arena.getName() + ".spawn2", arena.getSpawn2());
            arenaYaml.set("Arenas." + arena.getName() + ".pos1", arena.getPos1());
            arenaYaml.set("Arenas." + arena.getName() + ".pos2", arena.getPos2());
            arenaYaml.set("Arenas."+arena.getName()+".currentGameState",arena.getGameState());
            saveArenaYaml();
        }
        Main.getPlugin().getArenaManagerHashMap().clear();
        if (!arenaYaml.contains("Arenas.")) return;
        for (String arenaNames : arenaYaml.getConfigurationSection("Arenas.").getKeys(false)) {
            Location lobby = arenaYaml.getLocation("Arenas." + arenaNames + ".lobbyLocation");
            Location end = arenaYaml.getLocation("Arenas." + arenaNames + ".endLocation");
            Location spawn1 = arenaYaml.getLocation("Arenas." + arenaNames + ".spawn1");
            Location spawn2 = arenaYaml.getLocation("Arenas." + arenaNames + ".spawn2");
            Location pos1 = arenaYaml.getLocation("Arenas." + arenaNames + ".pos1");
            Location pos2 = arenaYaml.getLocation("Arenas." + arenaNames + ".pos2");
            Main.getPlugin().getArenaManagerHashMap().put(arenaNames, new Arena(arenaNames, canBeEnabled(arenaNames), new ArrayList<UUID>(), lobby,end,spawn1,spawn2,pos1,pos2, (canBeEnabled(arenaNames) ? (GameState.valueOf(arenaYaml.getString("Arenas."+arenaNames+".currentGameState")) == GameState.DISABLED ? GameState.WAITING : GameState.valueOf(arenaYaml.getString("Arenas."+arenaNames+".currentGameState"))) : GameState.DISABLED)));
            arenaYaml.set("Arenas."+arenaNames+".currentGameState",null);
            saveArenaYaml();
        }
    }*/

    public boolean playerIsAlreadyInArena(Player player){
        for(Arena arena : Main.getPlugin().getArenaManagerHashMap().values()){
            if(arena.getPlayerInGame().contains(player)){
                return true;
            }
        }
        return false;
    }

    public Arena getPlayerCurrentArena(Player player){
        for(Arena arena : Main.getPlugin().getArenaManagerHashMap().values()){
            if(arena.getPlayerInGame().contains(player)){
                return arena;
            }
        }
        return null;
    }

    public boolean isLocationWithinArea(Arena arena, Location location) {
        Location pos1 = arena.getPos1();
        Location pos2 = arena.getPos2();
        if(pos1 == null || pos2 == null)
            return false;

        double minX = Math.min(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }

    public boolean isWithinArena(Location location) {
        for (Arena arena : Main.getPlugin().getArenaManagerHashMap().values()) {
            if (Main.getPlugin().getArenaManager().isLocationWithinArea(arena, location)) {
                return true;
            }
        }
        return false;
    }

    public void removeEntitiesInArena(Arena arena) {
        Location pos1 = arena.getPos1();
        Location pos2 = arena.getPos2();
        World world = pos1.getWorld();
        double minX = Math.min(pos1.getX(), pos2.getX());
        double minY = Math.min(pos1.getY(), pos2.getY());
        double minZ = Math.min(pos1.getZ(), pos2.getZ());
        double maxX = Math.max(pos1.getX(), pos2.getX());
        double maxY = Math.max(pos1.getY(), pos2.getY());
        double maxZ = Math.max(pos1.getZ(), pos2.getZ());

        for (Entity entity : world.getEntities()) {
            Location entityLocation = entity.getLocation();
            double entityX = entityLocation.getX();
            double entityY = entityLocation.getY();
            double entityZ = entityLocation.getZ();

            if (entityX >= minX && entityX <= maxX && entityY >= minY && entityY <= maxY && entityZ >= minZ && entityZ <= maxZ) {
                if (entity.getType() != EntityType.PLAYER && entity.getType() != EntityType.ARMOR_STAND && entity.getType() != EntityType.ITEM_FRAME) {
                    entity.remove();
                }
            }
        }
    }

    public boolean playable(String arenaName){
        String arenaPath = "Arenas." + arenaName;
        if(arenaYaml.getLocation(arenaPath+".lobbyLocation") == null) return false;
        if(arenaYaml.getLocation(arenaPath+".specLocation") == null) return false;
        if(arenaYaml.contains(arenaPath+".minLocation")) return false;
        if(arenaYaml.contains(arenaPath+".maxLocation")) return false;
        if(arenaYaml.getLocation(arenaPath+".pos1") == null) return false;
        if(arenaYaml.getLocation(arenaPath+".pos2") == null) return false;
        if(arenaYaml.getLocation(arenaPath+".seekerWait") == null) return false;
        if(arenaYaml.getLocation(arenaPath+".seekerRelease") == null) return false;
        int maxPlayers = arenaYaml.getInt(arenaPath + ".maxPlayers");
        for(int i = 1; i <= maxPlayers; i++){
            if(arenaYaml.getLocation(arenaPath+".spawns."+i) == null) return false;
        }
        return true;
    }

    public boolean containsArena(String arenaName){
        return arenaYaml.contains("Arenas."+arenaName);
    }

    public void saveNewArena(String name) {
        arenaYaml.set("Arenas." + name + ".enabled", false);
        saveArenaYaml();
        addArenaSettings(name);
    }

    public void addArenaSettings(String name){
        arenaYaml.set("Arenas." + name + ".settings.starting-duration", 60);
        arenaYaml.set("Arenas." + name + ".settings.seeker-release-duration", 60);
        arenaYaml.set("Arenas." + name + ".settings.game-duration", 1200);
        arenaYaml.set("Arenas." + name + ".settings.ending-duration", 30);
        arenaYaml.set("Arenas." + name + ".settings.gamemode", "SURVIVAL".toUpperCase());
        arenaYaml.set("Arenas." + name + ".settings.scoreboard", true);
        arenaYaml.set("Arenas." + name + ".settings.firework", true);
        saveArenaYaml();
    }

    public int getStartingDuration(Arena arena){
        return arenaYaml.getInt("Arenas." + arena.getName() + ".settings.starting-duration");
    }
    public int getSeekerReleaseDuration(Arena arena){
        return arenaYaml.getInt("Arenas." + arena.getName() + ".settings.seeker-release-duration");
    }
    public int getGameDuration(Arena arena){
        return arenaYaml.getInt("Arenas." + arena.getName() + ".settings.game-duration");
    }
    public int getEndingDuration(Arena arena){
        return arenaYaml.getInt("Arenas." + arena.getName() + ".settings.ending-duration");
    }
    public GameMode getGamemode(Arena arena){
        return GameMode.valueOf(arenaYaml.getString("Arenas." + arena.getName() + ".settings.gamemode").toUpperCase());
    }
    public boolean allowScoreboard(Arena arena){
        return arenaYaml.getBoolean("Arenas." + arena.getName() + ".settings.scoreboard");
    }
    public boolean allowFirework(Arena arena){
        return arenaYaml.getBoolean("Arenas." + arena.getName() + ".settings.firework");
    }

    public Location getLobby(String arena){
        return arenaYaml.getLocation("Arenas."+arena+".lobbyLocation");
    }
    public Location getEnd(String arena){
        return arenaYaml.getLocation("Arenas."+arena+".lobbyLocation");
    }
    public Location getSeekerWait(String arena){
        return arenaYaml.getLocation("Arenas."+arena+".seekerWait");
    }
    public Location getSeekerRelease(String arena){
        return arenaYaml.getLocation("Arenas."+arena+".seekerRelease");
    }
    public Location getSpectator(String arena){
        return arenaYaml.getLocation("Arenas."+arena+".specLocation");
    }
    public Location getPos1(String arena){
        return arenaYaml.getLocation("Arenas."+arena+".pos1");
    }
    public Location getPos2(String arena){
        return arenaYaml.getLocation("Arenas."+arena+".pos2");
    }
    public int getMinPlayer(String name){
        return arenaYaml.getInt("Arenas."+ name + ".minPlayers");
    }

    public int getMaxPlayer(String name){
        return arenaYaml.getInt("Arenas."+ name + ".maxPlayers");
    }
    public List<Integer> getMaxPlayerList(String name){
        ArrayList<Integer> max = new ArrayList<>();
        int maxPlayers = arenaYaml.getInt("Arenas."+ name + ".maxPlayers");
        for(int i = 1; i <= maxPlayers; i++)
            max.add(i);
        return max;
    }

    public Location getSpawn(String name, int spawn){
        return arenaYaml.getLocation("Arenas."+name+".spawns."+spawn);
    }

    public List<Location> getSpawns(String name){
        List<Location> spawnsList = new ArrayList<>();
        for(String spawns : arenaYaml.getConfigurationSection("Arenas."+ name + ".spawns.").getKeys(false)){
            spawnsList.add(arenaYaml.getLocation("Arenas."+name+".spawns."+spawns));
        }
        return spawnsList;
    }

    public void removeArena(String name) {
        arenaYaml.set("Arenas." + name, null);
        saveArenaYaml();
    }

    public void setMinPlayers(String arena, int players){
        arenaYaml.set("Arenas."+arena+".minPlayers",players);
        saveArenaYaml();
        updateArena(arena);
    }

    public void setMaxPlayers(String arena, int players){
        arenaYaml.set("Arenas."+arena+".maxPlayers",players);
        saveArenaYaml();
        updateArena(arena);
    }

    public void setLobby(String arena, Location location){
        arenaYaml.set("Arenas."+arena+".lobbyLocation",location);
        saveArenaYaml();
        updateArena(arena);
    }

    public void setSpec(String arena, Location location){
        arenaYaml.set("Arenas."+arena+".specLocation",location);
        saveArenaYaml();
        updateArena(arena);
    }

    public void setPos1(String arena, Location location){
        arenaYaml.set("Arenas."+arena+".pos1",location);
        saveArenaYaml();
        updateArena(arena);
    }

    public void setPos2(String arena, Location location){
        arenaYaml.set("Arenas."+arena+".pos2",location);
        saveArenaYaml();
        updateArena(arena);
    }

    public void setSeekerWait(String arena, Location location){
        arenaYaml.set("Arenas."+arena+".seekerWait",location);
        saveArenaYaml();
        updateArena(arena);
    }

    public void setSeekerRelease(String arena, Location location){
        arenaYaml.set("Arenas."+arena+".seekerRelease",location);
        saveArenaYaml();
        updateArena(arena);
    }

    public void setSpawns(String arena, int spawn, Location location){
        arenaYaml.set("Arenas."+arena+".spawns."+spawn,location);
        saveArenaYaml();
        updateArena(arena);
    }

    public boolean isValidPlayerCount(int players, String name){
        int maxPlayer = plugin.getArenaManager().getMaxPlayer(name);
        return players >= 2 && players <= maxPlayer;
    }

    public boolean isValidPlayerCountForMaxPlayers(int players){
        return players >= 2 && players <= 24;
    }

    public void enableArena(String name){
        String arenaPath = "Arenas." + name;
        arenaYaml.set(arenaPath + ".enabled",true);
        saveArenaYaml();
        updateArena(name);
    }

    public void updateArena(String name){
        String arenaPath = "Arenas." + name;
        Location lobby = arenaYaml.getLocation(arenaPath + ".lobbyLocation");
        Location spec = arenaYaml.getLocation(arenaPath + ".specLocation");
        int maxPlayers = arenaYaml.getInt(arenaPath + ".maxPlayers");
        int minPlayers = arenaYaml.getInt(arenaPath + ".minPlayers");
        Location[] spawns = new Location[maxPlayers+1];
        for(int i = 1; i <= maxPlayers; i++)
            spawns[i] = arenaYaml.getLocation(arenaPath + ".spawns."+i);
        Location pos1 = arenaYaml.getLocation(arenaPath + ".pos1");
        Location pos2 = arenaYaml.getLocation(arenaPath + ".pos2");
        Location seekerWait =  arenaYaml.getLocation(arenaPath + ".seekerWait");
        Location seekerRelease =  arenaYaml.getLocation(arenaPath + ".seekerRelease");
        arenaYaml.set(arenaPath + ".enabled",true);
        saveArenaYaml();
        Arena arena = new Arena(name, true,new ArrayList<>(),new ArrayList<>(),spawns,lobby,spec,pos1,pos2,seekerWait,seekerRelease,GameState.WAITING,minPlayers,maxPlayers);
        plugin.getArenaManagerHashMap().put(name,arena);
    }

    public boolean isMinPlayersSet(String name){
        return arenaYaml.contains("Arenas."+ name + ".minPlayers");
    }

    public boolean isEnabled(String name){
        return !arenaYaml.getBoolean("Arenas."+name+".enabled");
    }
    public boolean isMaxPlayersSet(String name){
        return arenaYaml.contains("Arenas."+ name + ".maxPlayers");
    }

    public boolean isValidSpawnCount(String name, int spawn){
        int maxPlayers = arenaYaml.getInt("Arenas."+ name + ".maxPlayers");
        return spawn <= maxPlayers && spawn >= 1;
    }

    public Set<String> getAllArenas(){
        if(!arenaYaml.contains("Arenas.")) return null;
        return arenaYaml.getConfigurationSection("Arenas.").getKeys(false);
    }

    public Set<String> getAllEnabledArenas(){
        if(!arenaYaml.contains("Arenas.")) return null;
        Set<String> arenas = new HashSet<>();
        for(String arena : arenaYaml.getConfigurationSection("Arenas.").getKeys(false)){
            if(!isEnabled(arena)){
                arenas.add(arena);
            }
        }
        return arenas;
    }
}