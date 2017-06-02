/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package me.borawski.pvp.util;

import me.borawski.pvp.LevelPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.Map;

/**
 * Created by Ethan on 1/29/2017.
 */
public class ScoreUtil {

    public static void apply(final Player player) {
        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
        org.bukkit.scoreboard.Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        final Objective objective = scoreboard.registerNewObjective("GameSB", "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        final org.bukkit.scoreboard.Team team = scoreboard.registerNewTeam("team1");
        final org.bukkit.scoreboard.Team team2 = scoreboard.registerNewTeam("team2");
        final org.bukkit.scoreboard.Team team3 = scoreboard.registerNewTeam("team3");
        final org.bukkit.scoreboard.Team team4 = scoreboard.registerNewTeam("team4");
        final org.bukkit.scoreboard.Team team5 = scoreboard.registerNewTeam("team5");
        final org.bukkit.scoreboard.Team team6 = scoreboard.registerNewTeam("team6");
        final org.bukkit.scoreboard.Team team7 = scoreboard.registerNewTeam("team7");

        team.addPlayer(Bukkit.getOfflinePlayer(ChatColor.AQUA.toString()));
        team2.addPlayer(Bukkit.getOfflinePlayer(ChatColor.STRIKETHROUGH.toString()));
        team3.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLACK.toString()));
        team4.addPlayer(Bukkit.getOfflinePlayer(ChatColor.BLUE.toString()));
        team5.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_AQUA.toString()));
        team6.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_PURPLE.toString()));
        team7.addPlayer(Bukkit.getOfflinePlayer(ChatColor.DARK_GREEN.toString()));

        objective.getScore(ChatColor.AQUA.toString()).setScore(13);
        objective.getScore(ChatColor.STRIKETHROUGH.toString()).setScore(12);
        objective.getScore(ChatColor.BLACK.toString()).setScore(11);
        objective.getScore(ChatColor.BLUE.toString()).setScore(10);
        objective.getScore(ChatColor.DARK_AQUA.toString()).setScore(9);
        objective.getScore(ChatColor.DARK_PURPLE.toString()).setScore(8);
        objective.getScore(ChatColor.DARK_GREEN.toString()).setScore(7);

        new BukkitRunnable() {
            public void run() {
                Map<String, Integer> stats = LevelPlugin.getInstance().getUserManager().queryOnline(player.getUniqueId()).getStatistics();

                objective.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "KIT" + ChatColor.WHITE + "" + ChatColor.BOLD + "PVP");
                team.setPrefix(("&b*l* &r&7K " + stats.get("kills") + " Kills").replace("&", ChatColor.COLOR_CHAR + ""));
                team2.setPrefix("");
                team3.setPrefix(("&b*l* &r&7D " + stats.get("deaths") + " Deaths").replace("&", ChatColor.COLOR_CHAR + ""));
                team4.setPrefix("");
                team5.setPrefix(("&b*l* &r&7Lvl " + stats.get("level")).replace("&", ChatColor.COLOR_CHAR + ""));
                team6.setPrefix("");
                team7.setPrefix(("&b*l* &r&7" + ((stats.get("kills") / stats.get("requiredKills")) * 100) + "%").replace("&", ChatColor.COLOR_CHAR + ""));
            }
        }.runTaskTimer(LevelPlugin.getInstance(), 0L, 20L);

        player.setScoreboard(scoreboard);
    }

}
