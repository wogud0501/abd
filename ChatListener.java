/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.velocitypowered.api.event.Subscribe
 *  com.velocitypowered.api.event.player.PlayerChatEvent
 *  com.velocitypowered.api.event.player.PlayerChatEvent$ChatResult
 *  com.velocitypowered.api.proxy.Player
 *  net.kyori.adventure.text.Component
 */
package org.minecraft.plugin.reading;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import org.minecraft.plugin.reading.ChatDisablerModule;

public class ChatListener {
    private final ChatDisablerModule plugin;

    public ChatListener(ChatDisablerModule plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        Player player;
        if (this.plugin.isChatDisabled() && !(player = event.getPlayer()).hasPermission("chatdisabler.bypass")) {
            event.setResult(PlayerChatEvent.ChatResult.denied());
            player.sendMessage((Component)Component.text((String)"\ucc44\ud305\uc774 \ud604\uc7ac \ube44\ud65c\uc131\ud654 \ub418\uc5b4 \uc788\uc2b5\ub2c8\ub2e4."));
        }
    }
}

