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

import com.ehaqui.lib.menu.MenuListener;
import com.ehaqui.lib.menu.events.ItemClickEvent;
import com.ehaqui.lib.menu.exceptions.MenuException;
import com.ehaqui.lib.menu.items.Button;
import com.ehaqui.lib.menu.items.ButtonBack;
import com.ehaqui.lib.menu.items.ButtonStatic;
import com.ehaqui.lib.menu.items.ButtonSubMenu;
import com.ehaqui.lib.message.Message;
import com.ehaqui.lib.util.ItemUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;
import java.util.UUID;


/**
 * A Menu controlled by ItemStacks in an Inventory.
 */
@Getter
@Setter
public abstract class Menu<O>
{
    /**
     * The {@link ButtonStatic} that appears in empty slots if {@link Menu#fillEmptySlots()} is called.
     */
    @SuppressWarnings("deprecation")
    protected static final Button EMPTY_SLOT_ITEM = new ButtonStatic(" ", new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7));

    private JavaPlugin plugin;
    private String name = "";
    private Size size = Size.AUTO;
    private int maxSlot = 0;
    private Button[] items;

    private Menu parent;

    private boolean autoUpdate = false;
    private long updateTime = 1;
    private BukkitRunnable task;

    private O option;

    private UUID uuid;

    private boolean getClickedItem;
    private int clickedSlot;
    private ItemStack clickedItem;

    // Page
    /**
     * the default page size
     */
    public static final int DEFAULT_PAGE_SIZE = 9;

    private static final int PAGE_WINDOW = 10;

    /**
     * the list over which this class is paging
     */
    private List<O> list;

    /**
     * the page size
     */
    private int pageSize = DEFAULT_PAGE_SIZE;

    /**
     * the current page
     */
    private int page = 1;

    /**
     * the starting index
     */
    private int startingIndex;

    /**
     * the ending index
     */
    private int endingIndex;

    /**
     * the maximum number of pages
     */
    private int maxPages = 1;


    private MenuBackMethod backMethod = MenuBackMethod.BUTTON;

    /**
     * Creates an {@link Menu}.
     *
     * @param name       The name of the inventory.
     * @param size       The {@link Size} of the inventory.
     * @param plugin     The {@link org.bukkit.plugin.java.JavaPlugin} instance.
     * @param parent     The ItemMenu's parent.
     * @param autoupdate Set to true if wants the menu update periodically
     */
    public Menu(String name, Size size, JavaPlugin plugin, Menu parent, boolean autoupdate, long autoUpdateTime)
    {
        this.plugin = plugin;
        this.name = name;
        this.size = size;
        this.items = new Button[54];
        this.parent = parent;
        this.autoUpdate = autoupdate;
        this.updateTime = autoUpdateTime;
    }

    public Menu()
    {
    }

    public Menu(JavaPlugin plugin)
    {
        this("", Size.AUTO, plugin, null, false, 1);
        setBackMethod(MenuBackMethod.NONE);
    }

    /**
     * Creates an {@link Menu} with no parent.
     *
     * @param name   The name of the inventory.
     * @param plugin The Plugin instance.
     */
    public Menu(String name, JavaPlugin plugin)
    {
        this(ChatColor.translateAlternateColorCodes('&', name), Size.AUTO, plugin, null, false, 1);
    }

    /**
     * Creates an {@link Menu} with no parent.
     *
     * @param name   The name of the inventory.
     * @param size   The {@link Size} of the inventory.
     * @param plugin The Plugin instance.
     */
    public Menu(String name, Size size, JavaPlugin plugin)
    {
        this(ChatColor.translateAlternateColorCodes('&', name), size, plugin, null, false, 1);
    }

    /**
     * Creates an {@link Menu} with no parent.
     *
     * @param name   The name of the inventory.
     * @param size   The {@link Size} of the inventory.
     * @param plugin The Plugin instance.
     */
    public Menu(String name, Size size, JavaPlugin plugin, boolean autoUpdate)
    {
        this(ChatColor.translateAlternateColorCodes('&', name), size, plugin, null, autoUpdate, 1);
    }

    /**
     * Creates an {@link Menu} with no parent.
     *
     * @param name       The name of the inventory.
     * @param size       The {@link Size} of the inventory.
     * @param plugin     The Plugin instance.
     * @param autoUpdate Set to true to update periodically
     * @param updateTime Time in secconds to auto update
     */
    public Menu(String name, Size size, JavaPlugin plugin, boolean autoUpdate, long updateTime)
    {
        this(ChatColor.translateAlternateColorCodes('&', name), size, plugin, null, autoUpdate, updateTime);
    }

    /**
     * Gets the name of the {@link Menu}.
     *
     * @return The {@link Menu}'s name.
     */
    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = ChatColor.translateAlternateColorCodes('&', name);
    }

    /**
     * Gets the {@link Size} of the {@link Menu}.
     *
     * @return The {@link Menu}'s {@link Size}.
     */
    public Size getSize()
    {
        if (size == Size.AUTO)
            return Size.fit(maxSlot);

        return size;
    }

    /**
     * Checks if the {@link Menu} has a parent.
     *
     * @return True if the {@link Menu} has a parent, else false.
     */
    public boolean hasParent()
    {
        return parent != null;
    }

    /**
     * Gets the parent of the {@link Menu}.
     *
     * @return The {@link Menu}'s parent.
     */
    public Menu getParent()
    {
        return parent;
    }

    /**
     * Sets the parent of the {@link Menu}.
     *
     * @param parent The {@link Menu}'s parent.
     */
    public Menu<O> setParent(Menu parent)
    {
        this.parent = parent;
        return this;
    }

    public Menu<O> setParent(ItemClickEvent event)
    {
        setParent(event.getMenu());
        return this;
    }

    public Player getPlayer()
    {
        return Bukkit.getPlayer(getUuid());
    }

    public OfflinePlayer getOfflinePlayer()
    {
        return Bukkit.getOfflinePlayer(getUuid());
    }

    public void setPlayer(OfflinePlayer offlinePlayer)
    {
        this.uuid = offlinePlayer.getUniqueId();
    }

    public void setPlayer(Player player)
    {
        this.uuid = player.getUniqueId();
    }


    public abstract void setupButtons();

    /**
     * Sets the {@link Button} of a slot.
     *
     * @param position The slot position.
     * @param button   The {@link Button}.
     * @return The {@link Menu}.
     */
    @Deprecated
    public Menu setItem(int position, Button button)
    {
        return setButton(position, button);
    }

    public Menu setButton(int position, Button button)
    {
        if (size == Size.AUTO)
        {
            if (position > maxSlot)
            {
                maxSlot = position;
            }
        }

        position--;

        button.setOption(getOption());
        button.setPlugin(getPlugin());
        button.setUuid(getUuid());

        items[position] = button;
        return this;
    }

    @Deprecated
    public <T> Menu setItem(int position, Button<T> button, T option)
    {
        return setButton(position, button, option);
    }

    public <T> Menu setButton(int position, Button<T> button, T option)
    {
        if (size == Size.AUTO)
        {
            if (position > maxSlot)
            {
                maxSlot = position;
            }
        }

        position--;

        button.setOption(option);
        button.setPlugin(getPlugin());
        button.setUuid(getUuid());

        items[position] = button;
        return this;
    }

    @Deprecated
    public void setItem(int slot, Menu menu, String displayName, String icon, String... lore)
    {
        setButton(slot, menu, displayName, icon, lore);
    }

    public void setButton(int slot, Menu menu, String displayName, String icon, String... lore)
    {
        ItemStack item = ItemUtils.parseItem(icon);
        menu.setUuid(getUuid());

        ButtonSubMenu button = new ButtonSubMenu(displayName, item, menu, lore);

        setButton(slot, button);
    }


    public Menu setConfirmItem(Button button)
    {
        setButton(3, button);
        return this;
    }

    public <T> Menu setConfirmItem(Button<T> button, T option)
    {
        setButton(3, button, option);
        return this;
    }

    public Menu setCancelItem(Button button)
    {
        setButton(2, button);
        return this;
    }

    public <T> Menu setCancelItem(Button<T> button, T option)
    {
        setButton(2, button, option);
        return this;
    }

    public <T> Menu setItem(Button<T> button, T option)
    {
        setButton(1, button, option);
        return this;
    }

    public Menu setItem(Button button)
    {
        setButton(1, button);
        return this;
    }

    public void setBackButton()
    {
        if (getItem(getSize().getSize()) == null || getItem(getSize().getSize()) == EMPTY_SLOT_ITEM)
        {
            setButton(getSize().getSize(), new ButtonBack());
        }
        else
        {
            switch (getSize())
            {
                case MINIMAL:
                    setItem(getSize().getSize() + 4, new ButtonBack());
                    break;

                case SIX_LINE:
                    break;

                default:
                    setItem(getSize().getSize() + 9, new ButtonBack());
                    break;
            }
        }
    }

    /**
     * Get the {@link Button} of a position.
     *
     * @param position The slot position.
     * @return The {@link Menu} on position.
     */
    public Button getItem(int position)
    {
        return items[position - 1];
    }

    /**
     * Fills all empty slots in the {@link Menu} with a certain {@link Button}.
     *
     * @param ItemMenu The {@link Button}.
     * @return The {@link Menu}.
     */
    public Menu fillEmptySlots(Button ItemMenu)
    {
        for (int i = 0; i < getSize().getSize(); i++)
        {
            if (items[i] == null)
            {
                items[i] = ItemMenu;
            }
        }
        return this;
    }

    /**
     * Fills all empty slots in the {@link Menu} with the default empty slot item.
     *
     * @return The {@link Menu}.
     */
    public Menu fillEmptySlots()
    {
        return fillEmptySlots(EMPTY_SLOT_ITEM);
    }

    public void fillBorder()
    {
        // Top
        for (int i = 0; i < 9; i++)
        {
            if (items[i] == null)
                items[i] = Menu.EMPTY_SLOT_ITEM;
        }

        // Fill side
        for (int i = 0; i < getSize().getSize() / 9; i++)
        {
            if (items[i * 9] == null)
                items[i * 9] = Menu.EMPTY_SLOT_ITEM;

            if (items[i * 9 + 8] == null)
                items[i * 9 + 8] = Menu.EMPTY_SLOT_ITEM;
        }

        // Booton
        for (int i = getSize().getSize() - 9; i < getSize().getSize(); i++)
        {
            if (items[i] == null)
                items[i] = Menu.EMPTY_SLOT_ITEM;
        }
    }

    /**
     * Opens the {@link Menu} for a player.
     *
     * @param player The player.
     */
    public void open(Player player)
    {
        if (getUuid() == null)
            setPlayer(player);

        open(player, player.getLocation());
    }

    /**
     * Opens the {@link Menu} for a player.
     *
     * @param player   The player.
     * @param location Location that menu was open
     */
    public void open(Player player, Location location)
    {
        if (!MenuListener.getInstance().isRegistered(plugin))
        {
            MenuListener.getInstance().register(plugin);
        }

        items = new Button[54];

        setupButtons();

        if (backMethod == MenuBackMethod.BUTTON)
            setBackButton();

        Inventory inventory = null;

        if (getSize().getType() == InventoryType.CHEST)
        {
            inventory = Bukkit.createInventory(new MenuHolder(this, Bukkit.createInventory(player, getSize().getSize()), location), getSize().getSize(), name);
        }
        else
        {
            inventory = Bukkit.createInventory(new MenuHolder(this, Bukkit.createInventory(player, getSize().getType()), location), getSize().getType(), name);
        }

        apply(inventory, player);
        player.openInventory(inventory);
        new BukkitRunnable()
        {

            @Override
            public void run()
            {
                player.updateInventory();
            }
        }.runTaskLater(plugin, 2);
    }

    /**
     * Updates the {@link Menu} for a player.
     *
     * @param player The player to update the {@link Menu} for.
     */
    public void update(Player player)
    {
        if (player.getOpenInventory() != null)
        {
            Inventory inventory = player.getOpenInventory().getTopInventory();
            if (inventory.getHolder() instanceof MenuHolder && ((MenuHolder) inventory.getHolder()).getMenu().equals(this))
            {
                apply(inventory, player);
                player.updateInventory();
            }
        }
    }

    /**
     * Applies the {@link Menu} for a player to an Inventory.
     *
     * @param inventory The Inventory.
     * @param player    The Player.
     */
    private void apply(final Inventory inventory, final Player player)
    {
        task = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                items = new Button[54];

                setupButtons();

                if (backMethod == MenuBackMethod.BUTTON)
                    setBackButton();

                for (int i = 0; i < getSize().getSize(); i++)
                {
                    if (items[i] != null)
                    {
                        inventory.setItem(i, items[i].getFinalIcon(player));
                    }
                }

                if (autoUpdate)
                    player.updateInventory();
            }
        };

        if (autoUpdate)
        {
            task.runTaskTimer(plugin, 0, 20 * updateTime);
        }
        else
        {
            task.runTask(plugin);
        }
    }

    public void onInventoryClose(InventoryCloseEvent event)
    {
        task.cancel();

        onCloseMenu(event);
    }

    public void onCloseMenu(InventoryCloseEvent event)
    {

    }

    /**
     * Handles InventoryClickEvents for the {@link Menu}.
     */
    public void onInventoryClick(InventoryClickEvent event)
    {
        Player player = (Player) event.getWhoClicked();

        int slot = event.getRawSlot();

        if (isGetClickedItem() && !(slot >= 0 && slot < getSize().getSize()))
        {
            this.clickedSlot = slot;
            this.clickedItem = event.getCurrentItem();
            update(player);
            return;
        }

        if (slot >= 0 && slot < getSize().getSize() && items[slot] != null)
        {
            ItemClickEvent itemClickEvent = new ItemClickEvent(player);
            itemClickEvent.setItem(clickedItem);
            itemClickEvent.setClickedSlot(clickedSlot);
            itemClickEvent.setClickType(event.getClick());
            itemClickEvent.setEvent(event);
            itemClickEvent.setMenu(this);

            try
            {
                items[slot].onItemClick(itemClickEvent);
            } catch (MenuException ex)
            {
                Message.send(player, ex.getMessage());
                itemClickEvent.setWillClose(true);
            }

            if (itemClickEvent.willUpdate())
            {
                update(player);
            }
            else
            {
                player.updateInventory();

                if (itemClickEvent.willClose() || itemClickEvent.willGoBack())
                {
                    final String playerName = player.getName();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
                    {

                        public void run()
                        {
                            Player p = Bukkit.getPlayerExact(playerName);
                            if (p != null)
                            {
                                p.closeInventory();
                            }
                        }
                    }, 1);
                }

                if (itemClickEvent.willGoBack() && hasParent())
                {
                    final String playerName = player.getName();
                    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
                    {
                        public void run()
                        {
                            Player p = Bukkit.getPlayerExact(playerName);
                            if (p != null)
                            {
                                parent.open(p);
                            }
                        }
                    }, 3);
                }
            }
        }
    }


    private void calculatePages()
    {
        if (pageSize > 0)
        {
            // calculate how many pages there are
            if (list.size() % pageSize == 0)
            {
                maxPages = list.size() / pageSize;
            }
            else
            {
                maxPages = (list.size() / pageSize) + 1;
            }
        }
    }

    public void setList(List<O> list)
    {
        this.list = list;
        calculatePages();

        if (getSize().getSize() > 9)
            setPageSize(getSize().getSize() - 9);

        setPage(1);
    }

    /**
     * Gets the list that this instance is paging over.
     *
     * @return a List
     */
    public List<O> getList()
    {
        return this.list;
    }

    /**
     * Gets the subset of the list for the current page.
     *
     * @return a List
     */
    public List<O> getListForPage()
    {
        return list.subList(startingIndex, endingIndex);
    }

    /**
     * Gets the page size.
     *
     * @return the page size as an int
     */
    public int getPageSize()
    {
        return this.pageSize;
    }

    /**
     * Sets the page size.
     *
     * @param pageSize the page size as an int
     */
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
        calculatePages();
    }

    /**
     * Gets the page.
     *
     * @return the page as an int
     */
    public int getPage()
    {
        return this.page;
    }

    /**
     * Sets the page size.
     *
     * @param p the page as an int
     */
    public void setPage(int p)
    {
        if (p >= maxPages)
        {
            this.page = maxPages;
        }
        else if (p <= 1)
        {
            this.page = 1;
        }
        else
        {
            this.page = p;
        }

        // now work out where the sub-list should start and end
        startingIndex = pageSize * (page - 1);
        if (startingIndex < 0)
        {
            startingIndex = 0;
        }
        endingIndex = startingIndex + pageSize;
        if (endingIndex > list.size())
        {
            endingIndex = list.size();
        }
    }

    /**
     * Gets the maximum number of pages.
     *
     * @return the maximum number of pages as an int
     */
    public int getMaxPages()
    {
        return this.maxPages;
    }

    /**
     * Determines whether there is a previous page and gets the page number.
     *
     * @return the previous page number, or zero
     */
    public int getPreviousPage()
    {
        if (page > 1)
        {
            return page - 1;
        }
        else
        {
            return 0;
        }
    }

    /**
     * Determines whether there is a next page and gets the page number.
     *
     * @return the next page number, or 0
     */
    public int getNextPage()
    {
        if (page < maxPages)
        {
            return page + 1;
        }
        else
        {
            return 0;
        }
    }

    /**
     * Gets the minimum page in the window.
     *
     * @return the page number
     */
    public int getMinPageRange()
    {
        if (getPage() > PAGE_WINDOW)
        {
            return getPage() - PAGE_WINDOW;
        }
        else
        {
            return 1;
        }
    }

    /**
     * Gets the maximum page in the window.
     *
     * @return the page number
     */
    public int getMaxPageRange()
    {
        if (getPage() < (getMaxPages() - PAGE_WINDOW))
        {
            return getPage() + PAGE_WINDOW;
        }
        else
        {
            return getMaxPages();
        }
    }


    /**
     * Destroys the {@link Menu}.
     */
    public void destroy()
    {
        plugin = null;
        name = null;
        size = null;
        items = null;
        parent = null;
    }

    /**
     * Possible sizes of an {@link Menu}.
     */
    public enum Size
    {
        CONFIRM(3, InventoryType.FURNACE),

        MINIMAL(5, InventoryType.HOPPER),
        ONE_LINE(9, InventoryType.CHEST),
        TWO_LINE(18, InventoryType.CHEST),
        THREE_LINE(27, InventoryType.CHEST),
        FOUR_LINE(36, InventoryType.CHEST),
        FIVE_LINE(45, InventoryType.CHEST),
        SIX_LINE(54, InventoryType.CHEST),

        WORKBENCH(10, InventoryType.WORKBENCH),
        DISPENSER(9, InventoryType.DISPENSER),

        AUTO(0, InventoryType.CHEST);

        private final int size;
        private final InventoryType type;

        private Size(int size, InventoryType type)
        {
            this.size = size;
            this.type = type;
        }

        /**
         * Gets the required {@link Size} for an amount of slots.
         *
         * @param slots The amount of slots.
         * @return The required {@link Size}.
         */
        public static Size fit(int slots)
        {
            if (slots < 6)
            {
                return MINIMAL;
            }
            else if (slots < 10)
            {
                return ONE_LINE;
            }
            else if (slots < 19)
            {
                return TWO_LINE;
            }
            else if (slots < 28)
            {
                return THREE_LINE;
            }
            else if (slots < 37)
            {
                return FOUR_LINE;
            }
            else if (slots < 46)
            {
                return FIVE_LINE;
            }
            else
            {
                return SIX_LINE;
            }
        }

        /**
         * Gets the {@link Size}'s amount of slots.
         *
         * @return The amount of slots.
         */
        public int getSize()
        {
            return size;
        }

        /**
         * Gets the {@link Size}'s inventory type.
         *
         * @return The inventory type.
         */
        public InventoryType getType()
        {
            return type;
        }
    }

}
