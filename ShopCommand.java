/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.jetbrains.annotations.NotNull
 */
package org.minecraft.plugin.reading;

import java.util.Collections;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.minecraft.plugin.reading.MainPaperPlugin;

public class ShopCommand
extends Command
implements CommandExecutor {
    private final MainPaperPlugin plugin;

    public ShopCommand(String name, MainPaperPlugin plugin) {
        super(name);
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(String.valueOf(ChatColor.RED) + "\uc774 \uba85\ub839\uc5b4\ub294 \ud50c\ub808\uc774\uc5b4\ub9cc \uc0ac\uc6a9\ud560 \uc218 \uc788\uc2b5\ub2c8\ub2e4.");
            return false;
        }
        Player player = (Player)sender;
        this.openShop(player);
        return true;
    }

    private void openShop(Player player) {
        Inventory shopInventory = Bukkit.createInventory(null, (int)9, (String)"\uc0c1\uc810");
        ItemStack diamondSword = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = diamondSword.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(String.valueOf(ChatColor.YELLOW) + "\ub2e4\uc774\uc544\ubaac\ub4dc \uac80");
            meta.setLore(Collections.singletonList(String.valueOf(ChatColor.GRAY) + "\uac00\uaca9: " + String.valueOf(ChatColor.GOLD) + "5 \ub2e4\uc774\uc544\ubaac\ub4dc \ube14\ub7ed"));
            diamondSword.setItemMeta(meta);
        }
        shopInventory.setItem(4, diamondSword);
        player.openInventory(shopInventory);
    }

    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull @NotNull String @NotNull [] strings) {
        return false;
    }
}

