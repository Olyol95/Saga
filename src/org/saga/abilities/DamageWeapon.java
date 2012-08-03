package org.saga.abilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.saga.listeners.events.SagaEntityDamageEvent;
import org.saga.player.SagaPlayer;
import org.saga.utility.TwoPointFunction;

public class DamageWeapon extends Ability{

	
	/**
	 * Weapon damage key.
	 */
	private static String WEAPON_DAMAGE_KEY = "weapon damage";
	
	/**
	 * Initialises using definition.
	 * 
	 * @param definition ability definition
	 */
	public DamageWeapon(AbilityDefinition definition) {
		
        super(definition);
	
	}

	
	// Ability usage:
	/* 
	 * (non-Javadoc)
	 * 
	 * @see org.saga.abilities.Ability#onPlayerInteractPlayer(org.bukkit.event.player.PlayerInteractEntityEvent, org.saga.player.SagaPlayer)
	 */
	@Override
	public boolean triggerDefend(SagaEntityDamageEvent event) {

		
		// Only pvp:
		if(event.getAttackerPlayer() == null) return false;
		
		// Targets item:
		ItemStack targetItem = event.getAttackerPlayer().getItemInHand();
		
		// Can be damaged:
		if(targetItem == null || targetItem.getType().getMaxDurability() < 1) return false;
		
		// Damage:
		double raw = getDefinition().getFunction(WEAPON_DAMAGE_KEY).value(getScore());
		double armour = getArmourMultiplier(event.getDefenderPlayer());
		short damage = TwoPointFunction.randomRound(raw * armour).shortValue();
		
		// Damage:
		targetItem.setDurability((short) (targetItem.getDurability() + damage));
		
		return true;
		
		
	}
	
	/**
	 * Gets armour multiplier.
	 * 
	 * @param sagaPlayer saga player
	 * @return armour multiplier
	 */
	private double getArmourMultiplier(SagaPlayer sagaPlayer) {

		
		double armour = 0;
		PlayerInventory inventory = sagaPlayer.getPlayer().getInventory();

		ItemStack helmet = inventory.getHelmet();
		ItemStack chestplate = inventory.getChestplate();
		ItemStack leggings = inventory.getLeggings();
		ItemStack boots = inventory.getBoots();
		
		if(helmet != null && (helmet.getType().equals(Material.DIAMOND_HELMET) || helmet.getType().equals(Material.IRON_HELMET))){
			armour += 0.15;
		}

		if(chestplate != null && (chestplate.getType().equals(Material.DIAMOND_CHESTPLATE) || chestplate.getType().equals(Material.IRON_CHESTPLATE))){
			armour += 0.4;
		}

		if(leggings != null && (leggings.getType().equals(Material.DIAMOND_LEGGINGS) || leggings.getType().equals(Material.IRON_LEGGINGS) )){
			armour += 0.3;
		}

		if(boots != null && (boots.getType().equals(Material.DIAMOND_BOOTS) || boots.getType().equals(Material.IRON_BOOTS))){
			armour += 0.15;
		}

		return armour;
		
		
	}
	
	
	
}