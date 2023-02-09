package de.theredend2000.hideandseek.menus;

import de.theredend2000.hideandseek.Main;
import org.bukkit.entity.Player;

public class CommandMessagesManager {

    private Main plugin;

    public CommandMessagesManager(Main plugin){
        this.plugin = plugin;
    }

    public void sendHelpMessage(Player player){
        player.sendMessage("§b----------§4§lCommands§b----------");
        player.sendMessage("");
        player.sendMessage("§2§lCommands:");
        player.sendMessage("§6/hideandseek §8--> §7Opens this text.");
        player.sendMessage("§6/hideandseek help §8--> §7Opens this text.");
        player.sendMessage("§6/hideandseek menu §8--> §7Shows the menu for everything.");
        player.sendMessage("§6/hideandseek info §8--> §7Shows infos about the plugin.");
        player.sendMessage("");
        player.sendMessage("§b----------§4§lCommands§b----------");
    }

    public void sendInfoMessage(Player player){
        player.sendMessage("§b----------§4§lINFOS§b----------");
        player.sendMessage("");
        player.sendMessage("§2§lInfos:");
        player.sendMessage("§7Name: §6"+plugin.getDescription().getName());
        player.sendMessage("§7Plugin Version: §6"+plugin.getDescription().getVersion());
        player.sendMessage("§7Newest Version: §6Soon");
        player.sendMessage("§7Authors: §6TheRedEnd2000, Lumpi_2");
        player.sendMessage("");
        player.sendMessage("§b----------§4§lINFOS§b----------");
    }

}
