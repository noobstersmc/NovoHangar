package net.noobsters.novohangar;

import java.lang.reflect.Field;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.papermc.paper.event.player.AsyncChatEvent;
import kr.entree.spigradle.annotations.SpigotPlugin;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import us.jcedeno.libs.rapidinv.RapidInvManager;

@SpigotPlugin // Use @SpigotPlugin from spigradle to generate plugin.yml
public class NovoHangar extends JavaPlugin implements Listener {
    /** Mini message to easily parse messages. */
    private @Getter static MiniMessage miniMessage = MiniMessage.get();
    /** Protocol Lib to intercept packages with ease. */
    private @Getter ProtocolManager protocolManager;

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        // Register RapidInvManager
        RapidInvManager.register(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        protocolManager.addPacketListener(
                new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.SPAWN_ENTITY) {

                });
        protocolManager.addPacketListener(
                new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.PLAYER_INFO) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (event.getPacketType() == PacketType.Play.Server.PLAYER_INFO) {
                            var packet = event.getPacket();
                            // Reference https://wiki.vg/Protocol#Player_Info
                            var action = packet.getPlayerInfoAction().read(0);
                            if (action.compareTo(PlayerInfoAction.ADD_PLAYER) == 0/** 0 = Add Player */
                            ) {
                                var info = packet.getPlayerInfoDataLists().read(0);
                                int count = 0;
                                for (var entry : info) {
                                    var c_profile = entry.getProfile().withName("HolaPinche");

                                    var fields = c_profile.getHandle().getClass().getDeclaredFields();
                                    for (var field : fields) {
                                        System.out.println("Fields: " +field.getName());
                                    }
                                    count++;

                                }
                                packet.getPlayerInfoDataLists().write(0, info);

                            }
                        }
                    }
                });
    }

    public static void setValue(Object object, String field, Object value) {
        try {
            Field f = object.getClass().getDeclaredField(field);
            if (!f.isAccessible()) {
                f.setAccessible(true);
            }
            f.set(object, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager()instanceof Player player) {
            var attacked = e.getEntity();
            var msg = miniMessage.parse("<gradient:#5e4fa2:#f79459:red>Welcome to Noobsters!</gradient>");
            if (attacked instanceof LivingEntity entity) {
                entity.getHealth();
            }

            player.sendMessage(msg);
        }

    }

    @EventHandler
    public void onChatEvent(AsyncChatEvent e) {
        var player = e.getPlayer();
        player.sendMessage(miniMessage.parse("Why are you not talking!"));
    }

}
