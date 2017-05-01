package com.ehaqui.ehlib.bukkit;

import com.ehaqui.ehlib.Settings;
import com.ehaqui.lib.config.Configuration;
import com.ehaqui.lib.config.model.FlagPlayer;
import com.ehaqui.lib.config.model.FlagPlugin;
import com.ehaqui.lib.config.model.Players;
import com.ehaqui.lib.database.Database;
import com.ehaqui.lib.interfaces.Library;
import com.ehaqui.lib.message.Message;
import com.ehaqui.lib.packet.PacketLib;
import com.ehaqui.lib.vault.Vault;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class EhLibSpigot extends JavaPlugin
{
    @Getter
    private String serverVersion = getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];

    @Getter
    private static EhLibSpigot instance;

    @Getter
    private static Database globalDatabase;
    @Getter
    private static Database localDatabase;

    @Getter
    private static String serverName = Bukkit.getServerName();

    @Getter
    private static Configuration configg;

    @Getter
    private static String prefix = "[EhLib] ";

    private static List<Library> libs = new ArrayList<>();

    @Override
    public void onEnable()
    {
        instance = this;

        Configuration.loadLocalConfig(this, Settings.class);

        setupLibs();
        loadLibs();
        PacketLib.setupPackets();

        setupGlobalDatabase();
        setupLocalDatabase();

        configg = new Configuration(this, false, globalDatabase);
        configg.setupDatabase();
    }

    private void setupLibs()
    {
        addLib(new Vault());
    }

    public void loadLibs()
    {
        for (Library lib : libs)
        {
            lib.load();
        }
    }

    public static void addLib(Library library)
    {
        libs.add(library);
    }

    private boolean setupGlobalDatabase()
    {

        getLogger().info("Connecting to Global Database");

        globalDatabase = new Database(getLogger(), Settings.globalHost, null, Settings.globalDatabase, Settings.globalUser, Settings.globalPassword);

        try
        {
            globalDatabase.setup();
        } catch (Exception e)
        {
            return false;
        }

        if (globalDatabase.getConnection() != null)
        {
            globalDatabase.createTable(FlagPlayer.class);
            globalDatabase.createTable(FlagPlugin.class);
            globalDatabase.createTable(Players.class);

            return true;
        }

        return false;
    }


    private boolean setupLocalDatabase()
    {
        getLogger().info("Connecting to Local Database");

        localDatabase = new Database(getLogger(), Settings.localHost, null, Settings.localDatabase, Settings.localUser, Settings.localPassword);

        try
        {
            localDatabase.setup();
        } catch (Exception e)
        {
            return false;
        }

        if (localDatabase.getConnection() != null)
        {
            localDatabase.createTable(FlagPlayer.class);
            localDatabase.createTable(FlagPlugin.class);
            localDatabase.createTable(Players.class);

            return true;
        }

        return false;
    }

    public static void sendConsoleMessage(String message)
    {
       getInstance().getServer().getConsoleSender().sendMessage(Message.colorize(getPrefix() + message));
    }
}
