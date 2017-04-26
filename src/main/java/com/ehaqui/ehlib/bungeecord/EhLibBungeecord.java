package com.ehaqui.ehlib.bungeecord;

import com.ehaqui.ehlib.Settings;
import com.ehaqui.lib.config.Configuration;
import com.ehaqui.lib.config.model.FlagPlayer;
import com.ehaqui.lib.config.model.FlagPlugin;
import com.ehaqui.lib.config.model.Players;
import com.ehaqui.lib.database.Database;
import lombok.Getter;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public final class EhLibBungeecord extends Plugin implements Listener
{
    @Getter
    private static Database globalDatabase;
    @Getter
    private static Configuration config;
    @Getter
    private static EhLibBungeecord instance;

    @Override
    public void onEnable()
    {
        instance = this;

        Configuration.loadLocalConfig(this, Settings.class);
        setupDatabase();

        config = new Configuration(this, false, globalDatabase);
        config.setupDatabase();

        getProxy().getPluginManager().registerListener(this, this);
    }

    private boolean setupDatabase()
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
            globalDatabase.createTable(FlagPlugin.class);
            globalDatabase.createTable(Players.class);
            globalDatabase.createTable(FlagPlayer.class);

            return true;
        }

        return false;

    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerJoin(PostLoginEvent event)
    {
        config.updatePlayer(event.getPlayer().getUniqueId(), event.getPlayer().getName());
    }
}
