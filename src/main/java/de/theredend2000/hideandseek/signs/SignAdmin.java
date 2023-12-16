package de.theredend2000.hideandseek.signs;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.arenas.Arena;
import de.theredend2000.hideandseek.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.List;

public class SignAdmin {

	int taskID;
	private Main plugin;
	public SignAdmin(Main plugin){
		this.plugin = plugin;		
	}
	
	public void actualizarSigns() {
	    BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
	    final FileConfiguration config = plugin.getConfig();
 	    taskID = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
            public void run() { 
            	ejecutarActualizarSigns(config);
            }
        },0, 40L);
	}

	protected void ejecutarActualizarSigns(FileConfiguration config) {
		FileConfiguration signs = plugin.getSigns();
		if(signs.contains("Signs")) {
			for(String arenaName : signs.getConfigurationSection("Signs").getKeys(false)) {
				Arena arena = plugin.getArenaManagerHashMap().get(arenaName);
				GameState gameState = arena.getGameState();
				if(gameState != null) {
					int x = Integer.valueOf(signs.getString("Signs."+arena.getName()+".x"));
					int y = Integer.valueOf(signs.getString("Signs."+arena.getName()+".y"));
					int z = Integer.valueOf(signs.getString("Signs."+arena.getName()+".z"));
					World world = Bukkit.getWorld(signs.getString("Signs."+arena.getName()+".world"));
					if(world != null) {
						int chunkX = x >> 4;
						int chunkZ = z >> 4;
						if(world.isChunkLoaded(chunkX, chunkZ)) {
							Block block = world.getBlockAt(x,y,z);
							if(block.getType().name().contains("SIGN")) {
								Sign sign = (Sign) block.getState();
								List<String> lista = config.getStringList("signFormat");
								for(int i=0;i<lista.size();i++) {
									sign.setLine(i, ChatColor.translateAlternateColorCodes('&', lista.get(i).replace("%arena%", arena.getName())
											.replace("%status%", gameState.name()).replace("%current_players%", String.valueOf(arena.getPlayerInGame().size()))
											.replace("%max_players%", String.valueOf(arena.getMaxPlayers()))));
								}
								sign.update();
							}
						}
					}
				}
			}
		}
		
	}
}