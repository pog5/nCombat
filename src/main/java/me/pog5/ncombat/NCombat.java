package me.pog5.ncombat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class NCombat extends JavaPlugin implements Listener {
    public NCommand nCommand;

    private Map<Entity, Integer> timers;

    public boolean incombat = false;

    private static NCombat plugin;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        plugin = this;
        timers = new HashMap<>();
        getCommand("ncombat").setExecutor(new NCommand());
    }

    @Override
    public void onDisable() {
        plugin = null;
        timers.clear();

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // change zombie to player after testing
        if (!incombat && !event.isCancelled() && event.getDamager() instanceof Zombie && event.getEntity() instanceof Player) {
            Entity attacker = (Entity) event.getDamager();
            Player victim = (Player) event.getEntity();
            //if (!timers.containsKey(attacker)) {
            if (true) {
                timers.put(attacker, 6);
                victim.sendActionBar(ChatColor.RED + "In combat with " + ChatColor.BOLD + attacker.getName());
                //attacker.sendActionBar(ChatColor.RED + "In combat with " + ChatColor.BOLD + victim.getName());
                BukkitRunnable timer = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (timers.containsKey(attacker)) {
                            int secondspassed = timers.get(attacker) - 1;
                            if (secondspassed >= 1) {
                                timers.put(attacker, secondspassed);
                                victim.sendActionBar(ChatColor.RED + "In combat with " + ChatColor.BOLD + attacker.getName() + ChatColor.LIGHT_PURPLE + " | " + secondspassed + "s");
                                //attacker.sendActionBar(ChatColor.RED + "In combat with " + ChatColor.BOLD + victim.getName() + ChatColor.LIGHT_PURPLE + " | " + secondspassed + "s");
                                incombat = true;
                            } else if (secondspassed == 0) {
                                victim.sendActionBar(ChatColor.GREEN + "Out of combat!");
                                //attacker.sendActionBar(ChatColor.GREEN + "Out of combat!");
                                incombat = false;
                                timers.remove(attacker);
                            }
                        }
                    }
                };
                timer.runTaskTimerAsynchronously(this, 20, 20);
                plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, timer::cancel, 120);
            }
        }
    }

}
