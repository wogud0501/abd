/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.java.JavaPlugin
 */
package org.minecraft.plugin.reading;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONObject;
import org.json.JSONTokener;

public class CurrencyManager {
    private final JavaPlugin plugin;
    private final Map<String, Long> balances = new HashMap<String, Long>();
    private final File dataFile;

    public CurrencyManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFile = new File(plugin.getDataFolder(), "currency.json");
        this.loadBalances();
    }

    public long getBalance(Player player) {
        return this.balances.getOrDefault(player.getUniqueId().toString(), 0L);
    }

    public boolean removeBalance(Player player, long amount) {
        long currentBalance = this.getBalance(player);
        if (currentBalance < amount) {
            return false;
        }
        this.balances.put(player.getUniqueId().toString(), currentBalance - amount);
        this.saveBalances();
        return true;
    }

    public void addBalance(Player player, long amount) {
        long newBalance = this.getBalance(player) + amount;
        this.balances.put(player.getUniqueId().toString(), newBalance);
        this.saveBalances();
    }

    private void saveBalances() {
        JSONObject json = new JSONObject(this.balances);
        try (FileWriter writer = new FileWriter(this.dataFile);){
            writer.write(json.toString(4));
        }
        catch (IOException e) {
            this.plugin.getLogger().severe("\uc794\uc561\uc744 \uc800\uc7a5\ud558\ub294 \uc911 \uc624\ub958 \ubc1c\uc0dd: " + e.getMessage());
        }
    }

    private void loadBalances() {
        if (!this.dataFile.exists()) {
            return;
        }
        try (FileReader reader = new FileReader(this.dataFile);){
            JSONObject json = new JSONObject(new JSONTokener(reader));
            for (String playerId : json.keySet()) {
                this.balances.put(playerId, json.getLong(playerId));
            }
        }
        catch (IOException e) {
            this.plugin.getLogger().severe("\uc794\uc561\uc744 \ubd88\ub7ec\uc624\ub294 \uc911 \uc624\ub958 \ubc1c\uc0dd: " + e.getMessage());
        }
    }
}

