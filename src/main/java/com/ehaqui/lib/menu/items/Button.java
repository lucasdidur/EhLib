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

import com.ehaqui.lib.gui.anvil.AnvilClickEventHandler;
import com.ehaqui.lib.gui.anvil.AnvilGui;
import com.ehaqui.lib.gui.anvil.AnvilSlot;
import com.ehaqui.lib.menu.events.ItemClickEvent;
import com.ehaqui.lib.menu.exceptions.MenuException;
import com.ehaqui.lib.menu.menus.Menu;
import com.ehaqui.lib.message.Message;
import com.ehaqui.lib.util.ItemUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * An Item inside an {@link com.ehaqui.lib.menu.menus.Menu}.
 *
 * @param <T>
 */
@Getter
@Setter
public abstract class Button<T>
{
    private JavaPlugin plugin;
    private String displayName = "";
    private ItemStack icon = new ItemStack(Material.STAINED_GLASS_PANE);
    private List<String> lore = new ArrayList<>();
    private UUID uuid;

    private T option;

    public Button()
    {
        setName("");
        lore.clear();
    }

    public Button(Material material)
    {
        setName("");
        setIcon(material);
    }

    public Button(ItemStack item)
    {
        setItem(item);
    }

    @SuppressWarnings("deprecation")
    public Button(String displayName)
    {
        setName(displayName);
        setIcon(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));
    }

    public Button(String displayName, ItemStack icon)
    {
        setName(displayName);
        setIcon(icon);
    }

    public Button(String displayName, ItemStack icon, String... lore)
    {
        setName(displayName);
        setIcon(icon);
        setLore(lore);
    }

    public Player getPlayer()
    {
        return Bukkit.getPlayer(getUuid());
    }

    public void setPlayer(Player player)
    {
        this.uuid = player.getUniqueId();
    }

    public OfflinePlayer getPlayerOffline()
    {
        return Bukkit.getOfflinePlayer(getUuid());
    }

    /**
     * Sets the display name and lore of an ItemStack.
     *
     * @param itemStack   The ItemStack.
     * @param displayName The display name.
     * @param lore        The lore.
     * @return The ItemStack.
     */
    public static ItemStack setNameAndLore(ItemStack itemStack, String displayName, List<String> lore)
    {
        if (itemStack.getType() != Material.AIR)
        {
            ItemMeta meta = itemStack.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));

            List<String> lores = new ArrayList<String>();
            if (lore != null)
            {
                for (String lor : lore)
                {
                    lores.add(ChatColor.translateAlternateColorCodes('&', lor));
                }
            }

            meta.setLore(lores);
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public Button<T> setUuid(UUID uuid)
    {
        this.uuid = uuid;
        return this;
    }

    public Button<T> setOption(T option)
    {
        this.option = option;
        return this;
    }

    public Button<T> setName(String displayName, Object... args)
    {
        this.displayName = ChatColor.translateAlternateColorCodes('&', "&r" + String.format(displayName, args));
        return this;
    }

    public Button<T> setTitle(String displayName, Object... args)
    {
        setName(displayName, args);
        return this;
    }

    public Button<T> setIcon(ItemStack icon)
    {
        if (icon != null)
        {
            ItemMeta im = icon.getItemMeta();

            im.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_PLACED_ON, ItemFlag.HIDE_UNBREAKABLE);

            icon.setItemMeta(im);

            this.icon = icon;
        }

        return this;
    }

    public Button<T> setIconTexture(String texture)
    {
        setIcon(ItemUtils.getSkull(texture));
        return this;
    }

    public Button<T> setIcon(String itemString)
    {
        setIcon(ItemUtils.parseItem(itemString));
        return this;
    }

