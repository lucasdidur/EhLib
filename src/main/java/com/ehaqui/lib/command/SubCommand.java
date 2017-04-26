package com.ehaqui.lib.command;

import org.apache.commons.lang.Validate;
import org.bukkit.command.CommandSender;

import com.ehaqui.lib.localize.Localize;


/**
 * Represents a sub-command which can be generated by method chaining.
 * 
 * @author James Crasta
 * 
 */
public final class SubCommand
{
    private final String name;
    private final String permission;
    private boolean allow_console = false;
    private int minArgs = 0;
    private int maxArgs = -1;
    private SubHandler handler = null;
    private String description;
    private String usage = null;

    /**
     * Create a new SubCommand.
     * 
     * @param name
     *            The text of this subcommand.
     * @param permission
     *            The permission to check for this command. If null, don't use permissions.
     */
    public SubCommand(String name, String permission)
    {
        Validate.notEmpty(name);
        this.name = name;
        this.permission = permission;
    }

    /**
     * Allow this command to be used on the console. If this is not set, then the command only works for players.
     */
    public SubCommand allowConsole()
    {
        this.allow_console = true;
        return this;
    }

    /** If true, console access is allowed. */
    public boolean isConsoleAllowed()
    {
        return this.allow_console;
    }

    /** Check what the minimum number of args is */
    public int getMinArgs()
    {
        return minArgs;
    }

    /** Check what the maximum number of args is */
    public int getMaxArgs()
    {
        return maxArgs;
    }

    /**
     * Set the minimum number of arguments this command accepts.
     * 
     * If minArgs is >0, then the handler will not be executed unless this many arguments to the command are present.
     * 
     * @param minArgs
     *            The minimum number of args
     * @return the SubCommand, useful for chaining.
     */
    public SubCommand setMinArgs(int minArgs)
    {
        Validate.isTrue(minArgs >= 0, "minArgs cannot be negative");
        this.minArgs = minArgs;
        return this;
    }

    /**
     * Set the maximum number of arguments this command accepts.
     * 
     * If minArgs is >0, then the handler will not be executed unless this many arguments to the command are present.
     * 
     * @param maxArgs
     *            The maximum number of args
     * @return the SubCommand, useful for chaining.
     */
    public SubCommand setMaxArgs(int maxArgs)
    {
        this.maxArgs = maxArgs;
        return this;
    }

    public SubCommand setArgs(int minArgs, int maxArgs)
    {
        Validate.isTrue(minArgs >= 0, "minArgs cannot be negative");
        this.minArgs = minArgs;
        this.maxArgs = maxArgs;
        return this;
    }

    /**
     * Get the currently set SubHandler.
     * 
     * @return the SubHandler which is called when this command executes.
     */
    public SubHandler getHandler()
    {
        return handler;
    }

    /**
     * Set the SubHandler.
     * 
     * @param handler
     *            A SubHandler which is called when this command executes.
     * @return the SubCommand, useful for chaining.
     */
    public SubCommand setHandler(SubHandler handler)
    {
        Validate.notNull(handler);
        this.handler = handler;
        return this;
    }

    /**
     * Get the command's name.
     * 
     * @return The command's name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the usage string.
     * 
     * @return The usage string.
     */
    public String getUsage()
    {
        return this.usage;
    }

    /**
     * Set this command's usage string.
     * 
     * The usage string describes the parameters to this command in a visual manner, for display to the user.
     * 
     * @param usage
     *            a usage string.
     * @return the SubCommand, useful for chaining.
     */
    public SubCommand setUsage(String usage)
    {
        this.usage = usage;
        return this;
    }

    /**
     * Get the command's description.
     * 
     * @return The command's description.
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Set the command's description.
     * 
     * Command description should be a short blurb (one line) explaining what the command does. Long descriptions will probably wrap poorly. .
     * 
     * @param description
     *            Description text.
     * @return the SubCommand, useful for chaining.
     */
    public SubCommand setDescription(String description)
    {
        this.description = description;
        return this;
    }

    /**
     * Set the command's description.
     * 
     * Command description should be a short blurb (one line) explaining what the command does. Long descriptions will probably wrap poorly. .
     * 
     * @param key
     *            Keyworld of description text.
     * @param locate
     *            Locate for translation
     * @return the SubCommand, useful for chaining.
     */
    public SubCommand setDescription(String key, Localize locate)
    {
        this.description = locate.getS(key, key);
        return this;
    }

    /**
     * Set the command's description.
     * 
     * Command description should be a short blurb (one line) explaining what the command does. Long descriptions will probably wrap poorly. .
     * 
     * @param key
     *            Keyworld of description text.
     * @param def
     *            Default message if not found translation
     * @param locate
     *            Locate for translation
     * @return the SubCommand, useful for chaining.
     */
    public SubCommand setDescription(String key, String def, Localize locate)
    {
        this.description = locate.getS(key, def);
        return this;
    }

    /**
     * Get the permission node this command is required to use.
     * 
     * @return permission node, or null if no permission.
     */
    public String getPermission()
    {
        return permission;
    }

    /**
     * Check if a Player/CommandSender has the requisite permission for this command.
     * 
     * @param sender
     *            a ConsoleSender or Player.
     * @return True if the user has the permission or the command has no permission set, false otherwise.
     */
    public boolean checkPermission(CommandSender sender)
    {
        if (permission == null)
            return true;
        return sender.hasPermission(permission);
    }

}
