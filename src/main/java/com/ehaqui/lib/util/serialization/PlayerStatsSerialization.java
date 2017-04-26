package com.ehaqui.lib.util.serialization;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.ehaqui.lib.util.json.JSONException;
import com.ehaqui.lib.util.json.JSONObject;


/**
 * A class to help with the serialization of player stats, like exp level and health. <br/>
 * <br/>
 * This serialization class supports optional serialization.<br/>
 * TacoSerialization will create a folder in your server plugins directory (wherever that may be) called 'TacoSerialization'. Inside the folder will be a config.yml file. Various
 * values can be turned off to prevent some keys from being generated.
 *
 * @author KILL3RTACO
 * @since TacoSerialization 1.0
 */
public class PlayerStatsSerialization
{

    protected PlayerStatsSerialization()
    {
    }

    /**
     * Serialize a player's stats
     *
     * @param player
     *            The player whose stats to serialize
     * @return The serialized stats
     */
    public static JSONObject serializePlayerStats(Player player)
    {
        try
        {
            JSONObject root = new JSONObject();
            root.put("can-fly", player.getAllowFlight());
            root.put("display-name", player.getDisplayName());
            root.put("exhaustion", player.getExhaustion());
            root.put("exp", player.getExp());
            root.put("flying", player.isFlying());
            root.put("food", player.getFoodLevel());
            root.put("gamemode", player.getGameMode().name());
            root.put("health", player.getHealthScale());
            root.put("level", player.getLevel());
            root.put("potion-effects", PotionEffectSerialization.serializeEffects(player.getActivePotionEffects()));
            root.put("saturation", player.getSaturation());
            return root;
        } catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Serialize a player's stats as a string
     *
     * @param player
     *            The player whose stats to serialize
     * @return The serialization string
     */
    public static String serializePlayerStatsAsString(Player player)
    {
        return serializePlayerStatsAsString(player, false);
    }

    /**
     * Serialize a player's stats as a string
     *
     * @param player
     *            The player whose stats to serialize
     * @param pretty
     *            Whether the resulting string should be 'pretty' or not
     * @return The serialization string
     */
    public static String serializePlayerStatsAsString(Player player, boolean pretty)
    {
        return serializePlayerStatsAsString(player, pretty, 5);
    }

    /**
     * Serialize a player's stats as a string
     *
     * @param player
     *            The player whose stats to serialize
     * @param pretty
     *            Whether the resulting string should be 'pretty' or not
     * @param indentFactor
     *            The amount of spaces in a tab
     * @return The serialization string
     */
    public static String serializePlayerStatsAsString(Player player, boolean pretty, int indentFactor)
    {
        try
        {
            if (pretty)
            {
                return serializePlayerStats(player).toString(indentFactor);
            }
            else
            {
                return serializePlayerStats(player).toString();
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Apply stats to a player
     *
     * @param player
     *            The player to affect
     * @param stats
     *            The stats to apply
     */
    public static void applyPlayerStats(Player player, String stats)
    {
        try
        {
            applyPlayerStats(player, new JSONObject(stats));
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Apply stats to a player
     *
     * @param player
     *            The player to affect
     * @param stats
     *            The stats to apply
     */
    public static void applyPlayerStats(Player player, JSONObject stats)
    {
        try
        {
            if (stats.has("can-fly"))
            {
                player.setAllowFlight(stats.getBoolean("can-fly"));
            }
            if (stats.has("display-name"))
            {
                player.setDisplayName(stats.getString("display-name"));
            }
            if (stats.has("exhaustion"))
            {
                player.setExhaustion((float) stats.getDouble("exhaustion"));
            }
            if (stats.has("exp"))
            {
                player.setExp((float) stats.getDouble("exp"));
            }
            if (stats.has("flying"))
            {
                player.setFlying(stats.getBoolean("flying"));
            }
            if (stats.has("food"))
            {
                player.setFoodLevel(stats.getInt("food"));
            }
            if (stats.has("health"))
            {
                player.setHealth(stats.getDouble("health"));
            }
            if (stats.has("gamemode"))
            {
                player.setGameMode(GameMode.valueOf(stats.getString("gamemode")));
            }
            if (stats.has("level"))
            {
                player.setLevel(stats.getInt("level"));
            }
            if (stats.has("potion-effects"))
            {
                PotionEffectSerialization.setPotionEffects(stats.getString("potion-effects"), player);
            }
            if (stats.has("saturation"))
            {
                player.setSaturation((float) stats.getDouble("saturation"));
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

}