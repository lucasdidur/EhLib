package com.ehaqui.lib.packet.jsonstuff.jsonmsgs;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;

public class JSONMessages1_7R4 implements JSONMessages
{

    public void sendJSONMessage(CommandSender sender, String JSONString)
    {
        IChatBaseComponent comp = ChatSerializer.a(JSONString);
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(comp);
        ((CraftPlayer) sender).getHandle().playerConnection.sendPacket(packetPlayOutChat);
    }

}
