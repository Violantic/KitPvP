/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package me.borawski.pvp.user;

import java.util.Map;
import java.util.UUID;

/**
 * Created by Ethan on 1/29/2017.
 */
public interface User {

    UUID getUUID();
    String getName();
    String lastIP();
    long lastPlayed();
    Map<String, Integer> getStatistics();

}
