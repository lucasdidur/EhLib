package com.ehaqui.lib.packet.jsonstuff.jsonmsgs;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.IChatBaseComponent;
import net.minecraft.server.v1_8_R1.PacketPlayOutChat;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;

public class JSONMessages1_8R1 implements JSONMessages
{

    public void sendJSONMessage(CommandSender sender, String JSONString)
    {
        IChatBaseComponent comp = ChatSerializer.a(JSONString);
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(comp);
        ((CraftPlayer) sender).getHandle().playerConnection.sendPacket(packetPlayOutChat);
    }

}
