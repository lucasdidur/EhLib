package com.ehaqui.lib.util.serialization;

import com.ehaqui.lib.util.json.JSONException;
import com.ehaqui.lib.util.json.JSONObject;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.entity.Wolf;


/**
 * A class to help with the serialization of Wolves. <br/>
 * <br/>
 * This serialization class supports optional serialization.<br/>
 * TacoSerialization will create a folder in your server plugins directory (wherever that may be) called 'TacoSerialization'. Inside the folder will be a config.yml file. Various
 * values can be turned off to prevent some keys from being generated.
 *
 * @author KILL3RTACO
 * @since TacoSerialization 1.1
 */
public class WolfSerialization
{

    protected WolfSerialization()
    {
    }

    /**
     * Serialize a Wolf
     *
     * @param wolf
     *            The Wolf to serialize
     * @return The serialized Wolf
     */
    public static JSONObject serializeWolf(Wolf wolf)
    {
        try
        {
            JSONObject root = LivingEntitySerialization.serializeEntity(wolf);
            root.put("collar-color", ColorSerialization.serializeColor(wolf.getCollarColor().getColor()));
            return root;
        } catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Serialize a wolf as a string
     *
     * @param wolf
     *            The wolf to serialize
     * @return The serialization string
     */
    public static String serializeWolfAsString(Wolf wolf)
    {
        return serializeWolfAsString(wolf, false);
    }

    /**
     * Serialize a wolf as a string
     *
     * @param wolf
     *            The wolf to serialize
     * @param pretty
     *            Whether the resulting string should be 'pretty' or not
     * @return The serialization string
     */
    public static String serializeWolfAsString(Wolf wolf, boolean pretty)
    {
        return serializeWolfAsString(wolf, pretty, 5);
    }

    /**
     * Serialize a wolf as a string
     *
     * @param wolf
     *            The wolf to serialize
     * @param pretty
     *            Whether the resulting string should be 'pretty' or not
     * @param indentFactor
     *            The amount of spaces in a tab
     * @return The serialization string
     */
    public static String serializeWolfAsString(Wolf wolf, boolean pretty, int indentFactor)
    {
        try
        {
            if (pretty)
            {
                return serializeWolf(wolf).toString(indentFactor);
            }
            else
            {
                return serializeWolf(wolf).toString();
            }
        } catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Spawn a wolf in a desired location with desired stats
     *
     * @param location
     *            Where to spawn the wolf
     * @param stats
     *            The desired stats
     * @return The wolf spawned
     */
    public static Wolf spawnWolf(Location location, String stats)
    {
        try
        {
            return spawnWolf(location, new JSONObject(stats));
        } catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Spawn a wolf in a desired location with desired stats
     *
     * @param location
     *            Where to spawn the wolf
     * @param stats
     *            The desired stats
     * @return The wolf spawned
     */
    public static Wolf spawnWolf(Location location, JSONObject stats)
    {
        try
        {
            Wolf wolf = (Wolf) LivingEntitySerialization.spawnEntity(location, stats);
            wolf.setCollarColor(DyeColor.getByColor(ColorSerialization.getColor(stats.getString("collar-color"))));
            return wolf;
        } catch (JSONException e)
        {
            e.printStackTrace();
            return null;
        }
    }

}
