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
package com.ehaqui.lib.menu.menus;

import org.bukkit.Location;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;


/**
 * Allows you to set the {@link Menu} that created the Inventory as the Inventory's holder.
 */
public class MenuHolder implements InventoryHolder
{
    private Menu menu;
    private Inventory inventory;
    private Location location;

    public MenuHolder(Menu menu, Inventory inventory, Location location)
    {
        this.menu = menu;
        this.inventory = inventory;
        this.location = location;
    }

    /**
     * Gets the {@link Menu} holding the Inventory.
     *
     * @return The {@link Menu} holding the Inventory.
     */
    public Menu getMenu()
    {
        return menu;
    }

    @Override
    public Inventory getInventory()
    {
        return inventory;
    }

    public Location getLocation()
    {
        return location;
    }
}
