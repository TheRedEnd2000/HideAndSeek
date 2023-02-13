package de.theredend2000.hideandseek.gamestates;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.countdowns.LobbyCountdown;
import de.theredend2000.hideandseek.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

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

    public void updateScoreboard(){
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("lobby","lobby");
        objective.setDisplayName("§4Hide and Seek");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore("§1").setScore(10);
        objective.getScore("§7Start in:").setScore(9);
        if(lobbyCountdown.isRunning()) {
            objective.getScore("§6" + lobbyCountdown.getSeconds() + " Sekunden").setScore(8);
        }else
            objective.getScore("§6Waiting...").setScore(8);
        objective.getScore("§a").setScore(7);
        objective.getScore("§7Spieler:").setScore(6);
        objective.getScore("§6"+plugin.getGamePlayer().size()+"§b/§6"+LobbyState.MAX_PLAYER).setScore(5);
        objective.getScore("§b").setScore(4);
        objective.getScore("§7Spiel Startet wenn").setScore(3);
        objective.getScore("§6"+(plugin.getConfig().getInt("Settings.MinPlayerCount")- plugin.getGamePlayer().size())+" Spieler§7 mehr join").setScore(2);
        objective.getScore("§2").setScore(1);
        objective.getScore("§aPlugin §7by §6TheRedEnd2000").setScore(0);
        for(Player current : plugin.getGamePlayer())
            current.setScoreboard(board);
    }


    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    public LobbyCountdown getLobbyCountdown() {
        return lobbyCountdown;
    }
}
