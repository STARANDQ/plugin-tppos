package com.starandq.plugin.tppos.command;

import com.starandq.plugin.tppos.TPPOS;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class TPPOSCommand extends AbstractCommand{

    public TPPOSCommand() {
        super("market");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if(args.length == 0){
            sender.sendMessage("Reload TPPOS: /"+ label + " reload");
            return;
        }
        if(args[0].equalsIgnoreCase("reload")){
            if (!sender.hasPermission("market.reload")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return;
            }
            TPPOS.getInstance().reloadConfig();
            sender.sendMessage(ChatColor.RED + "Config reloaded.");
            return;
        }
        sender.sendMessage(ChatColor.RED + "Unknown command" + args[0]);
    }
}
