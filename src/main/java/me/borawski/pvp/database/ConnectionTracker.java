/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package me.borawski.pvp.database;

import me.borawski.pvp.LevelPlugin;

import java.sql.Connection;
import java.time.Duration;
import java.time.Instant;

/**
 * Created by Ethan on 2/24/2017.
 */
public class ConnectionTracker implements Runnable {

    private long lastUpdate;
    private Connection connection;
    private LevelPlugin instance;

    public ConnectionTracker(LevelPlugin instance, Connection connection) {
        this.instance = instance;
        this.connection = connection;
        this.lastUpdate = System.currentTimeMillis();
    }

    public long getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(long lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public LevelPlugin getInstance() {
        return instance;
    }

    public void setInstance(LevelPlugin instance) {
        this.instance = instance;
    }

    public void run() {
        long delta = Duration.between(Instant.ofEpochMilli(getLastUpdate()), Instant.ofEpochMilli(System.currentTimeMillis())).toMinutes();
        if(delta < 30L) return;
        getInstance().getDb().invokeConnection().call(connection);
    }

}
