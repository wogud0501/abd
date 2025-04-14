/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.velocitypowered.api.proxy.Player
 *  com.velocitypowered.api.proxy.messages.ChannelIdentifier
 *  com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier
 *  com.velocitypowered.api.scheduler.ScheduledTask
 */
package org.minecraft.plugin.reading;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.scheduler.ScheduledTask;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;
import org.minecraft.plugin.reading.MultiFunctionPlugin;
import org.minecraft.plugin.reading.PlayerTimeTrackerModule;

public class MeatRotModule {
    private final MultiFunctionPlugin plugin;
    private final PlayerTimeTrackerModule timeTracker;
    private ScheduledTask task;
    public static final MinecraftChannelIdentifier MEAT_CHANNEL = MinecraftChannelIdentifier.from((String)"meatrot:update");

    public MeatRotModule(MultiFunctionPlugin plugin, PlayerTimeTrackerModule timeTracker) {
        this.plugin = plugin;
        this.timeTracker = timeTracker;
    }

    public void initialize() {
        this.task = this.plugin.getServer().getScheduler().buildTask((Object)this.plugin, this::checkAndRotMeat).delay(1L, TimeUnit.MINUTES).repeat(1L, TimeUnit.MINUTES).schedule();
        this.plugin.getServer().getChannelRegistrar().register(new ChannelIdentifier[]{MEAT_CHANNEL});
        this.plugin.getLogger().info("\uace0\uae30 \ubd80\ud328 \ubaa8\ub4c8\uc774 \ucd08\uae30\ud654\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
    }

    private void checkAndRotMeat() {
        for (Player player : this.plugin.getServer().getAllPlayers()) {
            int playerMinutes = this.timeTracker.getPlayerTime(player.getUniqueId());
            if (playerMinutes <= 0 || playerMinutes % 60 != 0) continue;
            player.getCurrentServer().ifPresent(server -> {
                String message = "rot_meat:" + player.getUsername();
                server.sendPluginMessage((ChannelIdentifier)MEAT_CHANNEL, message.getBytes(StandardCharsets.UTF_8));
            });
        }
    }

    public void shutdown() {
        if (this.task != null) {
            this.task.cancel();
        }
    }
}

