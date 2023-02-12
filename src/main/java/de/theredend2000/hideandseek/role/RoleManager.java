package de.theredend2000.hideandseek.role;

import de.theredend2000.hideandseek.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RoleManager {

    private Main plugin;
    private HashMap<String, Role> playerRoles;
    private ArrayList<Player> players;
    private ArrayList<String> seekerPlayers;
    private ArrayList<String> hiderPlayers;

    public int seeker, hider;

    public RoleManager(Main plugin) {
        this.plugin = plugin;
        playerRoles = new HashMap<>();
        players = plugin.getGamePlayer();
        seekerPlayers = new ArrayList<>();
        hiderPlayers = new ArrayList<>();
    }

    public void calculateRoles() {
        //Delete
        int playersize = players.size();

        seeker = (int) Math.round(Math.log(playersize)* 2);
        hider = playersize - seeker;

        Collections.shuffle(plugin.getGamePlayer());

        int counter = 0;
        for(int i = counter; i < seeker; i++) {
            playerRoles.put(players.get(i).getName(), Role.Seeker);
            seekerPlayers.add(players.get(i).getName());
        }
        counter += seeker;

        for(int i = counter; i < hider + counter; i++) {
            playerRoles.put(players.get(i).getName(), Role.Hider);
            hiderPlayers.add(players.get(i).getName());
        }
    }

    public Role getPlayerRole(Player player) {
        return playerRoles.get(player.getName());
    }

    public ArrayList<String> getSeekerPlayers() {
        return seekerPlayers;
    }

    public ArrayList<String> getHiderPlayers() {
        return hiderPlayers;
    }
}
