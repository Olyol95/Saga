package org.saga.abilities;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.saga.Saga;
import org.saga.messages.AbilityMessages;
import org.saga.messages.effects.StatsEffectHandler;
import org.saga.player.SagaPlayer;

import java.util.ArrayList;

public class ChopDown extends Ability {

	/**
	 * Tree size key.
	 */
	private static String TREE_SIZE_KEY = "tree size";

	/**
	 * Amount logs a tree can have.
	 */
	private static Integer LOGS_LIMIT = 150;

	/**
	 * Minimum leaves to logs ratio.
	 */
	private static Integer MINIMUM_LEAVES_LOGS_RATIO = 1;

	// Initialisation:
	/**
	 * Initialises using definition.
	 * 
	 * @param definition
	 *            ability definition
	 */
	public ChopDown(AbilityDefinition definition) {

		super(definition);

	}

	// Usage:
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.saga.abilities.Ability#handleInteractPreTrigger(org.bukkit.event.
	 * player.PlayerInteractEvent)
	 */
	@Override
	public boolean handleInteractPreTrigger(PlayerInteractEvent event) {

		Block clickedBlock = event.getClickedBlock();
		return isLog(clickedBlock) && handlePreTrigger();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.Ability#trigger(org.bukkit.event.player.
	 * PlayerInteractEvent)
	 */
	@Override
	public boolean triggerInteract(PlayerInteractEvent event) {

		Block clickedBlock = event.getClickedBlock();
		ItemStack itemHand = event.getItem();
		Player player = event.getPlayer();

		// Drops:
		boolean triggered = false;

		// Get the tree:
		ArrayList<Block> blocks = new ArrayList<>();
		blocks.add(clickedBlock);
		ArrayList<Block> leaves = new ArrayList<>();
		getTree(clickedBlock, blocks, leaves);

		// Check ratio:
		if (blocks.size() == 0)
			return false;
		double ratio = (double) leaves.size() / (double) blocks.size();

		if (ratio < MINIMUM_LEAVES_LOGS_RATIO) {

			getSagaLiving().message(AbilityMessages.chopDownNotTree(this));
			return false;

		}

		// Tree to big:
		Integer treeSize = getDefinition().getFunction(TREE_SIZE_KEY)
				.value(getScore()).intValue();
		if (treeSize < blocks.size()) {

			getSagaLiving().message(
					AbilityMessages.chopDownNotStroungEnough(this, treeSize));
			return false;

		}

		// Chop down:
		for (Block block : blocks) {

			// Send event:
			BlockBreakEvent eventB = new BlockBreakEvent(block, player);
			Saga.plugin().getServer().getPluginManager().callEvent(eventB);
			if (eventB.isCancelled())
				return triggered;

			block.breakNaturally(itemHand);
			getSagaLiving().damageTool();

			triggered = true;

		}

		// Play effect:
		player.playEffect(clickedBlock.getLocation(), Effect.STEP_SOUND, Material.OAK_LOG);

		if (getSagaLiving() instanceof SagaPlayer)
			StatsEffectHandler.playAnimateArm((SagaPlayer) getSagaLiving());

		return true;

	}

	/**
	 * Adds a tree to the given lists.
	 * 
	 * @param anchor
	 *            anchor block
	 * @param logs
	 *            logs
	 * @param leaves
	 *            leaves
	 */
	private void getTree(Block anchor, ArrayList<Block> logs, ArrayList<Block> leaves) {

		// Limits:
		if (logs.size() > LOGS_LIMIT)
			return;

		scanSurroundingRing(anchor, logs, leaves);

		// Shift anchor one up:
		anchor = anchor.getRelative(BlockFace.UP);

		scanSurroundingRing(anchor, logs, leaves);

		// Up:
		if (isLog(anchor) && !logs.contains(anchor)) {
			logs.add(anchor);
			getTree(anchor, logs, leaves);
		} else if (isLeaf(anchor) && !leaves.contains(anchor)) {
			leaves.add(anchor);
		}

	}

	private void scanSurroundingRing(Block anchor, ArrayList<Block> logs, ArrayList<Block> leaves) {
		BlockFace[] directions = {
				BlockFace.NORTH,
				BlockFace.NORTH_EAST,
				BlockFace.EAST,
				BlockFace.SOUTH_EAST,
				BlockFace.SOUTH,
				BlockFace.SOUTH_WEST,
				BlockFace.WEST,
				BlockFace.NORTH_WEST,
		};
		for (BlockFace blockFace: directions) {
			Block nextAnchor = anchor.getRelative(blockFace);
			if (isLog(nextAnchor) && !logs.contains(nextAnchor)) {
				logs.add(nextAnchor);
				getTree(nextAnchor, logs, leaves);
			} else if (isLeaf(nextAnchor) && !leaves.contains(nextAnchor)) {
				leaves.add(nextAnchor);
			}
		}
	}

	private boolean isLog(Block block) {
		if (block == null) { return false; }
	    Material material = block.getType();
		return material == Material.OAK_LOG
				|| material == Material.ACACIA_LOG
				|| material == Material.BIRCH_LOG
				|| material == Material.DARK_OAK_LOG
				|| material == Material.JUNGLE_LOG
				|| material == Material.SPRUCE_LOG;
	}

	private boolean isLeaf(Block block) {
		if (block == null) { return false; }
		Material material = block.getType();
		return material == Material.OAK_LEAVES
				|| material == Material.ACACIA_LEAVES
				|| material == Material.BIRCH_LEAVES
				|| material == Material.DARK_OAK_LEAVES
				|| material == Material.JUNGLE_LEAVES
				|| material == Material.SPRUCE_LEAVES;
	}

}
