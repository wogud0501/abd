/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.inject.Inject
 *  com.velocitypowered.api.event.Subscribe
 *  com.velocitypowered.api.event.connection.PluginMessageEvent
 *  com.velocitypowered.api.event.proxy.ProxyInitializeEvent
 *  com.velocitypowered.api.plugin.Plugin
 *  com.velocitypowered.api.proxy.ProxyServer
 *  com.velocitypowered.api.proxy.messages.ChannelIdentifier
 *  com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier
 *  org.slf4j.Logger
 */
package org.minecraft.plugin.reading;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.ChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import java.nio.charset.StandardCharsets;
import org.minecraft.plugin.reading.AnnouncementModule;
import org.minecraft.plugin.reading.ChatDisablerModule;
import org.minecraft.plugin.reading.MeatRotModule;
import org.minecraft.plugin.reading.PlayerTimeTrackerModule;
import org.minecraft.plugin.reading.ServerSwitcherModule;
import org.minecraft.plugin.reading.WildRequestModule;
import org.slf4j.Logger;

@Plugin(id="multifunctionplugin", name="Multi-Function Plugin", version="0.1.0", authors={"Hyunwol"})
public class MultiFunctionPlugin {
    private final ProxyServer server;
    private final Logger logger;
    private final ChatDisablerModule chatDisablerModule;
    private final ServerSwitcherModule serverSwitcherModule;
    private final AnnouncementModule announcementModule;
    private final WildRequestModule wildRequestModule;
    private final PlayerTimeTrackerModule playerTimeTrackerModule;
    private final MeatRotModule meatRotModule;
    public static final MinecraftChannelIdentifier CHANNEL = MinecraftChannelIdentifier.from((String)"multifunctionplugin:chat");

    @Inject
    public MultiFunctionPlugin(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
        this.chatDisablerModule = new ChatDisablerModule(this);
        this.serverSwitcherModule = new ServerSwitcherModule(this);
        this.announcementModule = new AnnouncementModule(this);
        this.wildRequestModule = new WildRequestModule(this);
        this.playerTimeTrackerModule = new PlayerTimeTrackerModule(this);
        this.meatRotModule = new MeatRotModule(this, this.playerTimeTrackerModule);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.playerTimeTrackerModule.initialize();
        this.chatDisablerModule.initialize();
        this.serverSwitcherModule.initialize();
        this.announcementModule.initialize();
        this.wildRequestModule.initialize();
        this.meatRotModule.initialize();
        this.server.getChannelRegistrar().register(new ChannelIdentifier[]{CHANNEL});
        this.logger.info("\ub2e4\uc911 \uae30\ub2a5 \ud50c\ub7ec\uadf8\uc778\uc774 \ud65c\uc131\ud654 \ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (event.getIdentifier().equals((Object)CHANNEL)) {
            new String(event.getData(), StandardCharsets.UTF_8);
        }
    }

    public ProxyServer getServer() {
        return this.server;
    }

    public Logger getLogger() {
        return this.logger;
    }
}

