package com.ehaqui.lib.message;


import com.ehaqui.lib.packet.PacketLib;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


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
                if(sender instanceof Player)
                    PacketLib.getActionBarManager().sendActionbar((Player) sender, message);
                break;
            case TITLE:
                if(sender instanceof Player)
                PacketLib.getTitleManager().sendTitle((Player) sender, message,titleIn, titleStay, titleOut );
                break;
            case SUBTITLE:
                if(sender instanceof Player)
                PacketLib.getTitleManager().sendSubTitle((Player) sender, message,titleIn, titleStay, titleOut );
                break;

            case TITLEFULL:

                subtitle = colorize(subtitle.replace("%p", sender.getName()));

                if(sender instanceof Player)
                {
                    PacketLib.getTitleManager().sendTitle((Player) sender, message, titleIn, titleStay, titleOut);
                    PacketLib.getTitleManager().sendSubTitle((Player) sender, subtitle, titleIn, titleStay, titleOut);
                }
                break;

            case JSON:
                PacketLib.getJSONMessageSender().sendJSONMessage(sender, text);
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
