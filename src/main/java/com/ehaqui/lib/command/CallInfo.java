package com.ehaqui.lib.command;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ehaqui.lib.message.Message;
import com.ehaqui.lib.message.Message.Type;


/**
 * Represents all the arguments that go along with a call.
 * 
 * @author James Crasta
 * 
 */
public class CallInfo
{
    private CommandSender sender = null;
    private Player player = null;
    private String baseCommand = null;
    private MainCommand command = null;
    private SubCommand subCommand = null;
    private String replyPrefix = null;

    private List<String> args = null;

    /**
     * Create a new CallInfo representing one command invocation.
     * 
     * @param sender
     *            The CommandSender who invoked this (can be a console)
     * @param player
     *            The Player who invoked this (will be null if a console)
     * @param baseCommand
     *            The label of the base command being executed (for reference)
     * @param subCommand
     *            The SubCommand we're executing.
     * @param args
     *            The command arguments.
     * @param formatter
     *            The formatter being used by our MondoCommand.
     */
    public CallInfo(CommandSender sender, Player player, String baseCommand, SubCommand subCommand, List<String> args, FormatConfig formatter)
    {
        Validate.notNull(sender);
        Validate.notEmpty(baseCommand);
        Validate.notNull(subCommand);
        this.sender = sender;
        this.player = player;
        this.args = args;
        this.baseCommand = baseCommand;
        this.subCommand = subCommand;
        this.replyPrefix = ChatColor.translateAlternateColorCodes('&', formatter.getReplyPrefix());
    }

    /**
     * Create a new CallInfo representing one command invocation.
     * 
     * @param sender
     *            The CommandSender who invoked this (can be a console)
     * @param player
     *            The Player who invoked this (will be null if a console)
     * @param baseCommand
     *            The label of the base command being executed (for reference)
     * @param subCommand
     *            The SubCommand we're executing.
     * @param args
     *            The command arguments.
     * @param formatter
     *            The formatter being used by our MondoCommand.
     */
    public CallInfo(CommandSender sender, Player player, String baseCommand, MainCommand command, List<String> args, FormatConfig formatter)
    {
        Validate.notNull(sender);
        Validate.notEmpty(baseCommand);
        Validate.notNull(command);
        this.sender = sender;
        this.player = player;
        this.args = args;
        this.baseCommand = baseCommand;
        this.command = command;
        this.replyPrefix = ChatColor.translateAlternateColorCodes('&', formatter.getReplyPrefix());
    }

    /**
     * Get the player who invoked this. Can be null if running at the console.
     * 
     * @return a Player, or null if this is a console command
     */
    public Player getPlayer()
    {
        return player;
    }

    /**
     * Get the CommandSender who invoked this.
     * 
     * @return a CommandSender.
     */
    public CommandSender getSender()
    {
        return sender;
    }

    /**
     * Get a specific argument.
     * 
     * @param index
     *            The argument number.
     * @return The specific argument requested.
     */
    public String getArg(int index)
    {
        index--;

        if (args.size() > index && !args.isEmpty())
            return this.args.get(index);
        else
            return null;
    }

    /**
     * Get a specific argument.
     * 
     * @param index
     *            The argument number.
     * @param def
     *            The default string
     * @return The specific argument requested.
     */
    public String getArg(int index, String def)
    {
        index--;

        if (args.size() > index && !args.isEmpty())
            return this.args.get(index);
        else
            return def;
    }

    /**
     * Get the whole listignore of command arguments.
     * 
     * @return List of arguments.
     */
    public List<String> getArgs()
    {
        return this.args;
    }

    /**
     * Get an argument coerced into an int.
     * 
     * @param index
     *            the location in the arguments array.
     * @return The argumnt
     */
    public int getIntArg(int index)
    {
        return Integer.parseInt(getArg(index));
    }

    /**
     * Get an argument coerced into an int.
     * 
     * @param index
     *            the location in the arguments array.
     * @param def
     *            The default int
     * @return The argumnt
     */
    public int getIntArg(int index, int def)
    {
        try
        {
            return Integer.parseInt(getArg(index));
        } catch (Exception e)
        {
            return def;
        }
    }

    /**
     * Get all the arguments after the specified index joined into a single string.
     * 
     * This is useful if one of your last arguments is a free-form text entry (like for a chat message, or editing a sign/book text)
     * 
     * @param index
     *            The index to start at (inclusive)
     * @return A single string containing all the arguments till the end
     */
    public String getJoinedArgsAfter(int index)
    {
        index--;

        return StringUtils.join(args.subList(index, args.size()), " ");
    }

    /**
     * How many arguments we got.
     * 
     * @return Number of arguments
     */
    public int numArgs()
    {
        return this.args.size();
    }

    /**
     * Get the base command which was called for this sub-command call.
     * 
     * @return A base command string.
     */
    public String getBaseCommand()
    {
        return baseCommand;
    }

    /**
     * Get the SubCommand that invoked this call.
     * 
     * @return a SubCommand.
     */
    public SubCommand getSubCommand()
    {
        return subCommand;
    }

    /**
     * Get the Command that invoked this call.
     * 
     * @return a SubCommand.
     */
    public MainCommand getCommand()
    {
        return command;
    }

    /**
     * Respond to the call, interpolating colors and variables.
     * 
     * @param text
     *            A string of text. See {@link ChatMagic} documentation for more info.
     * @see ChatMagic#colorize
     */
    public void reply(Type type, String text, String subtitle)
    {
        Message.send(type, player, text, subtitle);
    }

    public void reply(Type type, String text)
    {
        Message.send(type, player, text);
    }

    /**
     * Respond to the call, interpolating colors and variables.
     * 
     * @param text
     *            A string of text. See {@link ChatMagic} documentation for more info.
     * @see ChatMagic#colorize
     */
    public void reply(String text)
    {
        reply(false, text);
    }

    /**
     * Respond to the call, interpolating colors and variables.
     * 
     * @param template
     *            A string template. See {@link ChatMagic} documentation for more info.
     * @param args
     *            Zero or more arguments to interpolate the template.
     * @see ChatMagic#colorize
     */
    public void reply(String template, Object... args)
    {
        reply(false, template, args);
    }

    /**
     * Re
     * 
     * @param prefix
     *            if True, prefix the message with the formater's prefix.
     * @param template
     *            A string template. See {@link ChatMagic} documentation for more info.
     * @param args
     *            Zero or more arguments to interpolate the template.
     */
    public void reply(boolean prefix, String template, Object... args)
    {
        if (prefix)
        {
            Message.send(sender, String.format(replyPrefix + template, args));
        }
        else
        {
            Message.send(sender, String.format(template, args));
        }
    }
}
