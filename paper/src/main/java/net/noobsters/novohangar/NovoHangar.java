package net.noobsters.novohangar;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import kr.entree.spigradle.annotations.SpigotPlugin;
import lombok.Getter;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.ChatColor;
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
                new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.PLAYER_INFO) {
                    @Override
                    public void onPacketSending(PacketEvent event) {
                        if (event.getPacketType() == PacketType.Play.Server.PLAYER_INFO) {
                            var packet = event.getPacket();
                            var player = event.getPlayer();
                            // Reference https://wiki.vg/Protocol#Player_Info
                            var action = packet.getPlayerInfoAction().read(0);
                            if (action.compareTo(PlayerInfoAction.ADD_PLAYER) == 0/** 0 = Add Player */
                            ) {
                                var cl = packet.deepClone();
                                cl.getPlayerInfoDataLists().write(0,
                                        List.of(new PlayerInfoData(
                                                WrappedGameProfile
                                                        .fromOfflinePlayer(Bukkit.getOfflinePlayer("InfinityZ")),
                                                69, NativeGameMode.SURVIVAL,
                                                WrappedChatComponent.fromText("TuMamacita"))));

                                ScheduleTaskForLater(() -> {
                                    try {
                                        protocolManager.sendServerPacket(player, cl);
                                    } catch (InvocationTargetException e) {
                                        e.printStackTrace();
                                    }
                                });

                            }
                            var fakeTeam = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);
                            fakeTeam.getStrings().write(0,
                                    player.getName().substring(0, Math.min(player.getName().length(), 10)) + "-fake");
                            try {
                                protocolManager.sendServerPacket(player, fakeTeam);
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    private void ScheduleTaskForLater(Runnable runnable) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, runnable, 20);
    }

    private String getPlayerMaskName(Player player) {
        var max = Math.max(10, (int) Math.random() * 10);
        var maskedName = "";
        var cColor = ChatColor.COLOR_CHAR;

        for (int i = 0; i < max; i++)
            maskedName += cColor;

        return maskedName;
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

}
