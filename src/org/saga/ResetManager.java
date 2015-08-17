package org.saga;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.UUID;

/**
 * Created by Oliver on 16/08/2015.
 */
public class ResetManager extends BukkitRunnable {

    private static ResetManager instance;
    private static boolean cancelled = false;

    private Hashtable<UUID,Long> resetTickets;

    public ResetManager() {

        resetTickets = new Hashtable<>();
        runTaskTimer(Saga.plugin(), 0, 20);

    }

    @Override
    public void run() {

        Long currentTime = System.currentTimeMillis();

        ArrayList<UUID> toRemove = new ArrayList<>();

        for (UUID player: resetTickets.keySet()) {

            if (resetTickets.get(player) + 30000 <= currentTime) {

                toRemove.add(player);

            }

        }

        for (UUID player: toRemove) {

            resetTickets.remove(player);

        }

        if (cancelled) cancel();

    }

    public static void load() {

        instance = new ResetManager();

    }

    public static void unload() {

        cancelled = true;
        instance = null;

    }

    public static ResetManager getInstance() {

        return instance;

    }

    public void insertTicket(UUID player) {

        resetTickets.put(player, System.currentTimeMillis());

    }

    public boolean checkTicket(UUID player) {

        return resetTickets.containsKey(player);

    }

    public void removeTicket(UUID player) {

        resetTickets.remove(player);

    }

}
