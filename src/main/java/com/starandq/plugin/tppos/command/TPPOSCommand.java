package com.starandq.plugin.tppos.command;

import com.google.common.collect.Lists;
import com.starandq.plugin.tppos.Message;
import com.starandq.plugin.tppos.TPPOS;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class TPPOSCommand extends AbstractCommand{

    LuckPerms api = LuckPermsProvider.get();
    private Group group;
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
        if (getPermissions(player) || player.isOp()) {
            if (args.length == 0) {
                if (!(sender instanceof Player)) {
                    Message.noAccess.send(sender);
                    return;
                }
                Message.usage.send(sender);
                return;
            }

            if (args.length == 3) {
                setPrimaryX(player.getLocation().getX());
                setPrimaryY(player.getLocation().getY());
                setPrimaryZ(player.getLocation().getZ());
                try {
                    Double.parseDouble(args[0]);
                    Double.parseDouble(args[1]);
                    Double.parseDouble(args[2]);
                } catch (Exception e) {
                    Message.usage.send(sender);
                    return;
                }

                if (!(sender instanceof Player)) {
                    Message.noAccess.send(sender);
                    return;
                }

                double desirableX = Double.parseDouble(args[0]);
                double desirableY = Double.parseDouble(args[1]);
                double desirableZ = Double.parseDouble(args[2]);

                if (desirableY >= -64 && desirableY <= 256) {
                    double coordY = desirableY;
                    while (true) {

                        Location loc = new Location(player.getWorld(), desirableX, desirableY, desirableZ);
                        Material check = loc.getBlock().getType();

                        if (check == Material.AIR) {
                            Location loc2 = new Location(player.getWorld(), desirableX, desirableY++, desirableZ);
                            Material check2 = loc2.getBlock().getType();

                            if (check2 == Material.AIR) {
                                player.teleport(loc);

                                Message.cancel.send(sender);
                                Message.successful.replace("{x}", String.valueOf(desirableX)).replace("{y}",
                                        String.valueOf(desirableY--)).
                                        replace("{z}", String.valueOf(desirableZ)).send(sender);

                                Message.teleportedOnSurface.replace("{y}",String.valueOf(desirableY-coordY)).send(sender);
                                break;

                            }
                        } else desirableY++;
                    }
                    return;
                } else {
                    Message.impossibleY.send(sender);
                    return;
                }


                //sender.sendMessage(desirableX + "   " + desirableY + "   "+ desirableZ);

            }
            if (args[0].equalsIgnoreCase("cancel")) {
                Location primaryLocation = new Location(player.getWorld(), getPrimaryX(), getPrimaryY(), getPrimaryZ());
                player.teleport(primaryLocation);
                Message.cancel.send(sender);
                Message.teleportedBack.replace("{x}", String.valueOf(Math.round(getPrimaryX()))).replace("{y}",
                        String.valueOf(Math.round(getPrimaryY()))).
                        replace("{z}", String.valueOf(Math.round(getPrimaryZ()))).send(sender);
                return;
            }


            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("tppos.reload")) {
                    sender.sendMessage(ChatColor.RED + "You don't have permission.");
                    return;
                }
                Bukkit.getPluginManager().disablePlugin(TPPOS.getInstance());
                Bukkit.getPluginManager().getPlugin("TPPOS").reloadConfig();
                Bukkit.getPluginManager().enablePlugin(TPPOS.getInstance());
                // TPPOS.getInstance().reloadConfig();
                sender.sendMessage(ChatColor.RED + "Config reloaded.");
                return;
            }
            sender.sendMessage(ChatColor.RED + "Unknown command " + args[0]);
        } else Message.noPermission.send(sender);
    }
    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if(args.length == 1) return Lists.newArrayList("reload", "cancel");
        return Lists.newArrayList();
    }
    private boolean getPermissions(Player player){
        User user = api.getUserManager().getUser(player.getUniqueId());
        PermissionNode node = PermissionNode.builder("tppos.permission").build();
        //user.getPrimaryGroup();
        Collection<Node> nodes =  user.getNodes();
        ArrayList<String> perms= new ArrayList<>();
        ArrayList<String> matches = new ArrayList<>();
        String regex = "group.[a-z].tppos.permission";
        Pattern p = Pattern.compile(regex);
        for (Node n:nodes
        ) {
            perms.add(n.toString());
        }
        for (String s:perms) {
            if (p.matcher(s).matches()) {
                matches.add(s);
            }
        }
        if(matches!=null)
            return true;
        return false;
    }

}
