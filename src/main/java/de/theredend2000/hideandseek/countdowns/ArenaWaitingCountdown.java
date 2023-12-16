package de.theredend2000.hideandseek.countdowns;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.arenas.Arena;
import de.theredend2000.hideandseek.arenas.ArenaManager;
import de.theredend2000.hideandseek.game.GameState;
import de.theredend2000.hideandseek.messages.MessageKey;
import de.theredend2000.hideandseek.messages.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ArenaWaitingCountdown {

    private HashMap<Arena, Integer> timeStarting;
    private MessageManager messageManager;
    private int taskId;
    private boolean isRunning;
    private Main plugin;

    public ArenaWaitingCountdown(){
        plugin = Main.getPlugin();
        timeStarting = new HashMap<>();
        taskId = -1;
        isRunning = false;
        messageManager = plugin.getMessageManager();
    }

    public void addStartingCountdownForArena(Arena arena){
        int startingtime = plugin.getArenaManager().getStartingDuration(arena);
        timeStarting.put(arena, startingtime);
        isRunning = true;

        if (taskId == -1) {
            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::updateCountdown, 20L, 20L);
        }
    }

    private void updateCountdown(){
        for(Arena arena : timeStarting.keySet()){
            if(arena.getGameState().equals(GameState.STARTING)) {
                int currentTime = timeStarting.get(arena);
                switch (currentTime){
                    case 300: case 120: case 60: case 30: case 10: case 5: case 4: case 3: case 2: case 1: case 0:
                        for (Player player : arena.getAllPlayers()) {
                            if (currentTime == 0)
                                player.sendMessage(messageManager.getMessage(MessageKey.BATTLE_STARTED_MESSAGE).replaceAll("%seconds%", String.valueOf(currentTime)));
                            else
                                player.sendMessage(messageManager.getMessage(MessageKey.BATTLE_START_MESSAGE).replaceAll("%seconds%", String.valueOf(currentTime)));
                        }
                        break;
                }
                if (currentTime == 0) {
                    plugin.getGameManager().startGame(arena);
                    plugin.getArenaDurationCountdown().startDurationCountdown(arena);
                    timeStarting.remove(arena);
                    isRunning = false;
                }
                if(arena.getGameState().equals(GameState.STARTING)) {
                    currentTime--;
                    timeStarting.put(arena, currentTime);
                }
            }else
                timeStarting.remove(arena);
        }
    }

    public int getCurrentTime(Arena arena){
        return timeStarting.getOrDefault(arena,0);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void setTimeStarting(int time, Arena arena){
        timeStarting.remove(arena);
        timeStarting.put(arena,time);
    }

    public int getTimeStarting(Arena arena) {
        return timeStarting.getOrDefault(arena,1000);
    }
}
