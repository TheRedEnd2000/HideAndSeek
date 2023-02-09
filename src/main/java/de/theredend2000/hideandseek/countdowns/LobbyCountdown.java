package de.theredend2000.hideandseek.countdowns;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.gamestates.GameState;
import de.theredend2000.hideandseek.gamestates.GameStateManager;
import de.theredend2000.hideandseek.gamestates.LobbyState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;

public class LobbyCountdown extends Countdown{

    private static final int COUNDTOWN_TIME = 60,
            IDLE_TIME = 10;

    private GameStateManager gameStateManager;
    private int seconds;
    private int idleID;
    private boolean isRunning = false;
    private boolean isIdling = false;
    private Main plugin;

    public LobbyCountdown(Main plugin, GameStateManager gameStateManager) {
        this.plugin = plugin;
        this.gameStateManager = gameStateManager;
    }


    @Override
    public void start() {
        isRunning = true;
        seconds = 60;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                switch (seconds) {
                    case 60: case 30: case 15: case 10: case 5: case 4: case 3: case 2:
                        for(Player current : plugin.getGamePlayer()){
                            current.sendMessage(Main.PREFIX+ "§7Das Spiel startet in §a"+seconds+ " Sekunden§7.");
                        }
                        break;
                    case 1:
                        for(Player current : plugin.getGamePlayer()){
                            current.sendMessage(Main.PREFIX+ "§7Das Spiel startet in §a"+seconds+ " Sekunde§7.");
                        }
                        break;
                    case 0:
                        gameStateManager.setGameStates(GameState.INGAME_STATE);
                        stop();
                        for(Player current : plugin.getGamePlayer()){
                            current.sendMessage(Main.PREFIX+ "§7Start");
                        }
                        break;

                    default:
                        break;
                }
                seconds --;
            }
        },0,20);
    }

    @Override
    public void stop() {
        if(isRunning) {
            Bukkit.getScheduler().cancelTask(taskID);
            isRunning = false;
            seconds = COUNDTOWN_TIME;
        }
    }

    public void startIdle() {
        isIdling = true;
        idleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                for(Player current : plugin.getGamePlayer()){
                    current.sendMessage(Main.PREFIX+"§7Bis zum Start fehlt(en) noch §6"+
                            (plugin.getConfig().getInt("Settings.MinPlayerCount") - plugin.getGamePlayer().size())+" Spieler§7!");
                }
            }
        },0,20*IDLE_TIME);
    }

    public void stopIdle() {
        if(isIdling) {
            isIdling = false;
            Bukkit.getScheduler().cancelTask(idleID);
        }
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public boolean isIdling() {
        return isIdling;
    }
}
