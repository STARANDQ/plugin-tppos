package com.starandq.plugin.tppos;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class TPPOS extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("market").setExecutor(new CommandExecutor() {
            @Override
            public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
                sender.sendMessage("hello");
                return true;
            }
        });

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
