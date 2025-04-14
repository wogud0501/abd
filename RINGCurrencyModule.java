/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.command.defaults.BukkitCommand
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.jetbrains.annotations.NotNull
 */
package org.minecraft.plugin.reading;

import java.util.Collections;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.minecraft.plugin.reading.CurrencyManager;
import org.minecraft.plugin.reading.CurrencyUseListener;

public class RINGCurrencyModule {
    private final JavaPlugin plugin;
    private final CurrencyManager currencyManager;

    public RINGCurrencyModule(JavaPlugin plugin) {
        this.plugin = plugin;
        this.currencyManager = new CurrencyManager(plugin);
    }

    public void initialize() {
        this.registerCommand((Command)new BalanceCommand("\uc794\uc561\ud655\uc778"));
        this.registerCommand((Command)new UseCurrencyCommand("\ud654\ud3d0\uc0ac\uc6a9"));
        this.registerCommand((Command)new GiveCurrencyCommand("\ud654\ud3d0\uc9c0\uae09"));
        this.plugin.getServer().getPluginManager().registerEvents((Listener)new CurrencyUseListener(this.currencyManager), (Plugin)this.plugin);
        this.plugin.getLogger().info("RING \ud654\ud3d0 \uc2dc\uc2a4\ud15c\uc774 \ud65c\uc131\ud654\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
    }

    private void registerCommand(Command command) {
        try {
            Bukkit.getServer().getCommandMap().register(command.getName(), command);
        }
        catch (Exception e) {
            this.plugin.getLogger().severe("\uba85\ub839\uc5b4 \ub4f1\ub85d \uc911 \uc624\ub958 \ubc1c\uc0dd: " + e.getMessage());
        }
    }

    private void logCurrencyUse(Player player, long amount) {
        String message = String.format("\u00a77[\ud654\ud3d0 \uc0ac\uc6a9] \u00a7e%s\u00a77\ub2d8\uc774 \u00a7e%d RING\u00a77\uc744 \uc0ac\uc6a9\ud588\uc2b5\ub2c8\ub2e4.", player.getName(), amount);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.hasPermission("currencymanager.admin")) continue;
            onlinePlayer.sendMessage(message);
        }
        this.plugin.getLogger().info(message);
    }

    public CurrencyManager getCurrencyManager() {
        return this.currencyManager;
    }

    public void shutdown() {
        this.plugin.getLogger().info("\ud654\ud3d0 \uc2dc\uc2a4\ud15c \ubaa8\ub4c8\uc774 \ube44\ud65c\uc131\ud654\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
    }

    private class BalanceCommand
    extends BukkitCommand {
        public BalanceCommand(String name) {
            super(name);
            this.setAliases(Collections.singletonList("bal"));
        }

        public boolean execute(@NotNull CommandSender sender, String commandLabel, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("\u00a7c\ud50c\ub808\uc774\uc5b4\ub9cc \uc0ac\uc6a9\ud560 \uc218 \uc788\uc2b5\ub2c8\ub2e4.");
                return true;
            }
            Player player = (Player)sender;
            long balance = RINGCurrencyModule.this.currencyManager.getBalance(player);
            player.sendMessage("\u00a7a[ RING \ud654\ud3d0 \uc2dc\uc2a4\ud15c ] \u00a77\ud604\uc7ac \uc794\uc561: \u00a7e" + balance + " RING");
            return true;
        }
    }

    private class UseCurrencyCommand
    extends BukkitCommand {
        public UseCurrencyCommand(String name) {
            super(name);
        }

        public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            long amount;
            if (!(sender instanceof Player)) {
                sender.sendMessage("\u00a7c\ud50c\ub808\uc774\uc5b4\ub9cc \uc0ac\uc6a9\ud560 \uc218 \uc788\uc2b5\ub2c8\ub2e4.");
                return true;
            }
            if (args.length < 1) {
                sender.sendMessage("\u00a76\uc0ac\uc6a9\ubc95: \u00a7e/\ud654\ud3d0\uc0ac\uc6a9 <\uae08\uc561>");
                return true;
            }
            Player player = (Player)sender;
            try {
                amount = Long.parseLong(args[0]);
            }
            catch (NumberFormatException e) {
                player.sendMessage("\u00a7c\uc62c\ubc14\ub978 \uae08\uc561\uc744 \uc785\ub825\ud574\uc8fc\uc138\uc694.");
                return true;
            }
            if (RINGCurrencyModule.this.currencyManager.removeBalance(player, amount)) {
                player.sendMessage("\u00a7a[ \uc131\uacf5 ] \u00a7e" + amount + " RING \u00a77\uc744(\ub97c) \uc0ac\uc6a9\ud588\uc2b5\ub2c8\ub2e4.");
                RINGCurrencyModule.this.logCurrencyUse(player, amount);
            } else {
                player.sendMessage("\u00a7c[ \uc2e4\ud328 ] \u00a77\uc794\uc561\uc774 \ubd80\uc871\ud569\ub2c8\ub2e4.");
            }
            return true;
        }
    }

    private class GiveCurrencyCommand
    extends BukkitCommand {
        public GiveCurrencyCommand(String name) {
            super(name);
        }

        public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            long amount;
            if (!sender.hasPermission("currencymanager.admin")) {
                sender.sendMessage("\u00a7c[ \uad8c\ud55c \ubd80\uc871 ] \u00a77\uc774 \uba85\ub839\uc5b4\ub97c \uc0ac\uc6a9\ud560 \uad8c\ud55c\uc774 \uc5c6\uc2b5\ub2c8\ub2e4.");
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage("\u00a76\uc0ac\uc6a9\ubc95: \u00a7e/\ud654\ud3d0\uc9c0\uae09 <\ud50c\ub808\uc774\uc5b4> <\uae08\uc561>");
                return true;
            }
            Player target = Bukkit.getPlayer((String)args[0]);
            if (target == null) {
                sender.sendMessage("\u00a7c\ud50c\ub808\uc774\uc5b4\ub97c \ucc3e\uc744 \uc218 \uc5c6\uc2b5\ub2c8\ub2e4.");
                return true;
            }
            try {
                amount = Long.parseLong(args[1]);
            }
            catch (NumberFormatException e) {
                sender.sendMessage("\u00a7c\uc62c\ubc14\ub978 \uae08\uc561\uc744 \uc785\ub825\ud574\uc8fc\uc138\uc694.");
                return true;
            }
            RINGCurrencyModule.this.currencyManager.addBalance(target, amount);
            sender.sendMessage("\u00a7a[ \uc131\uacf5 ] \u00a77" + target.getName() + "\uc5d0\uac8c \u00a7e" + amount + " RING\u00a77\uc744 \uc9c0\uae09\ud588\uc2b5\ub2c8\ub2e4.");
            target.sendMessage("\u00a7a[ \uc218\uc2e0 ] \u00a77" + amount + " RING\u00a77\uc744 \ubc1b\uc558\uc2b5\ub2c8\ub2e4.");
            return true;
        }
    }
}

