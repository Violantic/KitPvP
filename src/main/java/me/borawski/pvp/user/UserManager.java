/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package me.borawski.pvp.user;

import me.borawski.pvp.LevelPlugin;
import me.borawski.pvp.database.KitDB;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ethan on 1/29/2017.
 */
public class UserManager {

    private KitDB db;
    private Map<UUID, OnlineUser> userCache;

    public UserManager(KitDB db) {
        this.db = db;
        this.userCache = new ConcurrentHashMap<UUID, OnlineUser>();
    }

    public KitDB getDb() {
        return db;
    }

    public Map<UUID, OnlineUser> getUserCache() {
        return userCache;
    }

    public void setUserCache(Map<UUID, OnlineUser> userCache) {
        this.userCache = userCache;
    }

    public void register(final UUID uuid, final String name, final String lastIP) {
        getDb().computeIfAbsent(uuid.toString(), name, lastIP);

        // Give database a bit of recovery time in case it has to register new player. //
        new BukkitRunnable() {
            public void run() {
                userCache.put(uuid, new OnlineUser(uuid, name, lastIP));
            }
        }.runTaskLater(LevelPlugin.getInstance(), 5L);
    }

    public void unregister(UUID uuid) {
        OnlineUser user = queryOnline(uuid);
        for(String stat : user.getStatistics().keySet()) {
            getDb().setStat(uuid.toString(), stat, user.getStatistics().get(stat));
        }
        System.out.println("[KITPVP] Saving data for : " + uuid);
    }

    public User get(UUID uuid, UserType type) {
        return (type == UserType.ONLINE) ? queryOnline(uuid) : queryOffline(uuid);
    }

    public OnlineUser queryOnline(UUID uuid) {
        return userCache.get(uuid);
    }

    public User queryOffline(final UUID uuid) {
        return new User() {
            public UUID getUUID() {
                return uuid;
            }

            public String getName() {
                return getDb().getName(uuid.toString());
            }

            public String lastIP() {
                return getDb().getAddress(uuid.toString());
            }

            public long lastPlayed() {
                return getDb().getLastPlayed(uuid.toString());
            }

            public Map<String, Integer> getStatistics() {
                return getDb().getStats(uuid.toString());
            }
        };
    }

    public enum UserType {
        ONLINE,OFFLINE
    }
}
