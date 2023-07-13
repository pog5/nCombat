package me.pog5.ncombat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class NCommand implements CommandExecutor {
    public Map<Player, Boolean> bypassers;
    public NCombat nCombat;
    public boolean state = true;
    public NCommand() {}

    public boolean getState() {
        return state;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ncombat")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
                state = !state;
                sender.sendMessage(ChatColor.AQUA + "NCombat is now " + (state ? ChatColor.GREEN + "enabled" : ChatColor.RED + "disabled"));
                return true;
            } else if (args.length >= 1 && args[0].equalsIgnoreCase("bypass")) {
                if (args.length == 1) {
                    if (bypassers.get(sender) == null) {
                        bypassers.put((Player) sender, true);
                    } else {
                        bypassers.put((Player) sender, !bypassers.get(sender));
                    }
                    sender.sendMessage(ChatColor.AQUA + "You are " + (bypassers.get(sender) ? ChatColor.GREEN : ChatColor.RED + "no longer ") + ChatColor.AQUA + "bypassing nCombat");
                    return true;
                } else if (args[1].contains(Bukkit.getOnlinePlayers().toString())) {
                    sender.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + Bukkit.getPlayer(args[1]) + ChatColor.AQUA + "is " + (state ? ChatColor.GREEN + "bypassing nCombat" : ChatColor.RED + "no longer bypassing nCombat"));
                }
            }
        }
        return false;
    }
}
