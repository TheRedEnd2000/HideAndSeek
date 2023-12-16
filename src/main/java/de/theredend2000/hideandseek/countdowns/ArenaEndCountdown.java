package de.theredend2000.hideandseek.countdowns;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.arenas.Arena;
import de.theredend2000.hideandseek.game.GameState;
import de.theredend2000.hideandseek.messages.MessageKey;
import de.theredend2000.hideandseek.messages.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArenaEndCountdown {

    private final Map<Arena, Integer> timeEnding;
    private final MessageManager messageManager;
    private int taskId;

    public ArenaEndCountdown() {
        timeEnding = new HashMap<>();
        messageManager = Main.getPlugin().getMessageManager();
        taskId = -1;
    }

    public void addEndingCountdownForArena(Arena arena) {
        int endingTime = Main.getPlugin().getArenaManager().getEndingDuration(arena);
        timeEnding.put(arena, endingTime);

        if (taskId == -1) {
            taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), this::updateCountdown, 20L, 20L);
        }
    }

    private void updateCountdown() {
        for (Arena arena : timeEnding.keySet()) {
            if (arena.getGameState().equals(GameState.GAME_END)) {
                int currentTime = timeEnding.get(arena);
                switch (currentTime){
                    case 300: case 120: case 60: case 30: case 10: case 5: case 4: case 3: case 2: case 1: case 0:
                        for (Player player : arena.getAllPlayers()) {
                            if (currentTime == 0) {
                                player.sendMessage(messageManager.getMessage(MessageKey.BATTLE_ENDED_MESSAGE).replaceAll("%seconds%", String.valueOf(currentTime)));
                            } else {
                                player.sendMessage(messageManager.getMessage(MessageKey.BATTLE_END_MESSAGE).replaceAll("%seconds%", String.valueOf(currentTime)));
                            }
                        }
                        break;
                }

                /*Main.getPlugin().getItemManager().setPlayAgainItem(player);
                    Main.getPlugin().getItemManager().setLeaveItem(player);*/

                if (currentTime > 1 && arena.getAllPlayers() != null) {
                    //Main.getPlugin().getSpecialsManager().spawnRandomFirework(arena);
                }

                if (currentTime == 0) {
                    timeEnding.remove(arena);
                    Main.getPlugin().getGameManager().endGame(arena);
                } else {
                    timeEnding.put(arena, currentTime - 1);
                }
            }
        }

        if (timeEnding.isEmpty() && taskId != -1) {
            Bukkit.getScheduler().cancelTask(taskId);
            taskId = -1;
        }
    }

    public int getCurrentTime(Arena arena) {
        return timeEnding.getOrDefault(arena, 0);
    }
}
