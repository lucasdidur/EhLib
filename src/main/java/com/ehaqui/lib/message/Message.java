package com.ehaqui.lib.message;

import net.minecraft.server.v1_11_R1.*;
import net.minecraft.server.v1_11_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_11_R1.PacketPlayOutTitle.EnumTitleAction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;



/**
 * Created on 30/11/2014.
 *
 * @author OliverVsCreeper
 */
public class Message
{

    private static List<Player> recipients = new ArrayList<Player>();

    private static int titleIn = 2;
    private static int titleOut = 15;
    private static int titleStay = 60;

    public Message(Player player)
    {
        addRecipient(player);
    }

    /**
     * Adds a recipient to the recipient listignore
     *
     * @param player
     *            Recipient to add
     * @return Its own instance of Message, to allow easy method chaining in creation
     */
    public Message addRecipient(Player player)
    {
        recipients.add(player);
        return this;
    }

    /**
     * Adds a recipient to the recipient listignore
     *
     * @param player
     *            Recipient to add
     * @return Its own instance of Message, to allow easy method chaining in creation
     */
    public Message addRecipient(String player)
    {
        recipients.add(Bukkit.getPlayerExact(player));
        return this;
    }

    /**
     * Adds a recipient to the recipient listignore
     *
     * @param players
     *            Recipient to add
     * @return Its own instance of Message, to allow easy method chaining in creation
     */
    public Message addRecipient(List<Player> players)
    {
        for (Player player : players)
        {
            recipients.add(player);
        }
        return this;
    }

    /**
     * Sets the timing packet information to be sent along with Titles and Subtitles upon sending of messages
     *
     * @param in
     *            Fade in time of the title, in ticks
     * @param stay
     *            Time in which the title stays on the screen, in ticks
     * @param out
     *            Fade out time of the title, in ticks
     * @return
     */
    public Message setTimings(int in, int stay, int out)
    {
        titleIn = in;
        titleStay = stay;
        titleOut = out;
        return this;
    }

    public static void send(CommandSender sender, String message)
    {
        send(Type.CHAT, sender, message, null);
    }

    public static void send(Player player, String message)
    {
        send(Type.CHAT, player, message, null);
    }

    public static void send(Player player, String format, Object... args)
    {
        send(Type.CHAT, player, String.format(format, args), null);
    }

    public static void send(Type type, Player player, String message)
    {
        send(type, player, message, "");
    }

    public static void send(Type type, CommandSender sender, String message)
    {
        if (sender instanceof Player)
        {
            send(type, (Player) sender, message, "");
        } else
        {
            send(sender, message);
        }
    }

    public void send(String message)
    {
        for (Player player : recipients)
        {
            send(Type.CHAT, player, message, "");
        }
    }

    public void send(Type type, String message)
    {
        for (Player player : recipients)
        {
            send(type, player, message, "");
        }
    }

    public void send(Type type, String message, String subtitle)
    {
        for (Player player : recipients)
        {
            send(type, player, message, subtitle);
        }
    }

