package com.ehaqui.lib.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_11_R1.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;


public class BookUtils
{
    public static void openBook(Player p, List<String> pages)
    {
        ItemStack book = getBookW(pages);
        
        int slot = p.getInventory().getHeldItemSlot();
        ItemStack old = p.getInventory().getItem(slot);
        p.getInventory().setItem(slot, book);
        try
        {
            PacketContainer pc = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CUSTOM_PAYLOAD);
            pc.getModifier().writeDefaults();
            // NOTICE THE CODE BELOW!
            ByteBuf bf = Unpooled.buffer(256); // #1
            bf.setByte(0, (byte) 0); // #2
            bf.writerIndex(1); // #3
            pc.getModifier().write(1, MinecraftReflection.getPacketDataSerializer(bf));
            // END OF NOTABLE CODE
            pc.getStrings().write(0, "MC|BOpen");
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, pc);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        p.getInventory().setItem(slot, old);
    }
    
    public static void openBook(Player p, ItemStack book)
    {
        int slot = p.getInventory().getHeldItemSlot();
        ItemStack old = p.getInventory().getItem(slot);
        p.getInventory().setItem(slot, book);
        try
        {
            PacketContainer pc = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.CUSTOM_PAYLOAD);
            pc.getModifier().writeDefaults();
            // NOTICE THE CODE BELOW!
            ByteBuf bf = Unpooled.buffer(256); // #1
            bf.setByte(0, (byte) 0); // #2
            bf.writerIndex(1); // #3
            pc.getModifier().write(1, MinecraftReflection.getPacketDataSerializer(bf));
            // END OF NOTABLE CODE
            pc.getStrings().write(0, "MC|BOpen");
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, pc);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        p.getInventory().setItem(slot, old);
    }

    public static ItemStack getBook(String name, String title, String... pages)
    {
        ItemStack is = new ItemStack(Material.BOOK_AND_QUILL, 1);

        net.minecraft.server.v1_11_R1.ItemStack nmsis = CraftItemStack.asNMSCopy(is);
        NBTTagCompound bd = new NBTTagCompound();

        NBTTagList bp = new NBTTagList();
        for (String text : pages)
        {
            bp.add(new NBTTagString(ChatColor.translateAlternateColorCodes('&', text)));
        }
        bd.set("pages", bp);
        bd.set("author", new NBTTagString("ehhelp"));
        bd.set("title", new NBTTagString(title));
        nmsis.setTag(bd);
        is = CraftItemStack.asBukkitCopy(nmsis);

        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        is.setItemMeta(im);
        return is;
    }

    public static ItemStack getBookW(String... pages)
    {
        return getBookW(Arrays.asList(pages));
    }

    public static ItemStack getBookW(List<String> pages)
    {
        ItemStack is = new ItemStack(Material.WRITTEN_BOOK, 1);

        net.minecraft.server.v1_11_R1.ItemStack nmsis = CraftItemStack.asNMSCopy(is);
        NBTTagCompound bd = new NBTTagCompound();

        NBTTagList bp = new NBTTagList();
        for (String text : pages)
        {
            bp.add(new NBTTagString(ChatColor.translateAlternateColorCodes('&', text)));
        }
        bd.set("pages", bp);
        bd.set("author", new NBTTagString("ehhelp"));

        nmsis.setTag(bd);
        is = CraftItemStack.asBukkitCopy(nmsis);

        return is;
    }

    public static void addJson(ItemStack item, String JSON) {

        BookMeta meta = (BookMeta) item.getItemMeta();

        Field pagesField = null;

        try {

            pagesField = org.bukkit.craftbukkit.v1_11_R1.inventory.CraftMetaBook.class.getDeclaredField("pages");
            pagesField.setAccessible(true);

            @SuppressWarnings("unchecked")
            List<IChatBaseComponent> pages = (List<IChatBaseComponent>) pagesField.get(meta);

            pages.add(IChatBaseComponent.ChatSerializer.a(JSON));

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }

        item.setItemMeta(meta);
    }
}
