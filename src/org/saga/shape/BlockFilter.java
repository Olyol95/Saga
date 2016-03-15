package org.saga.shape;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.HashSet;

public class BlockFilter implements ShapeFilter {

	/**
	 * Materials.
	 */
	private HashSet<Material> materials = new HashSet<>();

	/**
	 * If true then the check will return the reverse result.
	 */
	private boolean flip = false;

	// Initialisation:
	/**
	 * Initialises.
	 * 
	 */
	public BlockFilter() {
	}

	/**
	 * Adds a material.
	 * 
	 * @param material
	 *            material
	 */
	public void addMaterial(Material material) {

		materials.add(material);

	}

	/**
	 * Flips the check result.
	 * 
	 */
	public void flip() {

		flip = true;

	}

	// Filtering:
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.saga.shape.ShapeFilter#checkBlock(org.bukkit.block.Block)
	 */
	@Override
	public boolean checkBlock(Block block) {

		if (flip)
			return !materials.contains(block.getType());

		return materials.contains(block.getType());

	}

}
