package de.theredend2000.hideandseek.gamestates;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.countdowns.EndingCountdown;
import de.theredend2000.hideandseek.countdowns.GameTimeCountdown;
import de.theredend2000.hideandseek.countdowns.HiderRunningCountdown;
import de.theredend2000.hideandseek.menus.MenuManager;
import de.theredend2000.hideandseek.role.Role;
import de.theredend2000.hideandseek.role.RoleManager;
import de.theredend2000.hideandseek.util.ConfigLocationUtil;
import de.theredend2000.hideandseek.voting.Map;
import de.theredend2000.hideandseek.voting.Voting;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class IngameState extends GameState{

    private Main plugin;
    private GameStateManager gameStateManager;
    private HiderRunningCountdown hiderRunningCountdown;
    private GameTimeCountdown gameTimeCountdown;
    private Map map;
    private ArrayList<Player> players, spectators;
    private HashMap<Player, Integer> hiderLives;
    private EndingCountdown endingCountdown;
    private boolean grace;

    public IngameState(Main plugin, GameStateManager gameStateManager) {
        this.plugin = plugin;;
        spectators = new ArrayList<>();
        this.gameStateManager = gameStateManager;
        hiderRunningCountdown = new HiderRunningCountdown(plugin);
        gameTimeCountdown = new GameTimeCountdown(plugin);
        hiderLives = new HashMap<>();
        endingCountdown = new EndingCountdown(plugin);
    }

    @Override
    public void start() {
        grace = true;
        players = plugin.getGamePlayer();
        Collections.shuffle(players);

        map = plugin.getVoting().getWinnerMap();
        map.load();

        for(Player current : players) {
            current.setGameMode(GameMode.SURVIVAL);
            current.setHealthScale(20);
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
            ingameScoreboard(current);
            if(playerRole == Role.Seeker) {
                current.sendTitle("" + playerRole.getName(), "§7Find and kill the hider.");
                current.sendMessage(Main.PREFIX + "§7Die Seeker sind: §c§l" + String.join(",", seekerPlayers.toString()));
                current.teleport(map.getSeekerWaitLocation());
            }else if(playerRole == Role.Hider){
                current.sendTitle("" + playerRole.getName(), "§7Stay hidden as long as possible.");
                hiderLives.put(current,3);
                sendHiderBar(current);
                for(int i = 0; i < plugin.getRoleManager().hider; i++) {
                    current.teleport(map.getSpawnLocations()[i]);
                    Bukkit.getConsoleSender().sendMessage("Teleported: "+current.getName()+" to "+map.getSpawnLocations()[i]);
                }
            }
        }
    }

    private void sendHiderBar(Player hider){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState) {
                            int lives = hiderLives.get(hider);
                            if (lives == 1) {
                                hider.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§6Lives left: §4❤§7❤❤"));
                            }
                            if (lives == 2) {
                                hider.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§6Lives left: §4❤❤§7❤"));
                            }
                            if (lives == 3) {
                                hider.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§6Lives left: §4❤❤❤"));
                            }
                        }else{
                            cancel();
                        }
                    }
                }.runTaskTimer(plugin,0,5);
            }



    public void checkGameEnding() {
        RoleManager roleManager = plugin.getRoleManager();
        if(gameTimeCountdown.getSeconds() == 0){
            plugin.getGameStateManager().setGameStates(GameState.ENDING_STATE);
            endingCountdown.start();
            for(Player player : plugin.getGamePlayer()){
                player.sendMessage("§6Winner Hider");
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(map.getSpectatorLocation());
            }
        }
        if(roleManager.getHiderPlayers().size() == 0){
            plugin.getGameStateManager().setGameStates(GameState.ENDING_STATE);
            endingCountdown.start();
            for(Player player : plugin.getGamePlayer()){
                player.sendMessage("§6Winner Seeker");
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(map.getSpectatorLocation());
            }
        }
        if(roleManager.getSeekerPlayers().size() == 0){
            plugin.getGameStateManager().setGameStates(GameState.ENDING_STATE);
            endingCountdown.start();
            for(Player player : plugin.getGamePlayer()){
                player.sendMessage("§6Winner Hider");
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(map.getSpectatorLocation());
            }
        }
    }

    public void ingameScoreboard(Player player){
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = board.registerNewObjective("ingame","ingame");
        objective.setDisplayName("§4Hide and Seek");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.getScore("§a").setScore(10);
        objective.getScore("§7Deine Rolle:").setScore(9);
        Role role = plugin.getRoleManager().getPlayerRole(player);
        objective.getScore(role.getName()).setScore(8);
        objective.getScore("§b").setScore(7);
        objective.getScore("§7Time left:").setScore(6);
        objective.getScore("§6"+ MenuManager.shortInteger(gameTimeCountdown.getSeconds())).setScore(5);
        objective.getScore("§l").setScore(4);
        objective.getScore("§7Verbleibende Hider: §6"+plugin.getRoleManager().getHiderPlayers().size()).setScore(3);
        if(isInGrace()){
            objective.getScore("§7Seeker freilassung in: §6"+hiderRunningCountdown.getSeconds()+" Sekunden.").setScore(2);
        }else{
            objective.getScore("§7Seeker verbleiben: §6"+plugin.getRoleManager().getSeekerPlayers().size()).setScore(2);
        }
        objective.getScore("§2").setScore(1);
        objective.getScore("§aPlugin §7by §6TheRedEnd2000").setScore(0);
        player.setScoreboard(board);
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

    public HashMap<Player, Integer> getHiderLives() {
        return hiderLives;
    }

}
