package org.saga.merchants;

import com.evilmidget38.UUIDFetcher;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

import java.util.UUID;
import java.util.Vector;

/**
 * Created by youle on 19/3/2016.
 */
public class Merchant {

    public static final BlockFace[] radial = { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };

    private transient EntityMerchant merchantEntity;

    private Location location;
    private String name;

    public Merchant(String name, Location location) {

        this.name = name;
        this.location = location;

        UUID entityID;

        try {
            entityID = UUIDFetcher.getUUIDOf(name);
        } catch (Exception e) {
            entityID = UUID.randomUUID();
        }

        if (entityID == null) entityID = UUID.randomUUID();

        merchantEntity = new EntityMerchant(name, entityID, location, location.getDirection());
        merchantEntity.spawn();

    }

}
