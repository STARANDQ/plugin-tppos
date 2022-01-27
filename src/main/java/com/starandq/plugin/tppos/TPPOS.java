package com.starandq.plugin.tppos;

import com.starandq.plugin.tppos.command.TPPOSCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class TPPOS extends JavaPlugin {
    private static TPPOS instance;

    public static TPPOS getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
       instance = this;
        new TPPOSCommand();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
