package de.theredend2000.hideandseek.countdowns;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.arenas.Arena;
import de.theredend2000.hideandseek.game.GameState;
import de.theredend2000.hideandseek.messages.MessageManager;
import de.theredend2000.hideandseek.role.Role;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArenaDurationCountdown {

    private final Map<Arena, Integer> timeDuration;
    private final MessageManager messageManager;
    private int taskId;
    private int maxDuration;
    private int seekerWaitTime;
    private int seekerReleaseTime;
    private Main plugin;

    public ArenaDurationCountdown() {
        plugin = Main.getPlugin();
        timeDuration = new HashMap<>();
        messageManager = Main.getPlugin().getMessageManager();
        taskId = -1;
    }

    public void startDurationCountdown(Arena arena) {
        maxDuration = plugin.getArenaManager().getGameDuration(arena);
        timeDuration.put(arena, maxDuration);
        seekerWaitTime = plugin.getArenaManager().getSeekerReleaseDuration(arena)-55;

        if (taskId == -1) {
            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), this::updateCountdown, 20L, 20L);
        }
    }

    private void updateCountdown() {
        for (Arena arena : timeDuration.keySet()) {
            if (arena.getGameState().equals(GameState.RUNNING)) {
                int currentTime = timeDuration.get(arena);
                int releaseTime = maxDuration-seekerWaitTime;
                seekerReleaseTime = currentTime-maxDuration+seekerWaitTime;
                switch (seekerReleaseTime){
                    case 300: case 120: case 60: case 30: case 10: case 5: case 4: case 3: case 2: case 1:
                        for(Player player : arena.getAllPlayers())
                            player.sendMessage(Main.PREFIX+"§cThe seekers releases in "+seekerReleaseTime+" second(s).");
                        break;
                }
                switch (currentTime){
                    case 60: case 30: case 10: case 5: case 4: case 3: case 2: case 1:
                        for(Player player : arena.getAllPlayers())
                            player.sendMessage(Main.PREFIX+"§cThe battle ends in "+currentTime+" second(s).");
                        break;
                }
                if(currentTime == releaseTime)
                    plugin.getGameManager().releaseSeeker(arena);
                if (currentTime == 0) {
                    ArrayList<Player> players = new ArrayList<>(arena.getPlayerInGame());
                    //Main.getPlugin().getGameManager().timerExpiredGameEnd(players,arena,kit);
                    plugin.getGameManager().winGame(Role.HIDER,arena);
                } else {
                    timeDuration.put(arena, currentTime - 1);
                }
            }
        }
    }

    public int getCurrentTime(Arena arena) {
        return timeDuration.getOrDefault(arena, 0);
    }

    public int getSeekerReleaseTime() {
        return seekerReleaseTime;
    }
    public boolean isReleased(){
        return seekerReleaseTime < 0;
    }
}
