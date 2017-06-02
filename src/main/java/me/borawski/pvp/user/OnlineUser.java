/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package me.borawski.pvp.user;

import me.borawski.pvp.LevelPlugin;
import org.bukkit.ChatColor;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Ethan on 1/29/2017.
 */
public class OnlineUser implements User {

    private UUID uuid;
    private String name;
    private String lastIp;
    private long lastPlayed;
    private Map<String, Integer> statistics;

    public OnlineUser(UUID uuid, String name, String lastIp) {
        this.uuid = uuid;
        this.name = name;
        this.lastIp = lastIp;
        this.lastPlayed = System.currentTimeMillis();
        this.statistics = LevelPlugin.getInstance().getDb().getStats(uuid.toString());
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String lastIP() {
        return lastIp;
    }

    public long lastPlayed() {
        return lastPlayed;
    }

    public Map<String, Integer> getStatistics() {
        return statistics;
    }

    /**
     * Getters and setters
     */

    public Integer getKills() {
        return getStatistics().get("kills");
    }

    public void setKills(int i) {
        getStatistics().put("kills", i);
    }

    public void addKill() {
        getStatistics().put("kills", getStatistics().get("kills")+1);
    }

    public Integer getDeaths() {
        return getStatistics().get("deaths");
    }

    public void setDeaths(int i) {
        getStatistics().put("deaths", i);
    }

    public void addDeath() {
        getStatistics().put("deaths", getStatistics().get("deaths")+1);
    }

    public Integer getLevel() {
        return getStatistics().get("ilevel");
    }

    public void setLevel(int i) {
        getStatistics().put("ilevel", i);
    }

    public void addLevel() {
        getStatistics().put("ilevel", getStatistics().get("ilevel")+1);
        increaseRequiredKills();
    }

    public Integer getRequiredKills() {
        return getStatistics().get("requiredKills");
    }

    public void increaseRequiredKills() {
        getStatistics().put("requiredKills", (int) (Math.round(getRequiredKills() * 1.75) + 0.0D));
    }

    public String getLevelString() {
        String block = "===";
        String origin = "";
        double percents = ((double)(getStatistics().get("kills") / (double)getStatistics().get("requiredKills")) * 100.0);
        String percent =  ChatColor.GRAY + "(" + Math.round(percents) + "%)";
        for(int i = 0; i < 10; i ++) {
            if((percents) > i*10) {
                origin += ChatColor.GREEN + "" + ChatColor.STRIKETHROUGH + "" + ChatColor.BOLD + "" + block;
            } else {
                origin += ChatColor.GRAY + "" + ChatColor.STRIKETHROUGH + "" + ChatColor.BOLD + "" + block;
            }
        }
        return ChatColor.GRAY  + "[" + origin + ChatColor.GRAY + "]" + " " + percent;
    }

    public void sendMessage(String msg) {
        try {
            LevelPlugin.getInstance().getServer().getPlayer(getUUID()).sendMessage(("&a&l(!) &r&a" + msg).replace("&", ChatColor.COLOR_CHAR + ""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRawMessage(String msg) {
        try {
            LevelPlugin.getInstance().getServer().getPlayer(getUUID()).sendMessage(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
