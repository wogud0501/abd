/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.PlayerInventory
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.messaging.PluginMessageListener
 */
package org.minecraft.plugin.reading;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.minecraft.plugin.reading.MainPaperPlugin;

public class MeatRotPaperModule
implements PluginMessageListener {
    private static final String CHANNEL = "meatrot:update";
    private final MainPaperPlugin plugin;

    public MeatRotPaperModule(MainPaperPlugin plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        this.plugin.getServer().getMessenger().registerIncomingPluginChannel((Plugin)this.plugin, CHANNEL, (PluginMessageListener)this);
        this.plugin.getLogger().info("\uace0\uae30 \ubd80\ud328 \ubaa8\ub4c8\uc774 \ud65c\uc131\ud654\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
    }

    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        Player target;
        if (!channel.equals(CHANNEL)) {
            return;
        }
        String[] parts = new String(message).split(":");
        if (parts[0].equals("rot_meat") && (target = this.plugin.getServer().getPlayer(parts[1])) != null) {
            this.rotMeat(target);
        }
    }

    private void rotMeat(Player player) {
        PlayerInventory inventory = player.getInventory();
        int totalRotted = 0;
        Material[] meatTypes = new Material[]{Material.BEEF, Material.COOKED_BEEF, Material.PORKCHOP, Material.COOKED_PORKCHOP, Material.CHICKEN, Material.COOKED_CHICKEN, Material.MUTTON, Material.COOKED_MUTTON, Material.RABBIT, Material.COOKED_RABBIT};
        block0: for (ItemStack item : inventory.getContents()) {
            if (item == null) continue;
            for (Material meatType : meatTypes) {
                if (item.getType() != meatType) continue;
                int amount = Math.min(10, item.getAmount());
                item.setAmount(item.getAmount() - amount);
                inventory.addItem(new ItemStack[]{new ItemStack(Material.ROTTEN_FLESH, amount)});
                totalRotted += amount;
                continue block0;
            }
        }
        if (totalRotted > 0) {
            player.sendMessage("\u00a7c[!] \uc2dc\uac04\uc774 \uc9c0\ub098 \ub2f9\uc2e0\uc758 \uace0\uae30 " + totalRotted + "\uac1c\uac00 \uc369\uc5c8\uc2b5\ub2c8\ub2e4!");
        }
    }

    public void shutdown() {
        this.plugin.getServer().getMessenger().unregisterIncomingPluginChannel((Plugin)this.plugin, CHANNEL);
    }
}

