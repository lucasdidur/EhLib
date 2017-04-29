package com.ehaqui.lib.packet.jsonstuff.jsonmsgs;

import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;

public class JSONMessages1_11R1 implements JSONMessages
{

    public void sendJSONMessage(CommandSender sender, String JSONString)
    {
        IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a(JSONString);
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(comp);
        ((CraftPlayer) sender).getHandle().playerConnection.sendPacket(packetPlayOutChat);
    }

}
