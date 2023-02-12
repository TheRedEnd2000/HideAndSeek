package de.theredend2000.hideandseek.gamestates;

import de.theredend2000.hideandseek.Main;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class GameProtectionListener implements Listener {

    private Main plugin;

    public GameProtectionListener(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevelChangeEvent(FoodLevelChangeEvent event){
        if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState || plugin.getGameStateManager().getCurrentGameState() instanceof IngameState || plugin.getGameStateManager().getCurrentGameState() instanceof EndingState){
            if(event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (plugin.getGamePlayer().contains(player)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntityEvent(EntityDamageByEntityEvent event){
        if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState || plugin.getGameStateManager().getCurrentGameState() instanceof EndingState){
            if(event.getEntity() instanceof Player && event.getDamager() instanceof Player && event.getDamager() != null) {
                Player player = (Player) event.getEntity();
                if (plugin.getGamePlayer().contains(player)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event){
        if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState || plugin.getGameStateManager().getCurrentGameState() instanceof EndingState){
            if(event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                if (plugin.getGamePlayer().contains(player)) {
                    if(event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState || plugin.getGameStateManager().getCurrentGameState() instanceof IngameState || plugin.getGameStateManager().getCurrentGameState() instanceof EndingState){
            Player player = event.getPlayer();
            if (plugin.getGamePlayer().contains(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryMove(InventoryClickEvent event){
        if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState || plugin.getGameStateManager().getCurrentGameState() instanceof IngameState || plugin.getGameStateManager().getCurrentGameState() instanceof EndingState){
            if(event.getWhoClicked() instanceof Player) {
                Player player = (Player) event.getWhoClicked();
                if (plugin.getGamePlayer().contains(player)) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onSwapItem(PlayerSwapHandItemsEvent event){
        if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState || plugin.getGameStateManager().getCurrentGameState() instanceof IngameState || plugin.getGameStateManager().getCurrentGameState() instanceof EndingState){
            Player player = event.getPlayer();
            if (plugin.getGamePlayer().contains(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState || plugin.getGameStateManager().getCurrentGameState() instanceof IngameState || plugin.getGameStateManager().getCurrentGameState() instanceof EndingState){
            Player player = event.getPlayer();
            if (plugin.getGamePlayer().contains(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState || plugin.getGameStateManager().getCurrentGameState() instanceof IngameState || plugin.getGameStateManager().getCurrentGameState() instanceof EndingState){
            Player player = event.getPlayer();
            if (plugin.getGamePlayer().contains(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event){
        if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState || plugin.getGameStateManager().getCurrentGameState() instanceof IngameState || plugin.getGameStateManager().getCurrentGameState() instanceof EndingState){
            Player player = event.getPlayer();
            if (plugin.getGamePlayer().contains(player)) {
                event.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onClickBlock(PlayerInteractEvent event){
        if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState || plugin.getGameStateManager().getCurrentGameState() instanceof IngameState || plugin.getGameStateManager().getCurrentGameState() instanceof EndingState){
            Player player = event.getPlayer();
            if (plugin.getGamePlayer().contains(player)) {
                event.setCancelled(true);
            }
        }
    }

}
