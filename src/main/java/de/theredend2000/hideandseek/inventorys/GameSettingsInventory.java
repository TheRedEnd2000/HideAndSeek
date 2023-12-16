package de.theredend2000.hideandseek.inventorys;

import com.Zrips.CMI.Modules.Anvil.CMIAnvilGUI;
import com.cryptomorin.xseries.XMaterial;
import com.massivecraft.massivecore.chestgui.ChestButtonSimple;
import de.theredend2000.hideandseek.ItemBuilder;
import de.theredend2000.hideandseek.Main;
import de.theredend2000.hideandseek.arenas.Arena;
import de.theredend2000.hideandseek.messages.MessageKey;
import de.theredend2000.hideandseek.messages.MessageManager;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuContainer;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.button.annotation.Position;
import org.mineacademy.fo.menu.model.InventoryDrawer;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;

import javax.swing.*;
import javax.swing.plaf.metal.MetalBorders;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameSettingsInventory extends Menu {

    private Main plugin;
    private MessageManager messageManager;
    private String arena;
    private Player player;

    @Position(10)
    private final Button changeMinPlayersButton;
    @Position(11)
    private final Button changeMaxPlayersButton;
    @Position(12)
    private final Button setLobbyButton;
    @Position(13)
    private final Button setPos1Button;
    @Position(14)
    private final Button setPos2Button;
    @Position(15)
    private final Button setSeekerWaitButton;
    @Position(16)
    private final Button setSeekerReleaseButton;
    @Position(19)
    private final Button setSpectatorsButton;
    @Position(20)
    private final Button openSpawnsButton;
    @Position(49)
    private final Button closeButton;

    public GameSettingsInventory(String arenaName, Player player2) {
        this.plugin = Main.getPlugin();
        this.messageManager = plugin.getMessageManager();
        this.player = player2;
        this.arena = arenaName;

        setTitle("§2Arena configuration");
        setSize(54);
        this.setViewer(player);

        this.closeButton = Button.makeSimple(ItemCreator.of(CompMaterial.BARRIER,"§cClose","§eClick to close."), HumanEntity::closeInventory);

        this.openSpawnsButton = new ButtonMenu(new SpawnsMenu(), CompMaterial.GRASS_BLOCK, "§2Spawns", "", "§7Click here to open the menu to", "§7configure all spawns of the arena.", "", "§eClick to open.");

        this.changeMinPlayersButton = new Button() {
            int currentMinPlayers = plugin.getArenaManager().getMinPlayer(arena);
            int maxPlayer = plugin.getArenaManager().getMaxPlayer(arena);

            @Override
            public void onClickedInMenu(Player player2, Menu menu, ClickType clickType) {
                new AnvilGUI.Builder()
                        .onClose(stateSnapshot -> {
                            Player player = stateSnapshot.getPlayer();
                            if (stateSnapshot.getText().matches("\\d+")) {
                                int count = Integer.parseInt(stateSnapshot.getText());
                                if (plugin.getArenaManager().isValidPlayerCount(count, arena)) {
                                    plugin.getArenaManager().setMinPlayers(arena, count);
                                    player.sendMessage("set to " + count);
                                } else
                                    player.sendMessage("between 2 and " + maxPlayer);
                            } else
                                player.sendMessage(messageManager.getMessage(MessageKey.FAILED_TO_READ_TEXT));
                            new GameSettingsInventory(arena, player).displayTo(player);
                        })
                        .onClick((slot, stateSnapshot) -> {
                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        })
                        .text(String.valueOf(currentMinPlayers))
                        .title("Set Min Players")
                        .plugin(plugin)
                        .open(player);
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.COAL, "§e§lMin Player", "", "§7Current: " + currentMinPlayers, "", "§eClick to change.").make();
            }
        };

        this.changeMaxPlayersButton = new Button() {
            int currentMaxPlayers = plugin.getArenaManager().getMaxPlayer(arena);

            @Override
            public void onClickedInMenu(Player player2, Menu menu, ClickType clickType) {
                new AnvilGUI.Builder()
                        .onClose(stateSnapshot -> {
                            Player player = stateSnapshot.getPlayer();
                            if (stateSnapshot.getText().matches("\\d+")) {
                                int count = Integer.parseInt(stateSnapshot.getText());
                                if (plugin.getArenaManager().isValidPlayerCountForMaxPlayers(count)) {
                                    plugin.getArenaManager().setMaxPlayers(arena, count);
                                    player.sendMessage("set to " + count);
                                } else
                                    player.sendMessage("between 2 and 24");
                            } else
                                player.sendMessage(messageManager.getMessage(MessageKey.FAILED_TO_READ_TEXT));
                            new GameSettingsInventory(arena, player).displayTo(player);
                        })
                        .onClick((slot, stateSnapshot) -> {
                            return Collections.singletonList(AnvilGUI.ResponseAction.close());
                        })
                        .text(String.valueOf(currentMaxPlayers))
                        .title("Set Max Players")
                        .plugin(plugin)
                        .open(player);
            }

            @Override
            public ItemStack getItem() {
                return ItemCreator.of(CompMaterial.REDSTONE, "§e§lMax Player", "", "§7Current: " + currentMaxPlayers, "", "§eClick to change.").make();
            }
        };

        this.setLobbyButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                plugin.getArenaManager().setLobby(arena, player.getLocation());
                player.sendMessage("lobby set for " + arena);
                player.closeInventory();
            }

            @Override
            public ItemStack getItem() {
                Location currentLocation = plugin.getArenaManager().getLobby(arena);
                return ItemCreator.of(CompMaterial.DIAMOND_BLOCK, "§e§lLobby", "§7Status: " + (currentLocation != null ? "§aSet" : "§cNot Set"), "§7Location: " + (currentLocation != null ? "§8X: §6" + currentLocation.getBlockX() + " §8Y: §6" + currentLocation.getBlockY() + " §8Z: §6" + currentLocation.getBlockZ() : "§cnone"), "", "§eClick to set.").make();
            }
        };

        this.setPos1Button = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                plugin.getArenaManager().setPos1(arena, player.getLocation());
                player.sendMessage("pos1 set for " + arena);
                player.closeInventory();
            }

            @Override
            public ItemStack getItem() {
                Location currentLocation = plugin.getArenaManager().getPos1(arena);
                return ItemCreator.of(CompMaterial.WOODEN_AXE, "§e§lPos 1", "§7Status: " + (currentLocation != null ? "§aSet" : "§cNot Set"), "§7Location: " + (currentLocation != null ? "§8X: §6" + currentLocation.getBlockX() + " §8Y: §6" + currentLocation.getBlockY() + " §8Z: §6" + currentLocation.getBlockZ() : "§cnone"), "", "§eClick to set.").make();
            }
        };

        this.setPos2Button = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                plugin.getArenaManager().setPos2(arena, player.getLocation());
                player.sendMessage("pos2 set for " + arena);
                player.closeInventory();
            }

            @Override
            public ItemStack getItem() {
                Location currentLocation = plugin.getArenaManager().getPos2(arena);
                return ItemCreator.of(CompMaterial.WOODEN_AXE, "§e§lPos 2", "§7Status: " + (currentLocation != null ? "§aSet" : "§cNot Set"), "§7Location: " + (currentLocation != null ? "§8X: §6" + currentLocation.getBlockX() + " §8Y: §6" + currentLocation.getBlockY() + " §8Z: §6" + currentLocation.getBlockZ() : "§cnone"), "", "§eClick to set.").make();
            }
        };

        this.setSeekerWaitButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                plugin.getArenaManager().setSeekerWait(arena, player.getLocation());
                player.sendMessage("seeker wait set for " + arena);
                player.closeInventory();
            }

            @Override
            public ItemStack getItem() {
                Location currentLocation = plugin.getArenaManager().getSeekerWait(arena);
                return ItemCreator.of(CompMaterial.CLOCK, "§e§lSeeker Waiting", "§7Status: " + (currentLocation != null ? "§aSet" : "§cNot Set"), "§7Location: " + (currentLocation != null ? "§8X: §6" + currentLocation.getBlockX() + " §8Y: §6" + currentLocation.getBlockY() + " §8Z: §6" + currentLocation.getBlockZ() : "§cnone"), "", "§eClick to set.").make();
            }
        };

        this.setSeekerReleaseButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                plugin.getArenaManager().setSeekerRelease(arena, player.getLocation());
                player.sendMessage("seeker release set for " + arena);
                player.closeInventory();
            }

            @Override
            public ItemStack getItem() {
                Location currentLocation = plugin.getArenaManager().getSeekerRelease(arena);
                return ItemCreator.of(CompMaterial.IRON_BOOTS, "§e§lSeeker Release", "§7Status: " + (currentLocation != null ? "§aSet" : "§cNot Set"), "§7Location: " + (currentLocation != null ? "§8X: §6" + currentLocation.getBlockX() + " §8Y: §6" + currentLocation.getBlockY() + " §8Z: §6" + currentLocation.getBlockZ() : "§cnone"), "", "§eClick to set.").make();
            }
        };

        this.setSpectatorsButton = new Button() {
            @Override
            public void onClickedInMenu(Player player, Menu menu, ClickType clickType) {
                plugin.getArenaManager().setSpec(arena, player.getLocation());
                player.sendMessage("spec set for " + arena);
                player.closeInventory();
            }

            @Override
            public ItemStack getItem() {
                Location currentLocation = plugin.getArenaManager().getSpectator(arena);
                return ItemCreator.of(CompMaterial.GLASS, "§e§lSpectator", "§7Status: " + (currentLocation != null ? "§aSet" : "§cNot Set"), "§7Location: " + (currentLocation != null ? "§8X: §6" + currentLocation.getBlockX() + " §8Y: §6" + currentLocation.getBlockY() + " §8Z: §6" + currentLocation.getBlockZ() : "§cnone"), "", "§eClick to set.").make();
            }
        };
    }

    public class SpawnsMenu extends MenuPagged<Integer> {
        SpawnsMenu() {
            super(GameSettingsInventory.this, plugin.getArenaManager().getMaxPlayerList(arena),true);

            setTitle("§2Arena Spawns");
            setSize(9*2);
            setViewer(player);
        }


        @Override
        protected ItemStack convertToItemStack(Integer integer) {
            Location currentLocation = plugin.getArenaManager().getSpawn(arena,integer);
            return ItemCreator.of(CompMaterial.GRASS_BLOCK, "§e§lSpawn " + integer,"§7Status: "+(currentLocation != null ? "§aSet" : "§cNot Set"),"§7Location: " + (currentLocation != null ? "§8X: §6" + currentLocation.getBlockX() + " §8Y: §6" + currentLocation.getBlockY() + " §8Z: §6" + currentLocation.getBlockZ() : "§cnone") ).make();
        }

        @Override
        protected void onPageClick(Player player, Integer integer, ClickType clickType) {
            player.sendMessage("set spawn "+integer);
            plugin.getArenaManager().setSpawns(arena,integer,player.getLocation());
            player.closeInventory();
        }
    }

    @Override
    protected void onPostDisplay(Player viewer) {
        animate(40, new MenuRunnable() {
            boolean toggle = true;
            @Override
            public void run() {
                int[] glass = new int[]{0,1,2,6,7,8,9,17,36,44,45,46,47,51,52,53};
                for (int i = 0; i<glass.length;i++)
                    setItem(glass[i],ItemCreator.of(toggle ? CompMaterial.LIME_STAINED_GLASS_PANE : CompMaterial.WHITE_STAINED_GLASS_PANE,"§c").make());
                int[] glass2 = new int[]{3,5,18,26,27,35,48,50};
                for (int i = 0; i<glass2.length;i++)
                    setItem(glass2[i],ItemCreator.of(!toggle ? CompMaterial.LIME_STAINED_GLASS_PANE : CompMaterial.WHITE_STAINED_GLASS_PANE,"§c").make());
                toggle = !toggle;
            }
        });
    }
}

