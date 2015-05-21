package org.saga.shape;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

public class BlockBottomFilter extends BlockFilter {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.saga.shape.BlockFilter#checkBlock(org.bukkit.block.Block)
	 */
	@Override
	public boolean checkBlock(Block block) {

		return !super.checkBlock(block.getRelative(BlockFace.DOWN)) && super.checkBlock(block);

	}

}
