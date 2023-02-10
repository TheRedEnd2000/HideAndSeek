package de.theredend2000.hideandseek.menus;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.voting.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;

public class AdminCommand implements CommandExecutor {

    private Main plugin;

    public AdminCommand(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            String permission = plugin.getConfig().getString("Permissions.UseAdminCommand");
            if(player.hasPermission(permission)){
               plugin.getMenuManager().createInventory(player);
               return true;
            }else{
                player.sendMessage(Main.PREFIX+plugin.getConfig().getString("Messages.NoPermissionMessage"));
            }
        }
        return false;
    }
}
