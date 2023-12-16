package de.theredend2000.hideandseek.placeholder;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.arenas.Arena;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlaceholderExtension extends PlaceholderExpansion {

    @Override
    public String getAuthor() {
        return "theredend2000";
    }

    @Override
    public String getIdentifier() {
        return "hideandseek";
    }

    @Override
    public String getVersion() {
        return Main.getPlugin().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if(params.equalsIgnoreCase("game_arena")){
            Arena arena = Main.getPlugin().getArenaManager().getPlayerCurrentArena(player);
            if(arena == null) return "no";
            return String.valueOf(arena.getName());
        }
        return null;
    }

}
