/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.velocitypowered.api.command.SimpleCommand
 *  com.velocitypowered.api.command.SimpleCommand$Invocation
 *  com.velocitypowered.api.proxy.Player
 *  net.kyori.adventure.text.Component
 *  net.kyori.adventure.text.TextComponent
 *  net.kyori.adventure.text.event.ClickEvent
 *  net.kyori.adventure.text.event.HoverEvent
 *  net.kyori.adventure.text.event.HoverEventSource
 *  net.kyori.adventure.text.format.NamedTextColor
 *  net.kyori.adventure.text.format.TextColor
 *  net.kyori.adventure.text.format.TextDecoration
 */
package org.minecraft.plugin.reading;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.minecraft.plugin.reading.ChatDisablerModule;

public class ChatToggleCommand
implements SimpleCommand {
    private final ChatDisablerModule module;

    public ChatToggleCommand(ChatDisablerModule module) {
        this.module = module;
    }

    public void execute(SimpleCommand.Invocation invocation) {
        if (!(invocation.source() instanceof Player)) {
            invocation.source().sendMessage(Component.text((String)"\uc774 \uba85\ub839\uc5b4\ub294 \ud50c\ub808\uc774\uc5b4\ub9cc \uc0ac\uc6a9\ud560 \uc218 \uc788\uc2b5\ub2c8\ub2e4.").color((TextColor)NamedTextColor.RED));
            return;
        }
        Player player = (Player)invocation.source();
        if (player.hasPermission("chatdisabler.toggle")) {
            this.module.setChatDisabled(!this.module.isChatDisabled());
            this.broadcastChatStatus(player);
        } else {
            player.sendMessage(Component.text((String)"\uc774 \uba85\ub839\uc5b4\ub97c \uc0ac\uc6a9\ud560 \uad8c\ud55c\uc774 \uc5c6\uc2b5\ub2c8\ub2e4.").color((TextColor)NamedTextColor.RED));
        }
    }

    private void broadcastChatStatus(Player player) {
        String status = this.module.isChatDisabled() ? "\ube44\ud65c\uc131\ud654" : "\ud65c\uc131\ud654";
        NamedTextColor statusColor = this.module.isChatDisabled() ? NamedTextColor.RED : NamedTextColor.GREEN;
        String oppositeStatus = this.module.isChatDisabled() ? "\ud65c\uc131\ud654" : "\ube44\ud65c\uc131\ud654";
        NamedTextColor oppositeColor = this.module.isChatDisabled() ? NamedTextColor.GREEN : NamedTextColor.RED;
        Component message = ((TextComponent)((TextComponent)Component.text((String)"\ucc44\ud305\uc774 \ud604\uc7ac ").append(((TextComponent)Component.text((String)status).color((TextColor)statusColor)).decorate(TextDecoration.BOLD))).append((Component)Component.text((String)" \ub418\uc5c8\uc2b5\ub2c8\ub2e4. "))).append(((TextComponent)((TextComponent)((TextComponent)Component.text((String)("[" + oppositeStatus + "]")).color((TextColor)oppositeColor)).decorate(TextDecoration.BOLD)).clickEvent(ClickEvent.runCommand((String)"/\ucc44\ud305\uc81c\uac70"))).hoverEvent((HoverEventSource)HoverEvent.showText((Component)Component.text((String)("\ud074\ub9ad\ud558\uc5ec \ucc44\ud305 " + oppositeStatus)))));
        this.module.getPlugin().getServer().getAllPlayers().forEach(p -> p.sendMessage(message));
    }
}

