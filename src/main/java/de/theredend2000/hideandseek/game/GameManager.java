package de.theredend2000.hideandseek.game;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.arenas.Arena;
import de.theredend2000.hideandseek.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import java.util.*;
import java.util.stream.Collectors;

public class GameManager {

    private Main plugin;

    public GameManager(){
        this.plugin = Main.getPlugin();
    }

    public void joinArena(Player player, Arena arena){
        //Main.getPlugin().getPlayerSavesManager().savePlayer(sender);
        player.teleport(arena.getLobbySpawn());
        showAllPlayers(arena);
        player.setLevel(0);
        player.setExp(0);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.setGameMode(GameMode.valueOf(Main.getPlugin().getConfig().getString("game.gamemode")));
        plugin.getRoleManager().addTeam(player);
        arena.addPlayerInGame(player);
        regeneratePlayer(player);
        for(Player players : arena.getPlayerInGame()){
            players.sendMessage(Main.PREFIX+"§a"+player.getDisplayName()+"§7 joined the game. §f[§e"+arena.getPlayerInGame().size()+"§f/§e"+arena.getMaxPlayers()+"§f]");
        }
        if(arena.getPlayerInGame().size() >= arena.getMinPlayers() && !plugin.getArenaWaitingCountdown().isRunning()) {
            arena.setGameState(GameState.STARTING);
            plugin.getArenaWaitingCountdown().addStartingCountdownForArena(arena);
            player.sendMessage("start");
        }
    }

    public void leaveGame(Player player,Arena arena){
        player.sendMessage("You left the arena");
        player.setFlying(false);
        player.setAllowFlight(false);
        regeneratePlayer(player);
        showAllPlayers(arena);
        plugin.getRoleManager().removeTeam(player);
        arena.getPlayerInGame().remove(player);
        arena.getSpectators().remove(player);
        plugin.getRoleManager().removePlayerRole(player);
        player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        //Main.getPlugin().getPlayerSavesManager().loadPlayer(player);
        for (PotionEffect effect : player.getActivePotionEffects())
            player.removePotionEffect(effect.getType());
        for(Player players : arena.getPlayerInGame()) {
            if (arena.getGameState() == GameState.WAITING)
                players.sendMessage(Main.PREFIX + "§a" + player.getDisplayName() + "§7 left the game. §f[§e" + arena.getPlayerInGame().size() + "§f/§e" + arena.getMaxPlayers() + "§f]");
            else
                players.sendMessage(Main.PREFIX+"§a"+player.getDisplayName()+"§7 left the game.");
        }
        if(arena.getPlayerInGame().size() < arena.getMinPlayers() && (arena.getGameState() == GameState.STARTING || arena.getGameState() == GameState.WAITING)) {
            arena.setGameState(GameState.WAITING);
            plugin.getArenaWaitingCountdown().setRunning(false);
        }else
            checkGameEnd(arena);
    }

    public void startGame(Arena arena){
        arena.setGameState(GameState.RUNNING);
        plugin.getRoleManager().calculateRoles(arena);
        for(Player player : arena.getPlayerInGame()){
            player.sendMessage("Your Role: "+plugin.getRoleManager().getPlayerRole(player));
        }
        Collections.shuffle(arena.getPlayerInGame());
        for(int i = 0; i < arena.getPlayerInGame().size(); i++){
            Player current = arena.getPlayerInGame().get(i);
            if(plugin.getRoleManager().getPlayerRole(current) == Role.HIDER)
                current.teleport(arena.getSpawns()[(i+1)]);
            else
                current.teleport(arena.getSeekerWait());
        }
    }

    public void releaseSeeker(Arena arena){
        for(Player player : arena.getPlayerInGame()){
            if(plugin.getRoleManager().getPlayerRole(player) == Role.SEEKER)
                player.teleport(arena.getSeekerRelease());
            player.sendMessage("seeker released");
        }
    }

