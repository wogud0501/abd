/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Effect
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.Sound
 *  org.bukkit.attribute.Attribute
 *  org.bukkit.attribute.AttributeModifier
 *  org.bukkit.attribute.AttributeModifier$Operation
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.PrepareAnvilEvent
 *  org.bukkit.inventory.AnvilInventory
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 */
package org.minecraft.plugin.reading;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.minecraft.plugin.reading.MainPaperPlugin;

public class RPGItemFusionModule
implements Listener {
    private final MainPaperPlugin plugin;
    private final Random random = new Random();
    private boolean isProcessing = false;
    private static final Map<String, Integer> fusionChances = new HashMap<String, Integer>();

    public RPGItemFusionModule(MainPaperPlugin plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        this.plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.plugin);
        this.plugin.getLogger().info("RPG \uc544\uc774\ud15c \ud569\uc131 \uc2dc\uc2a4\ud15c\uc774 \ud65c\uc131\ud654\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
    }

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent event) {
        String grade2;
        if (this.isProcessing) {
            return;
        }
        this.isProcessing = true;
        AnvilInventory anvilInventory = event.getInventory();
        ItemStack firstItem = anvilInventory.getItem(0);
        ItemStack secondItem = anvilInventory.getItem(1);
        if (firstItem == null || secondItem == null) {
            this.isProcessing = false;
            return;
        }
        if (firstItem.getType() != secondItem.getType()) {
            this.isProcessing = false;
            return;
        }
        if (!(this.isWeapon(firstItem.getType()) || this.isArmor(firstItem.getType()) || this.isTool(firstItem.getType()))) {
            this.isProcessing = false;
            return;
        }
        String grade1 = this.getItemGrade(firstItem);
        if (!grade1.equals(grade2 = this.getItemGrade(secondItem)) || !fusionChances.containsKey(grade1)) {
            this.isProcessing = false;
            return;
        }
        event.setResult(null);
        Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> {
            int successRate = fusionChances.get(grade1);
            if (this.random.nextInt(100) < successRate) {
                String nextGrade = this.getNextGrade(grade1);
                ItemStack fusedItem = this.createFusedItem(firstItem, nextGrade);
                anvilInventory.setItem(0, null);
                anvilInventory.setItem(1, null);
                if (!event.getViewers().isEmpty() && event.getViewers().get(0) instanceof Player) {
                    Player player = (Player)event.getViewers().get(0);
                    this.giveItemToPlayer(player, fusedItem);
                    this.broadcastFusionMessage(Collections.singletonList(player), fusedItem, nextGrade);
                }
            } else {
                anvilInventory.setItem(1, null);
                if (!event.getViewers().isEmpty() && event.getViewers().get(0) instanceof Player) {
                    Player player = (Player)event.getViewers().get(0);
                    player.sendMessage(String.valueOf(ChatColor.RED) + "\ud569\uc131\uc5d0 \uc2e4\ud328\ud588\uc2b5\ub2c8\ub2e4!");
                }
            }
            this.isProcessing = false;
        });
    }

    private void giveItemToPlayer(Player player, ItemStack item) {
        if (player.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(new ItemStack[]{item});
        } else {
            player.getWorld().dropItem(player.getLocation(), item);
            player.sendMessage(String.valueOf(ChatColor.RED) + "\uc778\ubca4\ud1a0\ub9ac\uac00 \uac00\ub4dd \ucc28\uc11c \uc544\uc774\ud15c\uc774 \ubc14\ub2e5\uc5d0 \ub5a8\uc5b4\uc84c\uc2b5\ub2c8\ub2e4!");
        }
    }

    private String getItemGrade(ItemStack item) {
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            for (String lore : item.getItemMeta().getLore()) {
                if (!lore.contains("\ub4f1\uae09: ")) continue;
                return ChatColor.stripColor((String)lore.replace("\ub4f1\uae09: ", "").trim());
            }
        }
        return "\uc77c\ubc18";
    }

    private String getNextGrade(String currentGrade) {
        switch (currentGrade) {
            case "\uc77c\ubc18": {
                return "\ub808\uc5b4";
            }
            case "\ub808\uc5b4": {
                return "\uc5d0\ud53d";
            }
            case "\uc5d0\ud53d": {
                return "\uc804\uc124";
            }
            case "\uc804\uc124": {
                return "\uc2e0\ud654";
            }
        }
        return "\uc77c\ubc18";
    }

    private ItemStack createFusedItem(ItemStack baseItem, String newGrade) {
        ItemStack newItem = baseItem.clone();
        ItemMeta meta = newItem.getItemMeta();
        if (meta == null) {
            return newItem;
        }
        meta.setDisplayName(this.getGradeColor(newGrade) + this.translateItemName(newItem.getType()));
        ArrayList<CallSite> lore = new ArrayList<CallSite>();
        lore.add((CallSite)((Object)("\u00a77\ub4f1\uae09: " + this.getGradeColor(newGrade) + newGrade)));
        meta.setLore(lore);
        this.applyGradeAttributes(newItem, meta, newGrade);
        newItem.setItemMeta(meta);
        return newItem;
    }

    private String translateItemName(Material material) {
        HashMap<Material, String> translations = new HashMap<Material, String>();
        translations.put(Material.DIAMOND_SWORD, "\uc11c\ub9ac\ud55c");
        translations.put(Material.IRON_SWORD, "\ucca0 \uac80");
        translations.put(Material.WOODEN_SWORD, "\ub098\ubb34 \uac80");
        translations.put(Material.DIAMOND_PICKAXE, "\ud0b9\uc624\uace1");
        translations.put(Material.IRON_PICKAXE, "\ucca0 \uace1\uad2d\uc774");
        translations.put(Material.WOODEN_PICKAXE, "\ub098\ubb34 \uace1\uad2d\uc774");
        return translations.getOrDefault(material, material.name());
    }

    private void applyGradeAttributes(ItemStack item, ItemMeta meta, String grade) {
        NamespacedKey attackKey = new NamespacedKey("reading", "attack_bonus_" + String.valueOf(UUID.randomUUID()));
        NamespacedKey healthKey = new NamespacedKey("reading", "health_bonus_" + String.valueOf(UUID.randomUUID()));
        NamespacedKey defenseKey = new NamespacedKey("reading", "defense_bonus_" + String.valueOf(UUID.randomUUID()));
        NamespacedKey speedKey = new NamespacedKey("reading", "attack_speed_bonus_" + String.valueOf(UUID.randomUUID()));
        double healthBonus = 0.0;
        double defenseBonus = 0.0;
        double attackBonus = 0.0;
        double attackSpeedBonus = 0.0;
        if (this.isArmor(item.getType())) {
            if ("\uc2e0\ud654".equals(grade)) {
                defenseBonus = 10.0;
                healthBonus = 8.0;
            } else if ("\uc804\uc124".equals(grade)) {
                defenseBonus = 6.0;
                healthBonus = 2.0;
            } else if ("\uc5d0\ud53d".equals(grade)) {
                defenseBonus = 4.0;
            } else if ("\ub808\uc5b4".equals(grade)) {
                defenseBonus = 2.0;
            }
        } else if (this.isWeapon(item.getType())) {
            if ("\uc2e0\ud654".equals(grade)) {
                attackBonus = 10.0;
                attackSpeedBonus = 2.0;
            } else if ("\uc804\uc124".equals(grade)) {
                attackBonus = 8.0;
                attackSpeedBonus = 1.5;
            } else if ("\uc5d0\ud53d".equals(grade)) {
                attackBonus = 6.0;
                attackSpeedBonus = 0.75;
            } else if ("\ub808\uc5b4".equals(grade)) {
                attackBonus = 4.0;
                attackSpeedBonus = 0.5;
            }
        }
        meta.addAttributeModifier(Attribute.MAX_HEALTH, new AttributeModifier(healthKey, healthBonus, AttributeModifier.Operation.ADD_NUMBER));
        meta.addAttributeModifier(Attribute.ARMOR, new AttributeModifier(defenseKey, defenseBonus, AttributeModifier.Operation.ADD_NUMBER));
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, new AttributeModifier(attackKey, attackBonus, AttributeModifier.Operation.ADD_NUMBER));
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, new AttributeModifier(speedKey, attackSpeedBonus, AttributeModifier.Operation.ADD_NUMBER));
    }

    private boolean isArmor(Material material) {
        return material == Material.DIAMOND_HELMET || material == Material.IRON_HELMET || material == Material.GOLDEN_HELMET || material == Material.LEATHER_HELMET || material == Material.CHAINMAIL_HELMET || material == Material.DIAMOND_CHESTPLATE || material == Material.IRON_CHESTPLATE || material == Material.GOLDEN_CHESTPLATE || material == Material.LEATHER_CHESTPLATE || material == Material.CHAINMAIL_CHESTPLATE || material == Material.DIAMOND_LEGGINGS || material == Material.IRON_LEGGINGS || material == Material.GOLDEN_LEGGINGS || material == Material.LEATHER_LEGGINGS || material == Material.CHAINMAIL_LEGGINGS || material == Material.DIAMOND_BOOTS || material == Material.IRON_BOOTS || material == Material.GOLDEN_BOOTS || material == Material.LEATHER_BOOTS || material == Material.CHAINMAIL_BOOTS;
    }

    private boolean isTool(Material material) {
        return material.name().endsWith("_SHOVEL") || material.name().endsWith("_AXE") || material.name().endsWith("_PICKAXE") || material.name().endsWith("_HOE");
    }

    private boolean isWeapon(Material material) {
        return material.name().endsWith("_SWORD") || material.name().endsWith("_AXE") || material.name().endsWith("_PICKAXE");
    }

    private void broadcastFusionMessage(List<Player> viewers, ItemStack fusedItem, String grade) {
        Object message = "";
        String displayName = fusedItem.getItemMeta().getDisplayName();
        Player player = viewers.get(0);
        switch (grade) {
            case "\uc2e0\ud654": {
                message = String.valueOf(ChatColor.LIGHT_PURPLE) + String.valueOf(ChatColor.BOLD) + "\u2728\u2728 \ucd95\ud558\ud569\ub2c8\ub2e4! " + String.valueOf(ChatColor.ITALIC) + "\uc2e0\ud654 \ub4f1\uae09 \uc544\uc774\ud15c" + String.valueOf(ChatColor.RESET) + "\uc744 \ud569\uc131\ud588\uc2b5\ub2c8\ub2e4! " + String.valueOf(ChatColor.LIGHT_PURPLE) + String.valueOf(ChatColor.BOLD) + displayName + String.valueOf(ChatColor.RESET) + " \u2728\u2728";
                Bukkit.getServer().broadcastMessage(String.valueOf(ChatColor.AQUA) + String.valueOf(ChatColor.BOLD) + ">>> " + String.valueOf(ChatColor.YELLOW) + String.valueOf(ChatColor.BOLD) + "\ud83c\udf89 " + String.valueOf(ChatColor.ITALIC) + "\ud83d\udd25" + String.valueOf(ChatColor.RED) + String.valueOf(ChatColor.BOLD) + " [ALL] " + String.valueOf(ChatColor.GREEN) + player.getName() + "\ub2d8\uc774 " + (String)message + " \ud83d\udd25" + String.valueOf(ChatColor.YELLOW) + " \ud83c\udf89" + String.valueOf(ChatColor.AQUA) + " <<<" + String.valueOf(ChatColor.RESET));
                player.playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
                player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1.0f, 1.0f);
                break;
            }
            case "\uc804\uc124": {
                message = String.valueOf(ChatColor.GOLD) + String.valueOf(ChatColor.BOLD) + "\ud83c\udf1f \ub300\ub2e8\ud569\ub2c8\ub2e4! " + String.valueOf(ChatColor.ITALIC) + "\uc804\uc124 \ub4f1\uae09 \uc544\uc774\ud15c" + String.valueOf(ChatColor.RESET) + "\uc744 \ud569\uc131\ud588\uc2b5\ub2c8\ub2e4! " + String.valueOf(ChatColor.GOLD) + String.valueOf(ChatColor.BOLD) + displayName + String.valueOf(ChatColor.RESET) + " \ud83c\udf1f";
                Bukkit.getServer().broadcastMessage(String.valueOf(ChatColor.AQUA) + String.valueOf(ChatColor.BOLD) + ">>> " + String.valueOf(ChatColor.YELLOW) + String.valueOf(ChatColor.BOLD) + "\ud83c\udf89 " + String.valueOf(ChatColor.ITALIC) + "\ud83d\udd25" + String.valueOf(ChatColor.RED) + String.valueOf(ChatColor.BOLD) + " [ALL] " + String.valueOf(ChatColor.GREEN) + player.getName() + "\ub2d8\uc774 " + (String)message + " \ud83d\udd25" + String.valueOf(ChatColor.YELLOW) + " \ud83c\udf89" + String.valueOf(ChatColor.AQUA) + " <<<" + String.valueOf(ChatColor.RESET));
                break;
            }
            case "\uc5d0\ud53d": {
                message = String.valueOf(ChatColor.RED) + String.valueOf(ChatColor.BOLD) + "\ud83d\udd25 \ucd95\ud558\ud569\ub2c8\ub2e4! " + String.valueOf(ChatColor.ITALIC) + "\uc5d0\ud53d \ub4f1\uae09 \uc544\uc774\ud15c" + String.valueOf(ChatColor.RESET) + "\uc744 \ud569\uc131\ud588\uc2b5\ub2c8\ub2e4! " + String.valueOf(ChatColor.RED) + String.valueOf(ChatColor.BOLD) + displayName + String.valueOf(ChatColor.RESET) + " \ud83d\udd25";
                player.sendMessage((String)message);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                break;
            }
            case "\ub808\uc5b4": {
                message = String.valueOf(ChatColor.BLUE) + String.valueOf(ChatColor.BOLD) + "\u2728 \ucd95\ud558\ud569\ub2c8\ub2e4! " + String.valueOf(ChatColor.ITALIC) + "\ub808\uc5b4 \ub4f1\uae09 \uc544\uc774\ud15c" + String.valueOf(ChatColor.RESET) + "\uc744 \ud569\uc131\ud588\uc2b5\ub2c8\ub2e4! " + String.valueOf(ChatColor.BLUE) + String.valueOf(ChatColor.BOLD) + displayName + String.valueOf(ChatColor.RESET) + " \u2728";
                player.sendMessage((String)message);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.0f);
                break;
            }
            default: {
                message = String.valueOf(ChatColor.WHITE) + String.valueOf(ChatColor.BOLD) + "\uc544\uc774\ud15c \ud569\uc131\uc774 \uc644\ub8cc\ub418\uc5c8\uc2b5\ub2c8\ub2e4: " + displayName;
                player.sendMessage((String)message);
            }
        }
    }

    private String getGradeColor(String grade) {
        switch (grade) {
            case "\uc2e0\ud654": {
                return ChatColor.LIGHT_PURPLE.toString();
            }
            case "\uc804\uc124": {
                return ChatColor.GOLD.toString();
            }
            case "\uc5d0\ud53d": {
                return ChatColor.RED.toString();
            }
            case "\ub808\uc5b4": {
                return ChatColor.BLUE.toString();
            }
        }
        return ChatColor.WHITE.toString();
    }

    public void shutdown() {
        this.plugin.getServer().getMessenger().unregisterIncomingPluginChannel((Plugin)this.plugin);
    }

    static {
        fusionChances.put("\uc77c\ubc18", 50);
        fusionChances.put("\ub808\uc5b4", 30);
        fusionChances.put("\uc5d0\ud53d", 20);
        fusionChances.put("\uc804\uc124", 2);
    }
}

