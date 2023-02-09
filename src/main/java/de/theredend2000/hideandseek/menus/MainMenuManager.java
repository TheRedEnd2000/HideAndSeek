package de.theredend2000.hideandseek.menus;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;

public class MainMenuManager {

    private Main plugin;

    public MainMenuManager(Main plugin){
        this.plugin = plugin;
    }

    public void createInventory(Player player){
        Inventory mapInventory = Bukkit.createInventory(player, 54, "Select or create an arena");
        int[] redglass = new int[]{45,46,47,48,49,50,51,52};
        for(int i = 0; i < redglass.length; i++){
            mapInventory.setItem(redglass[i], new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        }
        mapInventory.setItem(53, new ItemBuilder(Material.EMERALD_BLOCK).setDisplayname("§6§lCreate Map").setLore("§2Click here to create a new Map.").build());
        if(plugin.yaml.contains("Arenas")) {
            for (String maps : plugin.yaml.getConfigurationSection("Arenas.").getKeys(false)) {
                String author = plugin.yaml.getString("Arenas." + maps + ".Builder");
                mapInventory.addItem(new ItemBuilder(Material.PAPER).setDisplayname(maps).setLore("", "§7Builder: §6" + author, "", "§7Finished: §a§ltrue§7/§4§lfalse", "§2LEFT-CLICK §7Select this Map and Open Options", "§2RIGHT-CLICK §7Delete the Map").build());
            }
        }else
            mapInventory.setItem(22, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§4No Maps").setLore("§2Click the emerald block to create one.").build());
        player.openInventory(mapInventory);
    }

}