    public void findPlayer(Player hider, Player seeker, Arena arena){
        arena.getPlayerInGame().remove(hider);
        setSpectator(hider,arena);
        for(Player player : arena.getPlayerInGame()){
            player.sendMessage("The Seeker "+seeker.getDisplayName()+" found "+hider.getDisplayName()+". ");
            player.sendMessage("Hiders remaining: "+getRemainingHiders(arena));
        }
        checkGameEnd(arena);
    }

    public void setSpectator(Player player, Arena arena){
        arena.getSpectators().add(player);
        player.teleport(arena.getSpectatorSpawn());
        plugin.getRoleManager().removePlayerRole(player);
        plugin.getRoleManager().setPlayerRole(player,Role.SPECTATOR);
        for(Player inGame : arena.getPlayerInGame())
            inGame.hidePlayer(plugin,player);
    }

    public void checkGameEnd(Arena arena){
        if(getRemainingHiders(arena) == 0)
            winGame(Role.SEEKER,arena);
        if(getRemainingSeekers(arena) == 0)
            winGame(Role.HIDER,arena);
    }

    public void winGame(Role role, Arena arena){
        arena.setGameState(GameState.GAME_END);
        Main.getPlugin().getArenaEndCountdown().addEndingCountdownForArena(arena);
        //Main.getPlugin().getStatsManager().updatePlayerStats(winner,looser);
        for(Player player : arena.getAllPlayers()) {
            player.setGameMode(plugin.getArenaManager().getGamemode(arena));
            player.getInventory().clear();
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage("§d============§f[§9HideAndSeek§f]§d============");
            player.sendMessage("§7Winner >> " +role.getChatColor()+role.getName());
            player.sendMessage("§7Arena >> §3§l" + arena.getName());
            player.sendMessage("§d============§f[§9HideAndSeek§f]§d============");
            //player.sendTitle((player.equals(winner) ? "§6§lVictory" : "§c§lDefeat"), "§3" + winner.getDisplayName() + " won the duel.");
            regeneratePlayer(player);
            //Main.getPlugin().getItemManager().setPlayAgainItem(player);
            //Main.getPlugin().getItemManager().setLeaveItem(player);
            for (PotionEffect effect : player.getActivePotionEffects())
                player.removePotionEffect(effect.getType());
        }
        //Main.getPlugin().getPlayAgainHashMap().put(arena,new PlayAgain(arena,null,null));
    }

    /*public void timerExpiredGameEnd(ArrayList<Player> players, Arena arena, Kit kit){
        Main.getPlugin().getArenaEndCountdown().addEndingCountdownForArena(arena);
        for(Player player : players) {
            player.setGameMode(GameMode.valueOf(Main.getPlugin().getConfig().getString("game.gamemode")));
            player.getInventory().clear();
            player.teleport(arena.getEndSpawn());
            player.setAllowFlight(true);
            player.setFlying(true);
            player.sendMessage("§d============§f[§9Duel§f]§d============");
            player.sendMessage("§7Winner >> §4§lTimer expired");
            player.sendMessage("§7Arena >> §3§l" + arena.getName());
            player.sendMessage("§7Kit >> §5§l"+kit.getName());
            player.sendMessage("§d============§f[§9Duel§f]§d============");
            player.sendTitle("§4§lBattle ended!","§cThe timer has expired.");
            regeneratePlayer(player);
            Main.getPlugin().getItemManager().setPlayAgainItem(player);
            Main.getPlugin().getItemManager().setLeaveItem(player);
            for (PotionEffect effect : player.getActivePotionEffects())
                player.removePotionEffect(effect.getType());
        }
        Main.getPlugin().getBlockUtils().restoreBlocks(arena);
        Main.getPlugin().getPlayAgainHashMap().put(arena,new PlayAgain(arena,kit,null,null));
    }*/

