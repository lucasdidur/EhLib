package com.ehaqui.lib.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ehaqui.lib.command.dynamic.SubCommandFinder;
import com.ehaqui.lib.message.Message;
import com.ehaqui.lib.util.text.TextUtils;


/**
 * Handle commands dynamically with sub-command registration.
 * 
 * @author James Crasta
 * 
 */
public class MondoCommand implements CommandExecutor, SubHandler
{
    private static final FormatConfig BASE_FORMAT = new FormatConfig();
    private static final SubHandler fallbackHandler = new FallbackHandler();

    private final Map<String, SubCommand> subcommands = new LinkedHashMap<String, SubCommand>();
    private MainCommand command = null;
    private final FormatConfig formatter;

    /**
     * Create a new MondoCommand with the base formatting specification.
     */
    public MondoCommand()
    {
        this(BASE_FORMAT);
    }

    /**
     * Create a new MondoCommand, used for dynamic sub command handling.
     * 
     * @param formatter
     *            Configuration on how to format responses.
     */
    public MondoCommand(FormatConfig formatter)
    {
        Validate.notNull(formatter);
        this.formatter = formatter;
    }

    /** Implement onCommand so this can be registered as a CommandExecutor */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args)
    {
        Player player = null;
        if (sender instanceof Player)
        {
            player = (Player) sender;
            commandLabel = "/" + commandLabel;
        }
        List<String> callArgs = new ArrayList<String>(Arrays.asList(args));

        handleRawCommand(sender, player, commandLabel, callArgs);

        return true;
    }

    /** Implement the SubHandler interface so we can do sub-sub commands and such. */
    @Override
    public void handle(CallInfo call) throws MondoFailure
    {
        String commandLabel = call.getBaseCommand() + " " + call.getSubCommand().getName();
        handleRawCommand(call.getSender(), call.getPlayer(), commandLabel, call.getArgs());
    }

    /**
     * Handle a command, dispatching to the appropriate listeners.
     * 
     * @param sender
     *            A CommandSender who is the person or console who sent this command.
     * @param player
     *            A Player object (can be null)
     * @param commandLabel
     *            The current alias this command is running as
     * @param args
     *            The arguments that were passed to this command.
     */
    private void handleRawCommand(CommandSender sender, Player player, String commandLabel, List<String> args)
    {
        if (command != null)
        {
            if (!command.checkPermission(sender))
            {
                Message.send(Message.Type.ACTIONBAR, sender, formatter.getPermissionWarning());
                return;
            }

            if (args.size() < command.getMinArgs() && command.getMinArgs() != 0)
            {
                Message.send(sender, formatter.getTooFewArgs());
                String usageFormat = formatter.getUsageHeading() + "&a%s &d%s";
                Message.send(sender, String.format(usageFormat, commandLabel, command.getUsage()));
                return;
            }

            if ((args.size() - 1) > command.getMaxArgs() && command.getMaxArgs() != -1)
            {
                Message.send(sender, formatter.getTooManyArgs());
                String usageFormat = formatter.getUsageHeading() + "{GREEN}%s &d%s";
                Message.send(sender, String.format(usageFormat, commandLabel, command.getUsage()));
                return;
            }

            CallInfo call = new CallInfo(sender, player, commandLabel, command, args, formatter);

            try
            {
                command.getHandler().handle(call);
            } catch (MondoFailure e)
            {
                Message.send(Message.Type.ACTIONBAR, call.getPlayer(), "&c&l" + e.getMessage());
            }
        }
        else
        {
            if (args.size() == 0)
            {
                showUsage(sender, player, commandLabel);
                return;
            }

            String subcommandName = "";
            int i;

            for (i = args.size(); i >= 0; i--)
            {
                subcommandName = TextUtils.join(" ", args.subList(0, i)).trim();

                if (subcommands.containsKey(subcommandName))
                    break;
            }
            args.removeAll(Arrays.asList(subcommandName.split(" ")));

            SubCommand sub = subcommands.get(subcommandName);

            if (sub == null)
            {
                showUsage(sender, player, commandLabel);
                return;
            }

            if (!sub.checkPermission(sender))
            {
                Message.send(Message.Type.ACTIONBAR, sender, formatter.getPermissionWarning());
                return;
            }

            if (args.size() < sub.getMinArgs())
            {
                Message.send(sender, formatter.getTooFewArgs());
                String usageFormat = formatter.getUsageHeading() + "&a%s %s &d%s";
                Message.send(sender, String.format(usageFormat, commandLabel, sub.getName(), sub.getUsage()));
                return;
            }

            if (args.size() > sub.getMaxArgs() && sub.getMaxArgs() != -1)
            {
                Message.send(sender, formatter.getTooManyArgs());
                String usageFormat = formatter.getUsageHeading() + "{GREEN}%s %s &d%s";
                Message.send(sender, String.format(usageFormat, commandLabel, sub.getName(), sub.getUsage()));
                return;
            }

            List<String> callArgs = new ArrayList<>(args);
            CallInfo call = new CallInfo(sender, player, commandLabel, sub, callArgs, formatter);

            try
            {
                sub.getHandler().handle(call);
            } catch (MondoFailure e)
            {
                Message.send(Message.Type.ACTIONBAR, call.getPlayer(), "&c&l" + e.getMessage());
            }
        }

        return;
    }

    /**
     * Show the usage for this command.
     * 
     * @param sender
     *            A CommandSender who is requesting the usage.
     * @param player
     *            A Player object (can be null)
     * @param commandLabel
     *            The current command label.
     * @param slash
     *            An empty string if there should be a slash prefix, a slash otherwise.
     */
    private void showUsage(CommandSender sender, Player player, String commandLabel)
    {
        String headerFormat = formatter.getUsageHeading() + "%s" + formatter.getUsageCommandSuffix();
        Message.send(sender, String.format(headerFormat, commandLabel));

        for (SubCommand sub : availableCommands(sender, player))
        {
            formatter.writeUsageLine(sender, commandLabel, sub);
        }
    }

    /**
     * Add a sub-command to this MondoCommand.
     * 
     * @param name
     *            The name of this sub-command.
     * @param permission
     *            The permission string of a permission to check for this command.
     * @return a new SubCommand.
     */
    public SubCommand addSub(String name, String permission)
    {
        SubCommand cmd = new SubCommand(name, permission).setHandler(fallbackHandler);
        subcommands.put(name.toLowerCase(), cmd);
        return cmd;
    }

    /**
     * Add a sub-command to this MondoCommand.
     * 
     * @param name
     *            the name of this sub-command.
     * @return a new SubCommand.
     */
    public SubCommand addSub(String name)
    {
        return addSub(name, null);
    }

    private List<SubCommand> availableCommands(CommandSender sender, Player player)
    {
        ArrayList<SubCommand> items = new ArrayList<SubCommand>();
        boolean has_player = (player != null);
        for (SubCommand sub : subcommands.values())
        {
            if ((has_player || sub.isConsoleAllowed()) && sub.checkPermission(sender))
            {
                items.add(sub);
            }
        }
        return items;
    }

    /**
     * Add a sub-command to this MondoCommand.
     * 
     * @param permission
     *            The permission string of a permission to check for this command.
     * @return a new SubCommand.
     */
    public MainCommand add(String permission)
    {
        MainCommand cmd = new MainCommand(permission).setHandler(fallbackHandler);
        command = cmd;
        return cmd;
    }

    /**
     * Add a command to this MondoCommand.
     * 
     * @return a new SubCommand.
     */
    public MainCommand add()
    {
        MainCommand cmd = add(null);
        command = cmd;
        return cmd;
    }

    public void autoRegisterFrom(Object handler)
    {
        new SubCommandFinder(this).registerMethods(handler);
    }

    public List<SubCommand> listCommands()
    {
        return new ArrayList<SubCommand>(subcommands.values());
    }

}

final class FallbackHandler implements SubHandler
{

    @Override
    public void handle(CallInfo call) throws MondoFailure
    {
        throw new MondoFailure("This SubHandler does not have an appropriate handler registered.");
    }
}
