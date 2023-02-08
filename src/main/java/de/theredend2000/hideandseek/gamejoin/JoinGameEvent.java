package de.theredend2000.hideandseek.gamejoin;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.gamestates.EndingState;
import de.theredend2000.hideandseek.gamestates.IngameState;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class JoinGameEvent implements Listener {

    private Main plugin;

    public JoinGameEvent(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignEdit(SignChangeEvent event) {
        if(event.getPlayer().hasPermission(s)){
            String line0 = event.getLine(0);
            String line1 = event.getLine(1);
            if (line0 != null && line0.equalsIgnoreCase("[hs]") && line1 != null && line1.equalsIgnoreCase("join")) {
                event.setLine(0, "§b-=-=-=-");
                event.setLine(1, "§6[§4Hide and Seek§6]");
                event.setLine(2,"§aClick to Join");
                event.setLine(3, "§b-=-=-=-");
            }else if (line0 != null && line0.equalsIgnoreCase("[hs]") && line1 != null && line1.equalsIgnoreCase("leave")) {
                event.setLine(0, "§b-=-=-=-");
                event.setLine(1, "§6[§4Hide and Seek§6]");
                event.setLine(2,"§cClick to Leave");
                event.setLine(3, "§b-=-=-=-");
            }
        }else
            event.getPlayer().sendMessage(Main.NO_PERMISSION);
    }

    @EventHandler
    public void onClickSignToJoinEvent(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if(event.getClickedBlock() == null) return;
        if(!(event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR)) return;
        if(event.getClickedBlock().getState() instanceof Sign) {
            String warp = ((Sign) event.getClickedBlock().getState()).getLine(2);
            String warp2 = ((Sign) event.getClickedBlock().getState()).getLine(1);
            if (warp.equalsIgnoreCase("§aClick to Join") && warp2.equalsIgnoreCase("§6[§4Hide and Seek§6]]")) {
                plugin.getJoinGameManager().joinGame(player);
            }
        }
    }
}
