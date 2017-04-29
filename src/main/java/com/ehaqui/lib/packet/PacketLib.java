package com.ehaqui.lib.packet;


import com.ehaqui.ehlib.bukkit.EhLibSpigot;
import com.ehaqui.lib.packet.actionbars.*;
import com.ehaqui.lib.packet.bossbar.BossBar1_7R4;
import com.ehaqui.lib.packet.bossbar.BossBar1_8R3;
import com.ehaqui.lib.packet.bossbar.NewerBossBar;
import com.ehaqui.lib.packet.bossbar.OlderBossBar;
import com.ehaqui.lib.packet.holograms.*;
import com.ehaqui.lib.packet.jsonstuff.jsonitems.*;
import com.ehaqui.lib.packet.jsonstuff.jsonmsgs.*;
import com.ehaqui.lib.packet.particles.*;
import com.ehaqui.lib.packet.ping.*;
import com.ehaqui.lib.packet.tablist.*;
import com.ehaqui.lib.packet.titles.*;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import java.util.List;


public class PacketLib
{
    private static EhLibSpigot plugin = EhLibSpigot.getInstance();
    @Getter
    private static Titles titleManager;
    @Getter
    private static ActionBars actionBarManager;
    @Getter
    private static Particles particleManager;
    @Getter
    private static JSONMessages JSONMessageSender;
    @Getter
    private static JSONItems JSONItemGetter;
    @Getter
    private static Ping pingGetter;
    @Getter
    private static TabList tabListManager;

    /**
     * Not something you should mess with unless you need to reload ZotBox's packets.
     */
    public static void setupPackets()
    {
        long startTime = System.currentTimeMillis();

        switch (plugin.getServerVersion())
        {
            case "v1_11_R1":
                titleManager = new Titles1_11R1();
                actionBarManager = new ActionBars1_11R1();
                particleManager = new Particle1_11R1();
                JSONMessageSender = new JSONMessages1_11R1();
                pingGetter = new Ping1_11R1();
                JSONItemGetter = new JSONItems1_11R1();
                tabListManager = new TabList1_11R1();
                plugin.sendConsoleMessage("&e" + plugin.getServerVersion() + " &adetected successfully set up packets. (Took &e" + (System.currentTimeMillis() - startTime) + "ms&a)");
                break;
            case "v1_10_R1":
                titleManager = new Titles1_10R1();
                actionBarManager = new ActionBars1_10R1();
                particleManager = new Particle1_10R1();
                JSONMessageSender = new JSONMessages1_10R1();
                pingGetter = new Ping1_10R1();
                JSONItemGetter = new JSONItems1_10R1();
                tabListManager = new TabList1_10R1();
                plugin.sendConsoleMessage("&e" + plugin.getServerVersion() + " &adetected successfully set up packets. (Took &e" + (System.currentTimeMillis() - startTime) + "ms&a)");
                break;
            case "v1_9_R2":
                titleManager = new Titles1_9R2();
                actionBarManager = new ActionBars1_9R2();
                particleManager = new Particle1_9R2();
                JSONMessageSender = new JSONMessages1_9R2();
                pingGetter = new Ping1_9R2();
                JSONItemGetter = new JSONItems1_9R2();
                tabListManager = new TabList1_9R2();
                plugin.sendConsoleMessage("&e" + plugin.getServerVersion() + " &adetected successfully set up packets. (Took &e" + (System.currentTimeMillis() - startTime) + "ms&a)");
                break;
            case "v1_9_R1":
                titleManager = new Titles1_9R1();
                actionBarManager = new ActionBars1_9R1();
                particleManager = new Particle1_9R1();
                JSONMessageSender = new JSONMessages1_9R1();
                pingGetter = new Ping1_9R1();
                JSONItemGetter = new JSONItems1_9R1();
                tabListManager = new TabList1_9R1();
                plugin.sendConsoleMessage("&e" + plugin.getServerVersion() + " &adetected successfully set up packets. (Took &e" + (System.currentTimeMillis() - startTime) + "ms&a)");
                break;
            case "v1_8_R3":
                titleManager = new Titles1_8R3();
                actionBarManager = new ActionBars1_8R3();
                particleManager = new Particle1_8R3();
                JSONMessageSender = new JSONMessages1_8R3();
                pingGetter = new Ping1_8R3();
                JSONItemGetter = new JSONItems1_8R3();
                tabListManager = new TabList1_8R3();
                plugin.sendConsoleMessage("&e" + plugin.getServerVersion() + " &adetected successfully set up packets. (Took &e" + (System.currentTimeMillis() - startTime) + "ms&a)");
                break;
            case "v1_8_R2":
                titleManager = new Titles1_8R2();
                actionBarManager = new ActionBars1_8R2();
                particleManager = new Particle1_8R2();
                JSONMessageSender = new JSONMessages1_8R2();
                pingGetter = new Ping1_8R2();
                JSONItemGetter = new JSONItems1_8R2();
                tabListManager = new TabList1_8R2();
                plugin.sendConsoleMessage("&e" + plugin.getServerVersion() + " &adetected successfully set up packets. (Took &e" + (System.currentTimeMillis() - startTime) + "ms&a)");
                break;
            case "v1_8_R1":
                titleManager = new Titles1_8R1();
                actionBarManager = new ActionBars1_8R1();
                particleManager = new Particle1_8R1();
                JSONMessageSender = new JSONMessages1_8R1();
                pingGetter = new Ping1_8R1();
                JSONItemGetter = new JSONItems1_8R1();
                tabListManager = new TabList1_8R1();
                plugin.sendConsoleMessage("&e" + plugin.getServerVersion() + " &adetected successfully set up packets. (Took &e" + (System.currentTimeMillis() - startTime) + "ms&a)");
                break;
            case "v1_7_R4":
                JSONMessageSender = new JSONMessages1_7R4();
                pingGetter = new Ping1_7R4();
                JSONItemGetter = new JSONItems1_7R4();
                plugin.sendConsoleMessage("&cSome of the packets in ZotBox have been loaded due to &e1.7_R4 &csupport, " + "but most packets will not load for this version.");
                break;
            default:
                plugin.sendConsoleMessage("&cThere was a issue trying to setup packets for &d" + plugin.getServerVersion() + " &cbecause it is not supported by &bZotLib &c.");
                break;
        }
    }



