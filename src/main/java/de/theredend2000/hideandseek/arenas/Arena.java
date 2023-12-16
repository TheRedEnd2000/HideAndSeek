package de.theredend2000.hideandseek.arenas;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.game.GameState;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

public class Arena {

    private String name;
    private boolean enabled;
    private ArrayList<Player> playerInGame;
    private ArrayList<Player> spectators;
    private Location lobbySpawn;
    private Location spectatorSpawn;
    private Location[] spawns;
    private Location pos1;
    private Location pos2;
    private Location seekerWait;
    private Location seekerRelease;
    private GameState gameState;
    private int minPlayers;
    private int maxPlayers;

    public Arena(String name, boolean enabled, ArrayList<Player> playerInGame, ArrayList<Player> spectators, Location[] spawns, Location lobbySpawn, Location spectatorSpawn, Location pos1, Location pos2, Location seekerWait, Location seekerRelease, GameState gameState, int minPlayers, int maxPlayers) {
        this.name = name;
        this.enabled = enabled;
        this.playerInGame = playerInGame;
        this.spectators = spectators;
        this.lobbySpawn = lobbySpawn;
        this.spectatorSpawn = spectatorSpawn;
        this.spawns = spawns;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.seekerWait = seekerWait;
        this.seekerRelease = seekerRelease;
        this.gameState = gameState;
        this.minPlayers = minPlayers;
        this.maxPlayers = maxPlayers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public ArrayList<Player> getPlayerInGame() {
        return playerInGame;
    }

    public void addPlayerInGame(Player player){
        this.playerInGame.add(player);
    }

    public ArrayList<Player> getSpectators() {
        return spectators;
    }

    public ArrayList<Player> getAllPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        players.addAll(spectators);
        players.addAll(playerInGame);
        return players;
    }

    public Location getSpectatorSpawn() {
        return spectatorSpawn;
    }

    public Location[] getSpawns() {
        return spawns;
    }

    public boolean containsPlayerInGame(Player player){
        return this.playerInGame.contains(player);
    }

    public void removePlayerInGame(Player player){
        this.playerInGame.remove(player);
    }

    public boolean containsSpectator(Player player){
        return this.playerInGame.contains(player);
    }

    public void removeSpectator(Player player){
        this.playerInGame.remove(player);
    }

    public void addSpectator(Player player){
        this.playerInGame.add(player);
    }

    public void setSpawns(Location[] spawns) {
        this.spawns = spawns;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    public Location getPos1() {
        return pos1;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public Location getSeekerRelease() {
        return seekerRelease;
    }

    public Location getSeekerWait() {
        return seekerWait;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
