/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.velocitypowered.api.proxy.Player
 *  com.velocitypowered.api.proxy.ProxyServer
 *  com.velocitypowered.api.proxy.server.RegisteredServer
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

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import java.util.HashMap;
import java.util.Map;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;

public class ServerSwitcherGUI {
    private final ProxyServer server;
    private final Map<Integer, RegisteredServer> serverSlots;

    public ServerSwitcherGUI(ProxyServer server) {
        this.server = server;
        this.serverSlots = new HashMap<Integer, RegisteredServer>();
        this.initializeGUI();
    }

    private void initializeGUI() {
        int slot = 0;
        for (RegisteredServer registeredServer : this.server.getAllServers()) {
            this.serverSlots.put(slot, registeredServer);
            if (++slot < 9) continue;
            break;
        }
    }

    public void openGUI(Player player) {
        Component message = ((TextComponent)((TextComponent)Component.text((String)"=== \uc11c\ubc84 \ubaa9\ub85d ===").color((TextColor)NamedTextColor.GOLD)).decoration(TextDecoration.BOLD, true)).append((Component)Component.newline());
        for (Map.Entry<Integer, RegisteredServer> entry : this.serverSlots.entrySet()) {
            String serverName = entry.getValue().getServerInfo().getName();
            message = message.append(Component.text((String)("[" + String.valueOf(entry.getKey()) + "] ")).color((TextColor)NamedTextColor.YELLOW)).append(((TextComponent)((TextComponent)Component.text((String)serverName).color((TextColor)NamedTextColor.GREEN)).clickEvent(ClickEvent.runCommand((String)("/\uc11c\ubc84\uc774\ub3d9 " + String.valueOf(entry.getKey()))))).hoverEvent((HoverEventSource)HoverEvent.showText((Component)Component.text((String)"\ud074\ub9ad\ud558\uc5ec \uc774\ub3d9").color((TextColor)NamedTextColor.AQUA)))).append((Component)Component.newline());
        }
        message = message.append(((TextComponent)Component.text((String)"=============").color((TextColor)NamedTextColor.GOLD)).decoration(TextDecoration.BOLD, true));
        player.sendMessage(message);
    }

    public void handleClick(Player player, int slot) {
        RegisteredServer targetServer = this.serverSlots.get(slot);
        if (targetServer != null) {
            player.createConnectionRequest(targetServer).fireAndForget();
            Component message = ((TextComponent)Component.text((String)(targetServer.getServerInfo().getName() + " \uc11c\ubc84\ub85c \uc774\ub3d9\ud569\ub2c8\ub2e4. ( \ubcf8 \uba54\uc138\uc9c0\ub294 \uc0ac\uc6a9\ud55c \uc0ac\ub78c\ub9cc \ubcf4\uc785\ub2c8\ub2e4. ) ")).color((TextColor)NamedTextColor.YELLOW)).decoration(TextDecoration.BOLD, true);
            player.sendActionBar(message);
        }
    }
}

