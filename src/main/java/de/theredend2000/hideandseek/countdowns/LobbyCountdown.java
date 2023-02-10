package de.theredend2000.hideandseek.countdowns;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.gamestates.GameState;
import de.theredend2000.hideandseek.gamestates.GameStateManager;
import de.theredend2000.hideandseek.gamestates.LobbyState;
import de.theredend2000.hideandseek.role.Role;
import de.theredend2000.hideandseek.voting.Map;
import de.theredend2000.hideandseek.voting.Voting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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

                        if(seconds == 3) {
                            Voting voting = gameStateManager.getPlugin().getVoting();
                            Map winningMap;
                            if(voting != null)
                                winningMap = voting.getWinnerMap();
                            else {
                                ArrayList<Map> maps = gameStateManager.getPlugin().getMaps();
                                Collections.shuffle(maps);
                                winningMap = maps.get(0);
                            }
                            for(Player current : plugin.getGamePlayer()){
                                current.sendMessage(Main.PREFIX+"§7Der §aSieger §7des Votings mit §6"+winningMap.getVotes()+" Vote(s) §7ist die Map §6"+winningMap.getName());
                            }
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
                        for(Player player : plugin.getGamePlayer()) {
                        Voting voting = plugin.getVoting();
                        if (voting.getPlayerVotes().containsKey(player.getName())) {
                            voting.getVotingMaps()[voting.getPlayerVotes().get(player.getName())].removeVote();
                            voting.getPlayerVotes().remove(player.getName());
                            voting.initVotingInventory();
                        }

                            plugin.getRoleManager().calculateRoles(plugin.getGamePlayer().size());

                            ArrayList<String> seekerPlayers = plugin.getRoleManager().getSeekerPlayers();
                            for(Player current : plugin.getGamePlayer()) {
                                Role playerRole = plugin.getRoleManager().getPlayerRole(current);
                                current.sendMessage("§7----------§c§k524ghr4uth5h6u7hg43h5jgh§7----------");
                                current.sendMessage(Main.PREFIX + "§7Deine Rolle ist: §l" + playerRole.getChatColor() + playerRole.getName());
                                current.sendMessage("§7----------§c§k524ghr4uth5h6u7hg43h5jgh§7----------");
                                current.setDisplayName(playerRole.getChatColor() + current.getName());

                                if (playerRole == Role.Seeker) {
                                    current.sendMessage(Main.PREFIX + "§7Die Seeker sind: §c§l" + String.join(",", seekerPlayers));
                                    ItemStack sword = new ItemStack(Material.IRON_SWORD);
                                    ItemMeta swordMeta = sword.getItemMeta();
                                    swordMeta.setUnbreakable(true);
                                    swordMeta.setDisplayName("§cMurder Sword");
                                    swordMeta.addEnchant(Enchantment.DAMAGE_ALL, 1000, true);
                                    sword.setItemMeta(swordMeta);
                                    current.getInventory().setItem(1, sword);
                                }
                            }

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
