/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 */
package org.minecraft.plugin.reading;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.minecraft.plugin.reading.CurrencyManager;

public class SlotMachineModule
implements Listener {
    private final JavaPlugin plugin;
    private final CurrencyManager currencyManager;
    private final Random random = new Random();
    private final Map<Player, Boolean> playerSpinning = new HashMap<Player, Boolean>();
    private final Map<Player, Integer> playerBets = new HashMap<Player, Integer>();
    private static final int MAX_BET = 100000;
    private static final int MIN_BET = 1000;
    private static final Material[] SLOT_ITEMS = new Material[]{Material.SWEET_BERRIES, Material.MELON, Material.APPLE, Material.GOLDEN_APPLE, Material.DIAMOND};
    private static final double[] PAYOUTS = new double[]{2.0, 3.0, 5.0, 5.0, 10.0, 20.0};

    public SlotMachineModule(JavaPlugin plugin, CurrencyManager currencyManager) {
        this.plugin = plugin;
        this.currencyManager = currencyManager;
    }

    public void initialize() {
        this.plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.plugin);
        this.registerCommand(new SlotMachineCommand("\uc2ac\ub86f\uba38\uc2e0"));
        this.plugin.getLogger().info("\uc2ac\ub86f\uba38\uc2e0 \ubaa8\ub4c8\uc774 \ud65c\uc131\ud654\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
    }

    private void registerCommand(Command command) {
        try {
            Bukkit.getServer().getCommandMap().register(command.getName(), command);
        }
        catch (Exception e) {
            this.plugin.getLogger().severe("\uba85\ub839\uc5b4 \ub4f1\ub85d \uc911 \uc624\ub958 \ubc1c\uc0dd: " + e.getMessage());
        }
    }

    private void openSlotMachine(Player player) {
        int i;
        int currentBet = this.playerBets.getOrDefault(player, 1000);
        Inventory gui = Bukkit.createInventory(null, (int)54, (String)"\u00a76\u00a7l\u2605 \uc2ac\ub86f\uba38\uc2e0 \u2605");
        for (i = 0; i < 54; ++i) {
            gui.setItem(i, this.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, " ", new String[0]));
        }
        for (i = 20; i <= 24; ++i) {
            gui.setItem(i, this.createGuiItem(Material.WHITE_STAINED_GLASS_PANE, "\u00a7f\u00a7l?", new String[0]));
        }
        ItemStack betItem = this.createGuiItem(Material.GOLD_INGOT, "\u00a7e\u00a7l\ubc30\ud305\ud558\uae30", "\u00a7f\ud074\ub9ad\ud558\uc5ec \u00a76" + currentBet + " RING \u00a7f\ubc30\ud305");
        gui.setItem(49, betItem);
        ItemStack increaseBet = this.createGuiItem(Material.GREEN_WOOL, "\u00a7a\u00a7l\ubc30\ud305 \uae08\uc561 \uc99d\uac00", "\u00a7f\ubc30\ud305 \uae08\uc561\uc744 \u00a7a1000 RING \u00a7f\uc529 \uc99d\uac00");
        gui.setItem(48, increaseBet);
        ItemStack decreaseBet = this.createGuiItem(Material.RED_WOOL, "\u00a7c\u00a7l\ubc30\ud305 \uae08\uc561 \uac10\uc18c", "\u00a7f\ubc30\ud305 \uae08\uc561\uc744 \u00a7c1000 RING \u00a7f\uc529 \uac10\uc18c");
        gui.setItem(50, decreaseBet);
        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("\u00a76\u00a7l\u2605 \uc2ac\ub86f\uba38\uc2e0 \u2605")) {
            return;
        }
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getWhoClicked();
        int currentBet = this.playerBets.getOrDefault(player, 1000);
        if (event.getRawSlot() == 49 && !this.playerSpinning.getOrDefault(player, false).booleanValue()) {
            long balance = this.currencyManager.getBalance(player);
            if (balance < (long)currentBet) {
                player.sendMessage("\u00a7c\uc794\uc561\uc774 \ubd80\uc871\ud569\ub2c8\ub2e4. \ud604\uc7ac \uc794\uc561: \u00a7e" + balance + " RING");
                player.closeInventory();
                return;
            }
            this.spinSlotMachine(player, event.getClickedInventory());
        } else if (event.getRawSlot() == 48 && !this.playerSpinning.getOrDefault(player, false).booleanValue()) {
            if (currentBet < 100000) {
                this.playerBets.put(player, currentBet += 1000);
                player.sendMessage("\u00a7a\ubc30\ud305 \uae08\uc561\uc774 \u00a7e" + currentBet + " RING \u00a7a\uc73c\ub85c \uc99d\uac00\ud588\uc2b5\ub2c8\ub2e4.");
                this.openSlotMachine(player);
            } else {
                player.sendMessage("\u00a7c\ucd5c\ub300 \ubc30\ud305 \uae08\uc561\uc5d0 \ub3c4\ub2ec\ud588\uc2b5\ub2c8\ub2e4.");
            }
        } else if (event.getRawSlot() == 50 && !this.playerSpinning.getOrDefault(player, false).booleanValue()) {
            if (currentBet > 1000) {
                this.playerBets.put(player, currentBet -= 1000);
                player.sendMessage("\u00a7c\ubc30\ud305 \uae08\uc561\uc774 \u00a7e" + currentBet + " RING \u00a7c\uc73c\ub85c \uac10\uc18c\ud588\uc2b5\ub2c8\ub2e4.");
                this.openSlotMachine(player);
            } else {
                player.sendMessage("\u00a7c\ucd5c\uc18c \ubc30\ud305 \uae08\uc561\uc5d0 \ub3c4\ub2ec\ud588\uc2b5\ub2c8\ub2e4.");
            }
        }
    }

    private void spinSlotMachine(final Player player, final Inventory inventory) {
        this.playerSpinning.put(player, true);
        int currentBet = this.playerBets.getOrDefault(player, 1000);
        this.currencyManager.removeBalance(player, currentBet);
        player.sendMessage("\u00a76\ubc30\ud305 \u00a7e" + currentBet + " RING \u00a76\uc744(\ub97c) \ud558\uc168\uc2b5\ub2c8\ub2e4.");
        new BukkitRunnable(){
            int ticks = 0;

            public void run() {
                if (this.ticks >= 20) {
                    this.cancel();
                    SlotMachineModule.this.checkWin(player, inventory);
                    SlotMachineModule.this.playerSpinning.put(player, false);
                    return;
                }
                for (int i = 0; i < 5; ++i) {
                    inventory.setItem(20 + i, new ItemStack(SLOT_ITEMS[SlotMachineModule.this.random.nextInt(SLOT_ITEMS.length)]));
                }
                ++this.ticks;
            }
        }.runTaskTimer((Plugin)this.plugin, 0L, 5L);
    }

    private void checkWin(Player player, Inventory inventory) {
        ItemStack[] items = new ItemStack[5];
        for (int i = 0; i < 5; ++i) {
            items[i] = inventory.getItem(20 + i);
        }
        int maxConsecutive = 0;
        int currentConsecutive = 1;
        Material currentType = items[0].getType();
        for (int i = 1; i < 5; ++i) {
            if (items[i].getType() == currentType) {
                maxConsecutive = Math.max(maxConsecutive, ++currentConsecutive);
                continue;
            }
            currentConsecutive = 1;
            currentType = items[i].getType();
        }
        int currentBet = this.playerBets.getOrDefault(player, 1000);
        double multiplier = 0.0;
        if (maxConsecutive >= 3) {
            int index = Arrays.asList(SLOT_ITEMS).indexOf(currentType);
            if (maxConsecutive == 5) {
                switch (currentType) {
                    case SWEET_BERRIES: 
                    case MELON: 
                    case APPLE: {
                        multiplier = 5.0;
                        break;
                    }
                    case GOLDEN_APPLE: {
                        multiplier = 10.0;
                        break;
                    }
                    case DIAMOND: {
                        multiplier = 20.0;
                    }
                }
            } else if (maxConsecutive == 3) {
                switch (currentType) {
                    case SWEET_BERRIES: {
                        multiplier = 2.0;
                        break;
                    }
                    case MELON: 
                    case APPLE: {
                        multiplier = 3.0;
                        break;
                    }
                    case GOLDEN_APPLE: 
                    case DIAMOND: {
                        multiplier = 5.0;
                    }
                }
            }
            long winAmount = (long)((double)currentBet * multiplier);
            this.currencyManager.addBalance(player, winAmount);
            if (multiplier >= 10.0) {
                Bukkit.broadcastMessage((String)"\u00a7d\u00a7k||| \u00a76\u00a7l\ub300\ubc15 \uc7ad\ud31f! \u00a7d\u00a7k|||");
                Bukkit.broadcastMessage((String)("\u00a7e\u00a7l" + player.getName() + "\u00a7a\ub2d8\uc774 \uc2ac\ub86f\uba38\uc2e0\uc5d0\uc11c \u00a76\u00a7l" + winAmount + " RING\u00a7a\uc744 \ud68d\ub4dd\ud558\uc168\uc2b5\ub2c8\ub2e4!"));
                Bukkit.broadcastMessage((String)"\u00a7b\u00a7k*** \u00a7c\u00a7l\ucd95\ud558\ub4dc\ub9bd\ub2c8\ub2e4! \u00a7b\u00a7k***");
            }
            player.sendMessage("\u00a7a\u00a7l\ucd95\ud558\ud569\ub2c8\ub2e4! \u00a7e" + winAmount + " RING\u00a7a\uc744 \ud68d\ub4dd\ud558\uc168\uc2b5\ub2c8\ub2e4! \u00a77(" + maxConsecutive + "\uac1c \uc5f0\uc18d \uc77c\uce58)");
        } else {
            player.sendMessage("\u00a7c\u00a7l\uc544\uc27d\uac8c\ub3c4 \uaf5d\uc785\ub2c8\ub2e4. \ub2e4\uc74c \uae30\ud68c\uc5d0!");
        }
        long newBalance = this.currencyManager.getBalance(player);
        player.sendMessage("\u00a7f\ud604\uc7ac \uc794\uc561: \u00a7e" + newBalance + " RING");
    }

    private ItemStack createGuiItem(Material material, String name, String ... lore) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return item;
    }

    public void shutdown() {
        this.plugin.getLogger().info("\uc0c1\uc810 \uc2dc\uc2a4\ud15c \ubaa8\ub4c8\uc774 \ube44\ud65c\uc131\ud654\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
    }

    private class SlotMachineCommand
    extends Command {
        public SlotMachineCommand(String name) {
            super(name);
            this.setDescription("\uc2ac\ub86f\uba38\uc2e0\uc744 \uc2e4\ud589\ud569\ub2c8\ub2e4.");
            this.setUsage("/" + name);
        }

        public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            if (sender instanceof Player) {
                SlotMachineModule.this.openSlotMachine((Player)sender);
            } else {
                sender.sendMessage("\uc774 \uba85\ub839\uc5b4\ub294 \ud50c\ub808\uc774\uc5b4\ub9cc \uc0ac\uc6a9\ud560 \uc218 \uc788\uc2b5\ub2c8\ub2e4.");
            }
            return true;
        }
    }
}

