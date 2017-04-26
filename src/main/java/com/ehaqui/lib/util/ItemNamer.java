package com.ehaqui.lib.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;


public class ItemNamer
{
    private List<String> lore = new ArrayList<String>();
    private String name = null;
    private Material material = Material.AIR;

    public void addLore(String string)
    {
        this.lore.add(ChatColor.translateAlternateColorCodes('&', "&r" + string));
    }

    public void addLore(String string, Object... args)
    {
        this.lore.add(ChatColor.translateAlternateColorCodes('&', "&r" + String.format(string, args)));
    }

    public void addLore(List<String> list)
    {
        for (String string : list)
        {
            this.lore.add(ChatColor.translateAlternateColorCodes('&', "&r" + string));
        }
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNameOriginal(String name)
    {
        this.name = name;
    }

    public void setMaterial(Material material)
    {
        this.material = material;
    }

    public List<String> getLore()
    {
        return lore;
    }

    public ItemStack setOwnerHead(String owner, ItemStack item)
    {
        SkullMeta sm = (SkullMeta) item.getItemMeta();
        sm.setOwner(owner);
        item.setItemMeta(sm);

        return item;
    }

    public String getName()
    {
        return name;
    }

    public Material getMaterial()
    {
        return material;
    }

    public ItemStack setLore(ItemStack item)
    {
        ItemMeta meta = item.getItemMeta();

        meta.setLore(getLore());

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack setDisplayName(ItemStack item)
    {
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(getName());

        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getItem(ItemStack item)
    {

        if (material != Material.AIR)
            item.setType(getMaterial());

        ItemMeta meta = item.getItemMeta();

        if (name != null)
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', getName()));

        if (lore.size() > 0)
            meta.setLore(getLore());

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        
        item.setItemMeta(meta);

        return item;
    }

    public ItemStack getItem()
    {
        ItemStack item = new ItemStack(Material.AIR);

        return getItem(item);
    }

}
