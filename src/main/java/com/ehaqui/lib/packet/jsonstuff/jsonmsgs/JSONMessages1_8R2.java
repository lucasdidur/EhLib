package com.ehaqui.lib.packet.jsonstuff.jsonmsgs;

import net.minecraft.server.v1_8_R2.IChatBaseComponent;
import net.minecraft.server.v1_8_R2.PacketPlayOutChat;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftPlayer;

public class JSONMessages1_8R2 implements JSONMessages
{

    public void sendJSONMessage(CommandSender sender, String JSONString)
    {
        IChatBaseComponent comp = IChatBaseComponent.ChatSerializer.a(JSONString);
        PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(comp);
        ((CraftPlayer) sender).getHandle().playerConnection.sendPacket(packetPlayOutChat);
    }

}
