package com.ehaqui.lib.gui.anvil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import com.ehaqui.lib.gui.NMSManager;
import com.ehaqui.lib.util.ItemNamer;


/**
 * Created by chasechocolate.
 */
public class AnvilGui
{
    private Player player;
    private static Class<?> BlockPosition;
    private static Class<?> PacketPlayOutOpenWindow;
    private static Class<?> ContainerAnvil;
    private static Class<?> ChatMessage;
    private static Class<?> EntityHuman;
    private HashMap<AnvilSlot, ItemStack> items = new HashMap<>();
    private Inventory inv;
    private Listener listener;

    private void loadClasses()
    {
        BlockPosition = NMSManager.get().getNMSClass("BlockPosition");
        PacketPlayOutOpenWindow = NMSManager.get().getNMSClass("PacketPlayOutOpenWindow");
        ContainerAnvil = NMSManager.get().getNMSClass("ContainerAnvil");
        EntityHuman = NMSManager.get().getNMSClass("EntityHuman");
        ChatMessage = NMSManager.get().getNMSClass("ChatMessage");
    }

    public AnvilGui(final Player player, AnvilClickEventHandler handler, Plugin plugin)
    {
        loadClasses();
        this.player = player;

        this.listener = new Listener()
        {
            @EventHandler
            public void onInventoryClick(InventoryClickEvent event)
            {
                if (event.getWhoClicked() instanceof Player)
                {

                    if (event.getInventory().equals(inv))
                    {
                        event.setCancelled(true);

                        ItemStack item = event.getCurrentItem();
                        int slot = event.getRawSlot();
                        String name = "";

                        if (item != null)
                        {
                            if (item.hasItemMeta())
                            {
                                ItemMeta meta = item.getItemMeta();

                                if (meta.hasDisplayName())
                                {
                                    name = meta.getDisplayName();
                                }
                            }
                        }

                        AnvilClickEvent clickEvent = new AnvilClickEvent(AnvilSlot.bySlot(slot), name, player);

                        handler.onAnvilClick(clickEvent);

                        if (clickEvent.getWillClose())
                        {
                            event.getWhoClicked().closeInventory();
                        }

                        if (clickEvent.getWillDestroy())
                        {
                            destroy();
                        }
                    }
                }
            }

            @EventHandler
            public void onInventoryClose(InventoryCloseEvent event)
            {
                if (event.getPlayer() instanceof Player)
                {
                    Inventory inv = event.getInventory();
                    if (inv.equals(AnvilGui.this.inv))
                    {
                        inv.clear();
                        destroy();
                    }
                }
            }

            @EventHandler
            public void onPlayerQuit(PlayerQuitEvent event)
            {
                if (event.getPlayer().equals(getPlayer()))
                {
                    destroy();
                }
            }

            @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
            public void onPluginDisable(PluginDisableEvent event)
            {
                if (event.getPlugin().equals(plugin))
                {
                    closeOpenMenus();
                }
            }

            public void closeOpenMenus()
            {
                for (Player player : Bukkit.getOnlinePlayers())
                {
                    if (player.getOpenInventory() != null)
                    {
                        Inventory inventory = player.getOpenInventory().getTopInventory();

                        if (inventory.equals(inv))
                        {
                            inventory.clear();
                            destroy();
                            player.closeInventory();
                        }
                    }
                }
            }
        };

        Bukkit.getPluginManager().registerEvents(listener, plugin); // Replace with instance of main class
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setSlot(AnvilSlot slot, ItemStack item)
    {
        items.put(slot, item);
    }

    public void setSlot(AnvilSlot slot, String name, ItemStack icon, String... lore)
    {
        ItemNamer item = new ItemNamer();

        item.setName(name);

        for (String string : lore)
        {
            item.addLore(string);
        }

        items.put(slot, item.getItem(icon));
    }

    public void open()
    {
        try
        {
            Object p = NMSManager.get().getHandle(player);

            Object container = ContainerAnvil.getConstructor(NMSManager.get().getNMSClass("PlayerInventory"), NMSManager.get().getNMSClass("World"), BlockPosition, EntityHuman).newInstance(NMSManager.get().getPlayerField(player, "inventory"), NMSManager.get().getPlayerField(player, "world"), BlockPosition.getConstructor(int.class, int.class, int.class).newInstance(0, 0, 0), p);
            NMSManager.get().getField(NMSManager.get().getNMSClass("Container"), "checkReachable").set(container, false);

            // Set the items to the items from the inventory given
            Object bukkitView = NMSManager.get().invokeMethod("getBukkitView", container);
            inv = (Inventory) NMSManager.get().invokeMethod("getTopInventory", bukkitView);

            for (AnvilSlot slot : items.keySet())
            {
                inv.setItem(slot.getSlot(), items.get(slot));
            }

            // Counter stuff that the game uses to keep track of inventories
            int c = (int) NMSManager.get().invokeMethod("nextContainerCounter", p);

            // Send the packet
            Constructor<?> chatMessageConstructor = ChatMessage.getConstructor(String.class, Object[].class);
            Object playerConnection = NMSManager.get().getPlayerField(player, "playerConnection");
            Object packet = PacketPlayOutOpenWindow.getConstructor(int.class, String.class, NMSManager.get().getNMSClass("IChatBaseComponent"), int.class).newInstance(c, "minecraft:anvil", chatMessageConstructor.newInstance("Repairing", new Object[] {}), 0);

            Method sendPacket = NMSManager.get().getMethod("sendPacket", playerConnection.getClass(), PacketPlayOutOpenWindow);
            sendPacket.invoke(playerConnection, packet);

            // Set their active container to the container
            Field activeContainerField = NMSManager.get().getField(EntityHuman, "activeContainer");
            if (activeContainerField != null)
            {
                activeContainerField.set(p, container);

                // Set their active container window id to that counter stuff
                NMSManager.get().getField(NMSManager.get().getNMSClass("Container"), "windowId").set(activeContainerField.get(p), c);

                // Add the slot listener
                NMSManager.get().getMethod("addSlotListener", activeContainerField.get(p).getClass(), p.getClass()).invoke(activeContainerField.get(p), p);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void destroy()
    {
        player = null;
        items = null;

        HandlerList.unregisterAll(listener);

        listener = null;
    }
}
