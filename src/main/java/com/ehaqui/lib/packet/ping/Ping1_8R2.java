package com.ehaqui.lib.packet.ping;

import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Ping1_8R2 implements Ping
{

    public int getPing(Player player)
    {
        return ((CraftPlayer) player).getHandle().ping;
    }

}
