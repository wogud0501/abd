/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.velocitypowered.api.command.Command
 */
package org.minecraft.plugin.reading;

import com.velocitypowered.api.command.Command;
import org.minecraft.plugin.reading.ChatListener;
import org.minecraft.plugin.reading.ChatToggleCommand;
import org.minecraft.plugin.reading.MultiFunctionPlugin;

public class ChatDisablerModule {
    private final MultiFunctionPlugin plugin;
    private boolean chatDisabled = false;

    public ChatDisablerModule(MultiFunctionPlugin plugin) {
        this.plugin = plugin;
    }

    public void initialize() {
        this.plugin.getServer().getCommandManager().register("\ucc44\ud305\uc81c\uac70", (Command)new ChatToggleCommand(this), new String[0]);
        this.plugin.getServer().getEventManager().register((Object)this.plugin, (Object)new ChatListener(this));
    }

    public boolean isChatDisabled() {
        return this.chatDisabled;
    }

    public void setChatDisabled(boolean chatDisabled) {
        this.chatDisabled = chatDisabled;
    }

    public MultiFunctionPlugin getPlugin() {
        return this.plugin;
    }
}

