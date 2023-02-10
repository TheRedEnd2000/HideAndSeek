package de.theredend2000.hideandseek.countdowns;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.gamestates.IngameState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class HiderRunningCountdown extends Countdown {
    private int seconds;
    private Main plugin;

    public HiderRunningCountdown(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        seconds = plugin.getConfig().getInt("Settings.HidingTime");
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                switch (seconds) {
                    case 10: case 5: case 4: case 3: case 2:
                        for(Player player : plugin.getGamePlayer()){
                            player.sendMessage(Main.PREFIX+"§aDie Seeker werden in "+seconds+"§a Sekunden freigelassen");
                        }
                        break;
                    case 1:
                        for(Player player : plugin.getGamePlayer()){
                            player.sendMessage(Main.PREFIX+"§aDie Seeker werden in "+seconds+"§a Sekunde freigelassen");
                        }
                        break;
                    case 0:

                        IngameState ingameState = (IngameState) plugin.getGameStateManager().getCurrentGameState();
                        ingameState.setGrace(false);
                        stop();
                        for(Player player : plugin.getGamePlayer()){
                            player.sendMessage(Main.PREFIX+"§aDie Seeker wurden freigelassen.");
                        }
                        break;

                    default:
                        break;
                }
                seconds--;
            }
        },0,20);
    }

    @Override
    public void stop() {
        Bukkit.getScheduler().cancelTask(taskID);
    }
}
