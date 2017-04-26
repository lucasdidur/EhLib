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
package com.ehaqui.lib.menu.items;

import org.bukkit.inventory.ItemStack;

import com.ehaqui.lib.menu.events.ItemClickEvent;
import com.ehaqui.lib.menu.menus.Menu;


/**
 * A {@link Button} that opens a sub {@link com.ehaqui.lib.menu.menus.Menu}.
 */
public class ButtonSubMenu extends Button
{
    private final Menu menu;

    public ButtonSubMenu(String displayName, ItemStack icon, Menu menu)
    {
        super(displayName, icon);

        this.menu = menu;
    }

    public ButtonSubMenu(String displayName, ItemStack icon, Menu menu, String... lore)
    {
        super(displayName, icon, lore);

        this.menu = menu;
    }

    @Override
    public void onItemClick(ItemClickEvent event)
    {
        menu.setParent(event.getMenu());
        menu.setOption(getOption());
        menu.open(event.getPlayer());
    }
}
