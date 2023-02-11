package de.theredend2000.hideandseek.menus;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.util.ItemBuilder;
import jdk.tools.jlink.plugin.Plugin;
import net.md_5.bungee.chat.SelectorComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

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
        mapInventory.setItem(0, new ItemBuilder(Material.COMPARATOR).setDisplayname("§4Settings").setLore("§2Click here to change the settings.").setLocalizedName("settings").build());
        mapInventory.setItem(4, new ItemBuilder(Material.BOOK).setDisplayname("§e§lMap Selector").build());
        mapInventory.setItem(8, new ItemBuilder(Material.TARGET).setDisplayname("§7Page §61 §7of §610").build());
        mapInventory.setItem(45, new ItemBuilder(Material.ARROW).setDisplayname("§7Back").build());
        mapInventory.setItem(53, new ItemBuilder(Material.ARROW).setDisplayname("§7Next").build());
        mapInventory.setItem(50, new ItemBuilder(Material.OAK_SIGN).setDisplayname("§3Search Map").setLore("§2Click here to search for a Map.").setLocalizedName("search").build());
        mapInventory.setItem(49, new ItemBuilder(Material.EMERALD_BLOCK).setDisplayname("§6§lCreate Map").setLore("§2Click here to create a new Map.").setLocalizedName("createMap").build());
        if(plugin.yaml.contains("Arenas")) {
            for (String maps : plugin.yaml.getConfigurationSection("Arenas.").getKeys(false)) {
                String author = plugin.yaml.getString("Arenas." + maps + ".Builder");
                mapInventory.addItem(new ItemBuilder(Material.PAPER).setDisplayname("§5§l"+maps).setLore("", "§7Builder: §6" + author, "", "§7Finished: §6§l"+plugin.yaml.getBoolean("Arenas."+maps+".isFinished"), "§2LEFT-CLICK §7Select this Map and Open Options", "§2RIGHT-CLICK §7Delete the Map").build());
            }
        }else
            mapInventory.setItem(22, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§4No Maps").setLore("§2Click the emerald block to create one.").build());
        player.openInventory(mapInventory);
    }

    public void createSettingsInventory(Player player){
        Inventory settingsInventory = Bukkit.createInventory(player, 54, "Settings");
        int[] redglass = new int[]{0,1,2,3,5,6,7,8,9,10,16,17,18,26,27,35,36,37,43,44,45,46,47,48,50,51,52,53};
        for(int i = 0; i < redglass.length; i++){settingsInventory.setItem(redglass[i], new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        int[] orange = new int[]{12,13,14,19,20,21,23,24,25,28,29,30,31,32,33,34,38,39,41,42};
        for(int i = 0; i < orange.length; i++) {settingsInventory.setItem(orange[i], new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        settingsInventory.setItem(4, new ItemBuilder(Material.COMPARATOR).setDisplayname("Settings").setLore("§c").build());
        ItemStack is = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta isMeta = (SkullMeta) is.getItemMeta();
        isMeta.setOwner("craftingtable");
        isMeta.setDisplayName("§6§lPlayerSettings");
        isMeta.setLore(Arrays.asList("§2Click here to open the Player Settings."));
        isMeta.setLocalizedName("settings.playersettings");
        is.setItemMeta(isMeta);
        settingsInventory.setItem(11, is);
        settingsInventory.setItem(15,new ItemBuilder(Material.NETHERITE_AXE).setDisplayname("Abilitys").setLore("Click her to open the Ability Menu").setLocalizedName("settings.Ability").build());
        settingsInventory.setItem(22,new ItemBuilder(Material.CLOCK).setDisplayname("Playtime").setLore("Click here to open the Playtime Menu").setLocalizedName("settings.Playtime").build());
        settingsInventory.setItem(40,new ItemBuilder(Material.FEATHER).setDisplayname("Spectator").setLore("Click to switch the ...").setLocalizedName("settings.Spactator").build());
        settingsInventory.setItem(49, new ItemBuilder(Material.NETHER_STAR).setDisplayname("§eMain Menu").setLore("§7Click to go back to the Main Menu").setLocalizedName("settings.Mainmenu").build());
        player.openInventory(settingsInventory);
    }

    public void createSettingsPlayerSettingsInventory(Player player){
        Inventory settingsInventory = Bukkit.createInventory(player, 54, "Player Settings");
        int[] redglass2 = new int[]{0,1,2,3,5,6,7,8,9,17,18,26,27,35,36,41,44,46,47,48,50,51,52,53};
        int[] oragne = new int[]{11,15,19,20,22,24,25,28,31,34,37,40,43};
        int[] white = new int[]{12,14,21,23,30,32,39,41};
        for(int i = 0; i < redglass2.length; i++){settingsInventory.setItem(redglass2[i], new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        for(int i = 0; i < oragne.length; i++){settingsInventory.setItem(oragne[i], new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        for(int i = 0; i < white.length; i++){settingsInventory.setItem(white[i], new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        settingsInventory.setItem(10, new ItemBuilder(Material.REDSTONE_TORCH).setDisplayname("§3MinPlayerCount").setLore("§6LEFT-CLICK §e: §a+1","§6RIGHT-CLICK §e: §a-1","§7Currently§e:§5 "+plugin.getConfig().getInt("Settings.MinPlayerCount")).setLocalizedName("settings.playersettings.MinPlayerCount").build());
        settingsInventory.setItem(13,new ItemBuilder(Material.NETHERITE_SWORD).setDisplayname("§4Seeker").setLore("§8Click for Seeker Settings").setLocalizedName("settings.playersettings.Seeker").build());
        settingsInventory.setItem(16,new ItemBuilder(Material.SHIELD).setDisplayname("§aHider").setLore("§8Click for Hider Settings").setLocalizedName("settings.playersettings.Hider").build());
        settingsInventory.setItem(29,new ItemBuilder(Material.OAK_DOOR).setDisplayname("§1Switch§cTeam").setLore("§bActivate if you want to become a Seeker after you died").setLocalizedName("settings.playersettings.SwitchTeam").build());
        settingsInventory.setItem(38,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§8Click to Disable").setLocalizedName("settings.playersettings.SwitchTeam.Green_Dye").build());
        settingsInventory.setItem(33,new ItemBuilder(Material.STRUCTURE_VOID).setDisplayname("§0Nothingishere").setLore("§8Nothing is here too").setLocalizedName("settings.playersettings.Nothingishere").build());
        settingsInventory.setItem(42,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§c").setLocalizedName("settings.playersettings.Nothingishere.Green_dye").build());
        settingsInventory.setItem(49,new ItemBuilder(Material.COMPARATOR).setDisplayname("§f").setLore("§3Click to go Back to the Settings Menu").setLocalizedName("settings.playersettings.comperator").build());
        settingsInventory.setItem(4,new ItemBuilder(Material.PLAYER_HEAD).setDisplayname("§ePlayer Settings").setLore("§c").setLocalizedName("settings.playersettings.head").build());
        settingsInventory.setItem(45,new ItemBuilder(Material.ARROW).setDisplayname("§7Back").setLore("§c").setLocalizedName("settings.playersettings.back").build());
        player.openInventory(settingsInventory);
    }
    public void createSettingsPlayerSettingsSeekerInventory(Player player) {
        Inventory Seeker = Bukkit.createInventory(player, 54,"Seeker");
        int[] redglass = new int[]{0,1,3,5,7,8,9,17,18,26,27,35,36,44,45,46,47,48,50,52,53};
        int[] orange = new int[]{13,22,31,40};
        int[] ender = new int []{10,11,12,28,29,30};
        int[] ender2 = new int[]{14,15,16,32,33};
        for(int i = 0; i < redglass.length; i++){Seeker.setItem(redglass[i], new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        for(int i = 0; i < orange.length; i++){Seeker.setItem(orange[i], new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        for(int i = 0; i < ender.length; i++) {Seeker.setItem(ender[i], new ItemBuilder(Material.ENDER_EYE).setDisplayname("§4Seeker§e: "+(i+1)).build());}
        for(int i = 0; i < ender2.length; i++){Seeker.setItem(ender2[i], new ItemBuilder(Material.ENDER_EYE).setDisplayname("§bTime§e: "+(i+1)*10).setLore("§7Time in Seconds").build());}
        Seeker.setItem(49,new ItemBuilder(Material.COMPARATOR).setDisplayname("§f").setLore("§3Click to go Back to the Settings Menu").setLocalizedName("settings.playersettings.Seeker.comperator").build());
        Seeker.setItem(2,new ItemBuilder(Material.PLAYER_HEAD).setDisplayname("§9MaxSeekerCountBeginn").setLore("§7Maximum Player Count at the beginning of an game").setLocalizedName("settings.playersettings.Seeker.Head").build());
        Seeker.setItem(4,new ItemBuilder(Material.NETHERITE_SWORD).setDisplayname("§cSeeker").setLore("§7 The Seeker´s Menu").setLocalizedName("settings.playersettings.Seeker.Sword").build());
        Seeker.setItem(6,new ItemBuilder(Material.COMMAND_BLOCK).setDisplayname("§d TimetoSpawn").setLore("§1How much time the Hider have before the Seeker Spawn").setLocalizedName("settings.playersettings.Seeker.Commandblock").build());
        Seeker.setItem(45,new ItemBuilder(Material.ARROW).setDisplayname("§7Back").setLore("§c").setLocalizedName("settings.playersettings.seeker.back").build());
        Seeker.setItem(51,new ItemBuilder(Material.OAK_SIGN).setDisplayname("§fCustom Time").setLore("§7 In Seconds").setLocalizedName("settings.playersettings.seeker.Search").build());
        Seeker.setItem(34,new ItemBuilder(Material.ENDER_PEARL).setDisplayname("§b Custom Time§e: "+(plugin.getConfig().getInt("Settings.HidingTime"))).setLore("§7 In Seconds").setLocalizedName("settings.playersettings.seeker.Search").build());
        int Seekercount = plugin.getConfig().getInt("Settings.Seeker");
        if (Seekercount <= 0){plugin.getConfig().set("Settings.Seeker", 1);}
        if (Seekercount >= 7){plugin.getConfig().set("Settings.Seeker", 6);}
        plugin.saveConfig();
        int[] Disabled = new int[]{19,20,21,37,38,39};
        int[] Disabled2 = new int[]{23,24,25,41,42,43};
        for(int i = 0; i < Disabled.length; i++){Seeker.setItem(Disabled[i], new ItemBuilder(Material.RED_DYE).setDisplayname("§4Disabled").setLore("§3Click to Enable").setLocalizedName("settings.playersettings.Seeker.red_dye"+(i+1)).build());}
        for(int i = 0; i < Disabled2.length; i++){Seeker.setItem(Disabled2[i], new ItemBuilder(Material.RED_DYE).setDisplayname("§4Disabled").setLore("§3Click to Enable").setLocalizedName("settings.playersettings.Seeker.red_dye.s"+(i+1)).build());}
        switch (plugin.getConfig().getInt("Settings.Seeker")) {
            case 1:
                Seeker.setItem(19,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.playersettings.Seeker.Lime_dye1").build());
                break;
            case 2:
                Seeker.setItem(20,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.playersettings.Seeker.Lime_dye2").build());
                break;
            case 3:
                Seeker.setItem(21,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.playersettings.Seeker.Lime_dye3").build());
                break;
            case 4:
                Seeker.setItem(37,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.playersettings.Seeker.Lime_dye4").build());
                break;
            case 5:
                Seeker.setItem(38,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.playersettings.Seeker.Lime_dye5").build());
                break;
            case 6:
                Seeker.setItem(39,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.playersettings.Seeker.Lime_dye6").build());
                break;
        }
        switch (plugin.getConfig().getInt("Settings.HidingTime"))
        {
            case 10:
                Seeker.setItem(23,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.playersettings.Seeker.Lime_Dye.s.1").build());
                break;
            case 20:
                Seeker.setItem(24,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.playersettings.Seeker.Lime_Dye.s.2").build());
                break;
            case 30:
                Seeker.setItem(25,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.playersettings.Seeker.Lime_Dye.s.3").build());
                break;
            case 40:
                Seeker.setItem(41,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.playersettings.Seeker.Lime_Dye.s.4").build());
                break;
            case 50:
                Seeker.setItem(42,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.playersettings.Seeker.Lime_Dye.s.5").build());
                break;

        }
        player.openInventory(Seeker);
    }
    public void createSettingsPlayerSettingsHiderInventory(Player player) {Inventory Hider = Bukkit.createInventory(player, 54,"Hider");int[] redglass = new int[]{0,1,2,3,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53};for(int i = 0; i < redglass.length; i++){Hider.setItem(redglass[i], new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").setLore("§c").setLocalizedName("hider").build());}
        Hider.setItem(4, new ItemBuilder(Material.SHIELD).setDisplayname("§aHider").setLore("§c").setLocalizedName("settings.playersettings.hider.shield").build());Hider.setItem(22, new ItemBuilder(Material.STRUCTURE_VOID).setDisplayname("§0Nothingishere").setLore("§7 but...").setLocalizedName("settings.playersettings.hider.structurvoid").build());
        Hider.setItem(45,new ItemBuilder(Material.ARROW).setDisplayname("§7Back").setLore("§c").setLocalizedName("settings.playersettings.Hider.back").build());
        Hider.setItem(49,new ItemBuilder(Material.COMPARATOR).setDisplayname("§f").setLore("§3Click to go Back to the Settings Menu").setLocalizedName("settings.playersettings.Hider.comperator").build());
        player.openInventory(Hider);
    }

    public void createMapEditInventory(Player player, String mapname){
        Inventory mapInventory = Bukkit.createInventory(player, 54, "Edit Map");
        int[] orangeglass = new int[]{0,1,2,3,5,6,7,8,9,17,18,26,27,35,36,44,45,46,47,48,50,51,52,53};
        for(int i = 0; i < orangeglass.length; i++){
            mapInventory.setItem(orangeglass[i], new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        }
        int[] whiteglass = new int[]{10,16,19,25,28,29,31,33,34,38,39,40,41,42};
        for(int i = 0; i < whiteglass.length; i++){
            mapInventory.setItem(whiteglass[i], new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        }
        mapInventory.setItem(4, new ItemBuilder(Material.PAPER).setDisplayname("§6"+mapname).setLore("§7You currently editing this map.").build());
        if(plugin.yaml.contains("Arenas."+mapname+".Seeker")){
            mapInventory.setItem(30, new ItemBuilder(Material.RED_BED).setDisplayname("§c§lSeeker Spawn").setLore("§7Click to set the Seeker Spawn.","§cIt will use your coordinates and facing.","","§a✔ COMPLETE").setLocalizedName("map.seeker").build());
        }else{
            mapInventory.setItem(30, new ItemBuilder(Material.RED_BED).setDisplayname("§c§lSeeker Spawn").setLore("§7Click to set the Seeker Spawn.","§cIt will use your coordinates and facing.","","§4✘ INCOMPLETE").setLocalizedName("map.seeker").build());
        }
        if(plugin.yaml.contains("Arenas."+mapname+".Spectator")){
            mapInventory.setItem(32, new ItemBuilder(Material.WHITE_BED).setDisplayname("§2§lSpectator Spawn").setLore("§7Click to set the Spectator Spawn.","§cIt will use your coordinates and facing.","","§a✔ COMPLETE").setLocalizedName("map.spec").build());
        }else{
            mapInventory.setItem(32, new ItemBuilder(Material.WHITE_BED).setDisplayname("§2§lSpectator Spawn").setLore("§7Click to set the Spectator Spawn.","§cIt will use your coordinates and facing.","","§4✘ INCOMPLETE").setLocalizedName("map.spec").build());
        }
        mapInventory.setItem(37, new ItemBuilder(Material.COMPARATOR).setDisplayname("§4Settings").setLore("§7Open the Settings to configurate more.","§cIt will use your coordinates and facing.").setLocalizedName("map.settings").build());
        mapInventory.setItem(43, new ItemBuilder(Material.FIREWORK_ROCKET).setDisplayname("§a§lFINISH").setLore("§7Finish the setup.","§cYou must have everything completed.","§4If it is not finished, you can't play on the map.").setLocalizedName("map.finish").build());
        mapInventory.setItem(49, new ItemBuilder(Material.NETHER_STAR).setDisplayname("§e§lMain Menu").setLocalizedName("map.main").build());
        for(int i = 1; i < 11; i++){
            if(plugin.yaml.contains("Arenas."+mapname+"."+i)) {
                mapInventory.addItem( new ItemBuilder(Material.LIME_BED).setDisplayname("§2§lHider Spawn "+i).setLore("§7Click to set the Hider Spawn §7§l"+i+"§7.", "§cIt will use your coordinates and facing.","","§a✔ COMPLETE").setLocalizedName("map.hider"+i).build());
            }else{
                mapInventory.addItem( new ItemBuilder(Material.LIME_BED).setDisplayname("§2§lHider Spawn "+i).setLore("§7Click to set the Hider Spawn §7§l"+i+"§7.", "§cIt will use your coordinates and facing.","","§4✘ INCOMPLETE").setLocalizedName("map.hider"+i).build());
            }
        }
        player.openInventory(mapInventory);
    }

    public void createDeleteInventory(Player player, String mapname, int count, boolean isFinsihed){
        Inventory confirmInventory = Bukkit.createInventory(player, 27, "§4Delete a Map");
        if(isFinsihed){
            confirmInventory.setItem(11,new ItemBuilder(Material.RED_STAINED_GLASS).setDisplayname("§4Confirm").setLore("§4The Map be deleted FOREVER!","§eClick to Confirm").build());
        }else
            confirmInventory.setItem(11,new ItemBuilder(Material.BARRIER).setDisplayname("§4Confirm").setLore("§4The Map be deleted FOREVER!","§7Please wait "+count+" seconds to confirm.").build());
        confirmInventory.setItem(13,new ItemBuilder(Material.PAPER).setDisplayname("§6"+mapname).setLore("§7The Map you want to delete.").build());
        confirmInventory.setItem(15,new ItemBuilder(Material.RED_CONCRETE).setDisplayname("§4Cancel").build());
        player.openInventory(confirmInventory);
    }

    public void createNewMapInventory(Player player){
        Inventory settingsInventory = Bukkit.createInventory(player, 45, "Create a new Map");
        settingsInventory.clear();
        int[] limeglass = new int[]{0,1,2,3,4,5,6,7,8,9,17,18,26,27,35,36,37,38,39,40,41,42,43,44};
        for(int i = 0; i < limeglass.length; i++){
            settingsInventory.setItem(limeglass[i], new ItemBuilder(Material.LIME_STAINED_GLASS_PANE).setDisplayname("§c").build());
        }
        int[] whiteglass = new int[]{10,11,12,13,14,15,16,19,21,23,25,28,29,30,31,32,33,34};
        for(int i = 0; i < whiteglass.length; i++){
            settingsInventory.setItem(whiteglass[i], new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        }
        settingsInventory.setItem(20, new ItemBuilder(Material.NAME_TAG).setDisplayname("§3Map Name").setLore("§7Click to set the Map Name","§2Currently: §6"+plugin.yaml.getString("CreateMaps."+player.getName()+".Name")).setLocalizedName("createMap.name").build());
        settingsInventory.setItem(22, new ItemBuilder(Material.FIREWORK_ROCKET).setDisplayname("§2§lFINISH").setLocalizedName("createMap.finish").build());
        settingsInventory.setItem(24, new ItemBuilder(Material.GRASS_BLOCK).setDisplayname("§3Builder").setLore("§7Click to set the Map Builder","§2Currently: §6"+plugin.yaml.getString("CreateMaps."+player.getName()+".Builder")).setLocalizedName("createMap.builder").build());
        settingsInventory.setItem(40, new ItemBuilder(Material.NETHER_STAR).setDisplayname("§eMain Menu").setLocalizedName("createMap.main").build());
        player.openInventory(settingsInventory);
    }

}
