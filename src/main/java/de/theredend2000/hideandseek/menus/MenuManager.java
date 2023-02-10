package de.theredend2000.hideandseek.menus;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class MenuManager {

    private Main plugin;

    public MenuManager(Main plugin){
        this.plugin = plugin;
    }

    public void createInventory(Player player){
        Inventory mapInventory = Bukkit.createInventory(player, 54, "Select or create a map");
        int[] redglass = new int[]{1,2,3,5,6,7,9,17,18,26,27,35,36,44,46,47,48,51,52};
        for(int i = 0; i < redglass.length; i++){
            mapInventory.setItem(redglass[i], new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());
        }
        mapInventory.setItem(0, new ItemBuilder(Material.COMPARATOR).setDisplayname("§4Settings").setLore("§2Click here to change the settings.").build());
        mapInventory.setItem(4, new ItemBuilder(Material.BOOK).setDisplayname("§e§lMap Selector").build());
        mapInventory.setItem(8, new ItemBuilder(Material.TARGET).setDisplayname("§7Page §61 §7of §610").build());
        mapInventory.setItem(45, new ItemBuilder(Material.ARROW).setDisplayname("§7Back").build());
        mapInventory.setItem(53, new ItemBuilder(Material.ARROW).setDisplayname("§7Next").build());
        mapInventory.setItem(50, new ItemBuilder(Material.OAK_SIGN).setDisplayname("§3Search Map").setLore("§2Click here to search for a Map.").setLocalizedName("search").build());
        mapInventory.setItem(49, new ItemBuilder(Material.EMERALD_BLOCK).setDisplayname("§6§lCreate Map").setLore("§2Click here to create a new Map.").setLocalizedName("createMap").build());
        if(plugin.yaml.contains("Arenas")) {
            for (String maps : plugin.yaml.getConfigurationSection("Arenas.").getKeys(false)) {
                String author = plugin.yaml.getString("Arenas." + maps + ".Builder");
                mapInventory.addItem(new ItemBuilder(Material.PAPER).setDisplayname("§5§l"+maps).setLore("", "§7Builder: §6" + author, "", "§7Finished: §a§ltrue§7/§4§lfalse", "§2LEFT-CLICK §7Select this Map and Open Options", "§2RIGHT-CLICK §7Delete the Map").build());
            }
        }else
            mapInventory.setItem(22, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§4No Maps").setLore("§2Click the emerald block to create one.").build());
        player.openInventory(mapInventory);
    }

    public void createSettingsInventory(Player player){
        Inventory settingsInventory = Bukkit.createInventory(player, 54, "Settings");
        int[] whiteglass = new int[]{45,46,47,48,49,50,51,52};
        for(int i = 0; i < whiteglass.length; i++){
            settingsInventory.setItem(whiteglass[i], new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDisplayname("§c").build());
        }
        settingsInventory.setItem(53, new ItemBuilder(Material.EMERALD_BLOCK).setDisplayname("§6§lCreate Map").setLore("§2Click here to create a new Map.").build());
        settingsInventory.setItem(34,new ItemBuilder(Material.PAPER).setDisplayname("Halllo").setLore("halo1","2").setLocalizedName("settings-hallo").build());

        player.openInventory(settingsInventory);
    }

    public void createMapEditInventory(Player player, String mapname){
        Inventory mapInventory = Bukkit.createInventory(player, 54, "Map -> "+mapname);
        int[] whiteglass = new int[]{45,46,47,48,49,50,51,52};
        for(int i = 0; i < whiteglass.length; i++){
            mapInventory.setItem(whiteglass[i], new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDisplayname("§c").build());
        }
        mapInventory.setItem(0,new ItemBuilder(Material.MAP).build());
        player.openInventory(mapInventory);
    }

    public void createDeleteInventory(Player player, String mapname){
        Inventory confirmInventory = Bukkit.createInventory(player, 27, "Delete -> "+mapname);
        confirmInventory.setItem(11,new ItemBuilder(Material.RED_CONCRETE).setDisplayname("").build());
        player.openInventory(confirmInventory);
    }

    public void createNewMapInventory(Player player){
        Inventory settingsInventory = Bukkit.createInventory(player, 45, "Create a new Map");
        settingsInventory.clear();
        int[] limeglass = new int[]{0,1,2,3,4,5,6,7,8,9,17,18,25,26,27,35,36,37,38,39,40,41,42,43,44};
        for(int i = 0; i < limeglass.length; i++){
            settingsInventory.setItem(limeglass[i], new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDisplayname("§c").build());
        }
        int[] whiteglass = new int[]{10,11,12,13,14,15,16,19,21,23,28,29,30,31,32,33,34};
        for(int i = 0; i < whiteglass.length; i++){
            settingsInventory.setItem(whiteglass[i], new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        }
        settingsInventory.setItem(20, new ItemBuilder(Material.NAME_TAG).setDisplayname("§3Map Name").setLore("§7Click to set the Map Name","§2Currently: §6"+plugin.yaml.getString("CreateMaps."+player.getName()+".Name")).setLocalizedName("createMap.name").build());
        settingsInventory.setItem(22, new ItemBuilder(Material.FIREWORK_ROCKET).setDisplayname("§2§lFINISH").setLocalizedName("createMap.finish").build());
        settingsInventory.setItem(24, new ItemBuilder(Material.GRASS_BLOCK).setDisplayname("§3Builder").setLore("§7Click to set the Map Builder","§2Currently: §6"+plugin.yaml.getString("CreateMaps."+player.getName()+".Builder")).setLocalizedName("createMap.builder").build());
        player.openInventory(settingsInventory);
    }

}
