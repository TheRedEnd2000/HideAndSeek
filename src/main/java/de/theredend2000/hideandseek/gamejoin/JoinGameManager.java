package de.theredend2000.hideandseek.gamejoin;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.gamestates.EndingState;
import de.theredend2000.hideandseek.gamestates.GameState;
import de.theredend2000.hideandseek.gamestates.IngameState;
import de.theredend2000.hideandseek.gamestates.LobbyState;
import de.theredend2000.hideandseek.util.ConfigLocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class JoinGameManager {

    private Main plugin;

    public JoinGameManager(Main plugin){
        this.plugin = plugin;
    }

    public void joinGame(Player player){
        if(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState || plugin.getGameStateManager().getCurrentGameState() instanceof EndingState){

            //ADD PLAYER TO SPECTATOR

            return;
        }
        plugin.getGamePlayer().add(player);
        for(Player current : plugin.getGamePlayer()) {
            current.sendMessage(Main.PREFIX + "§a" + player.getDisplayName() + " §7joined the round. §b[§2" +
            plugin.getGamePlayer().size() + "§b/§2" + LobbyState.MAX_PLAYERS + "§b]");
        }
        plugin.getGameStateManager().setGameStates(GameState.LOBBY_STATE);
        player.getInventory().clear();
        player.setGameMode(GameMode.SURVIVAL);
        player.setHealthScale(20);
        player.setFoodLevel(20);

        ConfigLocationUtil locationUtil = new ConfigLocationUtil(plugin, "lobby");
        if (locationUtil.loadLocation() != null) {
            player.teleport(locationUtil.loadLocation());
        } else
            Bukkit.getConsoleSender().sendMessage(Main.PREFIX + "§cDie Lobby-Location wurde noch nicht gesetzt!");

        LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
    }

}