    public void endGame(Arena arena){
        arena.setGameState(GameState.WAITING);
        showAllPlayers(arena);
        for(Player player : arena.getAllPlayers()) {
            player.teleport(arena.getLobbySpawn());
            plugin.getRoleManager().removeTeam(player);
            player.setFlying(false);
            player.setAllowFlight(false);
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
            //Main.getPlugin().getPlayerSavesManager().loadPlayer(player);
            plugin.getRoleManager().removePlayerRole(player);
        }
        arena.getPlayerInGame().clear();
        arena.getSpectators().clear();
        //Main.getPlugin().getPlayAgainHashMap().remove(arena);
    }

    /*public void endDuelWithPlayAgain(Arena arena){
        for(UUID uuid : arena.getPlayerInGame()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            player.setFlying(false);
            player.setAllowFlight(false);
            player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
        }
        arena.getPlayerInGame().clear();
        Main.getPlugin().getArenaKit().remove(arena);
        Main.getPlugin().getPlayAgainHashMap().remove(arena);
    }*/

    public void endAllDuelsWhenClosing(){
        for(Arena arena : Main.getPlugin().getArenaManagerHashMap().values()){
            if(arena.getGameState() != GameState.WAITING || arena.getGameState() != GameState.DISABLED){
                for(Player player : arena.getPlayerInGame()){
                    regeneratePlayer(player);
                    player.teleport(arena.getLobbySpawn());
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                    for (PotionEffect effect : player.getActivePotionEffects())
                        player.removePotionEffect(effect.getType());
                    //Main.getPlugin().getPlayerSavesManager().loadPlayer(player);
                    arena.setGameState(GameState.WAITING);
                }
            }
        }
    }
    public Arena getRandomEnabledArena() {
        List<Arena> availableArenas = new ArrayList<>();
        for(Arena arena : Main.getPlugin().getArenaManagerHashMap().values()){
            if(arena.isEnabled() && (arena.getGameState().equals(GameState.WAITING) || arena.getGameState().equals(GameState.STARTING)))
                availableArenas.add(arena);
        }

        if(availableArenas.isEmpty()) return null;

        // Sortiere Arenen nach Spieleranzahl absteigend
        for(Arena arena : availableArenas)
            availableArenas.sort(Comparator.comparingInt(arenas -> arena.getPlayerInGame().size()).reversed());

        // Überprüfe, ob es Arenen mit Spielern gibt
        List<Arena> arenasWithPlayers = availableArenas.stream()
                .filter(arena -> !arena.getPlayerInGame().isEmpty())
                .collect(Collectors.toList());

        if (!arenasWithPlayers.isEmpty()) {
            // Wenn es Arenen mit Spielern gibt, gib die mit den meisten Spielern zurück
            Bukkit.broadcastMessage("arena with "+arenasWithPlayers.get(0).getPlayerInGame().size());
            return arenasWithPlayers.get(0);
        } else {
            // Wenn keine Arena Spieler hat, wähle eine zufällige Arena aus
            Random random = new Random();
            Bukkit.broadcastMessage("no arena with players found");
            return availableArenas.get(random.nextInt(availableArenas.size()));
        }
    }

    public void regeneratePlayer(Player player) {
        player.setHealth(player.getMaxHealth());
        player.setFoodLevel(20);
    }

    public int getRemainingHiders(Arena arena){
        int count = 0;
        for(Player player : arena.getPlayerInGame()){
            if(plugin.getRoleManager().getPlayerRole(player) == Role.HIDER)
                count++;
        }
        return count;
    }

    public int getRemainingSeekers(Arena arena){
        int count = 0;
        for(Player player : arena.getPlayerInGame()){
            if(plugin.getRoleManager().getPlayerRole(player) == Role.SEEKER)
                count++;
        }
        return count;
    }

    public void showAllPlayers(Arena arena) {
        List<Player> allPlayers = arena.getAllPlayers();

        for (Player player : allPlayers) {
            for (Player otherPlayer : allPlayers) {
                // Zeige jeden Spieler jedem anderen Spieler
                player.showPlayer(plugin, otherPlayer);
            }
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (Player otherPlayer : Bukkit.getOnlinePlayers()) {
                // Zeige jeden Spieler jedem anderen Spieler
                player.showPlayer(plugin, otherPlayer);
            }
        }
    }

}
