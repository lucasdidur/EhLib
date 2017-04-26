/*
 * This file is part of AmpMenus.
 *
 * Copyright (c) 2014 <http://github.com/ampayne2/AmpMenus/>
 *
 * AmpMenus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AmpMenus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with AmpMenus.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.ehaqui.lib.menu.events;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.ehaqui.lib.menu.menus.Menu;

import lombok.Getter;
import lombok.Setter;


/**
 * An event called when an Item in the {@link Menu} is clicked.
 */
@Getter
@Setter
public class ItemClickEvent
{
    private Player player;
    private boolean goBack = false;
    private boolean close = false;
    private boolean update = false;
    private boolean willOpenMenu = false;
    private ClickType clickType;

    private InventoryClickEvent event;

    private Menu menu;
    private ItemStack item;
    private int clickedSlot;

    public ItemClickEvent(Player player)
    {
        this.player = player;
    }

    /**
     * Gets the player who clicked.
     *
     * @return The player who clicked.
     */
    public Player getPlayer()
    {
        return player;
    }

    /**
     * Checks if the {@link Menu} will go back to the parent menu.
     *
     * @return True if the {@link Menu} will go back to the parent menu, else false.
     */
    public boolean willGoBack()
    {
        return goBack;
    }

    /**
     * Sets if the {@link Menu} will go back to the parent menu.
     *
     * @param goBack
     *            If the {@link Menu} will go back to the parent menu.
     */
    public void setWillGoBack(boolean goBack)
    {
        this.goBack = goBack;
        if (goBack)
        {
            close = false;
            update = false;
        }
    }

    /**
     * Checks if the {@link Menu} will close.
     *
     * @return True if the {@link Menu} will close, else false.
     */
    public boolean willClose()
    {
        return close;
    }

    /**
     * Sets if the {@link Menu} will close.
     *
     * @param close
     *            If the {@link Menu} will close.
     */
    public void setWillClose(boolean close)
    {
        this.close = close;
        if (close)
        {
            goBack = false;
            update = false;
        }
    }

    /**
     * Checks if the {@link Menu} will update.
     *
     * @return True if the {@link Menu} will update, else false.
     */
    public boolean willUpdate()
    {
        return update;
    }

    /**
     * Sets if the {@link Menu} will update.
     *
     * @param update
     *            If the {@link Menu} will update.
     */
    public void setWillUpdate(boolean update)
    {
        this.update = update;
        if (update)
        {
            goBack = false;
            close = false;
        }
    }

    /**
     * Set the iventory click event
     * 
     * @param event
     *            of click on inventory
     */
    public void setEvent(InventoryClickEvent event)
    {
        this.event = event;
    }

    /**
     * Return the event
     * 
     * @return event InventoryClick
     */
    public InventoryClickEvent getEvent()
    {
        return event;
    }

    public void setMenu(Menu itemMenu)
    {
        this.menu = itemMenu;
    }

    public Menu getMenu()
    {
        return menu;
    }
}
