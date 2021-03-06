package org.saga.listeners.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.saga.config.FactionConfiguration;
import org.saga.dependencies.PermissionsDependency;
import org.saga.listeners.events.SagaBuildEvent.BuildOverride;
import org.saga.listeners.events.SagaDamageEvent.PvPOverride;
import org.saga.messages.PlayerMessages;
import org.saga.messages.SettlementMessages;
import org.saga.player.SagaPlayer;
import org.saga.settlements.SagaChunk;

public class SagaEventHandler {

	public static void handleBuild(SagaBuildEvent event) {

		SagaChunk sagaChunk = event.getSagaChunk();
		SagaPlayer sagaPlayer = event.getSagaPlayer();

		// Forward to Saga chunk:
		if (sagaChunk != null)
			sagaChunk.onBuild(event);

		// Wilderness:
		else if (!PermissionsDependency.hasPermission(sagaPlayer,
				PermissionsDependency.WILDERNESS_BUILD_PERMISSION)) {

			event.addBuildOverride(BuildOverride.WILDERNESS_DENY);

			// Place:
			if (event.getWrappedEvent() instanceof BlockPlaceEvent) {
				Block block = event.getBlock();
				if (PermissionsDependency.hasPermission(sagaPlayer,
						PermissionsDependency.WILDERNESS_PLACE_PERMISSION + "."
								+ block.getType()))
					event.addBuildOverride(BuildOverride.WILDERNESS_SPECIFIC_BLOCK_ALLOW);
			}

			// Destroy:
			else if (event.getWrappedEvent() instanceof BlockBreakEvent) {
				Block block = event.getBlock();
				if (PermissionsDependency.hasPermission(sagaPlayer,
						PermissionsDependency.WILDERNESS_DESTROY_PERMISSION
								+ "." + block.getType()))
					event.addBuildOverride(BuildOverride.WILDERNESS_SPECIFIC_BLOCK_ALLOW);
			}
			
			else if (event.getWrappedEvent() instanceof HangingBreakByEntityEvent) {
				Entity block = event.getEntity();
				if (PermissionsDependency.hasPermission(sagaPlayer,
						PermissionsDependency.WILDERNESS_DESTROY_PERMISSION
								+ "." + Material.ITEM_FRAME.getId()))
					event.addBuildOverride(BuildOverride.WILDERNESS_SPECIFIC_BLOCK_ALLOW);
			}

		}

		// Conclude and inform:
		if (!event.getbuildOverride().isAllow()) {

			event.cancel();
			sagaPlayer.message(SettlementMessages.buildOverride(event
					.getbuildOverride()));

			return;

		}

	}

	public static void handleDamage(SagaDamageEvent event) {

		// Forward to Saga chunks:
		SagaChunk attackerChunk = event.attackerChunk;
		SagaChunk defenderChunk = event.defenderChunk;

		if (attackerChunk != null)
			attackerChunk.onDamage(event);
		if (defenderChunk != null && attackerChunk != defenderChunk)
			defenderChunk.onDamage(event);

		// PvP event:
		if (event.isPvP()) {

			// Damaged self:
			if (event.sagaDefender == event.sagaAttacker)
				event.addPvpOverride(PvPOverride.SELF_ALLOW);

			// Only faction versus faction:
			if (FactionConfiguration.config().factionOnlyPvp && !event.isFvF())
				event.addPvpOverride(PvPOverride.FACTION_ONLY_PVP_DENY);

			// Forward to factions:
			if (event.getAttackerFaction() != null)
				event.getAttackerFaction().onPvPAttack(event);
			if (event.getDefenderFaction() != null)
				event.getDefenderFaction().onPvPDefend(event);

			// PvP override:
			if (!event.getPvPOverride().isAllow()) {
				event.sagaAttacker.message(PlayerMessages.pvpOverride(event));
				event.cancel();
				return;
			}

		}

		if (event.isPvC()) {

			if (!event.getPvCOverride().isAllow()) {
				event.sagaAttacker.message(PlayerMessages.pvcOverride(event));
				event.cancel();
				return;
			}

		}

	}

}
