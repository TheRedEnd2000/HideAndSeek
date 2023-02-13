package de.theredend2000.hideandseek.countdowns;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.gamestates.IngameState;
import de.theredend2000.hideandseek.util.ConfigLocationUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameMode;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class EndingCountdown extends Countdown{

    private static final int ENDING_SECONDS = 20;

    private Main plugin;
    private int seconds;
    Team team = null;
    Scoreboard teamBoard = Bukkit.getScoreboardManager().getMainScoreboard();

    public EndingCountdown(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void start() {
        seconds = 20;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            @Override
            public void run() {
                switch (seconds) {
                    case 20:  case 15: case 10: case 5: case 4:case 3: case 2:
                        for(Player current : plugin.getGamePlayer()) {
                            current.sendMessage(Main.PREFIX + "§cDer Server restartet in §6" + seconds + " Sekunden§c!");
                            Firework firework = current.getWorld().spawn(current.getLocation(),Firework.class);
                            FireworkEffect effect = FireworkEffect.builder()
                                    .withColor(Color.LIME).withColor(Color.RED).withColor(Color.BLUE).withColor(Color.YELLOW).withColor(Color.PURPLE)
                                    .flicker(true)
                                    .trail(true)
                                    .withFade(Color.WHITE)
                                    .with(FireworkEffect.Type.BALL_LARGE)
                                    .build();
                            FireworkMeta meta = firework.getFireworkMeta();
                            meta.setPower(1);
                            meta.addEffect(effect);
                            firework.setFireworkMeta(meta);
                            for(Player player : plugin.getGamePlayer()){
                                player.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
                                plugin.getRoleManager().getHiderPlayers().clear();
                                plugin.getRoleManager().getSeekerPlayers().clear();
                                plugin.getRoleManager().getPlayerRoles().clear();
                                plugin.getVoting().getPlayerVotes().clear();
                                plugin.getVoting().getWinnerMap().clearVotes();
                            }
                        }
                        break;
                    case 19: case 18: case 17: case 16: case 14: case 13: case 12: case 11: case 9: case 8: case 7: case 6:
                        for(Player current : plugin.getGamePlayer()) {
                            Firework firework = current.getWorld().spawn(current.getLocation(),Firework.class);
                            FireworkEffect effect = FireworkEffect.builder()
                                    .withColor(Color.LIME).withColor(Color.RED).withColor(Color.BLUE).withColor(Color.YELLOW).withColor(Color.PURPLE)
                                    .flicker(true)
                                    .trail(true)
                                    .withFade(Color.WHITE)
                                    .with(FireworkEffect.Type.BALL_LARGE)
                                    .build();
                            FireworkMeta meta = firework.getFireworkMeta();
                            meta.setPower(1);
                            meta.addEffect(effect);
                            firework.setFireworkMeta(meta);
                        }
                        break;
                    case 1:
                        for(Player current : plugin.getGamePlayer()) {
                            current.sendMessage(Main.PREFIX+"§cDer Server restartet in §6"+seconds+" Sekunde§c!");
                            Firework firework = current.getWorld().spawn(current.getLocation(),Firework.class);
                            FireworkEffect effect = FireworkEffect.builder()
                                    .withColor(Color.LIME).withColor(Color.RED).withColor(Color.BLUE).withColor(Color.YELLOW).withColor(Color.PURPLE)
                                    .flicker(true)
                                    .trail(true)
                                    .withFade(Color.WHITE)
                                    .with(FireworkEffect.Type.BALL_LARGE)
                                    .build();
                            FireworkMeta meta = firework.getFireworkMeta();
                            meta.setPower(1);
                            meta.addEffect(effect);
                            firework.setFireworkMeta(meta);
                        }
                        break;
                    case 0:
                        for(Player player : plugin.getGamePlayer()){
                            ConfigLocationUtil locationUtil = new ConfigLocationUtil(plugin, "end");
                            if (locationUtil.loadLocation() != null) {
                                player.teleport(locationUtil.loadLocation());
                            }
                            player.setGameMode(GameMode.SURVIVAL);
                        }
                        plugin.getGameStateManager().getCurrentGameState().stop();
                        plugin.getGameStateManager().stopCurrentGameState();
                        plugin.getGamePlayer().clear();
                        stop();
                        break;
                    default:
                        break;
                }
                seconds --;
            }
        },0 ,20);
    }

    @Override
    public void stop() {
        Bukkit.getScheduler().cancelTask(taskID);
    }

    public int getSeconds() {
        return seconds;
    }

}
