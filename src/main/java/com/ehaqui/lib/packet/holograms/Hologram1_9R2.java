package com.ehaqui.lib.packet.holograms;

import com.ehaqui.ehlib.bukkit.EhLibSpigot;
import com.ehaqui.lib.message.Message;
import net.minecraft.server.v1_9_R2.EntityArmorStand;
import net.minecraft.server.v1_9_R2.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_9_R2.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_9_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_9_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Hologram1_9R2 implements Hologram
{

    private EhLibSpigot plugin = EhLibSpigot.getInstance();
    private List<EntityArmorStand> entityList;
    private Location location;
    private List<String> lines;
    private double lineSpread;
    private String id;

    public Hologram1_9R2(String id, List<String> lines, double lineSpread, Location location)
    {
        setId(id);
        setLines(lines);
        setLocation(location);
        setEntityList(new ArrayList<>());
        setLineSpread(lineSpread);
    }

    public Hologram showPlayer(Player p, int displayTime)
    {
        showPlayer(p);
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, () -> hidePlayer(p), displayTime);
        return this;
    }


    public Hologram showAll(int displayTime)
    {
        showAll();
        plugin.getServer().getScheduler().runTaskLaterAsynchronously(plugin, this::hideAll, displayTime);
        return this;
    }

    public Hologram showPlayer(Player p)
    {
        for (int j = -1; ++j < getEntityList().size(); )
        {
            PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(getEntityList().get(j));
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }

        return this;
    }

    public Hologram hidePlayer(Player p)
    {
        for (int j = -1; ++j < getEntityList().size(); )
        {
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(getEntityList().get(j).getId());
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
        }

        return this;
    }

    public Hologram showAll()
    {
        List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());
        for (int i = -1; ++i < players.size(); )
        {
            Player player = players.get(i);
            for (int j = -1; ++j < getEntityList().size(); )
            {
                PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(getEntityList().get(j));
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }

        return this;
    }

    public Hologram hideAll()
    {
        List<Player> players = new ArrayList<>(plugin.getServer().getOnlinePlayers());
        for (int i = -1; ++i < players.size(); )
        {
            Player player = players.get(i);
            for (int j = -1; ++j < getEntityList().size(); )
            {
                PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(getEntityList().get(j).getId());
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }

        return this;
    }

    public Hologram create()
    {
        getEntityList().clear();
        Location location = getLocation().subtract(0, getLineSpread(), 0).add(0, getLineSpread() * getLines().size(), 0);
        for (int i = -1; ++i < getLines().size(); )
        {
            EntityArmorStand entity = new EntityArmorStand(((CraftWorld) location.getWorld()).getHandle(), location.getX(), location.getY(), location.getZ());
            entity.setCustomName(Message.colorize(getLines().get(i)));
            entity.setCustomNameVisible(true);
            entity.setInvisible(true);
            entity.setGravity(false);
            entity.setInvulnerable(true);
            entity.setSmall(true);
            entity.setMarker(true);
            entity.setInvulnerable(true);
            getEntityList().add(entity);
            location = location.subtract(0, getLineSpread(), 0);
        }

        return this;
    }

    // Getters & Setters
    public Location getLocation()
    {
        return location;
    }

    public Hologram setLocation(Location location)
    {
        this.location = location;
        return this;
    }

    public List<String> getLines()
    {
        return lines;
    }

    public Hologram setLines(List<String> lines)
    {
        this.lines = lines;
        return this;
    }

    public List<EntityArmorStand> getEntityList()
    {
        return entityList;
    }

    public void setEntityList(List<EntityArmorStand> entityList)
    {
        this.entityList = entityList;
    }

    public Hologram setLineSpread(double lineSpread)
    {
        this.lineSpread = lineSpread;
        return this;
    }

    public double getLineSpread()
    {
        return lineSpread;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }
}
