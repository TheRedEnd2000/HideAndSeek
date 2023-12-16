package de.theredend2000.hideandseek.commands;

import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.arenas.Arena;
import de.theredend2000.hideandseek.game.GameState;
import de.theredend2000.hideandseek.inventorys.GameSettingsInventory;
import de.theredend2000.hideandseek.messages.MessageKey;
import de.theredend2000.hideandseek.messages.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ArenaCommands implements CommandExecutor, TabCompleter {

    private Main plugin;
    private MessageManager messageManager;

    public ArenaCommands(Main plugin) {
        this.plugin = plugin;
        messageManager = plugin.getMessageManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("")) {
                if (args[0].equalsIgnoreCase("arena")) {
                    if (args[1].equalsIgnoreCase("create") && args.length == 3) {
                        try {
                            String name = args[2];
                            if (isValidInput(name)) {
                                if (!plugin.getArenaManager().containsArena(name)) {
                                    plugin.getArenaManager().saveNewArena(name);
                                    player.sendMessage(messageManager.getMessage(MessageKey.ARENA_CREATED).replaceAll("%arena_name%", name));
                                } else
                                    player.sendMessage(messageManager.getMessage(MessageKey.ARENA_ALREADY_EXIST).replaceAll("%arena_name%", name));
                            } else
                                player.sendMessage(messageManager.getMessage(MessageKey.TYPE_NAME_CONTENT).replaceAll("%type%", "Arena"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (args[1].equalsIgnoreCase("delete") && args.length == 3) {
                        try {
                            String name = args[2];
                            if (plugin.getArenaManager().containsArena(name)) {
                                Main.getPlugin().getArenaManagerHashMap().remove(name);
                                Main.getPlugin().getArenaManager().removeArena(name);
                                player.sendMessage(messageManager.getMessage(MessageKey.ARENA_DELETED).replaceAll("%arena_name%", name));
                            } else
                                player.sendMessage(messageManager.getMessage(MessageKey.ARENA_NOT_FOUND));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (args[1].equalsIgnoreCase("enable") && args.length == 3) {
                        try {
                            String name = args[2];
                            if (plugin.getArenaManager().containsArena(name)) {
                                if(plugin.getArenaManager().playable(name)) {
                                    if(plugin.getArenaManager().isEnabled(name)) {
                                        plugin.getArenaManager().enableArena(name);
                                        player.sendMessage("enabled " + name);
                                    }else
                                        player.sendMessage("already enabled");
                                }else
                                    player.sendMessage("no");
                            } else
                                player.sendMessage(messageManager.getMessage(MessageKey.ARENA_NOT_FOUND));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if (args[1].equalsIgnoreCase("edit")) {
                        if(args.length == 3){
                            String name = args[2];
                            if (plugin.getArenaManager().containsArena(name)) {
                                new GameSettingsInventory(name,player).displayTo(player);
                            }
                        }
                        if (args[3].equalsIgnoreCase("setMinPlayers") && args.length == 5) {
                            String name = args[2];
                            if (plugin.getArenaManager().containsArena(name)) {
                                if(plugin.getArenaManager().isMaxPlayersSet(name)) {
                                    int maxPlayer = plugin.getArenaManager().getMaxPlayer(name);
                                    try {
                                        int players = Integer.parseInt(args[4]);
                                        if (plugin.getArenaManager().isValidPlayerCount(players,name)) {
                                            plugin.getArenaManager().setMinPlayers(name, players);
                                            player.sendMessage("set minplayer " + players);
                                        } else
                                            player.sendMessage("between 2 and "+maxPlayer);
                                    } catch (NumberFormatException e) {
                                        player.sendMessage("Use numbers");
                                    }
                                }else
                                    player.sendMessage("set max players");
                            } else
                                player.sendMessage(messageManager.getMessage(MessageKey.ARENA_NOT_FOUND));
                        } else if (args[3].equalsIgnoreCase("setMaxPlayers") && args.length == 5) {
                            String name = args[2];
                            if (plugin.getArenaManager().containsArena(name)) {
                                try {
                                    int players = Integer.parseInt(args[4]);
                                    if (plugin.getArenaManager().isValidPlayerCountForMaxPlayers(players)) {
                                        plugin.getArenaManager().setMaxPlayers(name, players);
                                        player.sendMessage("set maxplayer " + players);
                                    } else
                                        player.sendMessage("between 2 and 24");
                                } catch (NumberFormatException e) {
                                    player.sendMessage("Use numbers");
                                }
                            } else
                                player.sendMessage(messageManager.getMessage(MessageKey.ARENA_NOT_FOUND));
                        } else if (args[3].equalsIgnoreCase("lobby") && args.length == 4) {
                            String name = args[2];
                            if (plugin.getArenaManager().containsArena(name)) {
                                plugin.getArenaManager().setLobby(name, player.getLocation());
                                player.sendMessage("lobby set for " + name);
                            } else
                                player.sendMessage(messageManager.getMessage(MessageKey.ARENA_NOT_FOUND));
                        } else if (args[3].equalsIgnoreCase("seekerWait") && args.length == 4) {
                            String name = args[2];
                            if (plugin.getArenaManager().containsArena(name)) {
                                plugin.getArenaManager().setSeekerWait(name, player.getLocation());
                                player.sendMessage("seekerWait set for " + name);
                            } else
                                player.sendMessage(messageManager.getMessage(MessageKey.ARENA_NOT_FOUND));
                        }else if (args[3].equalsIgnoreCase("seekerRelease") && args.length == 4) {
                            String name = args[2];
                            if (plugin.getArenaManager().containsArena(name)) {
                                plugin.getArenaManager().setSeekerRelease(name, player.getLocation());
                                player.sendMessage("seekerRelease set for " + name);
                            } else
                                player.sendMessage(messageManager.getMessage(MessageKey.ARENA_NOT_FOUND));
                        }else if (args[3].equalsIgnoreCase("spectator") && args.length == 4) {
                            String name = args[2];
                            if (plugin.getArenaManager().containsArena(name)) {
                                plugin.getArenaManager().setSpec(name, player.getLocation());
                                player.sendMessage("spec set for " + name);
                            } else
                                player.sendMessage(messageManager.getMessage(MessageKey.ARENA_NOT_FOUND));
                        } else if (args[3].equalsIgnoreCase("pos1") && args.length == 4) {
                            String name = args[2];
                            if (plugin.getArenaManager().containsArena(name)) {
                                plugin.getArenaManager().setPos1(name, player.getLocation());
                                player.sendMessage("pos1 set for " + name);
                            } else
                                player.sendMessage(messageManager.getMessage(MessageKey.ARENA_NOT_FOUND));
                        } else if (args[3].equalsIgnoreCase("pos2") && args.length == 4) {
                            String name = args[2];
                            if (plugin.getArenaManager().containsArena(name)) {
                                plugin.getArenaManager().setPos2(name, player.getLocation());
                                player.sendMessage("pos2 set for " + name);
                            } else
                                player.sendMessage(messageManager.getMessage(MessageKey.ARENA_NOT_FOUND));
                        }  else if (args[3].equalsIgnoreCase("spawn") && args.length == 5) {
                            String name = args[2];
                            if (plugin.getArenaManager().containsArena(name)) {
                                if (plugin.getArenaManager().isMinPlayersSet(name) && plugin.getArenaManager().isMaxPlayersSet(name)) {
                                    try {
                                        int spawn = Integer.parseInt(args[4]);
                                        int maxPlayer = plugin.getArenaManager().getMaxPlayer(name);
                                        if (plugin.getArenaManager().isValidSpawnCount(name, spawn)) {
                                            plugin.getArenaManager().setSpawns(name, spawn, player.getLocation());
                                            player.sendMessage("set spawn " + spawn);
                                        } else
                                            player.sendMessage("between 1 and " + maxPlayer);
                                    } catch (NumberFormatException e) {
                                        player.sendMessage("Use numbers");
                                    }
                                } else
                                    player.sendMessage("min and max not set");
                            } else
                                player.sendMessage(messageManager.getMessage(MessageKey.ARENA_NOT_FOUND));
                        }
                    }
                }
            }
            if (args[0].equalsIgnoreCase("join") && args.length == 2) {
                try {
                    String name = args[1];
                    if(plugin.getArenaManager().containsArena(name)) {
                        if (plugin.getArenaManagerHashMap().containsKey(name) || !plugin.getArenaManager().isEnabled(name)) {
                            Arena arena = plugin.getArenaManagerHashMap().get(name);
                            if (!plugin.getArenaManager().playerIsAlreadyInArena(player)) {
                                if (arena.getPlayerInGame().size() != arena.getMaxPlayers()) {
                                    if(arena.getGameState().equals(GameState.STARTING) || arena.getGameState().equals(GameState.WAITING)){
                                        plugin.getGameManager().joinArena(player, arena);
                                        player.sendMessage("You joined " + name);
                                    }else
                                        player.sendMessage("Dieses Math hat bereits gestartet.");
                                } else
                                    player.sendMessage("This Match is full.");
                            } else
                                player.sendMessage("Aleady in");
                        } else
                            player.sendMessage("This arena is not enabled");
                    }else
                        player.sendMessage(messageManager.getMessage(MessageKey.ARENA_NOT_FOUND).replaceAll("%arena_name%", name));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (args[0].equalsIgnoreCase("leave") && args.length == 1) {
                try {
                    if(plugin.getArenaManager().playerIsAlreadyInArena(player)) {
                        Arena arena = plugin.getArenaManager().getPlayerCurrentArena(player);
                        plugin.getGameManager().leaveGame(player,arena);
                    }else
                        player.sendMessage("not in arena");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (args[0].equalsIgnoreCase("start") && args.length == 1) {
                try {
                    if(plugin.getArenaManager().playerIsAlreadyInArena(player)) {
                        Arena arena = plugin.getArenaManager().getPlayerCurrentArena(player);
                        if(arena.getGameState().equals(GameState.STARTING) || plugin.getArenaWaitingCountdown().getTimeStarting(arena) <= 5) {
                            if (arena.getPlayerInGame().size() >= arena.getMinPlayers()) {
                                plugin.getArenaWaitingCountdown().setTimeStarting(5, arena);
                                for (Player players : arena.getPlayerInGame())
                                    players.sendMessage("Start wurde beschleundigt");
                            } else
                                player.sendMessage("nicht genug spieler");
                        }else
                            player.sendMessage("Kann aktuell nicht benutzt werden");
                    }else
                        player.sendMessage("not in arena");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else if (args[0].equalsIgnoreCase("joinRandom") && args.length == 1) {
                try {
                    if(!plugin.getArenaManager().playerIsAlreadyInArena(player)) {
                        Arena arena = plugin.getGameManager().getRandomEnabledArena();
                        if(arena == null){
                            player.sendMessage("No arenas found.");
                            return false;
                        }
                        plugin.getGameManager().joinArena(player,arena);
                        player.sendMessage("joined "+arena.getName());
                    }else
                        player.sendMessage("not in arena");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean isValidInput(String input) {
        String pattern = "^[a-zA-Z0-9_-]+$";
        return input.matches(pattern);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender.hasPermission("")) {
            if (args[0].equalsIgnoreCase("arena")) {
                if (args.length == 2) {
                    String[] text = {"create", "delete", "edit", "enable"};
                    ArrayList<String> tabs = new ArrayList<>();
                    Collections.addAll(tabs, text);
                    return tabs;
                } else if (args.length == 3) {
                    if(args[1].equalsIgnoreCase("delete"))
                        return new ArrayList<>(plugin.getArenaManager().getAllArenas());
                    else if(args[1].equalsIgnoreCase("enable")) {
                        ArrayList<String> tabs = new ArrayList<>();
                        for(String enabledArena : plugin.getArenaManager().getAllArenas()){
                            if(plugin.getArenaManager().playable(enabledArena) && plugin.getArenaManager().isEnabled(enabledArena))
                                tabs.add(enabledArena);
                        }
                        return tabs != null ? tabs : Collections.singletonList("NoArenas");
                    }else if(args[1].equalsIgnoreCase("create"))
                        return Collections.singletonList("<name>");
                    else if(args[1].equalsIgnoreCase("edit"))
                        return new ArrayList<>(plugin.getArenaManager().getAllArenas());
                }else if(args.length == 4){
                    if(args[1].equalsIgnoreCase("edit")){
                        String[] text = {"setMinPlayers", "setMaxPlayers", "lobby", "spectator","pos1","pos2","seekerWait","seekerRelease","spawn"};
                        ArrayList<String> tabs = new ArrayList<>();
                        Collections.addAll(tabs, text);
                        return tabs;
                    }
                }else if(args.length == 5){
                    if(args[1].equalsIgnoreCase("edit")) {
                        if(args[3].equalsIgnoreCase("setMaxPlayers")){
                            ArrayList<String> tabs = new ArrayList<>();
                            for (int i = 2; i <= 24; i++)
                                tabs.add(String.valueOf(i));
                            return tabs;
                        }else if(args[3].equalsIgnoreCase("setMinPlayers")){
                            if (plugin.getArenaManager().isMaxPlayersSet(args[2])) {
                                ArrayList<String> tabs = new ArrayList<>();
                                for (int i = 2; i <= plugin.getArenaManager().getMaxPlayer(args[2]); i++)
                                    tabs.add(String.valueOf(i));
                                return tabs;
                            }else
                                return Collections.singletonList("Set Max Players first!");
                        }else if(args[3].equalsIgnoreCase("spawn")) {
                            if (plugin.getArenaManager().isMinPlayersSet(args[2]) && plugin.getArenaManager().isMaxPlayersSet(args[2])) {
                                ArrayList<String> tabs = new ArrayList<>();
                                for (int i = 1; i <= plugin.getArenaManager().getMaxPlayer(args[2]); i++)
                                    tabs.add(String.valueOf(i));
                                return tabs;
                            }else
                                return Collections.singletonList("Set Min and Max Players first!");
                        }
                    }
                }
            }
        }
        if (args.length == 1) {
            String[] textall = {"join", "joinRandom", "leave"};
            String[] textpermission = {"join", "joinRandom", "leave", "arena","start"};
            ArrayList<String> tabs = new ArrayList<>();
            if(sender.hasPermission(""))
                Collections.addAll(tabs,textpermission);
            else
                Collections.addAll(tabs, textall);
            return tabs;
        }else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("join")) {
                ArrayList<String> tabs = new ArrayList<>();
                for (String arenas : plugin.getArenaManager().getAllEnabledArenas()){
                    Arena arena = plugin.getArenaManagerHashMap().get(arenas);
                    if(arena.getGameState().equals(GameState.WAITING) || arena.getGameState().equals(GameState.STARTING))
                        tabs.add(arenas);
                }
                return tabs;
            }
        }
        return null;
    }
}
