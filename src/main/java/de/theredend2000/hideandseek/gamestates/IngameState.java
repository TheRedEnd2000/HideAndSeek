package de.theredend2000.hideandseek.gamestates;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.countdowns.HiderRunningCountdown;
import de.theredend2000.hideandseek.role.RoleManager;
import de.theredend2000.hideandseek.util.ConfigLocationUtil;
import de.theredend2000.hideandseek.voting.Map;
import de.theredend2000.hideandseek.voting.Voting;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collections;

public class IngameState extends GameState{

    private Main plugin;
    private GameStateManager gameStateManager;
    private HiderRunningCountdown hiderRunningCountdown;
    private Map map;
    private ArrayList<Player> players, spectators;
    private boolean grace;

    public IngameState(Main plugin, GameStateManager gameStateManager) {
        this.plugin = plugin;;
        spectators = new ArrayList<>();
        this.gameStateManager = gameStateManager;
        hiderRunningCountdown = new HiderRunningCountdown(plugin);
    }

    @Override
    public void start() {
        grace = true;
        players = plugin.getGamePlayer();
        Collections.shuffle(players);
        RoleManager roleManager = plugin.getRoleManager();

        map = plugin.getVoting().getWinnerMap();
        map.load();
        for(int i = 0; i < plugin.getRoleManager().getHiderPlayers().size(); i++) {
            for(Player player : plugin.getRoleManager().getHiderPlayers()) {
                player.teleport(map.getSpawnLocations()[i]);
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
    }



    public void checkGameEnding() {

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
        return;
    }

    @Override
    public void stop() {
        for(Player current : plugin.getGamePlayer()) {
            current.sendMessage("");
            current.sendMessage(Main.PREFIX + "§6Das Spiel ist vorbei! §7Die Gewinner sind: §6§l");
            current.sendMessage(Main.PREFIX+"§7Spielzeit: §6");
            current.sendMessage("");
            current.getInventory().clear();
            current.sendTitle("§6Spiel vorbei!", "§7Winner: ");
        }

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
