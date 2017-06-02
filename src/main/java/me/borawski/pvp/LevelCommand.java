/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package me.borawski.pvp;

import me.borawski.pvp.user.OnlineUser;
import me.borawski.pvp.user.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by Ethan on 1/29/2017.
 */
public class LevelCommand implements CommandExecutor {

    private LevelPlugin plugin;

    public LevelCommand(LevelPlugin plugin) {
        this.plugin = plugin;
    }

    public LevelPlugin getPlugin() {
        return plugin;
    }

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        if(args.length == 0) {
            OnlineUser user = getPlugin().getUserManager().queryOnline(((Player) commandSender).getUniqueId());
            user.sendRawMessage(("&e&l(!) &r&eYou are currently &e&nlevel " + user.getLevel() + "&r&e.").replace("&", ChatColor.COLOR_CHAR + ""));
            user.sendRawMessage(user.getLevelString());
            return false;
        } else if(args.length == 1) {
            UUID target;
            try {
                target = UUID.fromString(getPlugin().getDb().getUUID(args[0]));
            } catch (Exception e) {
                commandSender.sendMessage(("&c&l(!) &r&c" + args[0] + " has never logged on to this server before!").replace("&", ChatColor.COLOR_CHAR + ""));
                return false;
            }

            User user = getPlugin().getUserManager().queryOffline(target);
            commandSender.sendMessage(("&e&l(!) &r&e" + user.getName() + " is currently &e&nlevel " + user.getStatistics().get("ilevel") + " &r&e.").replace("&", ChatColor.COLOR_CHAR + ""));
        }

        return false;
    }
}