    /**
     * Allows you to create a packet hologram which you can customize based on the server's version.
     *
     * @param lines      A list of the lines for the hologram to display (include color codes).
     * @param lineSpread How far apart should each line be from each other?
     * @param location   The location where it should be displayed.
     * @return The pre-built hologram object. Use the .create() method to create the packet before showing players.
     */
    public static Hologram createNewHologram(String id, List<String> lines, double lineSpread, Location location)
    {
        switch (plugin.getServerVersion())
        {
            case "v1_11_R1":
                return new Hologram1_11R1(id, lines, lineSpread, location);
            case "v1_10_R1":
                return new Hologram1_10R1(id, lines, lineSpread, location);
            case "v1_9_R2":
                return new Hologram1_9R2(id, lines, lineSpread, location);
            case "v1_9_R1":
                return new Hologram1_9R1(id, lines, lineSpread, location);
            case "v1_8_R3":
                return new Hologram1_8R3(id, lines, lineSpread, location);
            case "v1_8_R2":
                return new Hologram1_8R2(id, lines, lineSpread, location);
            case "v1_8_R1":
                return new Hologram1_8R1(id, lines, lineSpread, location);
            default:
                return null;
        }
    }

    /**
     * Sends an older boss bar for 1.7.10-1.8.9.
     *
     * @param player player to send to.
     * @param text   the text that appears.
     * @return the OlderBossBar object.
     */
    public static OlderBossBar getOlderBossBar(Player player, String text)
    {
        switch (plugin.getServerVersion())
        {
            case "v1_8_R3":
                return new BossBar1_8R3(player, text);
            case "v1_8_R2":
                return new BossBar1_8R3(player, text);
            case "v1_8_R1":
                return new BossBar1_8R3(player, text);
            case "v1_7_R4":
                return new BossBar1_7R4(player, text);
            default:
                return null;
        }
    }

    /**
     * Sends an newer boss bar for 1.9+.
     * @param text the text that appears.
     * @param barColor the color of the boss bar
     * @param barStyle the boss bar style.
     * @param barFlag the flags for the boss bar.
     * @return the NewerBossBar object.
     */
    public static NewerBossBar getNewerBossBar(String text, BarColor barColor, BarStyle barStyle, BarFlag barFlag)
    {
        return new NewerBossBar(text, barColor, barStyle, barFlag);
    }

}
