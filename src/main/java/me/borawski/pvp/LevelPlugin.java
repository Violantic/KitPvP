/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package me.borawski.pvp;

import me.borawski.pvp.database.ConnectionTracker;
import me.borawski.pvp.database.KitDB;
import me.borawski.pvp.user.UserManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Ethan on 1/29/2017.
 */
public class LevelPlugin extends JavaPlugin {

    private static LevelPlugin instance;

    private KitDB db;
    private ConnectionTracker tracker;
    private UserManager userManager;
    private Economy economy;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(getConfig().contains("table"));
        saveConfig();

        instance = this;
        db = new KitDB(this, getConfig().getString("host"), getConfig().getString("table"), getConfig().getString("name"), getConfig().getString("user"), getConfig().getString("pass"));
        tracker = new ConnectionTracker(this, db.getConnection());
        userManager = new UserManager(getDb());

        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getCommand("level").setExecutor(new LevelCommand(this));
        getCommand("flsave").setExecutor(new CommandExecutor() {
            public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
                if (commandSender.isOp()) {
                    for (Player player : getServer().getOnlinePlayers()) {
                        getUserManager().unregister(player.getUniqueId());
                    }
                }
                return false;
            }
        });

        getServer().getScheduler().runTaskTimer(this, tracker, 0L, 20 * 60L);
        getServer().getScheduler().runTaskTimer(getInstance(), new Runnable() {
            public void run() {
                System.out.println("[KITPVP] GLOBAL SAVE : Saving " + getServer().getOnlinePlayers().size() + " players");
                for (Player player : getServer().getOnlinePlayers()) {
                    getUserManager().unregister(player.getUniqueId());
                }
            }
        }, 0L, 20 * 60 * 60L);
    }

    public static LevelPlugin getInstance() {
        return instance;
    }

    public KitDB getDb() {
        return db;
    }

    public ConnectionTracker getTracker() {
        return tracker;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public Economy getEconomy() {
        return economy;
    }
}
