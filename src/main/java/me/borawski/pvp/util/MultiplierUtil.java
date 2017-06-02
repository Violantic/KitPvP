/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package me.borawski.pvp.util;

import me.borawski.pvp.LevelPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

/**
 * Created by Ethan on 1/29/2017.
 */
public class MultiplierUtil {

    public static void rewardForKill(Player player) {
        int amount = 2;
        for(PermissionAttachmentInfo i : player.getEffectivePermissions()) {
            if(i.getPermission().contains("fadelevels.multi.")) {
                String[] args = i.getPermission().split("scale");
                Integer multiplier = Integer.parseInt(args[1]);
                amount = (amount * multiplier);
            }
        }

        // Issue using vault API so temporarily using this method of transaction. //
        LevelPlugin.getInstance().getServer().dispatchCommand(Bukkit.getConsoleSender(), "eco give " + player.getName() + " " + amount);
        //LevelPlugin.getInstance().getUserManager().queryOnline(player.getUniqueId()).sendMessage("You have earned $" + amount);
    }

}
