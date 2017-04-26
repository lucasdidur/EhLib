package com.ehaqui.lib.config;


import com.ehaqui.lib.config.model.FlagPlayer;
import com.ehaqui.lib.config.model.FlagPlugin;
import com.ehaqui.lib.config.model.Players;
import com.ehaqui.lib.database.Database;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import lombok.Getter;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.config.ConfigurationProvider;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Configuration
{
    private String pluginName;
    @Getter
    private Logger logger;

    private Map<String, HashMap<String, String>> cachePlugin = new HashMap<>();
    private Map<String, HashMap<UUID, HashMap<String, String>>> cachePlayer = new HashMap<>();

    private boolean useCache = true;

    private Database db;

    public Configuration(net.md_5.bungee.api.plugin.Plugin plugin)
    {
        this(plugin, true, com.ehaqui.ehlib.bungeecord.EhLibBungeecord.getGlobalDatabase());
    }

    public Configuration(org.bukkit.plugin.java.JavaPlugin plugin)
    {
        this(plugin, true, com.ehaqui.ehlib.bukkit.EhLibSpigot.getGlobalDatabase());
    }

    public Configuration(net.md_5.bungee.api.plugin.Plugin plugin, boolean cache)
    {
        this(plugin, cache, com.ehaqui.ehlib.bungeecord.EhLibBungeecord.getGlobalDatabase());
    }

    public Configuration(org.bukkit.plugin.java.JavaPlugin plugin, boolean cache)
    {
        this(plugin, cache, com.ehaqui.ehlib.bukkit.EhLibSpigot.getGlobalDatabase());
    }

    public Configuration(net.md_5.bungee.api.plugin.Plugin plugin, Database db)
    {
        this(plugin, true, db);
    }

    public Configuration(org.bukkit.plugin.java.JavaPlugin plugin, Database db)
    {
        this(plugin, true, db);
    }

    public Configuration(net.md_5.bungee.api.plugin.Plugin plugin, boolean useCache, Database db)
    {
        this.pluginName = plugin.getDescription().getName();
        this.logger = plugin.getLogger();

        this.useCache = useCache;

        this.db = db;

        if (useCache)
        {
            load();
        }
    }

    public Configuration(org.bukkit.plugin.java.JavaPlugin plugin, boolean useCache, Database db)
    {
        this.pluginName = plugin.getDescription().getName();
        this.logger = plugin.getLogger();

        this.useCache = useCache;

        this.db = db;
        if (useCache)
        {
            load();
        }
    }

    public void setupDatabase()
    {
        db.createTable(Players.class);
        db.createTable(FlagPlayer.class);
        db.createTable(FlagPlugin.class);
    }

    public static <T> T loadConfig(net.md_5.bungee.api.plugin.Plugin plugin, Class<T> clazz)
    {
        Configuration config = new Configuration(plugin, false);
        return config.loadClassBungee(clazz);
    }

    public static <T> T loadConfig(org.bukkit.plugin.java.JavaPlugin plugin, Class<T> clazz)
    {
        Configuration config = new Configuration(plugin, false);
        return config.loadClass(clazz, "global");
    }

    public static <T> T loadConfig(org.bukkit.plugin.java.JavaPlugin plugin, Class<T> clazz, String server)
    {
        Configuration config = new Configuration(plugin, false);
        return config.loadClass(clazz, server);
    }

    private HashMap<String, String> getHash(HashMap<String, String> map, String key, String value)
    {
        map.put(key, value);

        return map;
    }

    private HashMap<String, String> getHash(String key, String value)
    {
        HashMap<String, String> map = new HashMap<>();
        map.put(key, value);

        return map;
    }

    public boolean load()
    {
        db.getConnection();

        try
        {
            ResultSet rs;
            PreparedStatement pst;

            // Flag Plugin
            pst = db.getConnection().prepareStatement("SELECT * FROM `eh_core_flags_plugin` WHERE `plugin` = ?");
            pst.setString(1,  pluginName);

            rs = pst.executeQuery();

            while (rs.next())
            {
                cachePlugin.put(rs.getString("flag_server"), getHash(rs.getString("flag_key"), rs.getString("flag_value")));
            }
            rs.close();
            pst.close();

            // Flag Player
            pst = db.getConnection().prepareStatement("SELECT `uuid`, `flag_key`, `flag_value`, `flag_server` FROM `eh_core_flags_player` f JOIN `eh_core_players` p on f.`player_id` = p.`player_id` WHERE `plugin` = ?");
            pst.setString(1,  pluginName);

            rs = pst.executeQuery();

            while (rs.next())
            {
                String server = rs.getString("flag_server");

                UUID uuid = UUID.fromString(rs.getString("uuid"));

                if(cachePlayer.containsKey(server))
                {
                    HashMap<UUID, HashMap<String, String>> fPlayer = cachePlayer.get(server);
                    HashMap<String, String> fValue;

                    if (fPlayer.containsKey(uuid))
                    {
                        fValue = getHash(fPlayer.get(uuid), rs.getString("flag_key"), rs.getString("flag_value"));
                    }
                    else
                    {
                        fValue = getHash(rs.getString("flag_key"), rs.getString("flag_value"));
                    }

                    fPlayer.put(uuid, fValue);
                    cachePlayer.put(server, fPlayer);
                }
            }

            rs.close();
            pst.close();

        } catch (SQLException e)
        {
            e.printStackTrace();
            logger.severe("Error on load flags");
            return false;
        }

        return true;
    }

    public void updatePlayer(UUID uuid, String playerName)
    {
        HashMap<String, Object> data = new HashMap<>();
        data.put("uuid", uuid.toString());
        data.put("nick", playerName);

        HashMap<String, Object> update = new HashMap<>();
        update.put("lastseen", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        update.put("nick", playerName);

        db.updateFast(db.sql("eh_core_players", data, update));
    }

    public <T> T addFlagDefault(String key, T value, String server)
    {
        addFlag(key, value, false, server);

        return value;
    }

    public <T> T addFlagDefault(String key, T value)
    {
        addFlag(key, value, false, "global");

        return value;
    }

    public <T> T addFlag(String key, T value, String server)
    {
        addFlag(key, value, true, server);

        return value;
    }

    public <T> T addFlag(String key, T value)
    {
        addFlag(key, value, true, "global");

        return value;
    }

    public List<String> addFlagList(String key, List<String> list)
    {
        Gson gson = new Gson();
        addFlag(key, gson.toJson(list));

        return list;
    }

    public boolean addFlag(String key, Object value, boolean update, String server)
    {
        String query = "INSERT INTO `eh_core_flags_plugin` (`plugin`, `flag_key`, `flag_value`, `flag_server`) VALUES (?, ?, ?, ?)";

        if (update)
        {
            query += " ON DUPLICATE KEY UPDATE `flag_value` = ?";
        }

        try
        {
            PreparedStatement pst = db.getConnection().prepareStatement(query);
            pst.setString(1, pluginName);
            pst.setString(2, key);
            pst.setString(3, value == null ? "" : value.toString());
            pst.setString(4, server);
            if (update)
                pst.setString(5, value == null ? "" : value.toString());

            pst.executeUpdate();

            if (useCache)
            {
                HashMap<String, String> valueS = new HashMap<>();
                valueS.put(key, value.toString());

                cachePlugin.put(server, valueS);
            }

            return false;
        } catch (SQLIntegrityConstraintViolationException e)
        {
            if (!update)
            {
                return true;
            }

            e.printStackTrace();
            logger.severe("Error on insert flag '" + key + "'");

        } catch (SQLException e)
        {
            e.printStackTrace();
            logger.severe("Error on insert flag '" + key + "'");
        }

        return false;
    }

    public <T> T addFlagDefault(UUID uuid, String key, T value, String server)
    {
        addFlag(uuid, key, value, false, server);

        return value;
    }

    public <T> T addFlagDefault(UUID uuid, String key, T value)
    {
        addFlag(uuid, key, value, false, "global");

        return value;
    }

    public <T> T addFlag(UUID uuid, String key, T value, String server)
    {
        addFlag(uuid, key, value, true, server);

        return value;
    }

    public <T> T addFlag(UUID uuid, String key, T value)
    {
        addFlag(uuid, key, value, true, "global");

        return value;
    }

    public void addFlagList(UUID uuid, String key, Collection<String> list, String server)
    {
        Gson gson = new Gson();
        addFlag(uuid, key, gson.toJson(list), server);
    }

    public void addFlagList(UUID uuid, String key, Collection<String> list)
    {
        Gson gson = new Gson();
        addFlag(uuid, key, gson.toJson(list), "global");
    }

    public boolean addFlag(OfflinePlayer player, String key, Object value, String server)
    {
        return addFlag(player.getUniqueId(), key, value, true, server);
    }

    public boolean addFlag(OfflinePlayer player, String key, Object value)
    {
        return addFlag(player.getUniqueId(), key, value, true, "global");
    }

    public boolean addFlag(OfflinePlayer player, String key, Object value, boolean update, String server)
    {
        return addFlag(player.getUniqueId(), key, value, update, server);
    }

    public boolean addFlag(OfflinePlayer player, String key, Object value, boolean update)
    {
        return addFlag(player.getUniqueId(), key, value, update, "global");
    }

    public boolean addFlag(UUID uuid, String key, Object value, boolean update, String server)
    {
        String query = "INSERT INTO `eh_core_flags_player` (`plugin`, `player_id`, `flag_key`, `flag_value`, `flag_server`) VALUES (?, (SELECT `player_id` FROM `eh_core_players` WHERE `uuid` = ?), ?, ?, ?)";

        if (update)
        {
            query += " ON DUPLICATE KEY UPDATE `flag_value` = ?";
        }

        try
        {
            PreparedStatement pst = db.getConnection().prepareStatement(query);
            pst.setString(1, pluginName);
            pst.setString(2, uuid.toString());
            pst.setString(3, key);
            pst.setString(4, value == null ? "" : value.toString());
            pst.setString(5, server);
            if (update)
                pst.setString(6, value == null ? "" : value.toString());

            pst.executeUpdate();

            if (useCache)
            {
                if(cachePlayer.containsKey(server))
                {
                    HashMap<UUID, HashMap<String, String>> fPlayer = cachePlayer.get(server);
                    HashMap<String, String> fValue;

                    if (fPlayer.containsKey(uuid))
                    {
                        fValue = getHash(fPlayer.get(uuid), key, value == null ? "" : value.toString());
                    }
                    else
                    {
                        fValue = getHash(key, value == null ? "" : value.toString());
                    }

                    fPlayer.put(uuid, fValue);
                    cachePlayer.put(server, fPlayer);
                }
            }
            return true;

        } catch (MySQLIntegrityConstraintViolationException e)
        {
            if (!update)
            {
                return true;
            }

            e.printStackTrace();
            logger.severe("Error on insert flag '" + key + "' of player '" + uuid + "'");

        } catch (SQLException e)
        {
            e.printStackTrace();
            logger.severe("Error on insert flag '" + key + "' of player '" + uuid + "'");
        }

        return false;
    }

    public boolean hasFlag(String key)
    {
        return hasFlag(key, "global");
    }

    /**
     * Checa se o plugin possui uma configuração salva com a chave informada
     * @param key chave
     * @return
     */
    public boolean hasFlag(String key, String server)
    {
        if (useCache)
        {
            if(cachePlugin.containsKey(server))
            {
                return cachePlugin.get(server).containsKey(key);
            }
        }
        else
        {
            try
            {
                PreparedStatement pst = db.getConnection().prepareStatement("SELECT `flag_value` FROM `eh_core_flags_plugin` WHERE `plugin` = ? AND `flag_key` = ? AND `flag_server` = ?");
                pst.setString(1, pluginName);
                pst.setString(2, key);
                pst.setString(3, server);

                ResultSet rs = pst.executeQuery();
                if (rs.next())
                {
                    return true;
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean hasFlag(OfflinePlayer player, String key)
    {
        return hasFlag(player.getUniqueId(), key, "global");
    }

    public boolean hasFlag(OfflinePlayer player, String key, String server)
    {
        return hasFlag(player.getUniqueId(), key, server);
    }

    public boolean hasFlag(UUID uuid, String key, String server)
    {
        if (useCache)
        {
            if(cachePlayer.containsKey(server))
            {
                if(cachePlayer.get(server).containsKey(uuid))
                {
                    return cachePlayer.get(server).get(uuid).containsKey(key);
                }
            }
        }
        else
        {
            try
            {
                PreparedStatement pst = db.getConnection().prepareStatement("SELECT `flag_value` FROM `eh_core_flags_player` f JOIN `eh_core_players` p on f.`player_id` = p.`player_id` WHERE `plugin` = ? AND `uuid` = ? AND `flag_key` = ? AND `flag_server` = ?");
                pst.setString(1, pluginName);
                pst.setString(2, uuid.toString());
                pst.setString(3, key);
                pst.setString(4, server);

                ResultSet rs = pst.executeQuery();
                if (rs.next())
                {
                    return true;
                }
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }

        return false;
    }

    public List<String> getFlagList(String key)
    {
        return getFlagList(key, "global");
    }

    public List<String> getFlagList(String key, String server)
    {
        String flag = getFlag(key, server);

        Gson gson = new Gson();
        Type collectionType = new TypeToken<List<String>>()
        {
        }.getType();
        return gson.fromJson(flag, collectionType);
    }

    public Collection<String> getFlagList(OfflinePlayer player, String key)
    {
        return getFlagList(player.getUniqueId(), key, "global");
    }

    public Collection<String> getFlagList(OfflinePlayer player, String key, String server)
    {
        return getFlagList(player.getUniqueId(), key, server);
    }

    public Collection<String> getFlagList(UUID uuid, String key)
    {
        return getFlagList(uuid, key, "global");
    }

    public Collection<String> getFlagList(UUID uuid, String key, String server)
    {
        String flag = getFlag(uuid, key, server);

        Gson gson = new Gson();
        Type collectionType = new TypeToken<Collection<String>>()
        {
        }.getType();
        return gson.fromJson(flag, collectionType);
    }

    public String getFlag(String key, String server)
    {
        if (hasFlag(key, server))
        {
            if (useCache)
            {
                return cachePlugin.get(server).get(key);
            }
            else
            {
                try
                {
                    PreparedStatement pst = db.getConnection().prepareStatement("SELECT `flag_value` FROM `eh_core_flags_plugin` WHERE `plugin` = ? AND `flag_key` = ? AND `flag_server` = ?");
                    pst.setString(1, pluginName);
                    pst.setString(2, key);
                    pst.setString(3, server);

                    ResultSet rs = pst.executeQuery();
                    if (rs.next())
                    {
                        return rs.getString("flag_value");
                    }
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public int getFlagInteger(OfflinePlayer player, String key)
    {
        return Integer.valueOf(getFlag(player.getUniqueId(), key, "global"));
    }

    public int getFlagInteger(OfflinePlayer player, String key, String server)
    {
        return Integer.valueOf(getFlag(player.getUniqueId(), key, server));
    }

    public boolean getFlagBoolean(OfflinePlayer player, String key)
    {
        return Boolean.valueOf(getFlag(player.getUniqueId(), key, "global"));
    }

    public boolean getFlagBoolean(OfflinePlayer player, String key, String server)
    {
        return Boolean.valueOf(getFlag(player.getUniqueId(), key, server));
    }

    public String getFlag(OfflinePlayer player, String key)
    {
        return getFlag(player.getUniqueId(), key, "global");
    }

    public String getFlag(OfflinePlayer player, String key, String server)
    {
        return getFlag(player.getUniqueId(), key, server);
    }

    public String getFlag(UUID uuid, String key, String server)
    {
        if (hasFlag(uuid, key, server))
        {
            if (useCache)
            {
                return cachePlayer.get(server).get(uuid).get(key);
            }
            else
            {
                try
                {
                    PreparedStatement pst = db.getConnection().prepareStatement("SELECT `flag_value` FROM `eh_core_flags_player` f JOIN `eh_core_players` p on f.`player_id` = p.`player_id` WHERE `plugin` = ? AND `uuid` = ? AND `flag_key` = ? AND `flag_server` = ?");

                    pst.setString(1, pluginName);
                    pst.setString(2, uuid.toString());
                    pst.setString(3, key);
                    pst.setString(4, server);

                    ResultSet rs = pst.executeQuery();
                    if (rs.next())
                    {
                        return rs.getString("flag_value");
                    }
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public boolean deleteFlag(String key, String server)
    {
        try
        {
            PreparedStatement pst = db.getConnection().prepareStatement("DELETE FROM `eh_core_flags_plugin` WHERE `plugin` = ? AND `flag_key` = (SELECT `player_id` FROM `eh_core_players` WHERE `uuid` = ?) AND `flag_server` = ? AND `player_id` IS NULL;");
            pst.setString(1, pluginName);
            pst.setString(2, key);
            pst.setString(3, server);

            pst.executeUpdate();

            if (useCache)
            {
                cachePlugin.get(key).remove(key);
            }
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            logger.severe("Error on delete flag '" + key + "'");
            return false;
        }
    }

    public boolean deleteFlag(OfflinePlayer player, String key)
    {
        return deleteFlag(player.getUniqueId(), key, "global");
    }

    public boolean deleteFlag(OfflinePlayer player, String key, String server)
    {
        return deleteFlag(player.getUniqueId(), key, server);
    }

    public boolean deleteFlag(UUID uuid, String key, String server)
    {
        try
        {
            PreparedStatement pst = db.getConnection().prepareStatement("DELETE FROM `eh_core_flags_player` WHERE `plugin` = ? AND `player_id` = (SELECT `player_id` FROM `eh_core_players` WHERE `uuid` = ?) AND `flag_key` = ? AND `flag_server` = ?;");
            pst.setString(1, pluginName);
            pst.setString(2, uuid.toString());
            pst.setString(3, key);
            pst.setString(4, server);

            pst.executeUpdate();

            if (useCache)
            {
                cachePlugin.get(server).remove(key);
            }
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            logger.severe("Error on delete flag '" + key + "' of player '" + uuid + "'");
            return false;
        }
    }

    public <T> T loadClass(Class<T> clazz)
    {
        return loadClass(clazz, "global");
    }

    public <T> T loadClass(Class<T> clazz, String server)
    {
        T classToLoad = null;
        try
        {
            classToLoad = clazz.newInstance();

            if (classToLoad == null)
            {
                throw new IllegalArgumentException("class cannot be null");
            }
        } catch (InstantiationException | IllegalAccessException e1)
        {
            e1.printStackTrace();
        }

        for (Field field : clazz.getDeclaredFields())
        {
            try
            {
                ConfigValue annotation = field.getAnnotation(ConfigValue.class);
                if (annotation != null)
                {
                    field.setAccessible(true);
                    Object fValue = field.get(classToLoad);

                    if(annotation.global())
                        server = "global";

                    if (hasFlag(annotation.value(), server))
                    {
                        Object value = getFlag(annotation.value(), server);

                        if (field.getType() == String.class)
                        {
                            value = ((String) value).replace("&", "§");
                        }
                        else if (field.getType() == Integer.class || field.getType() == int.class)
                        {
                            value = Integer.parseInt((String) value);
                        }
                        else if (field.getType() == Long.class || field.getType() == long.class)
                        {
                            value = Long.parseLong((String) value);
                        }
                        else if (field.getType() == Double.class || field.getType() == double.class)
                        {
                            value = Double.parseDouble((String) value);
                        }
                        else if (field.getType() == Boolean.class || field.getType() == boolean.class)
                        {
                            value = Boolean.parseBoolean((String) value);
                        }
                        else if (field.getType() == Location.class)
                        {
                            Location location = null;

                            if (!value.equals(""))
                            {
                                String[] locations = ((String) value).split(",");

                                location = new Location(Bukkit.getServer().getWorld(locations[0].trim()), Double.parseDouble(locations[1].trim()), Double.parseDouble(locations[2].trim()), Double.parseDouble(locations[3].trim()));

                                if (locations.length > 4)
                                {
                                    location.setPitch(Float.parseFloat(locations[4].trim()));
                                    location.setYaw(Float.parseFloat(locations[5].trim()));
                                }
                            }

                            value = location;
                        }
                        else if (field.getType() == List.class)
                        {
                            value = getFlagList(annotation.value());
                            for (@SuppressWarnings("unchecked")
                                 ListIterator<String> iterator = ((List<String>) value).listIterator(); iterator.hasNext(); )
                            {
                                Object next = iterator.next();
                                if (next instanceof String)
                                {
                                    iterator.set(((String) next).replace("&", "§"));
                                }
                            }
                        }

                        field.set(classToLoad, value);
                    }
                    else
                    {
                        Object value = fValue;

                        if (fValue != null)
                        {
                            if (fValue instanceof List)
                            {
                                Location l = (Location) fValue;

                                value = l.getWorld().getName() + "," + l.getBlockX() + "," + l.getBlockY() + "," + l.getBlockZ() + "," + l.getPitch() + "," + l.getYaw();
                            }
                        }

                        addFlag(annotation.value(), value, server);

                    }
                }
            } catch (Exception e)
            {
                throw new RuntimeException("Failed to set config value for field '" + field.getName() + "' in " + clazz + " of server '" + server +"'", e);
            }
        }

        return classToLoad;
    }

    public <T> T loadClassBungee(Class<T> clazz)
    {
        return loadClassBungee(clazz, "global");
    }

    public <T> T loadClassBungee(Class<T> clazz, String server)
    {
        T classToLoad = null;
        try
        {
            classToLoad = clazz.newInstance();

            if (classToLoad == null)
            {
                throw new IllegalArgumentException("class cannot be null");
            }
        } catch (InstantiationException | IllegalAccessException e1)
        {
            e1.printStackTrace();
        }

        for (Field field : clazz.getDeclaredFields())
        {
            try
            {
                ConfigValue annotation = field.getAnnotation(ConfigValue.class);
                if (annotation != null)
                {
                    field.setAccessible(true);
                    Object fValue = field.get(classToLoad);

                    if(annotation.global())
                        server = "global";

                    if (hasFlag(annotation.value(), server))
                    {
                        Object value = getFlag(annotation.value(), server);

                        if (field.getType() == String.class)
                        {
                            value = ((String) value).replace("&", "§");
                        }
                        else if (field.getType() == Integer.class || field.getType() == int.class)
                        {
                            value = Integer.parseInt((String) value);
                        }
                        else if (field.getType() == Long.class || field.getType() == long.class)
                        {
                            value = Long.parseLong((String) value);
                        }
                        else if (field.getType() == Double.class || field.getType() == double.class)
                        {
                            value = Double.parseDouble((String) value);
                        }
                        else if (field.getType() == Boolean.class || field.getType() == boolean.class)
                        {
                            value = Boolean.parseBoolean((String) value);
                        }
                        else if (field.getType() == List.class)
                        {
                            value = getFlagList(annotation.value());
                            for (@SuppressWarnings("unchecked")
                                 ListIterator<String> iterator = ((List<String>) value).listIterator(); iterator.hasNext(); )
                            {
                                Object next = iterator.next();
                                if (next instanceof String)
                                {
                                    iterator.set(((String) next).replace("&", "§"));
                                }
                            }
                        }

                        field.set(classToLoad, value);
                    }
                    else
                    {
                        addFlag(annotation.value(), fValue, server);
                    }
                }
            } catch (Exception e)
            {
                throw new RuntimeException("Failed to set config value for field '" + field.getName() + "' in " + clazz + " of server '" + server +"'", e);
            }
        }

        return classToLoad;
    }

    public static <T> T loadLocalConfig(org.bukkit.plugin.Plugin plugin, Class<T> clazz)
    {
        return loadLocalConfig(plugin, clazz, "config.yml");
    }

    public static <T> T loadLocalConfig(org.bukkit.plugin.Plugin plugin, Class<T> clazz, String fileName)
    {
        T classToLoad = null;
        try
        {
            classToLoad = clazz.newInstance();

            if (classToLoad == null)
            {
                throw new IllegalArgumentException("class cannot be null");
            }
        } catch (InstantiationException | IllegalAccessException e1)
        {
            e1.printStackTrace();
        }


        File configFile = new File(plugin.getDataFolder(), fileName);

        if (!configFile.exists())
        {
            try{
                plugin.saveResource(fileName, false);

            }catch (IllegalArgumentException ex)
            {
                try
                {
                    if(!configFile.getParentFile().exists())
                        configFile.getParentFile().mkdir();

                    configFile.createNewFile();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        FileConfiguration config = new YamlConfiguration();

        try
        {
            config.load(configFile);
        } catch (IOException e)
        {
            e.printStackTrace();
        } catch (InvalidConfigurationException e)
        {
            e.printStackTrace();
        }


        for (Field field : clazz.getDeclaredFields())
        {
            try
            {
                ConfigValue annotation = field.getAnnotation(ConfigValue.class);
                if (annotation != null)
                {

                    field.setAccessible(true);
                    if (config.contains(annotation.value()))
                    {
                        Object value = config.get(annotation.value());

                        if (value instanceof String)
                        {
                            value = org.bukkit.ChatColor.translateAlternateColorCodes('&', (String) value);
                        }

                        if (value instanceof List)
                        {
                            for (ListIterator<String> iterator = ((List<String>) value).listIterator(); iterator.hasNext(); )
                            {
                                Object next = iterator.next();
                                if (next instanceof String)
                                {
                                    iterator.set(org.bukkit.ChatColor.translateAlternateColorCodes('&', (String) next));
                                }
                            }
                        }
                        field.set(classToLoad, value);
                    }
                    else
                    {
                        config.set(annotation.value(), field.get(classToLoad));
                    }
                }
            } catch (Exception e)
            {
                throw new RuntimeException("Failed to set config value for field '" + field.getName() + "' in " + clazz, e);
            }
        }

        try
        {
            config.save(configFile);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return classToLoad;
    }

    public static <T> T loadLocalConfig(net.md_5.bungee.api.plugin.Plugin plugin, Class<T> clazz)
    {
        return loadLocalConfig(plugin, clazz, "config.yml");
    }

    public static <T> T loadLocalConfig(net.md_5.bungee.api.plugin.Plugin plugin, Class<T> clazz, String fileName)
    {
        T classToLoad = null;
        try
        {
            classToLoad = clazz.newInstance();

            if (classToLoad == null)
            {
                throw new IllegalArgumentException("class cannot be null");
            }
        } catch (InstantiationException | IllegalAccessException e1)
        {
            e1.printStackTrace();
        }

        File configFile = new File(plugin.getDataFolder(), fileName);

        if (!configFile.exists())
        {
            try
            {
                BungeeCord.getInstance().getLogger().log(Level.INFO, "Creating file: " + configFile.getName());
                configFile.getParentFile().mkdirs();

                InputStream in = plugin.getResourceAsStream(configFile.getName());

                if (in != null)
                {
                    FileOutputStream localFileOutputStream = null;

                    localFileOutputStream = new FileOutputStream(configFile);
                    byte[] arrayOfByte = new byte[1024];
                    int i;
                    while ((i = in.read(arrayOfByte)) > 0)
                    {
                        localFileOutputStream.write(arrayOfByte, 0, i);
                    }
                    localFileOutputStream.close();
                }
                else
                {
                    configFile.createNewFile();
                }

            } catch (IOException ex)
            {
                BungeeCord.getInstance().getLogger().log(Level.SEVERE, "Could not create file: " + configFile.getName());
            }
        }

        net.md_5.bungee.config.Configuration config = null;
        try
        {
            config = ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).load(configFile);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        for (Field field : clazz.getDeclaredFields())
        {
            try
            {
                ConfigValue annotation = field.getAnnotation(ConfigValue.class);
                if (annotation != null)
                {

                    field.setAccessible(true);
                    if (config.get(annotation.value()) != null)
                    {
                        Object value = config.get(annotation.value());

                        if (value instanceof String)
                        {
                            value = net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', (String) value);
                        }

                        if (value instanceof List)
                        {
                            for (ListIterator<String> iterator = ((List<String>) value).listIterator(); iterator.hasNext(); )
                            {
                                Object next = iterator.next();
                                if (next instanceof String)
                                {
                                    iterator.set(net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', (String) next));
                                }
                            }
                        }
                        field.set(classToLoad, value);
                    }
                    else
                    {
                        config.set(annotation.value(), field.get(classToLoad));
                    }
                }
            } catch (Exception e)
            {
                throw new RuntimeException("Failed to set config value for field '" + field.getName() + "' in " + clazz, e);
            }
        }

        try
        {
            Files.createParentDirs(configFile);
            File temporaryFile = File.createTempFile(configFile.getName(), null, configFile.getParentFile());
            temporaryFile.deleteOnExit();
            ConfigurationProvider.getProvider(net.md_5.bungee.config.YamlConfiguration.class).save(config, temporaryFile);
            configFile.delete();
            temporaryFile.renameTo(configFile);
            temporaryFile.delete();
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        return classToLoad;
    }
}


