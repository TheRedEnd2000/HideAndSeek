package de.theredend2000.hideandseek.listeners;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.arenas.Arena;
import de.theredend2000.hideandseek.game.GameState;
import de.theredend2000.hideandseek.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.server.PluginDisableEvent;

public class GameListeners implements Listener {

    private Main plugin;

    public GameListeners(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onDisabled(PluginDisableEvent event){
        if(event.getPlugin().equals(Main.getPlugin())){
            //Main.getPlugin().getGameManager().endAllDuelsWhenClosing();
            Main.getPlugin().getArenaManager().saveAllArenas();
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event){
        if(event.getDamager() instanceof Player && event.getEntity() instanceof Player){
            Player damager = (Player) event.getDamager();
            Player entity = (Player) event.getEntity();
            if(plugin.getRoleManager().getPlayerRole(damager) == Role.HIDER)
                event.setCancelled(true);
            if(plugin.getRoleManager().getPlayerRole(damager) == Role.SEEKER && plugin.getRoleManager().getPlayerRole(entity) == Role.HIDER && plugin.getArenaManager().playerIsAlreadyInArena(damager) && plugin.getArenaManager().playerIsAlreadyInArena(entity) && (plugin.getArenaManager().getPlayerCurrentArena(damager) == plugin.getArenaManager().getPlayerCurrentArena(entity))){
                Arena arena = plugin.getArenaManager().getPlayerCurrentArena(entity);
                if(arena.getGameState() == GameState.RUNNING && plugin.getArenaDurationCountdown().isReleased())
                    plugin.getGameManager().findPlayer(entity,damager,arena);
                else {
                    event.setCancelled(true);
                    damager.sendMessage("Bro why tf you cheating...");
                }
                event.setDamage(0);
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event){
        Player player = event.getPlayer();
        if(plugin.getArenaManager().playerIsAlreadyInArena(player)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if(plugin.getArenaManager().playerIsAlreadyInArena(player)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onChange(EntityChangeBlockEvent event){
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.getArenaManager().playerIsAlreadyInArena(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event){
        if(event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (plugin.getArenaManager().playerIsAlreadyInArena(player)) {
                Arena arena = plugin.getArenaManager().getPlayerCurrentArena(player);
                if(arena.getGameState() != GameState.RUNNING)
                    event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if (plugin.getArenaManager().playerIsAlreadyInArena(player)) {
            event.setCancelled(true);
        }
    }
}
