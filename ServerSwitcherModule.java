/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.velocitypowered.api.command.Command
 */
package org.minecraft.plugin.reading;

import com.velocitypowered.api.command.Command;
import org.minecraft.plugin.reading.MultiFunctionPlugin;
import org.minecraft.plugin.reading.ServerSwitchCommand;
import org.minecraft.plugin.reading.ServerSwitcherGUI;

public class ServerSwitcherModule {
    private final MultiFunctionPlugin plugin;
    private final ServerSwitcherGUI gui;

    public ServerSwitcherModule(MultiFunctionPlugin plugin) {
        this.plugin = plugin;
        this.gui = new ServerSwitcherGUI(plugin.getServer());
    }

    public void initialize() {
        this.plugin.getServer().getCommandManager().register("\uc11c\ubc84\uc774\ub3d9", (Command)new ServerSwitchCommand(this.gui), new String[0]);
    }
}