    public static void send(Type type, CommandSender sender, String text, String subtitle)
    {
        String message = colorize(text.replace("%p", sender.getName()));

        switch (type)
        {
        case CHAT:
            sender.sendMessage(message);
            break;
        case ACTIONBAR:
            IChatBaseComponent icbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
            PacketPlayOutChat bar = new PacketPlayOutChat(icbc, (byte) 2);
            ((CraftPlayer) sender).getHandle().playerConnection.sendPacket(bar);
            break;
        case TITLE:
            PlayerConnection titleConnection = ((CraftPlayer) sender).getHandle().playerConnection;
            PacketPlayOutTitle titlePacketPlayOutTimes = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, titleIn, titleStay, titleOut);
            titleConnection.sendPacket(titlePacketPlayOutTimes);

            IChatBaseComponent titleMain = ChatSerializer.a("{\"text\": \"" + message + "\"}");
            PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleMain);
            titleConnection.sendPacket(packetPlayOutTitle);
            break;
        case SUBTITLE:
            PlayerConnection subtitleConnection = ((CraftPlayer) sender).getHandle().playerConnection;
            PacketPlayOutTitle subtitlePacketPlayOutTimes = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, titleIn, titleStay, titleOut);
            subtitleConnection.sendPacket(subtitlePacketPlayOutTimes);

            IChatBaseComponent subtitleSub = ChatSerializer.a("{\"text\": \"" + message + "\"}");
            PacketPlayOutTitle subtitlePacketPlayOutSubTitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitleSub);
            subtitleConnection.sendPacket(subtitlePacketPlayOutSubTitle);
            break;

        case TITLEFULL:
            subtitle = colorize(subtitle.replace("%p", sender.getName()));

            PlayerConnection titleConnection2 = ((CraftPlayer) sender).getHandle().playerConnection;
            PacketPlayOutTitle titlePacketPlayOutTimes2 = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, titleIn, titleStay, titleOut);
            titleConnection2.sendPacket(titlePacketPlayOutTimes2);

            IChatBaseComponent titleMain2 = ChatSerializer.a("{\"text\": \"" + message + "\"}");
            PacketPlayOutTitle packetPlayOutTitle2 = new PacketPlayOutTitle(EnumTitleAction.TITLE, titleMain2);
            titleConnection2.sendPacket(packetPlayOutTitle2);

            PlayerConnection subtitleConnection2 = ((CraftPlayer) sender).getHandle().playerConnection;
            PacketPlayOutTitle subtitlePacketPlayOutTimes2 = new PacketPlayOutTitle(EnumTitleAction.TIMES, null, titleIn, titleStay, titleOut);
            subtitleConnection2.sendPacket(subtitlePacketPlayOutTimes2);

            IChatBaseComponent subtitleSub2 = ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
            PacketPlayOutTitle subtitlePacketPlayOutSubTitle2 = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, subtitleSub2);
            subtitleConnection2.sendPacket(subtitlePacketPlayOutSubTitle2);
            break;

        case JSON:

            if (!(sender instanceof Player))
            {
                sender.sendMessage(message);
                break;
            }

            Packet<PacketListenerPlayOut> packet = new PacketPlayOutChat(ChatSerializer.a(text));
            ((CraftPlayer) ((Player) sender)).getHandle().playerConnection.sendPacket(packet);

            break;
        }

    }

    public static String colorize(String text)
    {
        text = ChatColor.translateAlternateColorCodes('&', text);

        for (ChatColor color : ChatColor.values())
        {
            text = text.replaceAll("\\{" + color.name().toLowerCase() + "\\}", color.toString());
            text = text.replaceAll("\\{" + color.name().toUpperCase() + "\\}", color.toString());
        }

        text = text.replaceAll("\\{HEADER\\\\}", ChatColor.GOLD.toString());
        text = text.replaceAll("\\{USAGE\\}", ChatColor.LIGHT_PURPLE.toString());
        text = text.replaceAll("\\{WARNING\\}", ChatColor.DARK_RED.toString());
        text = text.replaceAll("\\{ERROR\\}", ChatColor.RED.toString());
        text = text.replaceAll("\\{NOUN\\}", ChatColor.AQUA.toString());
        text = text.replaceAll("\\{VERB\\}", ChatColor.GRAY.toString());
        text = text.replaceAll("\\{MCMD\\}", ChatColor.GREEN.toString());
        text = text.replaceAll("\\{DESCRIPTION\\}", ChatColor.BLUE.toString());

        return text;
    }

    public enum Prefix
    {
        BLANK("%m"), INFO("&8[&2Info&8] &7%m"), WARNING("&8[&cAviso&8] &c%m"), ERROR("&4[&cERRO&8] &c%m");

        private String prefix;

        Prefix(String prefix)
        {
            this.prefix = prefix;
        }

        public String getPrefix()
        {
            return prefix;
        }
    }

    public enum Type
    {
        CHAT, ACTIONBAR, TITLE, SUBTITLE, TITLEFULL, JSON
    }

}
