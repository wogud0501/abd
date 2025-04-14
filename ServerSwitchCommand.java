/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.velocitypowered.api.command.SimpleCommand
 *  com.velocitypowered.api.command.SimpleCommand$Invocation
 *  com.velocitypowered.api.proxy.Player
 *  net.kyori.adventure.text.Component
 *  net.kyori.adventure.text.format.NamedTextColor
 *  net.kyori.adventure.text.format.TextColor
 */
package org.minecraft.plugin.reading;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.minecraft.plugin.reading.ServerSwitcherGUI;

public class ServerSwitchCommand
implements SimpleCommand {
    private final ServerSwitcherGUI gui;

    public ServerSwitchCommand(ServerSwitcherGUI gui) {
        this.gui = gui;
    }

    public void execute(SimpleCommand.Invocation invocation) {
        if (!(invocation.source() instanceof Player)) {
            invocation.source().sendMessage((Component)Component.text((String)"\uc774 \uba85\ub839\uc5b4\ub294 \ud50c\ub808\uc774\uc5b4\ub9cc \uc0ac\uc6a9\ud560 \uc218 \uc788\uc2b5\ub2c8\ub2e4."));
        } else {
            Player player = (Player)invocation.source();
            String[] args = (String[])invocation.arguments();
            if (args.length == 0) {
                this.gui.openGUI(player);
            } else if (args.length == 1) {
                try {
                    int slot = Integer.parseInt(args[0]);
                    this.gui.handleClick(player, slot);
                }
                catch (NumberFormatException var5) {
                    player.sendMessage(Component.text((String)"\uc62c\ubc14\ub978 \uc11c\ubc84 \ubc88\ud638\ub97c \uc785\ub825\ud574\uc8fc\uc138\uc694.").color((TextColor)NamedTextColor.RED));
                }
            } else {
                player.sendMessage(Component.text((String)"\uc0ac\uc6a9\ubc95: /\uc11c\ubc84\uc774\ub3d9 [\uc11c\ubc84\ubc88\ud638]").color((TextColor)NamedTextColor.RED));
            }
        }
    }
}

