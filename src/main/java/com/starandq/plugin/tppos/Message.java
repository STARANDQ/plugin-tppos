package com.starandq.plugin.tppos;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Message {
    noPermission,usage,cancel, impossibleY, impossible, successful, noAccess, reloaded, teleportedBack, teleportedOnSurface;

    private List<String> msg;

    @SuppressWarnings("unchecked")
    public static void load(FileConfiguration c){
        for(Message message:Message.values()){
            Object obj = c.get("messages." + message.name().replace("_","."));
            if(obj instanceof List){
                message.msg = (List<String>) obj;
            }
            else{
                message.msg = Lists.newArrayList((String) obj);
            }
        }
    }

    public Sender replace(String from, String to){
        Sender sender = new Sender();
        return sender.replace(from,to);
    }
    public void send(CommandSender player){
        new Sender().send(player);
    }

    public class Sender{
        private final Map<String, String> placeholder = new HashMap<>();

        public void send(CommandSender player){
            for(String message: Message.this.msg){
                sendMessage(player, replacePlaceholders(message) );
            }
        }
        public Sender replace(String from, String to){
            placeholder.put(from,to);
            return this;
        }
        private void sendMessage(CommandSender player, String message){
            message = getColor(message);

            if(message.startsWith("json:")) {
                player.sendMessage(new TextComponent(ComponentSerializer.parse(message.substring(5))));
            }
            else player.sendMessage(message);
        }

        private String replacePlaceholders(String message){
            if(!message.contains("{")) return message;
            for(Map.Entry<String, String> entry : placeholder.entrySet()){
                message = message.replace(entry.getKey(), entry.getValue());
            }
            return message;
        }

        private String getColor(String text){
            String result = "";

            for (int i = 0; i < text.length(); i++) {
                if(text.charAt(i) == '&'){
                    i++;
                    if(text.charAt(i) == '1') result += ChatColor.DARK_BLUE;
                    else if(text.charAt(i) == '2') result += ChatColor.DARK_GREEN;
                    else if(text.charAt(i) == '3') result += ChatColor.DARK_AQUA;
                    else if(text.charAt(i) == '4') result += ChatColor.DARK_RED;
                    else if(text.charAt(i) == '5') result += ChatColor.DARK_PURPLE;
                    else if(text.charAt(i) == '6') result += ChatColor.GOLD;
                    else if(text.charAt(i) == '7') result += ChatColor.GRAY;
                    else if(text.charAt(i) == '8') result += ChatColor.DARK_GRAY;
                    else if(text.charAt(i) == '9') result += ChatColor.BLUE;
                    else if(text.charAt(i) == 'a') result += ChatColor.GREEN;
                    else if(text.charAt(i) == 'b') result += ChatColor.AQUA;
                    else if(text.charAt(i) == 'c') result += ChatColor.RED;
                    else if(text.charAt(i) == 'd') result += ChatColor.LIGHT_PURPLE;
                    else if(text.charAt(i) == 'e') result += ChatColor.YELLOW;
                    else if(text.charAt(i) == 'f') result += ChatColor.WHITE;
                    else {
                        i--;
                        result+="&";
                    }
                }else {
                    result += text.charAt(i);
                }
            }

            return result;
        }
    }


}