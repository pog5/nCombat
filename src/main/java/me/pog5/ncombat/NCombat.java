package me.pog5.ncombat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Zombie;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.command.CommandExecutor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public final class NCombat extends JavaPlugin implements Listener {

    private Map<Entity, Integer> timers;

    public boolean enabled = true;
    boolean incombat = false;
    boolean isMoving;

    private static NCombat plugin;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        plugin = this;
        timers = new HashMap<>();
        getCommand("ncombat").setExecutor((CommandExecutor) new NCommand());
    }


    @Override
    public void onDisable() {
        plugin = null;
        timers.clear();
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        isMoving = event.getFrom().distanceSquared(event.getTo()) > 0;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        // replace zombie with actual player once testing is finished
        if (enabled && !event.isCancelled() && event.getDamager() instanceof Zombie && event.getEntity() instanceof Player) {
            Entity attacker = (Entity) event.getDamager();
            Player victim = (Player) event.getEntity();
            if (!timers.containsKey(attacker)) {
                timers.put(attacker, 6);
                victim.sendActionBar(ChatColor.RED + "In combat with " + ChatColor.BOLD + attacker.getName());
                BukkitRunnable timer = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (timers.containsKey(attacker)) {
                            int secondspassed = timers.get(attacker) - 1;
                            //if (isMoving) secondspassed = timers.get(attacker);
                            //else secondspassed = timers.get(attacker) - 1;
                            if (secondspassed >= 1) {
                                timers.put(attacker, secondspassed);
                                victim.sendActionBar(ChatColor.RED + "In combat with " + ChatColor.BOLD + attacker.getName() + ChatColor.LIGHT_PURPLE + " | " + secondspassed + "s");
                                incombat = true;
                                Bukkit.broadcastMessage("debug: " + attacker + secondspassed);
                            } else if (secondspassed == 0) {
                                victim.sendActionBar(ChatColor.GREEN + "Out of combat!");
                                incombat = false;
                                timers.remove(attacker);
                            }
                        }
                    }
                };
                timer.runTaskTimerAsynchronously(this, 20, 20);
                plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, timer::cancel, 120);
            }
            Bukkit.broadcastMessage("In combat for 5 seconds: " + attacker.getName() + " hitting " + victim.getName());
        }
    }

}
