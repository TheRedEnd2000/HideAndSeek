package de.theredend2000.hideandseek.voting;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.gamestates.LobbyState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class VotingListener implements Listener {

    private Main plugin;
    private Voting voting;
    private int START_SECONDS = 5;

    public VotingListener(Main plugin) {
        this.plugin = plugin;
        voting = plugin.getVoting();
    }

    @EventHandler
    public void onInteractEvent(PlayerInteractEvent event) {
        if(!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) return;
        Player player = event.getPlayer();
        ItemStack item = player.getItemInHand();
        if(item.getItemMeta() == null) return;
        if(item.getItemMeta().getDisplayName().equals("§6Map-Voting")) {
            player.openInventory(voting.getVotingInventory());
        }
        if(item.getItemMeta().getDisplayName().equals("§2Start")){
            if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
                LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
                if(lobbyState.getLobbyCountdown().isRunning() && (lobbyState.getLobbyCountdown().getSeconds() > START_SECONDS)){
                    lobbyState.getLobbyCountdown().setSeconds(START_SECONDS);
                    player.sendMessage(Main.PREFIX+"§2Der Start wurde beschleunigt!");
                    for(Player current: plugin.getGamePlayer()) {
                        current.sendMessage(Main.PREFIX + "§aDer Start wurde von §6" + player.getDisplayName() + "§a beschleunigt");
                        if(current.isOp()){
                            current.getInventory().remove(new ItemStack(Material.DIAMOND));
                        }
                    }
                }else
                    player.sendMessage(Main.PREFIX+"§cEs läuft gerade kein Spiel oder wurde schon gestartet!");
            }else
                player.sendMessage(Main.PREFIX+"§cEs läuft gerade kein Spiel oder wurde schon gestartet!");
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if(!event.getView().getTitle().equals(Voting.VOTING_INVENTORY_TITLE)) return;
        event.setCancelled(true);
        for(int i = 0; i < voting.getVotingInventoryOrder().length; i++) {
            if(voting.getVotingInventoryOrder()[i] == event.getSlot()) {
                voting.vote(player, i);
                return;
            }
        }
    }

}
