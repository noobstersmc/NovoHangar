package net.noobsters.novohangar;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import kr.entree.spigradle.annotations.SpigotPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;
import us.jcedeno.libs.rapidinv.RapidInvManager;

@SpigotPlugin
public class NovoHangar extends JavaPlugin implements Listener {
    private static MiniMessage miniMessage = MiniMessage.get();

    @Override
    public void onEnable() {
        // Register RapidInvManager
        RapidInvManager.register(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager()instanceof Player player) {
            var attacked = e.getEntity();
            var msg = miniMessage
                    .parse("<gradient:#5e4fa2:#f79459:red>Welcome to Java 16 and Spigot 1.16.5!</gradient>");
            if (attacked instanceof LivingEntity entity) {
                entity.getHealth();
            }

            player.sendMessage(msg);
        }

    }

    public String getHealthBar(LivingEntity livingEntity) {
        var str = "||||||||||||||||||||";
        /** Obtain the entities health and max health */
        var health = livingEntity.getHealth();
        var maxHealth = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        /** Divide ensureing not 0 division */
        var percent = health / Math.min(1, maxHealth);

        return "||||||||||||||||||||".substring(0, Math.max((int) livingEntity.getHealth(), 19));
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        // e.getPlayer().sendMessage(e.getTo().toString());
    }

}
