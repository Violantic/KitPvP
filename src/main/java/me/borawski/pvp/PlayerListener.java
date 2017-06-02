package me.borawski.pvp;

import me.borawski.pvp.util.KillUtil;
import me.borawski.pvp.util.MultiplierUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Created by Ethan on 1/29/2017.
 */
public class PlayerListener implements Listener {

    private LevelPlugin plugin;

    public PlayerListener(LevelPlugin plugin) {
        this.plugin = plugin;
    }

    public LevelPlugin getPlugin() {
        return plugin;
    }

    @EventHandler
    public void login(PlayerLoginEvent event) {
        final Player player = event.getPlayer();
        getPlugin().getUserManager().register(player.getUniqueId(), player.getName(), "");
    }

    @EventHandler
    public void logout(PlayerQuitEvent event) {
        getPlugin().getUserManager().unregister(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onKill(PlayerDeathEvent event) {
        Player player = event.getEntity().getKiller();
        if(player != null) {
            KillUtil.computeNewKill(getPlugin().getUserManager().queryOnline(player.getUniqueId()));
            MultiplierUtil.rewardForKill(player);
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        if(!(event.getEntity() instanceof  Player)) return;
        Player player = (Player) event.getEntity();
        getPlugin().getUserManager().queryOnline(player.getUniqueId()).addDeath();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        event.setFormat(event.getFormat().replace("{LEVEL}", getPlugin().getUserManager().queryOnline(event.getPlayer().getUniqueId()).getLevel() + ""));
    }

}