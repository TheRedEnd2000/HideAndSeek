package de.theredend2000.hideandseek.role;

import de.theredend2000.hideandseek.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RoleManager {

    private Main plugin;
    private ArrayList<Player> players;
    private int seekerChange, hiderChange;
    private HashMap<String, Role> playerRoles;
    private ArrayList<Player> seekerPlayers;
    private ArrayList<Player> hiderPlayers;

    public RoleManager(Main plugin) {
        this.plugin = plugin;
        players = new ArrayList<>();
        playerRoles = new HashMap<>();
        seekerPlayers = new ArrayList<>();
        hiderPlayers = new ArrayList<>();
    }

    public void calculateRoles(int size) {
        int playerSize = players.size();

        seekerChange = (int) Math.round(Math.log(playerSize) * 1.2);
        Bukkit.getConsoleSender().sendMessage("SeekerChange "+seekerChange);
        hiderChange = playerSize - seekerChange;
        Bukkit.getConsoleSender().sendMessage("HiderChnage "+hiderChange);

        Collections.shuffle(players);

        int counter = 0;
        for(int i = counter; i < seekerChange; i++) {
            playerRoles.put(players.get(i).getName(), Role.Seeker);
            seekerPlayers.add(players.get(i));
            Bukkit.getConsoleSender().sendMessage("Seeker add "+players.get(i).getName());
        }
        counter += seekerChange;

        for(int i = counter; i < hiderChange + counter; i++) {
            playerRoles.put(players.get(i).getName(), Role.Hider);
            hiderPlayers.add(players.get(i));
            Bukkit.getConsoleSender().sendMessage("Hider add "+players.get(i).getName());
        }
    }

    public ArrayList<Player> getSeekerPlayers() {
        return seekerPlayers;
    }

    public Role getPlayerRole(Player player) {
        return playerRoles.get(player.getName());
    }

    public ArrayList<Player> getHiderPlayers() {
        return hiderPlayers;
    }
}
