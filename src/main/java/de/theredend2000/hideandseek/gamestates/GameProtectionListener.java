package de.theredend2000.hideandseek.gamestates;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.role.Role;
import de.theredend2000.hideandseek.role.RoleManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;

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
    public void onEntityDamageByEntityEventINGAME(EntityDamageByEntityEvent event){
        if(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState){
            if(event.getEntity() instanceof Player && event.getDamager() instanceof Player && event.getDamager() != null) {
                Player player = (Player) event.getEntity();
                Player damager = (Player) event.getDamager();
                Role rolePlayer = plugin.getRoleManager().getPlayerRole(player);
                Role roleDamager = plugin.getRoleManager().getPlayerRole(damager);
                if(roleDamager == Role.Spectator || rolePlayer == Role.Spectator){
                    event.setCancelled(true);
                }else if (rolePlayer == Role.Hider && roleDamager == Role.Hider) {
                    event.setCancelled(true);
                }else if(rolePlayer == Role.Seeker && roleDamager == Role.Hider){
                    event.setCancelled(true);
                }else if(rolePlayer == Role.Hider && roleDamager == Role.Seeker){
                    IngameState ingameState = (IngameState) plugin.getGameStateManager().getCurrentGameState();
                    if(ingameState.isInGrace()){
                        damager.sendMessage(Main.PREFIX+"§cYou can't damage players in waiting countdown");
                        event.setCancelled(true);
                        return;
                    }
                    int lives = ingameState.getHiderLives().get(player);
                    event.setDamage(0);
                    if(lives == 3){
                        ingameState.getHiderLives().remove(player);
                        ingameState.getHiderLives().put(player, lives-1);
                    }
                    if(lives == 2){
                        ingameState.getHiderLives().remove(player);
                        ingameState.getHiderLives().put(player, lives-1);
                    }
                    if(lives == 1){
                        ingameState.getHiderLives().remove(player);
                        plugin.getRoleManager().getPlayerRoles().remove(player.getName());
                        plugin.getRoleManager().getHiderPlayers().remove(player.getName());
                        if(plugin.getRoleManager().getHiderPlayers().size() == 0) {
                            ingameState.checkGameEnding();
                            return;
                        }
                        player.teleport(plugin.getVoting().getWinnerMap().getSpectatorLocation());
                        plugin.getRoleManager().getPlayerRoles().put(player.getName(), Role.Spectator);
                        player.sendMessage("Spectator");
                        player.setGameMode(GameMode.SPECTATOR);
                        ingameState.ingameScoreboard(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageEvent(EntityDamageEvent event){
        if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState || plugin.getGameStateManager().getCurrentGameState() instanceof IngameState || plugin.getGameStateManager().getCurrentGameState() instanceof EndingState){
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
    public void onPickup(PlayerPickupItemEvent event){
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
