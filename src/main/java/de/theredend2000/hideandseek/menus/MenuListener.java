package de.theredend2000.hideandseek.menus;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.voting.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Objects;

public class MenuListener implements Listener {

    private Main plugin;
    private ArrayList<Player> namePlayer;
    private ArrayList<Player> builderPlayer;
    private ArrayList<Player> searchPlayer;

    public MenuListener(Main plugin){
        this.plugin = plugin;
        namePlayer = new ArrayList<>();
        builderPlayer = new ArrayList<>();
        searchPlayer = new ArrayList<>();
    }

    @EventHandler
    public void onClickMainInventory(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
            if (event.getView().getTitle().equals("Select or create a map")) {
                event.setCancelled(true);
                if (event.getCurrentItem() != null) {
                    if(event.getCurrentItem().getType().equals(Material.PAPER)){
                        if(event.getAction() == InventoryAction.PICKUP_ALL) {
                            String mapName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                            plugin.getMenuManager().createMapEditInventory(player, mapName);
                        }else if(event.getAction() == InventoryAction.PICKUP_HALF){
                            String mapName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                            plugin.getMenuManager().createDeleteInventory(player, mapName, 5, true);
                            new BukkitRunnable() {
                                int seconds = 5;
                                @Override
                                public void run() {
                                    if (player.getOpenInventory().getTitle().equals("§4Delete a Map")) {
                                        if (seconds == 0) {
                                            cancel();
                                            plugin.getMenuManager().createDeleteInventory(player, mapName, seconds, true);
                                            return;
                                        }
                                        plugin.getMenuManager().createDeleteInventory(player, mapName, seconds, false);
                                        seconds--;
                                    }else{
                                        cancel();
                                    }
                                }
                            }.runTaskTimer(plugin,0,20);
                        }
                    }else if (event.getCurrentItem().getItemMeta().hasLocalizedName()) {
                        switch (event.getCurrentItem().getItemMeta().getLocalizedName()) {
                            case "createMap":
                                plugin.yaml.set("CreateMaps."+player.getName()+".Name","null");
                                plugin.yaml.set("CreateMaps."+player.getName()+".Builder","null");
                                plugin.saveData();
                                plugin.getMenuManager().createNewMapInventory(player);
                                break;
                            case "search":
                                player.closeInventory();
                                player.sendMessage(Main.PREFIX+"§7Type Map Name in Chat. Type '§4cancel§7' to cancel.");
                                searchPlayer.add(player);
                                break;
                            case "settings":
                                player.closeInventory();
                                plugin.getMenuManager().createSettingsInventory(player);
                                break;
                        }
                    }
                }
            }
        }
    @EventHandler
    public void onClickNewMapInventory(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals("Create a new Map")) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getItemMeta().hasLocalizedName()) {
                    switch (event.getCurrentItem().getItemMeta().getLocalizedName()) {
                        case "createMap.name":
                            player.closeInventory();
                            player.sendMessage(Main.PREFIX+"§7Type map name in Chat. Type '§4cancel§7' to cancel to task");
                            namePlayer.add(player);
                            break;
                        case "createMap.finish":
                            if(!plugin.yaml.getString("CreateMaps."+player.getName()+".Name").equalsIgnoreCase("null") && !plugin.yaml.getString("CreateMaps."+player.getName()+".Builder").equalsIgnoreCase("null")){
                                String mapName = plugin.yaml.getString("CreateMaps." + player.getName() + ".Name");
                                String mapBuilder = plugin.yaml.getString("CreateMaps." + player.getName() + ".Builder");
                                Map map = new Map(plugin, mapName);
                                map.create(mapBuilder);
                                player.sendMessage(Main.PREFIX+"§7The Map §6"+mapName+"§7 by §6"+mapBuilder+"§7 was created§a successfully§7.");
                                player.closeInventory();
                                plugin.initVoting();
                            }else
                                player.sendMessage(Main.PREFIX+"§cPlease finish the setup");
                            break;
                        case "createMap.builder":
                            player.closeInventory();
                            player.sendMessage(Main.PREFIX+"§7Type map builder in Chat. Type '§4cancel§7' to cancel to task");
                            builderPlayer.add(player);
                            break;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClickDeleteInventory(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals("§4Delete a Map")) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                if(event.getCurrentItem().getType().equals(Material.RED_CONCRETE)){
                    plugin.getMenuManager().createInventory(player);
                }else if(event.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS)){
                    player.closeInventory();
                    String mapName = ChatColor.stripColor(event.getInventory().getItem(13).getItemMeta().getDisplayName());
                    plugin.yaml.set("Arenas."+mapName, null);
                    plugin.saveData();
                    plugin.initVoting();
                    player.sendMessage(Main.PREFIX+"§7The Map §6"+mapName+" §7got§c deleted§7.");
                }
            }
        }
    }

    @EventHandler
    public void onChatEvent(PlayerChatEvent event){
        if(namePlayer.contains(event.getPlayer())){
            event.setCancelled(true);
            namePlayer.remove(event.getPlayer());
            if(event.getMessage().equalsIgnoreCase("cancel")){
                plugin.getMenuManager().createNewMapInventory(event.getPlayer());
                event.getPlayer().sendMessage(Main.PREFIX+"§cTask canceled.");
                return;
            }
            plugin.yaml.set("CreateMaps."+event.getPlayer().getName()+".Name", event.getMessage());
            plugin.saveData();
            plugin.getMenuManager().createNewMapInventory(event.getPlayer());
        }
        if(builderPlayer.contains(event.getPlayer())){
            event.setCancelled(true);
            builderPlayer.remove(event.getPlayer());
            if(event.getMessage().equalsIgnoreCase("cancel")){
                plugin.getMenuManager().createNewMapInventory(event.getPlayer());
                event.getPlayer().sendMessage(Main.PREFIX+"§cTask canceled.");
                return;
            }
            plugin.yaml.set("CreateMaps."+event.getPlayer().getName()+".Builder", event.getMessage());
            plugin.saveData();
            plugin.getMenuManager().createNewMapInventory(event.getPlayer());
        }
        if(searchPlayer.contains(event.getPlayer())){
            event.setCancelled(true);
            searchPlayer.remove(event.getPlayer());
            if(event.getMessage().equalsIgnoreCase("cancel")){
                plugin.getMenuManager().createInventory(event.getPlayer());
                event.getPlayer().sendMessage(Main.PREFIX+"§cTask canceled.");
                return;
            }
            Map map = new Map(plugin, event.getMessage());
            if(map.exists()){
                plugin.getMenuManager().createMapEditInventory(event.getPlayer(), event.getMessage());
                event.getPlayer().sendMessage(Main.PREFIX+"§a§lSuccess!");
            }else{
                plugin.getMenuManager().createInventory(event.getPlayer());
                event.getPlayer().sendMessage(Main.PREFIX+"§cI can't find this Map. Try again.");
            }
        }
    }

    @EventHandler
    public void onClickSettingsInventory(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals("Settings")) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                if (event.getCurrentItem().getItemMeta().hasLocalizedName())
                {
                    switch (event.getCurrentItem().getItemMeta().getLocalizedName()) {
                        case "settings.playersettings":
                            plugin.getMenuManager().createSettingsPlayerSettingsInventory(player);
                            break;
                        case "settings.mainmenu":
                            plugin.getMenuManager().createInventory(player);
                            break;


                    }

                }
            }
        }
    }
    @EventHandler
    public void onClickPlayerSettingsInventory(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();
        if (event.getView().getTitle().equals("Playersettings"))
        {
            event.setCancelled(true);
            if(event.getCurrentItem() != null)
            {
                if (event.getCurrentItem().getItemMeta().hasLocalizedName())
                {
                    switch (event.getCurrentItem().getItemMeta().getLocalizedName())
                    {
                        case "settings.playersettings.Seeker":
                            break;
                    }


                }


            }



        }
    }
}
