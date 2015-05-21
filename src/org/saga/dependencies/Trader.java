package org.saga.dependencies;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface Trader {

	/**
	 * Gets the trading name.
	 * 
	 * @return trading name
	 */
	String getName();

	/**
	 * Adds coins.
	 * 
	 * @param amount
	 *            amount
	 * @return true if coins were added
	 */
	boolean addCoins(Double amount);

	/**
	 * Removes coins.
	 * 
	 * @param amount
	 *            amount
	 * @return true if coins were removed
	 */
	boolean removeCoins(Double amount);

	/**
	 * Gets traders currency.
	 * 
	 * @return currency amount of currency the trader has
	 */
	Double getCoins();

	/**
	 * Adds currency
	 * 
	 * @param itemStack
	 *            item stack
	 */
	void addItem(ItemStack itemStack);

	/**
	 * Removes currency
	 * 
	 * @param itemStack
	 *            irem stack
	 */
	void removeItem(ItemStack itemStack);

	/**
	 * Gets item count.
	 * 
	 * @param item
	 *            material
	 * @return
	 */
	Integer getAmount(Material material);

	/**
	 * Gets item price.
	 * 
	 * @return item price, null if none
	 */
	Double getSellPrice(Material material);

	/**
	 * Gets item price.
	 * 
	 * @return item price, null if none
	 */
	Double getBuyPrice(Material material);

}
