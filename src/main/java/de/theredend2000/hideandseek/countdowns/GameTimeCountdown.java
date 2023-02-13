package de.theredend2000.hideandseek.countdowns;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.gamestates.IngameState;
import de.theredend2000.hideandseek.menus.MenuListener;
import de.theredend2000.hideandseek.menus.MenuManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimeCountdown extends Countdown{

    private int seconds;
    private Main plugin;

    public GameTimeCountdown(Main plugin) {
        this.plugin = plugin;
    }

    private void run(){
        if(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState){
            new BukkitRunnable() {
                @Override
                public void run() {
                    for(Player player : plugin.getGamePlayer()){
                        if(seconds == 0){
                            cancel();
                            return;
                        }
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(MenuManager.shortInteger(seconds)));
                    }
                }
            }.runTaskTimer(plugin,0,20);
        }
    }

    @Override
    public void start() {
        seconds = plugin.getConfig().getInt("Settings.TimeToPlay");
        //run();
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                switch (seconds) {
                    case 900: case 600: case 300:
                        for(Player player : plugin.getGamePlayer()){
                            player.sendMessage(Main.PREFIX+"§aThe Game ends in §6"+seconds / 60+" minutes.");
                        }
                        break;
                    case 60:
                        for(Player player : plugin.getGamePlayer()){
                            player.sendMessage(Main.PREFIX+"§aThe Game ends in §6"+seconds / 60+" minute.");
                        }
                        break;
                    case 0:
                        IngameState ingameState = (IngameState) plugin.getGameStateManager().getCurrentGameState();
                        stop();
                        for(Player player : plugin.getGamePlayer()){
                            player.sendMessage(Main.PREFIX+"§aVorbei.");
                        }
                        ingameState.checkGameEnding();
                        break;

                    default:
                        break;
                }
                seconds--;
                if(!(plugin.getGameStateManager().getCurrentGameState() instanceof IngameState)){
                    stop();
                    return;
                }
                IngameState ingameState = (IngameState) plugin.getGameStateManager().getCurrentGameState();
                for(Player player : plugin.getGamePlayer()) {
                    ingameState.ingameScoreboard(player);
                }
            }
        },0,20);
    }

    @Override
    public void stop() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public int getSeconds() {
        return seconds;
    }
}