public Button<T> setIcon(Material material)
    {
        this.icon.setType(material);
        return this;
    }

    public Button<T> setHeadOwner(String owner)
    {
        setIcon(new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal()));

        SkullMeta sm = (SkullMeta) getIcon().getItemMeta();
        sm.setOwner(owner);
        getIcon().setItemMeta(sm);

        return this;
    }


    public Button<T> setMaterial(Material material)
    {
        this.icon.setType(material);
        return this;
    }

    public Button<T> addLore(String lore, Object... args)
    {
        for (String string: lore.split("\n"))
        {
            this.lore.add(ChatColor.translateAlternateColorCodes('&', "&r" + String.format(string, args)).replace("\r", ""));
        }

        return this;
    }

    public Button<T> setLore(String... lore)
    {
        setLore(Arrays.asList(lore));
        return this;
    }

    public Button<T> setLore(List<String> lore)
    {
        this.lore.clear();
        for (String string : lore)
        {
            addLore(string);
        }

        return this;
    }

    public void openMenu(Class<? extends Menu> clazz, ItemClickEvent event)
    {
        openMenu(clazz, event, null);
    }

    public <R> void openMenu(Class<? extends Menu> clazz, ItemClickEvent event, R option)
    {
        try
        {
            Menu menu = clazz.newInstance();
            if (option == null)
                menu.setOption(getOption());
            else
                menu.setOption(option);
            menu.setParent(event);
            menu.setPlugin(getPlugin());
            menu.setUuid(getUuid());

            menu.open(event.getPlayer());

            event.setWillOpenMenu(true);
        } catch (InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Gets the ItemStack to be shown to the player.
     *
     * @param player The player.
     * @return The final icon.
     */
    public ItemStack getFinalIcon(Player player)
    {
        return setNameAndLore(getIcon().clone(), getDisplayName(), getLore());
    }

    /**
     * Called when the ItemMenu<T> is clicked.
     *
     * @param event The {@link com.ehaqui.lib.menu.events.ItemClickEvent}.
     */
    public abstract void onItemClick(ItemClickEvent event) throws MenuException;

    public void process(String text) throws MenuException
    {

    }

    /**
     * Sets the current item;
     *
     * @param item
     */
    public Button<T> setItem(ItemStack item)
    {
        this.icon = item;

        if (item.getType() != Material.AIR)
        {

            if (item.getItemMeta().hasDisplayName())
                this.displayName = item.getItemMeta().getDisplayName();
            else
                this.displayName = "";

            if (item.getItemMeta().hasLore())
                this.lore = item.getItemMeta().getLore();
            else
                this.lore = new ArrayList<>();
        }

        return this;
    }

    public void getText(ItemClickEvent event, String text, boolean back, String... lore)
    {
        getText(event, text, new ItemStack(Material.EMERALD_BLOCK), back, lore);
    }

    public void getText(ItemClickEvent event, String text, String... lore)
    {
        getText(event, text, new ItemStack(Material.EMERALD_BLOCK), lore);
    }

    public void getText(ItemClickEvent event, String text, ItemStack icon, String... lore)
    {
        getText(event, text, icon, true, lore);
    }

    public void getText(ItemClickEvent event, String text, ItemStack icon, boolean back, String... lore)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {

                AnvilGui gui = new AnvilGui(event.getPlayer(), (AnvilClickEventHandler) event2 ->
                {
                    boolean error = false;

                    if (event2.getSlot() == AnvilSlot.OUTPUT)
                    {
                        try
                        {
                            process(event2.getText());
                        } catch (MenuException ex)
                        {
                            send("&c" + ex.getMessage());
                            error = true;
                        }
                    }

                    if (back || error)
                    {
                        new BukkitRunnable()
                        {
                            @Override
                            public void run()
                            {
                                Menu menu = event.getMenu();

                                if (menu.getPlugin() == null)
                                    menu.setPlugin(getPlugin());

                                menu.setUuid(getUuid());
                                menu.open(event.getPlayer());
                            }
                        }.runTaskLater(getPlugin(), 3);
                    }

                    event.setWillClose(true);
                }, getPlugin());

                gui.setSlot(AnvilSlot.INPUT_LEFT, text, icon, lore);
                gui.open();

            }
        }.runTaskLater(getPlugin(), 3);

        event.setWillClose(true);
    }

    public void send(String message)
    {
        Message.send(getPlayer(), message);
    }

    public void send(Message.Type type, String... message)
    {
        if (message.length > 1)
            Message.send(type, getPlayer(), message[0], message[1]);
        else
            Message.send(type, getPlayer(), message[0]);
    }
}
