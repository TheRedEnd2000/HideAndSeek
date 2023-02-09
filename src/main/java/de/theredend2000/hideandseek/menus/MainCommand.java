package de.theredend2000.hideandseek.menus;

import de.theredend2000.hideandseek.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Objects;

public class MainCommand implements CommandExecutor {

    private Main plugin;

    public MainCommand(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            if(player.hasPermission(Objects.requireNonNull(plugin.getConfig().getString("Permissions.UseMainCommand")))){
                if(args.length == 0){
                    plugin.getCommandMessagesManager().sendHelpMessage(player);
                    plugin.getCommandMessagesManager().sendInfoMessage(player);
                    return true;
                }else if(args.length == 1){
                    if(args[0].equalsIgnoreCase("help")){
                        plugin.getCommandMessagesManager().sendHelpMessage(player);
                    }else if(args[0].equalsIgnoreCase("info")){
                        plugin.getCommandMessagesManager().sendInfoMessage(player);
                    }else if(args[0].equalsIgnoreCase("menu")){
                        plugin.getMainMenuManager().createInventory(player);
                    }else if(args[0].equalsIgnoreCase("s")){
                    }
                }else
                    player.sendMessage(Main.PREFIX+"§7Usage: §6/hideandseek");
            }else{
                player.sendMessage(Main.PREFIX+plugin.getConfig().getString("Messages.NoPermissionMessage").replaceAll("&","§"));
            }
        }
        return false;
    }
}
