package org.saga.messages.effects;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.saga.Saga;
import org.saga.player.SagaLiving;
import org.saga.player.SagaPlayer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;

public class StatsEffectHandler {

	public static void playAnimateArm(SagaPlayer sagaPlayer) {

		try {

			Class<?> CraftPlayer = Class.forName("org.bukkit.craftbukkit."+Saga.plugin().getBukkitPackageVersion()+".entity.CraftPlayer");
			Class<?> EntityPlayer = Class.forName("net.minecraft.server." + Saga.plugin().getBukkitPackageVersion() + ".EntityPlayer");
			Class<?> PlayerConnection = Class.forName("net.minecraft.server."+Saga.plugin().getBukkitPackageVersion()+".PlayerConnection");
			Class<?> PacketPlayInArmAnimation = Class.forName("net.minecraft.server."+Saga.plugin().getBukkitPackageVersion()+".PacketPlayInArmAnimation");
			Method getHandle = CraftPlayer.getDeclaredMethod("getHandle");
			Method a = PlayerConnection.getDeclaredMethod("a",PacketPlayInArmAnimation);
			Field playerConnection = EntityPlayer.getDeclaredField("playerConnection");

			Player player = sagaPlayer.getPlayer();

			a.invoke(playerConnection.get(EntityPlayer.cast(getHandle.invoke(CraftPlayer.cast(player)))),PacketPlayInArmAnimation.newInstance());

			//Location loc = player.getLocation();

			/**((CraftServer) Bukkit.getServer())
			 .getServer()
			 .getPlayerList()
			 .sendPacketNearby(
			 loc.getX(),
			 loc.getY(),
			 loc.getZ(),
			 64,
			 ((CraftWorld) loc.getWorld()).getHandle().dimension,
			 new PacketPlayInArmAnimation());*/

		} catch (Exception e) {

			Saga.plugin().getLogger().log(Level.SEVERE, "Error enabling Saga! is it up to date?");
			e.printStackTrace();

			Saga.plugin().onDisable();

		}

	}

	public static void playParry(SagaLiving sagaliving) {

		LivingEntity living = sagaliving.getWrapped();

		Location loc = living.getLocation();

		loc.getWorld().playSound(loc, Sound.ANVIL_LAND, 1.0f, 2.0f);

	}

	public static void playParry(SagaPlayer sagaliving) {

		LivingEntity living = sagaliving.getWrapped();

		Location loc = living.getLocation();

		loc.getWorld().playSound(loc, Sound.ANVIL_LAND, 1.0f, 2.0f);

	}

	public static void playLevelUp(SagaPlayer sagaPlayer) {

		sagaPlayer.playGlobalSound(Sound.LEVEL_UP, 1.0F, 0.5F);

	}

	public static void playSpellCast(SagaLiving sagaLiving) {

		// Smoke:
		for (int i = 5; i <= 12; i++) {
			sagaLiving.playGlobalEffect(Effect.SMOKE, i);
		}

		// Sound:
		sagaLiving.playGlobalEffect(Effect.GHAST_SHOOT, 0);

	}

	public static void playCrush(SagaLiving sagaLiving) {

		Location loc = sagaLiving.getLocation();

		double deg = 0.0;
		double radius = 0.5;

		int[] datas = new int[] { 5, 2, 1, 0, 3, 6, 7, 8 };

		for (int i = 0; i < 8; i++) {

			double nx = radius * Math.cos(deg);
			double nz = radius * -Math.sin(deg);
			Location target = loc.clone().add(nx, 0.0, nz);
			loc.getWorld().playEffect(target, Effect.SMOKE, datas[i]);
			deg += Math.PI / 4.0;

		}

		// Sound:
		loc.getWorld().playSound(loc, Sound.FALL_BIG, 0.5f, 0.5f);

	}

	public static void playRecharge(SagaLiving sagaLiving) {

		// Flames:
		sagaLiving.playGlobalEffect(Effect.BLAZE_SHOOT, 0);
		sagaLiving.playGlobalEffect(Effect.MOBSPAWNER_FLAMES, 6);

		// Sound:
		sagaLiving.playGlobalEffect(Effect.GHAST_SHOOT, 0);

	}

	public static void playSign(SagaPlayer sagaPlayer) {

		// Flames:
		// sagaPlayer.playGlobalEffect(Effect.BLAZE_SHOOT, 0);
		sagaPlayer.playGlobalEffect(Effect.MOBSPAWNER_FLAMES, 6);

		// Sound:
		sagaPlayer.playEffect(Effect.CLICK1, 0);

	}

}
