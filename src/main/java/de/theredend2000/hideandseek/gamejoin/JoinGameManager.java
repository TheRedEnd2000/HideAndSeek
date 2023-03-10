package de.theredend2000.hideandseek.gamejoin;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.countdowns.LobbyCountdown;
import de.theredend2000.hideandseek.gamestates.EndingState;
import de.theredend2000.hideandseek.gamestates.GameState;
import de.theredend2000.hideandseek.gamestates.IngameState;
import de.theredend2000.hideandseek.gamestates.LobbyState;
import de.theredend2000.hideandseek.util.ConfigLocationUtil;
import de.theredend2000.hideandseek.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Objects;

public class JoinGameManager {

    private Main plugin;

    public JoinGameManager(Main plugin){
        this.plugin = plugin;
    }

    public void joinGame(Player player) {
        if (plugin.getCanplayMaps().size() == 0) {
            player.sendMessage(Main.PREFIX+"§cThere are no maps to play on!");
            return;
        }
        if (plugin.getGamePlayer().contains(player)) {
            player.sendMessage(Main.PREFIX + "§cYou already joined the game!");
            return;
        }
            if (plugin.getGameStateManager().getCurrentGameState() instanceof IngameState || plugin.getGameStateManager().getCurrentGameState() instanceof EndingState) {

                //ADD PLAYER TO SPECTATOR
                player.sendMessage("§aAdded Spec");

                return;
            }
            if (plugin.getGamePlayer().size() >= LobbyState.MAX_PLAYER) {
                player.sendMessage(Main.PREFIX + "§cThe Game is already full!");
                return;
            }
            plugin.getGamePlayer().add(player);
            for (Player current : plugin.getGamePlayer()) {
                current.sendMessage(Main.PREFIX + "§a" + player.getDisplayName() + " §7joined the round. §b[§2" +
                        plugin.getGamePlayer().size() + "§b/§2" + LobbyState.MAX_PLAYER + "§b]");
            }
            plugin.getGameStateManager().setGameStates(GameState.LOBBY_STATE);
        LobbyState ls = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
        ls.updateScoreboard();
            player.getInventory().clear();
            player.setGameMode(GameMode.SURVIVAL);
            player.setHealthScale(20);
            player.setFoodLevel(20);
            if(player.isOp()){
                player.getInventory().setItem(1, new ItemBuilder(Material.DIAMOND).setDisplayname("§2Start").build());
            }
            if(plugin.getVoting() != null)
                player.getInventory().setItem(4, new ItemBuilder(Material.NETHER_STAR).setDisplayname("§6Map-Voting").build());

            ConfigLocationUtil locationUtil = new ConfigLocationUtil(plugin, "lobby");
            if (locationUtil.loadLocation() != null) {
                player.teleport(locationUtil.loadLocation());
            }

            LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
            LobbyCountdown countdown = lobbyState.getLobbyCountdown();
            if (plugin.getGamePlayer().size() >= plugin.getConfig().getInt("Settings.MinPlayerCount")) {
                if (!countdown.isRunning()) {
                    countdown.start();
                }
            }
        }


    public void leaveGame(Player player) {
        if (plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
            LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
            LobbyCountdown countdown = lobbyState.getLobbyCountdown();
            if (plugin.getGamePlayer().contains(player)) {
                plugin.getGamePlayer().remove(player);
                player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                for (Player current : plugin.getGamePlayer()) {
                    current.sendMessage(Main.PREFIX + "§a" + player.getDisplayName() + " §7left the round. §b[§2" +
                            plugin.getGamePlayer().size() + "§b/§2" + LobbyState.MAX_PLAYER + "§b]");
                }
                if (plugin.getGamePlayer().size() == 0) {
                    plugin.getGameStateManager().stopGame();
                    countdown.stop();
                    plugin.getGamePlayer().clear();
                }
                player.sendMessage(Main.PREFIX + "§cYou left the game.");
                player.getInventory().clear();
                player.setGameMode(GameMode.SURVIVAL);
                player.setHealthScale(20);
                player.setFoodLevel(20);

                ConfigLocationUtil locationUtil = new ConfigLocationUtil(plugin, "end");
                if (locationUtil.loadLocation() != null) {
                    player.teleport(locationUtil.loadLocation());
                }

                if (plugin.getGamePlayer().size() < plugin.getConfig().getInt("Settings.MinPlayerCount")) {
                    if (countdown.isRunning()) {
                        countdown.stop();
                    }
                }
            } else
                player.sendMessage(Main.PREFIX + "§cYou are not in the Game.");
        }
    }


}
