package de.theredend2000.hideandseek.role;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.arenas.Arena;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RoleManager {

    private Main plugin;
    private HashMap<Player, Role> playerRoles;
    private ArrayList<Player> players;
    private Scoreboard scoreboard;
    private Team team;

    private int seeker, hider;

    public RoleManager(Main plugin){
        this.plugin = plugin;
        playerRoles = new HashMap<>();
        players = new ArrayList<>();
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        team = scoreboard.registerNewTeam("hide_nametag_game");
        team.setNameTagVisibility(NameTagVisibility.HIDE_FOR_OWN_TEAM);
    }

    public void calculateRoles(Arena arena) {
        int playerSize = arena.getPlayerInGame().size();
        players.addAll(arena.getPlayerInGame());
        seeker = (int) Math.round(Math.log(playerSize)*1.2);
        hider = playerSize - seeker;

        Bukkit.broadcastMessage(String.valueOf(seeker));
        Bukkit.broadcastMessage(String.valueOf(hider));

        Collections.shuffle(players);

        for (int i = 0; i < seeker; i++) {
            setPlayerRole(players.get(i),Role.SEEKER);
            players.remove(players.get(i));
        }

        for (Player player : players) setPlayerRole(player,Role.HIDER);
    }

    public Role getPlayerRole(Player player){
        return playerRoles.get(player);
    }

    public void removePlayerRole(Player player){
        playerRoles.remove(player);
    }
    public void setPlayerRole(Player player,Role role){
        playerRoles.put(player,role);
    }

    public void addTeam(Player player){
        team.addPlayer(player);
    }

    public void removeTeam(Player player){
        team.removePlayer(player);
    }


}
