package de.theredend2000.hideandseek.gamestates;

import de.theredend2000.hideandseek.Main;

public class LobbyState extends GameState{

    private Main plugin;

    public static final int MIN_PLAYERS = 1,
                            MAX_PLAYERS = 10;


    public LobbyState(Main plugin, GameStateManager gameStateManager) {
        this.plugin = plugin;
    }


    @Override
    public void start() {

    }

    @Override
    public void stop() {
    }


}
