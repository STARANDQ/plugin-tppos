package com.starandq.plugin.tppos.command;

import com.google.common.collect.Lists;
import com.starandq.plugin.tppos.Message;
import com.starandq.plugin.tppos.TPPOS;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class TPPOSCommand extends AbstractCommand{

    public TPPOSCommand() {
        super("tppos");
    }
    private Double primaryX;
    private Double primaryY;
    private Double primaryZ;

    public Double getPrimaryX() {
        return primaryX;
    }

    public void setPrimaryX(Double primaryX) {
        this.primaryX = primaryX;
    }

    public Double getPrimaryY() {
        return primaryY;
    }

    public void setPrimaryY(Double primaryY) {
        this.primaryY = primaryY;
    }

    public Double getPrimaryZ() {
        return primaryZ;
    }

    public void setPrimaryZ(Double primaryZ) {
        this.primaryZ = primaryZ;
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0){
            Message.usage.send(sender);
            return;
        }

        if(args.length==3){
            setPrimaryX(player.getLocation().getX());
            setPrimaryY(player.getLocation().getY());
            setPrimaryZ(player.getLocation().getZ());
                try {
                    Double.parseDouble(args[0]);
                    Double.parseDouble(args[1]);
                    Double.parseDouble(args[2]);
                }
                catch (Exception e){
                    sender.sendMessage(e.getMessage());
                    return;
                };
                if(!(sender instanceof Player)){
                    Message.noAccess.send(sender);
                    return;
                }

                double desirableX = Double.parseDouble(args[0]);
                double desirableY =Double.parseDouble(args[1]);
                double desirableZ = Double.parseDouble(args[2]);

                if(desirableY >=-64 && desirableY<=256){

                    while(true){

                        Location loc = new Location(player.getWorld(), desirableX, desirableY, desirableZ);
                        Material check = loc.getBlock().getType();

                        if(check == Material.AIR){
                            Location loc2 = new Location(player.getWorld(), desirableX, desirableY++, desirableZ);
                            Material check2 = loc2.getBlock().getType();

                            if(check2==Material.AIR){
                                player.teleport(loc);
                                Message.cancel.send(sender);
                                Message.successful.replace("{x}", String.valueOf(desirableX)).replace("{y}", String.valueOf(desirableY)).
                                        replace("{z}",String.valueOf(desirableZ)).send(sender);
                                break;
                            }
                        }
                        else desirableY++;



                        }
                    }


                //sender.sendMessage(desirableX + "   " + desirableY + "   "+ desirableZ);

        }
        if(args[0].equalsIgnoreCase("cancel")){
            Location primaryLocation = new Location(player.getWorld(), getPrimaryX(), getPrimaryY(), getPrimaryZ());
            player.teleport(primaryLocation);
            Message.cancel.send(sender);
            Message.teleportedBack.replace("{x}", String.valueOf(getPrimaryX())).replace("{y}", String.valueOf(getPrimaryY())).
                    replace("{z}",String.valueOf(getPrimaryZ())).send(sender);
            return;
        }
        sender.sendMessage(ChatColor.RED + "Unknown command" + args[0]);

        if(args[0].equalsIgnoreCase("reload")){
           if (!sender.hasPermission("tppos.reload")) {
                sender.sendMessage(ChatColor.RED + "You don't have permission.");
                return;
            }
            TPPOS.getInstance().reloadConfig();

            sender.sendMessage(ChatColor.RED + "Config reloaded.");
            return;
        }
        sender.sendMessage(ChatColor.RED + "Unknown command " + args[0]);
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if(args.length == 1) return Lists.newArrayList("reload");
        return Lists.newArrayList();
    }

}
