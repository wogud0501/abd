/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.minecraft.plugin.reading;

import org.bukkit.plugin.java.JavaPlugin;
import org.minecraft.plugin.reading.CurrencyManager;
import org.minecraft.plugin.reading.MeatRotPaperModule;
import org.minecraft.plugin.reading.RINGCurrencyModule;
import org.minecraft.plugin.reading.RPGCraftingPaperModule;
import org.minecraft.plugin.reading.RPGItemFusionModule;
import org.minecraft.plugin.reading.ShopSystemModule;
import org.minecraft.plugin.reading.SlotMachineModule;

public class MainPaperPlugin
extends JavaPlugin {
    private MeatRotPaperModule MeatRotPaperModule;
    private RPGCraftingPaperModule RPGCraftingPaperModule;
    private ShopSystemModule ShopSystemModule;
    private RPGItemFusionModule RPGItemFusionModule;
    private RINGCurrencyModule RINGCurrencyModule;
    private SlotMachineModule SlotMachineModule;

    public void onEnable() {
        this.MeatRotPaperModule = new MeatRotPaperModule(this);
        this.RPGCraftingPaperModule = new RPGCraftingPaperModule(this);
        this.RPGItemFusionModule = new RPGItemFusionModule(this);
        this.RINGCurrencyModule = new RINGCurrencyModule(this);
        this.RINGCurrencyModule.initialize();
        CurrencyManager currencyManager = this.RINGCurrencyModule.getCurrencyManager();
        this.ShopSystemModule = new ShopSystemModule(this, currencyManager);
        this.SlotMachineModule = new SlotMachineModule(this, currencyManager);
        this.MeatRotPaperModule.initialize();
        this.RPGCraftingPaperModule.initialize();
        this.ShopSystemModule.initialize();
        this.RPGItemFusionModule.initialize();
        this.SlotMachineModule.initialize();
        this.getLogger().info("Paper \ud1b5\ud569 \ud50c\ub7ec\uadf8\uc778\uc774 \ud65c\uc131\ud654\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
    }

    public void onDisable() {
        if (this.MeatRotPaperModule != null) {
            this.MeatRotPaperModule.shutdown();
        }
        if (this.RPGCraftingPaperModule != null) {
            this.RPGCraftingPaperModule.shutdown();
        }
        if (this.ShopSystemModule != null) {
            this.ShopSystemModule.shutdown();
        }
        if (this.RPGItemFusionModule != null) {
            this.RPGItemFusionModule.shutdown();
        }
        if (this.RINGCurrencyModule != null) {
            this.RINGCurrencyModule.shutdown();
        }
        if (this.SlotMachineModule != null) {
            this.SlotMachineModule.shutdown();
        }
        this.getLogger().info("Paper \ud1b5\ud569 \ud50c\ub7ec\uadf8\uc778\uc774 \ube44\ud65c\uc131\ud654\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
    }
}

