package com.ehaqui.lib.packet.ping;

import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Ping1_10R1 implements Ping
{

    public int getPing(Player player)
    {
        return ((CraftPlayer) player).getHandle().ping;
    }

}
