package org.saga.merchants;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.saga.Saga;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by youle on 19/3/2016.
 */
public class EntityMerchant {

    private Object entity;
    private Object worldServer;
    Class<?> EntityPlayer;
    Class<?> CraftPlayer;
    Class<?> PlayerConnection;
    Class<?> Entity;
    Class<?> EntityHuman;
    Class<?> EntityLiving;
    Class<?> WorldServer;
    Field aPField;

    public EntityMerchant(String name, UUID uuid, Location location, Vector direction) {

        try {
            Entity = Class.forName("net.minecraft.server." + Saga.plugin().getBukkitPackageVersion() + ".Entity");
            EntityPlayer = Class.forName("net.minecraft.server." + Saga.plugin().getBukkitPackageVersion() + ".EntityPlayer");
            EntityHuman = Class.forName("net.minecraft.server." + Saga.plugin().getBukkitPackageVersion() + ".EntityHuman");
            CraftPlayer = Class.forName("org.bukkit.craftbukkit." + Saga.plugin().getBukkitPackageVersion() + ".entity.CraftPlayer");
            PlayerConnection = Class.forName("net.minecraft.server." + Saga.plugin().getBukkitPackageVersion() + ".PlayerConnection");
            WorldServer = Class.forName("net.minecraft.server." + Saga.plugin().getBukkitPackageVersion() + ".WorldServer");
            EntityLiving = Class.forName("net.minecraft.server." + Saga.plugin().getBukkitPackageVersion() + ".EntityLiving");
            aPField = EntityLiving.getDeclaredField("aP");

            Class<?> MinecraftServer = Class.forName("net.minecraft.server." + Saga.plugin().getBukkitPackageVersion() + ".MinecraftServer");
            Method getServer = MinecraftServer.getDeclaredMethod("getServer");
            Object minecraftServer = getServer.invoke(null);

            Class<?> CraftWorld = Class.forName("org.bukkit.craftbukkit." + Saga.plugin().getBukkitPackageVersion() + ".CraftWorld");
            Class<?> World = Class.forName("net.minecraft.server." + Saga.plugin().getBukkitPackageVersion() + ".World");
            Method getHandle = CraftWorld.getDeclaredMethod("getHandle");
            worldServer = getHandle.invoke(CraftWorld.cast(location.getWorld()));

            Class<?> PlayerInteractManager = Class.forName("net.minecraft.server." + Saga.plugin().getBukkitPackageVersion() + ".PlayerInteractManager");
            Constructor playerInteractManagerConstructor = PlayerInteractManager.getConstructor(World);
            Object playerInteractManager = playerInteractManagerConstructor.newInstance(World.cast(worldServer));

            Constructor entityPlayerConstructor = EntityPlayer.getConstructor(MinecraftServer, WorldServer, GameProfile.class, PlayerInteractManager);
            GameProfile gameProfile = new GameProfile(uuid, name);

            Method getMinecraftSessionService = null;
            for (Method method : MinecraftServer.getMethods()) {
                if (method.getReturnType().equals(MinecraftSessionService.class)) {
                    getMinecraftSessionService = method;
                    break;
                }
            }
            Method fillProfileProperties = MinecraftSessionService.class.getDeclaredMethod("fillProfileProperties", GameProfile.class, boolean.class);
            gameProfile = (GameProfile) fillProfileProperties.invoke(getMinecraftSessionService.invoke(minecraftServer), gameProfile, true);

            entity = entityPlayerConstructor.newInstance(minecraftServer, worldServer, gameProfile, playerInteractManager);

            setLocation(location);
            ((net.minecraft.server.v1_9_R1.EntityPlayer)entity).getBukkitEntity().getLocation().setDirection(direction);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setLocation(Location location) {

        try {
            Method setLocation = Entity.getDeclaredMethod("setLocation", double.class, double.class, double.class, float.class, float.class);
            setLocation.invoke(entity, location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setHeadRotation(double rotation) {

        try {
            aPField.set(EntityPlayer.cast(entity), (byte) rotation * 256 / 360);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void spawn() {

        try {

            Method getHandle = CraftPlayer.getDeclaredMethod("getHandle");
            Field playerConnectionField = EntityPlayer.getDeclaredField("playerConnection");

            Class<?> PacketPlayOutPlayerInfo = Class.forName("net.minecraft.server." + Saga.plugin().getBukkitPackageVersion() + ".PacketPlayOutPlayerInfo");
            Class<?> PacketPlayOutNamedEntitySpawn = Class.forName("net.minecraft.server." + Saga.plugin().getBukkitPackageVersion() + ".PacketPlayOutNamedEntitySpawn");
            Class<?> PacketPlayOutEntityHeadRotation = Class.forName("net.minecraft.server." + Saga.plugin().getBukkitPackageVersion() + ".PacketPlayOutEntityHeadRotation");

            Class EnumPlayerInfoAction = Class.forName("net.minecraft.server." + Saga.plugin().getBukkitPackageVersion() + ".PacketPlayOutPlayerInfo$EnumPlayerInfoAction");
            Object ADD_PLAYER = EnumPlayerInfoAction.getDeclaredField("ADD_PLAYER").get(EnumPlayerInfoAction);
            Object REMOVE_PLAYER = EnumPlayerInfoAction.getDeclaredField("REMOVE_PLAYER").get(EnumPlayerInfoAction);
            Constructor<?> packetPlayOutPlayerInfoConstructor = PacketPlayOutPlayerInfo.getDeclaredConstructor(EnumPlayerInfoAction, Iterable.class);

            Constructor<?> packetPlayOutNamedEntitySpawnConstructor = PacketPlayOutNamedEntitySpawn.getConstructor(EntityHuman);
            Constructor<?> packetPlayOutEntityHeadRotationConstructor = PacketPlayOutEntityHeadRotation.getConstructor(Entity, byte.class);

            Class<?> Packet = Class.forName("net.minecraft.server." + Saga.plugin().getBukkitPackageVersion() + ".Packet");
            Method sendPacket = PlayerConnection.getDeclaredMethod("sendPacket", Packet);

            byte aP = ((Float)aPField.get(EntityPlayer.cast(entity))).byteValue();

            ArrayList<Object> playerCollection = new ArrayList<>();
            playerCollection.add(EntityPlayer.cast(entity));

            Object packetPlayOutPlayerInfoAdd = packetPlayOutPlayerInfoConstructor.newInstance(ADD_PLAYER, playerCollection);
            Object packetPlayOutNamedEntitySpawn = packetPlayOutNamedEntitySpawnConstructor.newInstance(EntityPlayer.cast(entity));
            Object packetPlayOutEntityHeadRotation = packetPlayOutEntityHeadRotationConstructor.newInstance(Entity.cast(entity), aP);
            Object packetPlayOutPlayerInfoRemove = packetPlayOutPlayerInfoConstructor.newInstance(REMOVE_PLAYER, playerCollection);

            for (Player player : Saga.plugin().getServer().getOnlinePlayers()) {

                Object playerConnection = playerConnectionField.get(getHandle.invoke(CraftPlayer.cast(player)));

                sendPacket.invoke(playerConnection, packetPlayOutPlayerInfoAdd);
                sendPacket.invoke(playerConnection, packetPlayOutNamedEntitySpawn);
                //sendPacket.invoke(playerConnection, packetPlayOutEntityHeadRotation);
                //sendPacket.invoke(playerConnection, packetPlayOutPlayerInfoRemove);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
