package com.ehaqui.lib.gui.anvil;

import org.bukkit.entity.Player;

import lombok.Getter;


@Getter
public class AnvilClickEvent
{
    private AnvilSlot slot;

    private String text;
    private Player player;

    private boolean close = true;
    private boolean destroy = true;

    public AnvilClickEvent(AnvilSlot slot, String text, Player player)
    {
        this.slot = slot;
        this.text = text;
        this.player = player;
    }

    public AnvilSlot getSlot()
    {
        return slot;
    }

    public boolean getWillClose()
    {
        return close;
    }

    public void setWillClose(boolean close)
    {
        this.close = close;
    }

    public boolean getWillDestroy()
    {
        return destroy;
    }

    public void setWillDestroy(boolean destroy)
    {
        this.destroy = destroy;
    }
}
