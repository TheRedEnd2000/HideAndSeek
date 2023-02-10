package de.theredend2000.hideandseek.menus;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.countdowns.LobbyCountdown;
import de.theredend2000.hideandseek.gamestates.LobbyState;
import de.theredend2000.hideandseek.util.ConfigLocationUtil;
import de.theredend2000.hideandseek.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

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
                if(args.length == 0){
                    plugin.getCommandMessagesManager().sendHelpMessage(player);
                    plugin.getCommandMessagesManager().sendInfoMessage(player);
                    return true;
                }else if(args.length == 1){
                    if(args[0].equalsIgnoreCase("help")){
                        plugin.getCommandMessagesManager().sendHelpMessage(player);
                    }else if(args[0].equalsIgnoreCase("info")){
                        plugin.getCommandMessagesManager().sendInfoMessage(player);
                    }else if(args[0].equalsIgnoreCase("chest")){
                        Inventory inventory = Bukkit.createInventory(player, 54, "Chest");
                        for(int i = 0; i < 54; i++){
                            inventory.setItem(i,new ItemBuilder(Material.WHITE_STAINED_GLASS_PANE).setDisplayname(String.valueOf(i)).build());
                        }
                        player.openInventory(inventory);
                    }else
                    if(args[0].equalsIgnoreCase("start")){
                        String permission = plugin.getConfig().getString("Permissions.StartGameCommand");
                        if(player.hasPermission(permission)){
                            if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState){
                                LobbyCountdown lobbyCountdown = new LobbyCountdown(plugin,plugin.getGameStateManager());
                                lobbyCountdown.setSeconds(5);
                            }
                        }else
                            player.sendMessage(Main.PREFIX+plugin.getConfig().getString("Messages.NoPermissionMessage"));
                    }else
                        player.sendMessage(Main.PREFIX+"§7Usage: §6/hideandseek");
                }else
                    player.sendMessage(Main.PREFIX+"§7Usage: §6/hideandseek");
        }
        return false;
    }
}
