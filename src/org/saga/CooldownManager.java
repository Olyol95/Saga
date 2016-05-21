package org.saga;

import net.minecraft.server.v1_9_R2.Tuple;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.Hashtable;
import java.util.UUID;

/**
 * Created by youle on 17/3/2016.
 */
public class CooldownManager {

    private static CooldownManager instance;

    private Hashtable<UUID, Hashtable<String,Tuple<Long,Integer>>> cooldownMap;

    public CooldownManager() {

        cooldownMap = new Hashtable<>();

    }

    public static void load() {

        instance = new CooldownManager();

    }

    public static void unload() {

        instance = null;

    }

    public static CooldownManager getInstance() {

        return instance;

    }

    public void putCooldown(Player player, String command, int cooldown) {

        UUID playerID = player.getUniqueId();

        if (!cooldownMap.containsKey(playerID)) {
            cooldownMap.put(playerID, new Hashtable<String, Tuple<Long, Integer>>());
        }

        cooldownMap.get(playerID).put(command, new Tuple<>(System.currentTimeMillis(),cooldown));

    }

    public int getTimeRemaining(Player player, String command) {

        UUID playerID = player.getUniqueId();

        if (!cooldownMap.containsKey(playerID)) {
            return 0;
        }

        if (!cooldownMap.get(playerID).containsKey(command)) {
            return 0;
        }

        Long castTime = cooldownMap.get(playerID).get(command).a();
        int cooldown = cooldownMap.get(playerID).get(command).b();

        int difference = (int) (cooldown - ((System.currentTimeMillis() - castTime) / 1000));

        return difference > 0 ? difference : 0;

    }

}
