/*
 * Copyright (c) 2017. This code was written by Ethan Borawski, any use without permission will result in a court action. Check out my GitHub @ https://github.com/Violantic
 */

package me.borawski.pvp.database;

import me.borawski.pvp.LevelPlugin;
import me.borawski.pvp.util.Callback;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Ethan on 1/29/2017.
 */
public class KitDB {

    private LevelPlugin instance;

    private String host;
    private String name;
    private String user;
    private String pass;
    private String table;
    private Connection connection;

    public KitDB(LevelPlugin instance, final String host, String table, String name, String user, String pass) {
        this.instance = instance;
        this.host = host;
        this.table = table;
        this.name = name;
        this.user = user;
        this.pass = pass;

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + getHostz() + ":3306/" + getHost(), getUser(), getPass());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        SETUP_TABLE();
    }

    public LevelPlugin getInstance() {
        return instance;
    }

    /**
     * Core SQL
     **/

    public String getHostz() {
        return host;
    }

    public String getHost() {
        return name;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getTable() {
        return table;
    }

    public Connection getConnection() {
        return connection;
    }

    public Callback<Connection> invokeConnection() {
        return new Callback<Connection>() {
            public void call(Connection callback) {
                try {
                    callback = DriverManager.getConnection("jdbc:mysql://" + getHostz() + ":3306/" + getHost(), getUser(), getPass());
                    System.out.println("[KITPVP] Refreshing connection @ " + getHostz());
                    getInstance().getTracker().setLastUpdate(System.currentTimeMillis());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    public void SETUP_TABLE() {
        String users = "CREATE TABLE IF NOT EXISTS " + getTable() + "_users(id INT NOT NULL AUTO_INCREMENT, uuid VARCHAR(60), name VARCHAR(16), lastIP VARCHAR(60), lastPlayed BIGINT, kills INT(11), deaths INT(11), ilevel INT(11), requiredKills INT(11), PRIMARY KEY(id))";
        PreparedStatement statement = null;
        try {
            statement = getConnection().prepareStatement(users);
            executeAsync(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String GET_TABLE() {
        return getTable();
    }

    /**
     * Util
     **/
    public void executeAsync(final PreparedStatement statement) {
        instance.getServer().getScheduler().runTaskAsynchronously(getInstance(),
                new Runnable() {
                    public void run() {
                        try {
                            getInstance().getTracker().setLastUpdate(System.currentTimeMillis());
                            statement.executeUpdate();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void setStat(String uuid, String name, int value) {
        try {
            PreparedStatement statement = getConnection().prepareStatement("UPDATE " + GET_TABLE() + "_users SET " + name + "='" + value + "' WHERE uuid='" + uuid + "'");
            executeAsync(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<String, Integer> getStats(String uuid) {
        Map<String, Integer> stats = new ConcurrentHashMap<String, Integer>();
        ResultSet set = query("uuid", uuid);
        try {
            while (set.next()) {
                stats.put("kills", set.getInt("kills"));
                stats.put("deaths", set.getInt("deaths"));
                stats.put("ilevel", set.getInt("ilevel"));
                stats.put("requiredKills", set.getInt("requiredKills"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return stats;
    }

    /**
     * Query for a specific row of data with the modifier, and the value.
     * Example, if you would like to find an entire User row, you would
     * call this method, and on invocation the parameters would be 'uuid', and '{desired uuid you want to find here}'.
     *
     * @param modifier
     * @param desiredTarget
     * @return
     */
    public ResultSet query(String modifier, String desiredTarget) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM " + GET_TABLE() + "_users WHERE " + modifier + "='" + desiredTarget + "';");
            getInstance().getTracker().setLastUpdate(System.currentTimeMillis());
            return statement.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void computeIfAbsent(String uuid, String name, String lastIp) {
        try {
            ResultSet set = getConnection().prepareStatement("SELECT * FROM " + getTable() + "_users WHERE uuid='" + uuid + "'").executeQuery();
            if (!set.next()) {
                String insert = "INSERT INTO " + getTable() + "_users VALUES (NULL, '" + uuid + "', '" + name + "', '" + lastIp + "', " + System.currentTimeMillis() + ", 0, 0, 1, 5)";
                PreparedStatement register = getConnection().prepareStatement(insert);
                executeAsync(register);
            } else {
                System.out.println("[KITPVP] : computing player data for : " + uuid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getUUID(String name) {
        String query = "SELECT * FROM " + getTable() + "_users WHERE name='" + name + "';";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                getInstance().getTracker().setLastUpdate(System.currentTimeMillis());
                return set.getString("uuid");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getName(String uuid) {
        String query = "SELECT * FROM " + getTable() + "_users WHERE uuid='" + uuid + "';";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                getInstance().getTracker().setLastUpdate(System.currentTimeMillis());
                return set.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getAddress(String uuid) {
        String query = "SELECT * FROM " + getTable() + "_users WHERE uuid='" + uuid + "';";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                getInstance().getTracker().setLastUpdate(System.currentTimeMillis());
                return set.getString("lastIP");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public long getLastPlayed(String uuid) {
        String query = "SELECT * FROM " + getTable() + "_users WHERE uuid='" + uuid + "';";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                getInstance().getTracker().setLastUpdate(System.currentTimeMillis());
                return set.getLong("lastPlayed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0L;
    }

    public int getId(String uuid) {
        String query = "SELECT id FROM " + getTable() + "_users WHERE uuid='" + uuid + "'";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                getInstance().getTracker().setLastUpdate(System.currentTimeMillis());
                return set.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
