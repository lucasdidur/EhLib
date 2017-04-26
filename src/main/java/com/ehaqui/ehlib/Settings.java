package com.ehaqui.ehlib;

import com.ehaqui.lib.config.ConfigValue;

/**
 * Created by Lucas on 26/09/2016.
 */
public class Settings
{
    @ConfigValue("database.global.host")
    public static String globalHost = "localhost";
    @ConfigValue("database.global.database")
    public static String globalDatabase = "minecraft";
    @ConfigValue("database.global.user")
    public static String globalUser = "user";
    @ConfigValue("database.global.password")
    public static String globalPassword = "pass";

    @ConfigValue("database.local.host")
    public static String localHost = "localhost";
    @ConfigValue("database.local.database")
    public static String localDatabase = "minecraft";
    @ConfigValue("database.local.user")
    public static String localUser = "user";
    @ConfigValue("database.local.password")
    public static String localPassword = "pass";

}
