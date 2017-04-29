package com.ehaqui.lib.packet.titles;

import com.ehaqui.lib.message.Message;
import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.EnumTitleAction;
import net.minecraft.server.v1_8_R1.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Titles1_8R1 implements Titles
{

    @Override
    public void sendTitle(Player player, String text, int fadein, int displaytime, int fadeout)
    {
        text = Message.colorize(text);
        fadein = 20 * fadein;
        displaytime = 20 * displaytime;
        fadeout = 20 * fadeout;

        PacketPlayOutTitle title = new PacketPlayOutTitle(EnumTitleAction.TITLE,
                ChatSerializer.a("{\"text\":\"" + text + "\"}"), fadein, displaytime, fadeout);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
    }

    @Override
    public void sendSubTitle(Player player, String text, int fadein, int displaytime, int fadeout)
    {
        text = Message.colorize(text);
        fadein = 20 * fadein;
        displaytime = 20 * displaytime;
        fadeout = 20 * fadeout;

        PacketPlayOutTitle subtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE,
                ChatSerializer.a("{\"text\":\"" + text + "\"}"), fadein, displaytime, fadeout);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(subtitle);
    }

    @Override
    public void removeTitle(Player player)
    {
        PacketPlayOutTitle clear = new PacketPlayOutTitle(EnumTitleAction.CLEAR,
                ChatSerializer.a("{\"text\":\"Clear\"}"), 20, 40, 20);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(clear);
    }

    @Override
    public void resetTitle(Player player)
    {
        PacketPlayOutTitle reset = new PacketPlayOutTitle(EnumTitleAction.RESET,
                ChatSerializer.a("{\"text\":\"Reset\"}"), 20, 40, 20);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(reset);
    }

}
