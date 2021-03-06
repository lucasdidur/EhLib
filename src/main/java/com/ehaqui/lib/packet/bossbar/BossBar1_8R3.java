package com.ehaqui.lib.packet.bossbar;

import com.ehaqui.ehlib.bukkit.EhLibSpigot;
import com.ehaqui.lib.message.Message;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class BossBar1_8R3 implements OlderBossBar
{

    private EhLibSpigot plugin = EhLibSpigot.getInstance();
    private String text;
    private EntityWither wither;
    private Player player;
    private BukkitTask task;

    public BossBar1_8R3(Player player, String text)
    {
        setPlayer(player);
        setText(text);
        task = plugin.getServer().getScheduler().runTaskTimer(plugin, this::update, 0, 0);
    }

    private void update()
    {
        if (player != null && player.isOnline())
        {
            hide();
            WorldServer world = ((CraftWorld) player.getWorld()).getHandle();
            setWither(new EntityWither(world));
            Location location = player.getLocation().add(player.getLocation().getDirection().multiply(50));
            getWither().setLocation(location.getX(), location.getY(), location.getZ(), location.getPitch(), location.getYaw());
            getWither().setCustomName(Message.colorize(getText()));
            getWither().setInvisible(true);
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(getWither());
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            return;
        }

        task.cancel();
    }

    public void show()
    {
        if (task == null)
        {
            task = plugin.getServer().getScheduler().runTaskTimer(plugin, this::update, 0, 20);
        }
    }

    public void hide()
    {
        if (getWither() != null)
        {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(getWither().getId());
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public void stop()
    {
        if (task != null)
        {
            task.cancel();
        }
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }

    public double getProgress()
    {
        return getWither().getHealth();
    }

    public void setProgress(double progress)
    {
        getWither().setHealth((float) progress);
    }

    public EntityWither getWither()
    {
        return wither;
    }

    public void setWither(EntityWither wither)
    {
        this.wither = wither;
    }

    private void setPlayer(Player player)
    {
        this.player = player;
    }
}
