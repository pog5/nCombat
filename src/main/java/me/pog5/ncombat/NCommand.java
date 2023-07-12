package me.pog5.ncombat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class NCommand implements CommandExecutor {
    public NCombat nCombat;
    public NCommand() {}

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ncombat")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
                Bukkit.broadcastMessage("command: " + command + Arrays.toString(args) + "state: " + nCombat.enabled);
                nCombat.enabled = !nCombat.enabled;
                sender.sendMessage("NCombat is now " + (nCombat.isEnabled() ? "enabled" : "disabled"));
                return true;
            }
        }
        return false;
    }
}
