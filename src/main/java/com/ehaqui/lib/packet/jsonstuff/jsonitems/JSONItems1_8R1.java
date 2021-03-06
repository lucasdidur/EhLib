package com.ehaqui.lib.packet.jsonstuff.jsonitems;

import net.minecraft.server.v1_8_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class JSONItems1_8R1 implements JSONItems
{
    @Override
    public String getJSONItem(ItemStack itemStack)
    {
        net.minecraft.server.v1_8_R1.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = new NBTTagCompound();
        compound = nmsItemStack.save(compound);
        return compound.toString();
    }
}
