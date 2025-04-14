/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.Listener
 *  org.bukkit.event.player.PlayerJoinEvent
 */
package org.minecraft.plugin.reading;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.minecraft.plugin.reading.CurrencyManager;

public class CurrencyUseListener
implements Listener {
    private final CurrencyManager currencyManager;

    public CurrencyUseListener(CurrencyManager currencyManager) {
        this.currencyManager = currencyManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (this.currencyManager.getBalance(player) != 0L) {
            player.sendMessage("\ud604\uc7ac \uc794\uc561: " + this.currencyManager.getBalance(player) + " RING");
        }
    }
}

