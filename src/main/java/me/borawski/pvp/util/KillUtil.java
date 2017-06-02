/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package me.borawski.pvp.util;

import me.borawski.pvp.user.OnlineUser;
import org.bukkit.ChatColor;

/**
 * Created by Ethan on 1/29/2017.
 */
public class KillUtil {

    public static void computeNewKill(OnlineUser user) {
        Double requiredKills = user.getRequiredKills() + 0.0D;
        user.addKill();
        //Double newK = user.getKills() + 0.0D;
        //user.sendRawMessage(ChatColor.GRAY + "" + Math.round((newK/requiredKills)*100.0D) + "%");
        if(user.getKills() >= requiredKills) {
            user.addLevel();
            user.setKills(0);
            user.sendMessage("You have leveled up to level &n" + user.getLevel());
            user.sendRawMessage("&7Check your progress using the /level command.".replace("&", ChatColor.COLOR_CHAR + ""));
        }
    }

}
