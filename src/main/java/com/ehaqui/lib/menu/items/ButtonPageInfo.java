package com.ehaqui.lib.menu.items;

import com.ehaqui.lib.menu.events.ItemClickEvent;
import com.ehaqui.lib.menu.exceptions.MenuException;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Lucas on 13/01/2017.
 */
public class ButtonPageInfo extends Button
{
    public ButtonPageInfo(int page, int maxPages)
    {
        setName("Informações");
        setIcon(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 10));
        addLore("Pagina (%d/%d)", page, maxPages);
    }

    @Override
    public void onItemClick(ItemClickEvent event) throws MenuException
    {
        event.getMenu().setPage(event.getMenu().getPreviousPage());
        event.setWillUpdate(true);
    }
}
