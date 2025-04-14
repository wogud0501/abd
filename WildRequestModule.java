/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.velocitypowered.api.command.Command
 *  com.velocitypowered.api.command.CommandSource
 *  com.velocitypowered.api.command.SimpleCommand
 *  com.velocitypowered.api.command.SimpleCommand$Invocation
 *  com.velocitypowered.api.proxy.Player
 *  net.kyori.adventure.text.Component
 *  net.kyori.adventure.text.TextComponent
 *  net.kyori.adventure.text.format.NamedTextColor
 *  net.kyori.adventure.text.format.TextColor
 *  net.kyori.adventure.text.format.TextDecoration
 */
package org.minecraft.plugin.reading;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.minecraft.plugin.reading.MultiFunctionPlugin;

public class WildRequestModule {
    private final MultiFunctionPlugin plugin;

    public WildRequestModule(MultiFunctionPlugin plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        this.plugin.getServer().getCommandManager().register("\uc57c\uc0dd\uc791\uc5c5\uc694\uccad", (Command)new WildRequestCommand(), new String[0]);
        this.plugin.getLogger().info("\uc57c\uc0c1 \uc791\uc5c5 \uc694\uccad \ubaa8\ub4c8\uc774 \ucd08\uae30\ud654 \ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
    }

    private class WildRequestCommand
    implements SimpleCommand {
        private WildRequestCommand() {
        }

        public void execute(SimpleCommand.Invocation invocation) {
            CommandSource source = invocation.source();
            CharSequence[] args = (String[])invocation.arguments();
            if (!(source instanceof Player)) {
                source.sendMessage(Component.text((String)"\uc774 \uba85\ub839\uc5b4\ub294 \ud50c\ub808\uc774\uc5b4\ub9cc \uc0ac\uc6a9\ud560 \uc218 \uc788\uc2b5\ub2c8\ub2e4.").color((TextColor)NamedTextColor.RED));
                return;
            }
            Player player = (Player)source;
            if (!player.hasPermission("user.request.wild")) {
                player.sendMessage(Component.text((String)"\uc774 \uba85\ub839\uc5b4\ub97c \uc0ac\uc6a9\ud560 \uad8c\ud55c\uc774 \uc5c6\uc2b5\ub2c8\ub2e4.").color((TextColor)NamedTextColor.RED));
                return;
            }
            String message = args.length > 0 ? String.join((CharSequence)" ", args) : "\uba54\uc2dc\uc9c0 \uc5c6\uc74c";
            Component requestMessage = ((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)Component.text((String)"\u25b6 \uc57c\uc0dd \uc791\uc5c5 \uc694\uccad \u25c0\n").color((TextColor)NamedTextColor.GREEN)).decorate(TextDecoration.BOLD)).append(((TextComponent)Component.text((String)"\uc694\uccad\uc790: ").color((TextColor)NamedTextColor.WHITE)).append(Component.text((String)player.getUsername()).color((TextColor)NamedTextColor.YELLOW)))).append(((TextComponent)Component.text((String)"\n\ud604\uc7ac \uc11c\ubc84: ").color((TextColor)NamedTextColor.WHITE)).append(Component.text((String)player.getCurrentServer().map(server -> server.getServerInfo().getName()).orElse("\uc54c \uc218 \uc5c6\uc74c")).color((TextColor)NamedTextColor.YELLOW)))).append(((TextComponent)Component.text((String)"\n\uc694\uccad \uc2dc\uac04: ").color((TextColor)NamedTextColor.WHITE)).append(Component.text((String)LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).color((TextColor)NamedTextColor.YELLOW)))).append(((TextComponent)Component.text((String)"\n\uba54\uc2dc\uc9c0: ").color((TextColor)NamedTextColor.WHITE)).append(Component.text((String)message).color((TextColor)NamedTextColor.YELLOW)));
            boolean adminFound = false;
            for (Player admin : WildRequestModule.this.plugin.getServer().getAllPlayers()) {
                if (!admin.hasPermission("admin.receive.wild.request")) continue;
                admin.sendMessage(requestMessage);
                adminFound = true;
            }
            if (adminFound) {
                player.sendMessage(Component.text((String)"\uc57c\uc0dd \uc791\uc5c5 \uc694\uccad\uc774 \uad00\ub9ac\uc790\uc5d0\uac8c \uc804\uc1a1\ub418\uc5c8\uc2b5\ub2c8\ub2e4.").color((TextColor)NamedTextColor.GREEN));
            } else {
                player.sendMessage(Component.text((String)"\ud604\uc7ac \uc628\ub77c\uc778 \uc0c1\ud0dc\uc778 \uad00\ub9ac\uc790\uac00 \uc5c6\uc2b5\ub2c8\ub2e4. \ub098\uc911\uc5d0 \ub2e4\uc2dc \uc2dc\ub3c4\ud574\uc8fc\uc138\uc694.").color((TextColor)NamedTextColor.RED));
            }
        }
    }
}

