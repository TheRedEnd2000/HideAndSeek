package de.theredend2000.hideandseek.gamestates;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.countdowns.GameTimeCountdown;
import de.theredend2000.hideandseek.countdowns.HiderRunningCountdown;
import de.theredend2000.hideandseek.role.Role;
import de.theredend2000.hideandseek.role.RoleManager;
import de.theredend2000.hideandseek.util.ConfigLocationUtil;
import de.theredend2000.hideandseek.voting.Map;
import de.theredend2000.hideandseek.voting.Voting;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collections;

public class IngameState extends GameState{

    private Main plugin;
    private GameStateManager gameStateManager;
    private HiderRunningCountdown hiderRunningCountdown;
    private GameTimeCountdown gameTimeCountdown;
    private Map map;
    private ArrayList<Player> players, spectators;
    private boolean grace;

    public IngameState(Main plugin, GameStateManager gameStateManager) {
        this.plugin = plugin;;
        spectators = new ArrayList<>();
        this.gameStateManager = gameStateManager;
        hiderRunningCountdown = new HiderRunningCountdown(plugin);
        gameTimeCountdown = new GameTimeCountdown(plugin);
    }

    @Override
    public void start() {
        grace = true;
        players = plugin.getGamePlayer();
        Collections.shuffle(players);

        map = plugin.getVoting().getWinnerMap();
        map.load();
        for(int i = 0; i < players.size(); i++) {
            Role playerRole = plugin.getRoleManager().getPlayerRole(players.get(i));
            if(playerRole == Role.Hider) {
                players.get(i).teleport(map.getSpawnLocations()[i]);
            }
        }
        for(Player current : players) {
            current.setGameMode(GameMode.SURVIVAL);
            current.setHealth(20);
            current.setFoodLevel(20);
            current.setAllowFlight(false);
            current.setFlying(false);
            current.getInventory().clear();
        }
        hiderRunningCountdown.start();
        gameTimeCountdown.start();

        plugin.getRoleManager().calculateRoles();

        ArrayList<String> seekerPlayers = plugin.getRoleManager().getSeekerPlayers();
        for(Player current : plugin.getGamePlayer()) {
            Role playerRole = plugin.getRoleManager().getPlayerRole(current);
            if(playerRole == Role.Seeker) {
                current.sendTitle("" + playerRole.getName(), "§7Find and kill the hider.");
                current.sendMessage(Main.PREFIX + "§7Die Seeker sind: §c§l" + String.join(",", seekerPlayers.toString()));
                current.teleport(map.getSeekerWaitLocation());
            }else if(playerRole == Role.Hider){
                current.sendTitle("" + playerRole.getName(), "§7Stay hidden as long as possible.");
                for(int i = 0; i < plugin.getRoleManager().hider; i++)
                    current.teleport(map.getSpawnLocations()[i]);
            }
        }
    }



    public void checkGameEnding() {
        RoleManager roleManager = plugin.getRoleManager();
        if(roleManager.getHiderPlayers().size() == 0 || gameTimeCountdown.getSeconds() == 0){
            plugin.getGameStateManager().setGameStates(GameState.ENDING_STATE);
            for(Player player : plugin.getGamePlayer()){
                player.sendMessage("§6");
            }
        }
    }

    public void addSpectator(Player player) {
        map.load();
        spectators.add(player);
        player.setGameMode(GameMode.SURVIVAL);
        player.teleport(map.getSpectatorLocation());
        ConfigLocationUtil locationUtil = new ConfigLocationUtil(plugin, "spectator");
        if (locationUtil.loadLocation() != null) {
            player.teleport(locationUtil.loadLocation());
            player.teleport(map.getSpectatorLocation());
        }
    }

    @Override
    public void stop() {

    }

    public void setGrace(boolean grace) {
        this.grace = grace;
    }

    public boolean isInGrace() {
        return grace;
    }

    public ArrayList<Player> getSpectators() {
        return spectators;
    }

    public Map getMap() {
        return map;
    }
}
