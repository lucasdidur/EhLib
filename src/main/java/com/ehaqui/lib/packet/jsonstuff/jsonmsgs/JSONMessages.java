package com.ehaqui.lib.packet.jsonstuff.jsonmsgs;

import org.bukkit.command.CommandSender;

public interface JSONMessages
{
    void sendJSONMessage(CommandSender sender, String JSONString);
}
