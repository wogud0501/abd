/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.NamespacedKey
 *  org.bukkit.attribute.Attribute
 *  org.bukkit.attribute.AttributeModifier
 *  org.bukkit.attribute.AttributeModifier$Operation
 *  org.bukkit.entity.HumanEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.inventory.CraftItemEvent
 *  org.bukkit.inventory.EquipmentSlot
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 */
package org.minecraft.plugin.reading;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.minecraft.plugin.reading.MainPaperPlugin;

public class RPGCraftingPaperModule
implements Listener {
    private final MainPaperPlugin plugin;
    private final Random random = new Random();

    public RPGCraftingPaperModule(MainPaperPlugin plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        this.plugin.getServer().getPluginManager().registerEvents((Listener)this, (Plugin)this.plugin);
        this.plugin.getLogger().info("RPG \uc81c\uc791 \uc2dc\uc2a4\ud15c \ubaa8\ub4c8\uc774 \ud65c\uc131\ud654\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
    }

    @EventHandler
    public void onCraftItem(CraftItemEvent event) {
        HumanEntity humanEntity;
        ItemStack result = event.getCurrentItem();
        if (result == null || !((humanEntity = event.getWhoClicked()) instanceof Player)) {
            return;
        }
        Player player = (Player)humanEntity;
        if (this.isArmor(result.getType()) || this.isWeapon(result.getType())) {
            if (result.hasItemMeta() && result.getItemMeta().hasLore()) {
                return;
            }
            this.applyRPGAttributes(player, result);
            event.setCancelled(true);
            event.getInventory().setResult(null);
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().addItem(new ItemStack[]{result});
            } else {
                player.getWorld().dropItemNaturally(player.getLocation(), result);
                player.sendMessage(String.valueOf(ChatColor.RED) + "\uc778\ubca4\ud1a0\ub9ac\uac00 \uac00\ub4dd \ucc28\uc11c \uc544\uc774\ud15c\uc774 \ubc14\ub2e5\uc5d0 \ub5a8\uc5b4\uc84c\uc2b5\ub2c8\ub2e4!");
            }
            Bukkit.getScheduler().runTask((Plugin)this.plugin, () -> {
                ItemStack[] matrix = event.getInventory().getMatrix();
                for (int i = 0; i < matrix.length; ++i) {
                    if (matrix[i] == null || matrix[i].getAmount() <= 0) continue;
                    matrix[i].setAmount(matrix[i].getAmount() - 1);
                }
            });
        }
    }

    private void applyRPGAttributes(Player player, ItemStack item) {
        String grade = this.assignGrade();
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            String gradeColor = this.getGradeColor(grade);
            meta.setDisplayName(gradeColor + item.getType().name());
            ArrayList<CallSite> lore = new ArrayList<CallSite>();
            lore.add((CallSite)((Object)("\u00a77\ub4f1\uae09: " + gradeColor + grade)));
            if (this.isArmor(item.getType())) {
                this.applyArmorAttributes(item, meta, grade, this.getEquipmentSlot(item.getType()), player);
            } else if (this.isWeapon(item.getType())) {
                this.applyWeaponAttributes(item, meta, grade, player);
            }
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
    }

    private String assignGrade() {
        double rand = this.random.nextDouble();
        if (rand < 0.05) {
            return "\uc804\uc124";
        }
        if (rand < 0.1) {
            return "\uc5d0\ud53d";
        }
        return rand < 0.3 ? "\ub808\uc5b4" : "\uc77c\ubc18";
    }

    private void applyArmorAttributes(ItemStack item, ItemMeta meta, String grade, EquipmentSlot slot, Player player) {
        Material material = item.getType();
        NamespacedKey armorKey = new NamespacedKey("reading", "armor_modifier_" + String.valueOf(UUID.randomUUID()));
        NamespacedKey healthKey = new NamespacedKey("reading", "health_modifier_" + String.valueOf(UUID.randomUUID()));
        double armorBonus = 0.0;
        double healthBonus = 0.0;
        switch (material) {
            case DIAMOND_HELMET: 
            case DIAMOND_CHESTPLATE: 
            case DIAMOND_LEGGINGS: 
            case DIAMOND_BOOTS: {
                armorBonus = 8.0;
                break;
            }
            case IRON_HELMET: 
            case IRON_CHESTPLATE: 
            case IRON_LEGGINGS: 
            case IRON_BOOTS: {
                armorBonus = 6.0;
                break;
            }
            case LEATHER_HELMET: 
            case LEATHER_CHESTPLATE: 
            case LEATHER_LEGGINGS: 
            case LEATHER_BOOTS: {
                armorBonus = 4.0;
                break;
            }
            default: {
                return;
            }
        }
        if (grade.equals("\uc804\uc124")) {
            armorBonus += 6.0;
            healthBonus += 2.0;
            this.broadcastObtainedMessage(player, grade);
        } else if (grade.equals("\uc5d0\ud53d")) {
            armorBonus += 4.0;
            this.broadcastObtainedMessage(player, grade);
        } else if (grade.equals("\ub808\uc5b4")) {
            armorBonus += 2.0;
        }
        meta.addAttributeModifier(Attribute.ARMOR, new AttributeModifier(armorKey, armorBonus, AttributeModifier.Operation.ADD_NUMBER, slot.getGroup()));
        if (healthBonus > 0.0) {
            meta.addAttributeModifier(Attribute.MAX_HEALTH, new AttributeModifier(healthKey, healthBonus, AttributeModifier.Operation.ADD_NUMBER, slot.getGroup()));
        }
        meta.setDisplayName(this.translateItemName(material));
    }

    private void applyWeaponAttributes(ItemStack item, ItemMeta meta, String grade, Player player) {
        Material material = item.getType();
        NamespacedKey damageKey = new NamespacedKey("reading", "damage_modifier_" + String.valueOf(UUID.randomUUID()));
        NamespacedKey healthKey = new NamespacedKey("reading", "health_modifier_" + String.valueOf(UUID.randomUUID()));
        NamespacedKey speedKey = new NamespacedKey("reading", "attack_speed_modifier_" + String.valueOf(UUID.randomUUID()));
        NamespacedKey miningSpeedKey = new NamespacedKey("reading", "mining_speed_modifier_" + String.valueOf(UUID.randomUUID()));
        double attackBonus = 0.0;
        double healthBonus = 0.0;
        double attackSpeedBonus = 0.0;
        double miningSpeedBonus = 0.0;
        switch (material) {
            case DIAMOND_SWORD: {
                attackBonus = 8.0;
                break;
            }
            case DIAMOND_AXE: {
                attackBonus = 10.0;
                break;
            }
            case DIAMOND_SHOVEL: 
            case DIAMOND_HOE: 
            case IRON_SHOVEL: {
                attackBonus = 4.0;
                break;
            }
            case DIAMOND_PICKAXE: 
            case IRON_SWORD: {
                attackBonus = 6.0;
                break;
            }
            case IRON_PICKAXE: {
                attackBonus = 5.0;
                break;
            }
            case IRON_AXE: {
                attackBonus = 7.0;
                break;
            }
            case IRON_HOE: {
                attackBonus = 3.0;
                break;
            }
            default: {
                return;
            }
        }
        if (grade.equals("\uc804\uc124")) {
            attackBonus += 8.0;
            healthBonus += 2.0;
            attackSpeedBonus += 1.0;
            miningSpeedBonus += 3.0;
            this.broadcastObtainedMessage(player, grade);
        } else if (grade.equals("\uc5d0\ud53d")) {
            attackBonus += 6.0;
            attackSpeedBonus += 0.75;
            miningSpeedBonus += 2.0;
            this.broadcastObtainedMessage(player, grade);
        } else if (grade.equals("\ub808\uc5b4")) {
            attackBonus += 4.0;
            attackSpeedBonus += 0.5;
            miningSpeedBonus += 1.0;
        }
        meta.addAttributeModifier(Attribute.ATTACK_DAMAGE, new AttributeModifier(damageKey, attackBonus, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND.getGroup()));
        if (healthBonus > 0.0) {
            meta.addAttributeModifier(Attribute.MAX_HEALTH, new AttributeModifier(healthKey, healthBonus, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND.getGroup()));
        }
        meta.addAttributeModifier(Attribute.ATTACK_SPEED, new AttributeModifier(speedKey, attackSpeedBonus, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND.getGroup()));
        if (material.name().contains("PICKAXE") || material.name().contains("AXE")) {
            meta.addAttributeModifier(Attribute.LUCK, new AttributeModifier(miningSpeedKey, miningSpeedBonus, AttributeModifier.Operation.ADD_NUMBER, EquipmentSlot.HAND.getGroup()));
        }
        meta.setDisplayName(this.translateItemName(material));
    }

    private void broadcastObtainedMessage(Player player, String grade) {
        ChatColor color = grade.equals("\uc804\uc124") ? ChatColor.YELLOW : ChatColor.LIGHT_PURPLE;
        String message = String.valueOf(color) + String.valueOf(ChatColor.BOLD) + player.getName() + "\ub2d8\uc774 " + grade + " \uc544\uc774\ud15c\uc744 \ud68d\ub4dd\ud558\uc168\uc2b5\ub2c8\ub2e4!";
        Bukkit.broadcastMessage((String)message);
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

    private String getGradeColor(String grade) {
        switch (grade) {
            case "\uc804\uc124": {
                return "\u00a76";
            }
            case "\uc5d0\ud53d": {
                return "\u00a75";
            }
            case "\ub808\uc5b4": {
                return "\u00a7b";
            }
        }
        return "\u00a7f";
    }

    private boolean isArmor(Material material) {
        return material.name().endsWith("_HELMET") || material.name().endsWith("_CHESTPLATE") || material.name().endsWith("_LEGGINGS") || material.name().endsWith("_BOOTS");
    }

    private boolean isWeapon(Material material) {
        return material.name().endsWith("_SWORD") || material.name().endsWith("_AXE") || material.name().endsWith("_PICKAXE") || material.name().endsWith("_SHOVEL") || material.name().endsWith("_HOE");
    }

    private EquipmentSlot getEquipmentSlot(Material material) {
        if (material.name().endsWith("_HELMET")) {
            return EquipmentSlot.HEAD;
        }
        if (material.name().endsWith("_CHESTPLATE")) {
            return EquipmentSlot.CHEST;
        }
        if (material.name().endsWith("_LEGGINGS")) {
            return EquipmentSlot.LEGS;
        }
        return material.name().endsWith("_BOOTS") ? EquipmentSlot.FEET : EquipmentSlot.HAND;
    }

    public void shutdown() {
        this.plugin.getServer().getMessenger().unregisterIncomingPluginChannel((Plugin)this.plugin);
    }
}

