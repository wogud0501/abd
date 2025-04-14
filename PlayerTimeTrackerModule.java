/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.velocitypowered.api.event.Subscribe
 *  com.velocitypowered.api.event.connection.DisconnectEvent
 *  com.velocitypowered.api.event.connection.PostLoginEvent
 *  com.velocitypowered.api.scheduler.ScheduledTask
 */
package org.minecraft.plugin.reading;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.scheduler.ScheduledTask;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.minecraft.plugin.reading.MultiFunctionPlugin;

public class PlayerTimeTrackerModule {
    private final MultiFunctionPlugin plugin;
    private final Map<UUID, Integer> playerTimes = new HashMap<UUID, Integer>();
    private ScheduledTask timeUpdateTask;

    public PlayerTimeTrackerModule(MultiFunctionPlugin plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        this.plugin.getServer().getEventManager().register((Object)this.plugin, (Object)this);
        this.startTimeUpdateTask();
        this.plugin.getLogger().info("\ud50c\ub808\uc774\uc5b4 \uc2dc\uac04 \ucd94\uc801 \ubaa8\ub4c8\uc774 \ucd08\uae30\ud654\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
    }

    private void startTimeUpdateTask() {
        this.timeUpdateTask = this.plugin.getServer().getScheduler().buildTask((Object)this.plugin, () -> this.plugin.getServer().getAllPlayers().forEach(player -> {
            UUID playerId = player.getUniqueId();
            int newTime = this.playerTimes.getOrDefault(playerId, 0) + 1;
            if (newTime >= 60) {
                this.playerTimes.put(playerId, 0);
            } else {
                this.playerTimes.put(playerId, newTime);
            }
        })).repeat(1L, TimeUnit.MINUTES).schedule();
    }

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        if (!this.playerTimes.containsKey(playerId)) {
            this.playerTimes.put(playerId, 0);
        }
        this.plugin.getLogger().info(String.format("\ud50c\ub808\uc774\uc5b4 %s\uac00 \uc811\uc18d\ud588\uc2b5\ub2c8\ub2e4. \ub204\uc801 \uc2dc\uac04: %d\ubd84", event.getPlayer().getUsername(), this.playerTimes.get(playerId)));
    }

    @Subscribe
    public void onPlayerQuit(DisconnectEvent event) {
        UUID playerId = event.getPlayer().getUniqueId();
        this.plugin.getLogger().info(String.format("\ud50c\ub808\uc774\uc5b4 %s\uac00 \ud1f4\uc7a5\ud588\uc2b5\ub2c8\ub2e4. \ub204\uc801 \uc2dc\uac04: %d\ubd84", event.getPlayer().getUsername(), this.playerTimes.getOrDefault(playerId, 0)));
    }

    public int getPlayerTime(UUID playerId) {
        return this.playerTimes.getOrDefault(playerId, 0);
    }

    public void shutdown() {
        if (this.timeUpdateTask != null) {
            this.timeUpdateTask.cancel();
        }
    }
}

