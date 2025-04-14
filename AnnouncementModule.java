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
 */
package org.minecraft.plugin.reading;

import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.minecraft.plugin.reading.MultiFunctionPlugin;

public class AnnouncementModule {
    private final MultiFunctionPlugin plugin;

    public AnnouncementModule(MultiFunctionPlugin plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        this.plugin.getServer().getCommandManager().register("\uacf5\uc9c0\uae34\uae09", (Command)new AnnouncementCommand("\uae34\uae09", NamedTextColor.RED), new String[0]);
        this.plugin.getServer().getCommandManager().register("\uacf5\uc9c0\uc77c\ubc18", (Command)new AnnouncementCommand("\uc77c\ubc18", NamedTextColor.WHITE), new String[0]);
        this.plugin.getServer().getCommandManager().register("\uacf5\uc9c0\uacbd\uace0", (Command)new AnnouncementCommand("\uacbd\uace0", NamedTextColor.YELLOW), new String[0]);
        this.plugin.getLogger().info("\uacf5\uc9c0 \ubaa8\ub4c8\uc774 \ucd08\uae30\ud654\ub418\uc5c8\uc2b5\ub2c8\ub2e4.");
    }

    private class AnnouncementCommand
    implements SimpleCommand {
        private final String type;
        private final NamedTextColor color;

        public AnnouncementCommand(String type, NamedTextColor color) {
            this.type = type;
            this.color = color;
        }

        public void execute(SimpleCommand.Invocation invocation) {
            CommandSource source = invocation.source();
            CharSequence[] args = (String[])invocation.arguments();
            if (!source.hasPermission("announcement.admin")) {
                source.sendMessage(Component.text((String)"\uc774 \uba85\ub839\uc5b4\ub97c \uc0ac\uc6a9\ud560 \uad8c\ud55c\uc774 \uc5c6\uc2b5\ub2c8\ub2e4.").color((TextColor)NamedTextColor.RED));
                return;
            }
            if (args.length == 0) {
                source.sendMessage(Component.text((String)("\uc0ac\uc6a9\ubc95: /\uacf5\uc9c0" + this.type + " <\uba54\uc2dc\uc9c0>")).color((TextColor)NamedTextColor.RED));
                return;
            }
            String message = String.join((CharSequence)" ", args);
            this.broadcastMessage(message, source);
        }

        private void broadcastMessage(String message, CommandSource source) {
            String authorName = source instanceof Player ? ((Player)source).getUsername() : "Console";
            AnnouncementModule.this.plugin.getServer().getAllPlayers().forEach(arg_0 -> AnnouncementCommand.lambda$broadcastMessage$0(switch (this.type) {
                case "\uae34\uae09" -> ((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)Component.text((String)"\n").append(Component.text((String)"\u26a0 ").color((TextColor)NamedTextColor.GOLD))).append(Component.text((String)"\uae34\uae09 \uacf5\uc9c0\uc0ac\ud56d").color((TextColor)NamedTextColor.RED))).append(Component.text((String)" \u26a0\n").color((TextColor)NamedTextColor.GOLD))).append(Component.text((String)"\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\n").color((TextColor)NamedTextColor.RED))).append(Component.text((String)"\u25b6 ").color((TextColor)NamedTextColor.GOLD))).append(Component.text((String)message).color((TextColor)this.color))).append((Component)Component.text((String)"\n"))).append(Component.text((String)"\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\u2501\n").color((TextColor)NamedTextColor.RED))).append(Component.text((String)"\uacf5\uc9c0 \uc791\uc131\uc790: ").color((TextColor)NamedTextColor.GRAY))).append(Component.text((String)authorName).color((TextColor)NamedTextColor.GOLD))).append((Component)Component.text((String)"\n"));
                case "\uacbd\uace0" -> ((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)Component.text((String)"\n").append(Component.text((String)"\u26a1 ").color((TextColor)NamedTextColor.GOLD))).append(Component.text((String)"\uacbd\uace0 \uacf5\uc9c0\uc0ac\ud56d").color((TextColor)NamedTextColor.YELLOW))).append(Component.text((String)" \u26a1\n").color((TextColor)NamedTextColor.GOLD))).append(Component.text((String)"\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\n").color((TextColor)NamedTextColor.YELLOW))).append(Component.text((String)"\u25b6 ").color((TextColor)NamedTextColor.GOLD))).append(Component.text((String)message).color((TextColor)this.color))).append((Component)Component.text((String)"\n"))).append(Component.text((String)"\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\u2594\n").color((TextColor)NamedTextColor.YELLOW))).append(Component.text((String)"\uacf5\uc9c0 \uc791\uc131\uc790: ").color((TextColor)NamedTextColor.GRAY))).append(Component.text((String)authorName).color((TextColor)NamedTextColor.GOLD))).append((Component)Component.text((String)"\n"));
                default -> ((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)((TextComponent)Component.text((String)"\n").append(Component.text((String)"\u2709 ").color((TextColor)NamedTextColor.AQUA))).append(Component.text((String)"\uc77c\ubc18 \uacf5\uc9c0\uc0ac\ud56d").color((TextColor)NamedTextColor.WHITE))).append(Component.text((String)" \u2709\n").color((TextColor)NamedTextColor.AQUA))).append(Component.text((String)"\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\n").color((TextColor)NamedTextColor.WHITE))).append(Component.text((String)"\u25b6 ").color((TextColor)NamedTextColor.AQUA))).append(Component.text((String)message).color((TextColor)this.color))).append((Component)Component.text((String)"\n"))).append(Component.text((String)"\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\u2504\n").color((TextColor)NamedTextColor.WHITE))).append(Component.text((String)"\uacf5\uc9c0 \uc791\uc131\uc790: ").color((TextColor)NamedTextColor.GRAY))).append(Component.text((String)authorName).color((TextColor)NamedTextColor.AQUA))).append((Component)Component.text((String)"\n"));
            }, arg_0));
        }

        private static /* synthetic */ void lambda$broadcastMessage$0(Component formattedMessage, Player player) {
            player.sendMessage(formattedMessage);
        }
    }
}

