package com.ehaqui.lib.packet.tablist;

import org.bukkit.entity.Player;

public interface TabList
{

    void sendCustomTabList(Player player, String headerText, String footerText);

    void sendCustomTabListToAll(String headerText, String footerText);

}
