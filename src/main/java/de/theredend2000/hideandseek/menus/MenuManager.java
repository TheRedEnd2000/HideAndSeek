package de.theredend2000.hideandseek.menus;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.util.ItemBuilder;
import de.theredend2000.hideandseek.voting.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

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
        mapInventory.setItem(53, new ItemBuilder(Material.ARROW).setDisplayname("§7Next").setLocalizedName("next").build());
        mapInventory.setItem(50, new ItemBuilder(Material.OAK_SIGN).setDisplayname("§3Search Map").setLore("§2Click here to search for a Map.").setLocalizedName("search").build());
        if(plugin.yaml.contains("lobby") && plugin.yaml.contains("end")) {
            mapInventory.setItem(48, new ItemBuilder(Material.LAPIS_BLOCK).setDisplayname("§5Setup Lobby and End Location").setLore("§7Click to open.","","§a✔ COMPLETE").setLocalizedName("lobby/end").build());
        }else
            mapInventory.setItem(48, new ItemBuilder(Material.LAPIS_BLOCK).setDisplayname("§5Setup Lobby and End Location").setLore("§7Click to open.","","§4✘ INCOMPLETE").setLocalizedName("lobby/end").build());
        mapInventory.setItem(49, new ItemBuilder(Material.EMERALD_BLOCK).setDisplayname("§6§lCreate Map").setLore("§2Click here to create a new Map.").setLocalizedName("createMap").build());
        if(plugin.yaml.contains("Arenas")) {
            for (String maps : plugin.yaml.getConfigurationSection("Arenas.").getKeys(false)) {
                String author = plugin.yaml.getString("Arenas." + maps + ".Builder");
                if (mapInventory.getItem(43) == null) {
                    mapInventory.addItem(new ItemBuilder(Material.PAPER).setDisplayname("§5§l" + maps).setLore("", "§7Builder: §6" + author, "", "§7Finished: §6§l" + plugin.yaml.getBoolean("Arenas." + maps + ".isFinished"), "§2LEFT-CLICK §7Select this Map and Open Options", "§2RIGHT-CLICK §7Delete the Map").build());
                } else {
                    Bukkit.getConsoleSender().sendMessage("Map added to page 2: " + maps);
                }
            }
        }else
            mapInventory.setItem(22, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§4No Maps").setLore("§2Click the emerald block to create one.").build());
        player.openInventory(mapInventory);
    }

    public void createSettingsInventory(Player player){
        Inventory settingsInventory = Bukkit.createInventory(player, 54, "Settings");
        int[] redglass = new int[]{0,1,2,3,5,6,7,8,9,10,16,17,18,26,27,35,36,37,43,44,45,46,47,48,50,51,52,53};
        for(int i = 0; i < redglass.length; i++){settingsInventory.setItem(redglass[i], new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        int[] orange = new int[]{12,13,14,19,20,21,23,24,25,28,29,30,22,32,33,34,38,39,40,41,42};
        for(int i = 0; i < orange.length; i++) {settingsInventory.setItem(orange[i], new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        settingsInventory.setItem(4, new ItemBuilder(Material.COMPARATOR).setDisplayname("§4Settings").build());
        ItemStack is = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta isMeta = (SkullMeta) is.getItemMeta();
        isMeta.setOwner("craftingtable");
        isMeta.setDisplayName("§6§lPlayerSettings");
        isMeta.setLore(Arrays.asList("§2Click here to open the Player Settings."));
        isMeta.setLocalizedName("settings.playersettings");
        is.setItemMeta(isMeta);
        settingsInventory.setItem(11, is);
        settingsInventory.setItem(15,new ItemBuilder(Material.NETHERITE_AXE).setDisplayname("§5Abilitys").setLore("Click her to open the Ability Menu").setLocalizedName("settings.Ability").build());
        int time = plugin.getConfig().getInt("Settings.TimeToPlay");
        settingsInventory.setItem(31,new ItemBuilder(Material.CLOCK).setDisplayname("§2Playtime").setLore("§5LEFT-CLICK§e: §a+1 min","§5RIGHT-CLICK§e: §c-1 min","§5MIDDLE-CLICK§e: §a+10 min","§5DROP§e: §c-10 min","","§7Currently: §6"+shortInteger(time)).setLocalizedName("settings.Playtime").build());
        settingsInventory.setItem(49, new ItemBuilder(Material.NETHER_STAR).setDisplayname("§eMain Menu").setLore("§7Click to go back to the Main Menu").setLocalizedName("settings.Mainmenu").build());
        player.openInventory(settingsInventory);
    }

    public void createLobbyAndEndInventory(Player player) {
        Inventory lobbyAndEndLocation = Bukkit.createInventory(player, 9,"Lobby and End Location");
        int[] redglass = new int[]{0,1,2,3,4,5,6,7,8};
        for(int i = 0; i < redglass.length; i++) {lobbyAndEndLocation.setItem(redglass[i], new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        lobbyAndEndLocation.setItem(0, new ItemBuilder(Material.ARROW).setDisplayname("§7Back").setLocalizedName("l-e.back").build());
        lobbyAndEndLocation.setItem(6,new ItemBuilder(Material.BOOK).setDisplayname("§6§lInfos").setLore("").build());
        if(plugin.yaml.contains("lobby")){
            lobbyAndEndLocation.setItem(7,new ItemBuilder(Material.EMERALD_BLOCK).setDisplayname("§2Lobby Location").setLore("§7Set the location were all players will be teleported when joining a game.","","§a✔ COMPLETE").setLocalizedName("l-e.lobby").build());
        }else{
            lobbyAndEndLocation.setItem(7,new ItemBuilder(Material.EMERALD_BLOCK).setDisplayname("§2Lobby Location").setLore("§7Set the location were all players will be teleported when joining a game.","","§4✘ INCOMPLETE").setLocalizedName("l-e.lobby").build());
        }
        if(plugin.yaml.contains("end")){
            lobbyAndEndLocation.setItem(8,new ItemBuilder(Material.REDSTONE_BLOCK).setDisplayname("§2Ending Location").setLore("§7Set the location were all players will be teleported after a game.","","§a✔ COMPLETE").setLocalizedName("l-e.end").build());
        }else{
            lobbyAndEndLocation.setItem(8,new ItemBuilder(Material.REDSTONE_BLOCK).setDisplayname("§2Ending Location").setLore("§7Set the location were all players will be teleported after a game.","","§4✘ INCOMPLETE").setLocalizedName("l-e.end").build());
        }
        player.openInventory(lobbyAndEndLocation);
    }

    public void createSettingsPlayerSettingsInventory(Player player){
        Inventory settingsInventory = Bukkit.createInventory(player, 54, "Player Settings");
        int[] redglass2 = new int[]{0,1,2,3,5,6,7,8,9,17,18,26,27,35,36,41,44,46,47,48,50,51,52,53};
        int[] oragne = new int[]{11,15,19,20,22,24,25,28,34,37,40,43};
        int[] white = new int[]{12,14,21,23,30,32,39,41};
        for(int i = 0; i < redglass2.length; i++){settingsInventory.setItem(redglass2[i], new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        for(int i = 0; i < oragne.length; i++){settingsInventory.setItem(oragne[i], new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        for(int i = 0; i < white.length; i++){settingsInventory.setItem(white[i], new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        settingsInventory.setItem(31, new ItemBuilder(Material.REDSTONE_TORCH).setDisplayname("§3MinPlayerCount").setLore("§6LEFT-CLICK §e: §a+1","§6RIGHT-CLICK §e: §a-1","§7Currently§e:§5 "+plugin.getConfig().getInt("Settings.MinPlayerCount")).setLocalizedName("settings.playersettings.MinPlayerCount").build());
        settingsInventory.setItem(13,new ItemBuilder(Material.NETHERITE_SWORD).setDisplayname("§4Seeker").setLore("§7Click for Seeker Settings").setLocalizedName("settings.playersettings.Seeker").build());
        settingsInventory.setItem(16,new ItemBuilder(Material.SHIELD).setDisplayname("§aHider").setLore("§7Click for Hider Settings").setLocalizedName("settings.playersettings.Hider").build());
        settingsInventory.setItem(29,new ItemBuilder(Material.OAK_DOOR).setDisplayname("§bSwitchTeam").setLore("§7Activate if you want to become a Seeker after you died as Hider").build());
        settingsInventory.setItem(33,new ItemBuilder(Material.STRUCTURE_VOID).setDisplayname("§0Nothingishere").setLore("§8Nothing is here too").build());
        if(plugin.getConfig().getBoolean("Settings.SwitchTeam")){
            settingsInventory.setItem(38,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§7Click to Disable").setLocalizedName("settings.playersettings.SwitchTeam.en").build());
        }else
            settingsInventory.setItem(38,new ItemBuilder(Material.RED_DYE).setDisplayname("§4Disabled").setLore("§7Click to Enable").setLocalizedName("settings.playersettings.SwitchTeam.dis").build());
        settingsInventory.setItem(42,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLocalizedName("settings.playersettings.Nothingishere.Green_dye").build());
        settingsInventory.setItem(49,new ItemBuilder(Material.COMPARATOR).setDisplayname("§eMain Menu").setLocalizedName("settings.playersettings.comperator").build());
        settingsInventory.setItem(4,new ItemBuilder(Material.PLAYER_HEAD).setDisplayname("§6Player Settings").build());
        settingsInventory.setItem(45,new ItemBuilder(Material.ARROW).setDisplayname("§7Back").setLocalizedName("settings.playersettings.back").build());
        settingsInventory.setItem(10,new ItemBuilder(Material.FEATHER).setDisplayname("§5Spectators").setLocalizedName("settings.playersettings.Spectator").build());
        player.openInventory(settingsInventory);
    }
    public void createSettingsPlayerSettingsSeekerInventory(Player player) {
        Inventory Seeker = Bukkit.createInventory(player, 54,"Seeker");
        int[] redglass = new int[]{0,1,3,5,7,8,9,17,18,26,27,13,22,31,40,35,36,44,45,46,47,48,50,51,52,53};
        int[] orange = new int[]{10,11,12,20,29,37,38,39};
        int[] ender = new int[]{19,21};
        int[] ender2 = new int[]{14,15,16,32,33,34};
        for(int i = 0; i < redglass.length; i++){Seeker.setItem(redglass[i], new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        for(int i = 0; i < orange.length; i++){Seeker.setItem(orange[i], new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        for(int i = 0; i < ender.length; i++){Seeker.setItem(ender[i], new ItemBuilder(Material.ENDER_EYE).setDisplayname("§bSeeker§e: "+(i+1)).build());}
        for(int i = 0; i < ender2.length; i++){Seeker.setItem(ender2[i], new ItemBuilder(Material.ENDER_EYE).setDisplayname("§bTime§e: "+(i+1)*10).setLore("§7Time in Seconds").build());}
        Seeker.setItem(49,new ItemBuilder(Material.COMPARATOR).setDisplayname("§4Settings Menu").setLocalizedName("settings.playersettings.Seeker.comperator").build());
        Seeker.setItem(2,new ItemBuilder(Material.PLAYER_HEAD).setDisplayname("§6Max Seeker").setLore("§7Maximum Seeker Count in a game.").setLocalizedName("settings.playersettings.Seeker.Head").build());
        Seeker.setItem(4,new ItemBuilder(Material.NETHERITE_SWORD).setDisplayname("§cSeeker").setLore("§7The Seeker's Menu").setLocalizedName("settings.playersettings.Seeker.Sword").build());
        Seeker.setItem(6,new ItemBuilder(Material.COMMAND_BLOCK).setDisplayname("§6TimeToSpawn").setLore("§7How much time the Hider have before the Seeker Spawn").setLocalizedName("settings.playersettings.Seeker.Commandblock").build());
        Seeker.setItem(45,new ItemBuilder(Material.ARROW).setDisplayname("§8Back").setLocalizedName("settings.playersettings.seeker.back").build());
        plugin.saveConfig();
        int[] Disabled = new int[]{28,30};
        int[] Disabled2 = new int[]{23,24,25,41,42,43};
        for(int i = 0; i < Disabled.length; i++){Seeker.setItem(Disabled[i], new ItemBuilder(Material.RED_DYE).setDisplayname("§4Click to Select").setLocalizedName("settings.playersettings.Seeker.select"+(i+1)).build());}
        for(int i = 0; i < Disabled2.length; i++){Seeker.setItem(Disabled2[i], new ItemBuilder(Material.RED_DYE).setDisplayname("§4Click to Select").setLocalizedName("settings.playersettings.Seeker.select.time"+(i+1)*10).build());}
        switch (plugin.getConfig().getInt("Settings.Seeker")) {
            case 1:
                Seeker.setItem(28, new ItemBuilder(Material.LIME_DYE).setDisplayname("§aSelected").build());
                break;
            case 2:
                Seeker.setItem(30,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aSelected").build());
                break;
        }
        switch (plugin.getConfig().getInt("Settings.TimeToHide")) {
            case 10:
                Seeker.setItem(23,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aSelected").build());
                break;
            case 20:
                Seeker.setItem(24,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aSelected").build());
                break;
            case 30:
                Seeker.setItem(25,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aSelected").build());
                break;
            case 40:
                Seeker.setItem(41,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aSelected").build());
                break;
            case 50:
                Seeker.setItem(42,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aSelected").build());
                break;
            case 60:
                Seeker.setItem(43,new ItemBuilder(Material.LIME_DYE).setDisplayname("§aSelected").build());
                break;
        }
        player.openInventory(Seeker);
    }
    public void createSettingsPlayerSettingsHiderInventory(Player player) {
        Inventory Hider = Bukkit.createInventory(player, 54,"Hider");
        int[] redglass = new int[]{0,1,2,3,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53};
        for(int i = 0; i < redglass.length; i++){Hider.setItem(redglass[i], new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").setLore("§c").setLocalizedName("hider").build());}
        Hider.setItem(4, new ItemBuilder(Material.SHIELD).setDisplayname("§aHider").setLore("§c").setLocalizedName("settings.playersettings.hider.shield").build());
        Hider.setItem(22, new ItemBuilder(Material.STRUCTURE_VOID).setDisplayname("§0Nothingishere").setLore("§7 but...").setLocalizedName("settings.playersettings.hider.structurvoid").build());
        Hider.setItem(45,new ItemBuilder(Material.ARROW).setDisplayname("§7Back").setLore("§c").setLocalizedName("settings.playersettings.Hider.back").build());
        Hider.setItem(49,new ItemBuilder(Material.COMPARATOR).setDisplayname("§eSettings").setLore("§3Click to go back to the Settings Menu").setLocalizedName("settings.playersettings.Hider.comperator").build());
        player.openInventory(Hider);
    }
    public void createSettingsAbillitysSeekerInventory(Player player)
    {Inventory Abilitys = Bukkit.createInventory(player, 54, "Abilities Seeker");
        int[] redglass = new int[]{0,1,2,3,5,6,7,8,9,17,18,26,27,13,22,31,40,35,36,44,45,46,47,48,50,51,52,53};
        int[] white = new int[]{10,11,12,14,15,16,19,20,21,22,23,24,25,28,24,34,37,43};
        for (int i = 0; i < redglass.length; i++){Abilitys.setItem(redglass[i], new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        for (int i = 0; i < white.length; i++){Abilitys.setItem(white[i], new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        Abilitys.setItem(4 , new ItemBuilder(Material.NETHERITE_AXE).setDisplayname("§3Abilities").build());
        Abilitys.setItem(13, new ItemBuilder(Material.LIME_CONCRETE).setDisplayname("§aAbilities Enabled").setLore("§3Click to Disable").setLocalizedName("settings.Abilities.Lime_concrete").build());
        Abilitys.setItem(29, new ItemBuilder(Material.SHIELD).setDisplayname("Invincible").setLocalizedName("settings.Abilities.Invincible").build());
        Abilitys.setItem(30, new ItemBuilder(Material.BOW).setDisplayname("Blindness").setLocalizedName("settings.Abilities.Blindness").build());
        Abilitys.setItem(31, new ItemBuilder(Material.TRIDENT).setDisplayname("Slowness").setLocalizedName("settings.Abilities.Slowness").build());
        Abilitys.setItem(32, new ItemBuilder(Material.GLOWSTONE_DUST).setDisplayname("Glowing").setLocalizedName("settings.Abilities.Glowing").build());
        Abilitys.setItem(33, new ItemBuilder(Material.BLUE_ICE).setDisplayname("Freeze").setLocalizedName("settings.Abilities.Freeze").build());
        Abilitys.setItem(45,new ItemBuilder(Material.ARROW).setDisplayname("§7Back").setLore("§c").setLocalizedName("settings.Abilities.back").build());
        Abilitys.setItem(49,new ItemBuilder(Material.COMPARATOR).setDisplayname("§eSettings").setLore("§3Click to go back to the Settings Menu").setLocalizedName("settings.Abilities.Settings.back").build());


        int[] DisEnabled = new int[]{38,39,40,41,42};
        for (int i = 0; i < DisEnabled.length; i++){Abilitys.setItem(DisEnabled[i], new ItemBuilder(Material.RED_DYE).setDisplayname("§4Disable").setLore("§3Click to Enable").build());}
        if (plugin.getConfig().getBoolean("Ability.Invincible")){Abilitys.setItem(38, new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.Abilities.Lime_Dye1").build());}
        if (plugin.getConfig().getBoolean("Ability.Blindness")){Abilitys.setItem(39, new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.Abilities.Lime_Dye2").build());}
        if (plugin.getConfig().getBoolean("Ability.Slowness")){Abilitys.setItem(40, new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.Abilities.Lime_Dye3").build());}
        if (plugin.getConfig().getBoolean("Ability.Glowing")){Abilitys.setItem(41, new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.Abilities.Lime_Dye4").build());}
        if (plugin.getConfig().getBoolean("Ability.Freeze")){Abilitys.setItem(42, new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.Abilities.Lime_Dye5").build());}
        player.openInventory(Abilitys);
    }

    public void createSettingsAbillitysHiderInventory(Player player){
        Inventory Abilitys = Bukkit.createInventory(player, 54, "Abilities Hider");
        int[] redglass = new int[]{0,1,2,3,5,6,7,8,9,17,18,26,27,13,22,31,40,35,36,44,45,46,47,48,50,51,52,53};
        int[] white = new int[]{10,11,12,14,15,16,19,20,21,22,23,24,25,28,24,34,37,43};
        for (int i = 0; i < redglass.length; i++){Abilitys.setItem(redglass[i], new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        for (int i = 0; i < white.length; i++){Abilitys.setItem(white[i], new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        Abilitys.setItem(4 , new ItemBuilder(Material.NETHERITE_AXE).setDisplayname("§3Abilities").build());
        Abilitys.setItem(13, new ItemBuilder(Material.LIME_CONCRETE).setDisplayname("§aAbilities Enabled").setLore("§3Click to Disable").setLocalizedName("settings.Abilities.Lime_concrete").build());
        Abilitys.setItem(29, new ItemBuilder(Material.SHIELD).setDisplayname("Invincible").setLocalizedName("settings.Abilities.Invincible").build());
        Abilitys.setItem(30, new ItemBuilder(Material.BOW).setDisplayname("Blindness").setLocalizedName("settings.Abilities.Blindness").build());
        Abilitys.setItem(31, new ItemBuilder(Material.TRIDENT).setDisplayname("Slowness").setLocalizedName("settings.Abilities.Slowness").build());
        Abilitys.setItem(32, new ItemBuilder(Material.GLOWSTONE_DUST).setDisplayname("Glowing").setLocalizedName("settings.Abilities.Glowing").build());
        Abilitys.setItem(33, new ItemBuilder(Material.BLUE_ICE).setDisplayname("Freeze").setLocalizedName("settings.Abilities.Freeze").build());
        Abilitys.setItem(45,new ItemBuilder(Material.ARROW).setDisplayname("§7Back").setLore("§c").setLocalizedName("settings.Abilities.back").build());
        Abilitys.setItem(49,new ItemBuilder(Material.COMPARATOR).setDisplayname("§eSettings").setLore("§3Click to go back to the Settings Menu").setLocalizedName("settings.Abilities.Settings.back").build());


        int[] DisEnabled = new int[]{38,39,40,41,42};
        for (int i = 0; i < DisEnabled.length; i++){Abilitys.setItem(DisEnabled[i], new ItemBuilder(Material.RED_DYE).setDisplayname("§4Disable").setLore("§3Click to Enable").build());}
        if (plugin.getConfig().getBoolean("Ability.Invincible")){Abilitys.setItem(38, new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.Abilities.Lime_Dye1").build());}
        if (plugin.getConfig().getBoolean("Ability.Blindness")){Abilitys.setItem(39, new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.Abilities.Lime_Dye2").build());}
        if (plugin.getConfig().getBoolean("Ability.Slowness")){Abilitys.setItem(40, new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.Abilities.Lime_Dye3").build());}
        if (plugin.getConfig().getBoolean("Ability.Glowing")){Abilitys.setItem(41, new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.Abilities.Lime_Dye4").build());}
        if (plugin.getConfig().getBoolean("Ability.Freeze")){Abilitys.setItem(42, new ItemBuilder(Material.LIME_DYE).setDisplayname("§aEnabled").setLore("§3Click to Disable").setLocalizedName("settings.Abilities.Lime_Dye5").build());}
        player.openInventory(Abilitys);
    }
    public void createsettingsAbilitiesInvincebleSeekerInventory(Player player){
        Inventory Invincible = Bukkit.createInventory(player,27,"InvincibleItemSettingsSeeker");
        int[] redglass = new int[]{0,1,2,3,5,6,7,8,9,17,19,20,21,22,23,24,25,26};
        for(int i = 0; i < redglass.length; i++) {Invincible.setItem(redglass[i],new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        Invincible.setItem(10,new ItemBuilder(Material.CLOCK).setDisplayname("§3Time").setLore("§7Set the Duration of the Abilitie").setLocalizedName("settings.Abilities.Invincible.Clock").build());
        Invincible.setItem(4,new ItemBuilder(Material.SHIELD).setDisplayname("§7Invincible").setLocalizedName("settings.Abilities.Invincible.Shield").build());
        Invincible.setItem(16,new ItemBuilder(Material.REPEATER).setDisplayname("§4Amount").setLore("§7Set the amount of uses before you cant use it anymore").setLocalizedName("settings.Abilities.Invincible.repeater").build());
        Invincible.setItem(11,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Invincible.setItem(15,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Invincible.setItem(12,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Invincible.setItem(14,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Invincible.setItem(13,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Invincible.setItem(18,new ItemBuilder(Material.ARROW).setDisplayname("§7").setLore("§3 Click to go Back to the Abilities Menu").setLocalizedName("settings.Abilities.Invincible.Back").build());
        Invincible.setItem(22,new ItemBuilder(Material.COMPARATOR).setDisplayname("§eSettings").setLore("§3Click to go back to the Settings Menu").setLocalizedName("settings.Abilities.Invincible.Back.s").build());
        player.openInventory(Invincible);
    }
    public void createsettingsAbilitiesInvincebleHiderInventory(Player player){
        Inventory Invincible = Bukkit.createInventory(player,27,"InvincibleItemSettingsHider");
        int[] redglass = new int[]{0,1,2,3,5,6,7,8,9,17,19,20,21,22,23,24,25,26};
        for(int i = 0; i < redglass.length; i++) {Invincible.setItem(redglass[i],new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        Invincible.setItem(10,new ItemBuilder(Material.CLOCK).setDisplayname("§3Time").setLore("§7Set the Duration of the Abilitie").setLocalizedName("settings.Abilities.Invincible.Clock").build());
        Invincible.setItem(4,new ItemBuilder(Material.SHIELD).setDisplayname("§7Invincible").setLocalizedName("settings.Abilities.Invincible.Shield").build());
        Invincible.setItem(16,new ItemBuilder(Material.REPEATER).setDisplayname("§4Amount").setLore("§7Set the amount of uses before you cant use it anymore").setLocalizedName("settings.Abilities.Invincible.repeater").build());
        Invincible.setItem(11,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Invincible.setItem(15,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Invincible.setItem(12,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Invincible.setItem(14,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Invincible.setItem(13,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Invincible.setItem(18,new ItemBuilder(Material.ARROW).setDisplayname("§7").setLore("§3 Click to go Back to the Abilities Menu").setLocalizedName("settings.Abilities.Invincible.Back").build());
        Invincible.setItem(22,new ItemBuilder(Material.COMPARATOR).setDisplayname("§eSettings").setLore("§3Click to go back to the Settings Menu").setLocalizedName("settings.Abilities.Invincible.Back.s").build());
        player.openInventory(Invincible);
    }
    public void createsettingsAbilitiesBlindnessSeekerInventory(Player player){
        Inventory Bli = Bukkit.createInventory(player,27,"BlindnessItemSettingsSeeker");
        int[] redglass = new int[]{0,1,2,3,5,6,7,8,9,17,19,20,21,22,23,24,25,26};
        for(int i = 0; i < redglass.length; i++) {Bli.setItem(redglass[i],new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        Bli.setItem(10,new ItemBuilder(Material.CLOCK).setDisplayname("§3Time").setLore("§7Set the Duration of the Abilitie").setLocalizedName("settings.Abilities.Bli.Clock").build());
        Bli.setItem(4,new ItemBuilder(Material.BOW).setDisplayname("§7Blindness").setLocalizedName("settings.Abilities.Bli.Bow").build());
        Bli.setItem(16,new ItemBuilder(Material.REPEATER).setDisplayname("§4Amount").setLore("§7Set the amount of uses before you cant use it anymore").setLocalizedName("settings.Abilities.Bli.repeater").build());
        Bli.setItem(11,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Bli.setItem(15,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Bli.setItem(12,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Bli.setItem(14,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Bli.setItem(13,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Bli.setItem(18,new ItemBuilder(Material.ARROW).setDisplayname("§7").setLore("§3 Click to go Back to the Abilities Menu").setLocalizedName("settings.Abilities.Bli.Back").build());
        Bli.setItem(22,new ItemBuilder(Material.COMPARATOR).setDisplayname("§eSettings").setLore("§3Click to go back to the Settings Menu").setLocalizedName("settings.Abilities.Bli.Back.s").build());
        player.openInventory(Bli);
    }
    public void createsettingsAbilitiesBlindnessHiderInventory(Player player){
        Inventory Bli = Bukkit.createInventory(player,27,"BlindnessItemSettingsHider");
        int[] redglass = new int[]{0,1,2,3,5,6,7,8,9,17,19,20,21,22,23,24,25,26};
        for(int i = 0; i < redglass.length; i++) {Bli.setItem(redglass[i],new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        Bli.setItem(10,new ItemBuilder(Material.CLOCK).setDisplayname("§3Time").setLore("§7Set the Duration of the Abilitie").setLocalizedName("settings.Abilities.Bli.Clock").build());
        Bli.setItem(4,new ItemBuilder(Material.BOW).setDisplayname("§7Blindness").setLocalizedName("settings.Abilities.Bli.Bow").build());
        Bli.setItem(16,new ItemBuilder(Material.REPEATER).setDisplayname("§4Amount").setLore("§7Set the amount of uses before you cant use it anymore").setLocalizedName("settings.Abilities.Bli.repeater").build());
        Bli.setItem(11,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Bli.setItem(15,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Bli.setItem(12,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Bli.setItem(14,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Bli.setItem(13,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Bli.setItem(18,new ItemBuilder(Material.ARROW).setDisplayname("§7").setLore("§3 Click to go Back to the Abilities Menu").setLocalizedName("settings.Abilities.Bli.Back").build());
        Bli.setItem(22,new ItemBuilder(Material.COMPARATOR).setDisplayname("§eSettings").setLore("§3Click to go back to the Settings Menu").setLocalizedName("settings.Abilities.Bli.Back.s").build());
        player.openInventory(Bli);
    }
    public void createsettingsAbilitiesSlownessSeekerInventory(Player player){
        Inventory Sli = Bukkit.createInventory(player,27,"SlownessItemSettingsSeeker");
        int[] redglass = new int[]{0,1,2,3,5,6,7,8,9,17,19,20,21,22,23,24,25,26};
        for(int i = 0; i < redglass.length; i++) {Sli.setItem(redglass[i],new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        Sli.setItem(10,new ItemBuilder(Material.CLOCK).setDisplayname("§3Time").setLore("§7Set the Duration of the Abilitie").setLocalizedName("settings.Abilities.Sli.Clock").build());
        Sli.setItem(4,new ItemBuilder(Material.TRIDENT).setDisplayname("§7Slowness").setLocalizedName("settings.Abilities.Sli.Trident").build());
        Sli.setItem(16,new ItemBuilder(Material.REPEATER).setDisplayname("§4Amount").setLore("§7Set the amount of uses before you cant use it anymore").setLocalizedName("settings.Abilities.Sli.repeater").build());
        Sli.setItem(11,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Sli.setItem(15,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Sli.setItem(12,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Sli.setItem(14,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Sli.setItem(13,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Sli.setItem(18,new ItemBuilder(Material.ARROW).setDisplayname("§7").setLore("§3 Click to go Back to the Abilities Menu").setLocalizedName("settings.Abilities.Sli.Back").build());
        Sli.setItem(22,new ItemBuilder(Material.COMPARATOR).setDisplayname("§eSettings").setLore("§3Click to go back to the Settings Menu").setLocalizedName("settings.Abilities.Sli.Back.s").build());
        player.openInventory(Sli);
    }
    public void createsettingsAbilitiesSlownessHiderInventory(Player player){
        Inventory Sli = Bukkit.createInventory(player,27,"SlownessItemSettingsHider");
        int[] redglass = new int[]{0,1,2,3,5,6,7,8,9,17,19,20,21,22,23,24,25,26};
        for(int i = 0; i < redglass.length; i++) {Sli.setItem(redglass[i],new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        Sli.setItem(10,new ItemBuilder(Material.CLOCK).setDisplayname("§3Time").setLore("§7Set the Duration of the Abilitie").setLocalizedName("settings.Abilities.Sli.Clock").build());
        Sli.setItem(4,new ItemBuilder(Material.TRIDENT).setDisplayname("§7Slowness").setLocalizedName("settings.Abilities.Sli.Trident").build());
        Sli.setItem(16,new ItemBuilder(Material.REPEATER).setDisplayname("§4Amount").setLore("§7Set the amount of uses before you cant use it anymore").setLocalizedName("settings.Abilities.Sli.repeater").build());
        Sli.setItem(11,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Sli.setItem(15,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Sli.setItem(12,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Sli.setItem(14,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Sli.setItem(13,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Sli.setItem(18,new ItemBuilder(Material.ARROW).setDisplayname("§7").setLore("§3 Click to go Back to the Abilities Menu").setLocalizedName("settings.Abilities.Sli.Back").build());
        Sli.setItem(22,new ItemBuilder(Material.COMPARATOR).setDisplayname("§eSettings").setLore("§3Click to go back to the Settings Menu").setLocalizedName("settings.Abilities.Sli.Back.s").build());
        player.openInventory(Sli);

    }
    public void createsettingsAbilitiesGlowingSeekerInventory(Player player){
        Inventory Gli = Bukkit.createInventory(player,27,"GlowingItemSettingsSeeker");
        int[] redglass = new int[]{0,1,2,3,5,6,7,8,9,17,19,20,21,22,23,24,25,26};
        for(int i = 0; i < redglass.length; i++) {Gli.setItem(redglass[i],new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        Gli.setItem(10,new ItemBuilder(Material.CLOCK).setDisplayname("§3Time").setLore("§7Set the Duration of the Abilitie").setLocalizedName("settings.Abilities.Gli.Clock").build());
        Gli.setItem(4,new ItemBuilder(Material.GLOWSTONE_DUST).setDisplayname("§7Glowing").setLocalizedName("settings.Abilities.Gli.Glowstonedust").build());
        Gli.setItem(16,new ItemBuilder(Material.REPEATER).setDisplayname("§4Amount").setLore("§7Set the amount of uses before you cant use it anymore").setLocalizedName("settings.Abilities.Gli.repeater").build());
        Gli.setItem(11,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Gli.setItem(15,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Gli.setItem(12,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Gli.setItem(14,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Gli.setItem(13,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Gli.setItem(18,new ItemBuilder(Material.ARROW).setDisplayname("§7").setLore("§3 Click to go Back to the Abilities Menu").setLocalizedName("settings.Abilities.Gli.Back").build());
        Gli.setItem(22,new ItemBuilder(Material.COMPARATOR).setDisplayname("§eSettings").setLore("§3Click to go back to the Settings Menu").setLocalizedName("settings.Abilities.Gli.Back.s").build());
        player.openInventory(Gli);
    }
    public void createsettingsAbilitiesGlowingHiderInventory(Player player){
        Inventory Gli = Bukkit.createInventory(player,27,"GlowingItemSettingsHider");
        int[] redglass = new int[]{0,1,2,3,5,6,7,8,9,17,19,20,21,22,23,24,25,26};
        for(int i = 0; i < redglass.length; i++) {Gli.setItem(redglass[i],new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        Gli.setItem(10,new ItemBuilder(Material.CLOCK).setDisplayname("§3Time").setLore("§7Set the Duration of the Abilitie").setLocalizedName("settings.Abilities.Gli.Clock").build());
        Gli.setItem(4,new ItemBuilder(Material.GLOWSTONE_DUST).setDisplayname("§7Glowing").setLocalizedName("settings.Abilities.Gli.Glowstonedust").build());
        Gli.setItem(16,new ItemBuilder(Material.REPEATER).setDisplayname("§4Amount").setLore("§7Set the amount of uses before you cant use it anymore").setLocalizedName("settings.Abilities.Gli.repeater").build());
        Gli.setItem(11,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Gli.setItem(15,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Gli.setItem(12,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Gli.setItem(14,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Gli.setItem(13,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Gli.setItem(18,new ItemBuilder(Material.ARROW).setDisplayname("§7").setLore("§3 Click to go Back to the Abilities Menu").setLocalizedName("settings.Abilities.Gli.Back").build());
        Gli.setItem(22,new ItemBuilder(Material.COMPARATOR).setDisplayname("§eSettings").setLore("§3Click to go back to the Settings Menu").setLocalizedName("settings.Abilities.Gli.Back.s").build());
        player.openInventory(Gli);

    }
    public void createsettingsAbilitiesFreezeSeekerInventory(Player player){
        Inventory Freeze = Bukkit.createInventory(player,27,"FreezeItemSettingsSeeker");
        int[] redglass = new int[]{0,1,2,3,5,6,7,8,9,17,19,20,21,22,23,24,25,26};
        for(int i = 0; i < redglass.length; i++) {Freeze.setItem(redglass[i],new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        Freeze.setItem(10,new ItemBuilder(Material.CLOCK).setDisplayname("§3Time").setLore("§7Set the Duration of the Abilitie").setLocalizedName("settings.Abilities.Freeze.Clock").build());
        Freeze.setItem(4,new ItemBuilder(Material.BLUE_ICE).setDisplayname("§7Freeze").setLocalizedName("settings.Abilities.Freeze.Ice").build());
        Freeze.setItem(16,new ItemBuilder(Material.REPEATER).setDisplayname("§4Amount").setLore("§7Set the amount of uses before you cant use it anymore").setLocalizedName("settings.Abilities.Freeze.repeater").build());
        Freeze.setItem(11,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Freeze.setItem(15,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Freeze.setItem(12,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Freeze.setItem(14,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Freeze.setItem(13,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Freeze.setItem(18,new ItemBuilder(Material.ARROW).setDisplayname("§7").setLore("§3 Click to go Back to the Abilities Menu").setLocalizedName("settings.Abilities.Freeze.Back").build());
        Freeze.setItem(22,new ItemBuilder(Material.COMPARATOR).setDisplayname("§eSettings").setLore("§3Click to go back to the Settings Menu").setLocalizedName("settings.Abilities.Freeze.Back.s").build());
        player.openInventory(Freeze);
    }
    public void createsettingsAbilitiesFreezeHiderInventory(Player player){
        Inventory Freeze = Bukkit.createInventory(player,27,"FreezeItemSettingsHider");
        int[] redglass = new int[]{0,1,2,3,5,6,7,8,9,17,19,20,21,22,23,24,25,26};
        for(int i = 0; i < redglass.length; i++) {Freeze.setItem(redglass[i],new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        Freeze.setItem(10,new ItemBuilder(Material.CLOCK).setDisplayname("§3Time").setLore("§7Set the Duration of the Abilitie").setLocalizedName("settings.Abilities.Freeze.Clock").build());
        Freeze.setItem(4,new ItemBuilder(Material.BLUE_ICE).setDisplayname("§7Freeze").setLocalizedName("settings.Abilities.Freeze.Ice").build());
        Freeze.setItem(16,new ItemBuilder(Material.REPEATER).setDisplayname("§4Amount").setLore("§7Set the amount of uses before you cant use it anymore").setLocalizedName("settings.Abilities.Freeze.repeater").build());
        Freeze.setItem(11,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Freeze.setItem(15,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Freeze.setItem(12,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Freeze.setItem(14,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Freeze.setItem(13,new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        Freeze.setItem(18,new ItemBuilder(Material.ARROW).setDisplayname("§7").setLore("§3 Click to go Back to the Abilities Menu").setLocalizedName("settings.Abilities.Freeze.Back").build());
        Freeze.setItem(22,new ItemBuilder(Material.COMPARATOR).setDisplayname("§eSettings").setLore("§3Click to go back to the Settings Menu").setLocalizedName("settings.Abilities.Freeze.Back.s").build());
        player.openInventory(Freeze);

    }

    public void createsettingsAbilitiesTeamChooseInventory(Player player){
        Inventory ATCI = Bukkit.createInventory(player,27, "Choose Team");
        int[] redglass = new int[]{0,1,2,3,5,6,7,8,9,17,19,20,21,22,23,24,25,26};
        for ( int i = 0; i < redglass.length; i++) {ATCI.setItem(redglass[i],new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setDisplayname("§c").build());}
        ATCI.setItem(4, new ItemBuilder(Material.STRUCTURE_VOID).setDisplayname("§fChoose Team on which you wanna ably the Abilities").build());
        ATCI.setItem(11, new ItemBuilder(Material.NETHERITE_SWORD).setDisplayname("§4Seeker").setLore("§7Click here to choose the Abilities for the Seeker Team").setLocalizedName("settings.Abilities.Playerchoose.Seeker").build());
        ATCI.setItem(15, new ItemBuilder(Material.SHIELD).setDisplayname("§aHider").setLore("§7Click here to choose the Abilities for the Hider Team").setLocalizedName("settings.Abilities.Playerchoose.Hider").build());
        ATCI.setItem(10, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        ATCI.setItem(13, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        ATCI.setItem(16, new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        ATCI.setItem(12, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        ATCI.setItem(14, new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE).setDisplayname("§c").build());
        ATCI.setItem(18, new ItemBuilder(Material.ARROW).setDisplayname("§7Back").setLore("§3Click to go back to the Settings Menu").setLocalizedName("settings.Abilities.Playerchoose.Back").build());
        ATCI.setItem(22, new ItemBuilder(Material.COMPARATOR).setDisplayname("§3Settings").setLore("§7").setLocalizedName("settings.Abilities.Playerchoose.Settings").build());
        player.openInventory(ATCI);
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
        if(plugin.yaml.contains("Arenas."+mapname+".SeekerWait")){
            mapInventory.setItem(30, new ItemBuilder(Material.RED_BED).setDisplayname("§c§lSeeker Waiting Spawn").setLore("§7Teleports the seeker to this location while the hiders are hiding.","§cIt will use your coordinates and facing.","","§a✔ COMPLETE").setLocalizedName("map.seeker1").build());
        }else{
            mapInventory.setItem(30, new ItemBuilder(Material.RED_BED).setDisplayname("§c§lSeeker Waiting Spawn").setLore("§7Teleports the seeker to this location while the hiders are hiding.","§cIt will use your coordinates and facing.","","§4✘ INCOMPLETE").setLocalizedName("map.seeker1").build());
        }
        if(plugin.yaml.contains("Arenas."+mapname+".Spectator")){
            mapInventory.setItem(32, new ItemBuilder(Material.WHITE_BED).setDisplayname("§2§lSpectator Spawn").setLore("§7Click to set the Spectator Spawn.","§cIt will use your coordinates and facing.","","§a✔ COMPLETE").setLocalizedName("map.spec").build());
        }else{
            mapInventory.setItem(32, new ItemBuilder(Material.WHITE_BED).setDisplayname("§2§lSpectator Spawn").setLore("§7Click to set the Spectator Spawn.","§cIt will use your coordinates and facing.","","§4✘ INCOMPLETE").setLocalizedName("map.spec").build());
        }
        if(plugin.yaml.contains("Arenas."+mapname+".SeekerStart")){
            mapInventory.setItem(31, new ItemBuilder(Material.RED_BED).setDisplayname("§c§lSeeker Starting Spawn").setLore("§7Teleports the seeker after the hiding time to this location.","§cIt will use your coordinates and facing.","","§a✔ COMPLETE").setLocalizedName("map.seeker2").build());
        }else{
            mapInventory.setItem(31, new ItemBuilder(Material.RED_BED).setDisplayname("§c§lSeeker Starting Spawn").setLore("§7Teleports the seeker after the hiding time to this location.","§cIt will use your coordinates and facing.","","§4✘ INCOMPLETE").setLocalizedName("map.seeker2").build());
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

    public static String shortInteger(int duration) {
        String string = "";

        int minutes = 0;
        int seconds = 0;

        if(duration / 60 >= 1){
            minutes = duration / 60;
            duration = duration - ((duration / 60 ) * 60);
        }
        if(duration >=1){
            seconds = duration;
        }
        if(minutes <= 9){
            string = string + "0"+minutes+":";
        }else {
            string = string+minutes+":";
        }
        if(seconds <= 9){
            string = string + "0"+seconds;
        }else {
            string = string+seconds;
        }

        return string;
    }

}
