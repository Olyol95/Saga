package org.saga.commands;

import java.io.File;
import java.util.Collection;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.saga.ResetManager;
import org.saga.Saga;
import org.saga.SagaLogger;
import org.saga.abilities.AbilityDefinition;
import org.saga.config.AbilityConfiguration;
import org.saga.config.AttributeConfiguration;
import org.saga.config.EconomyConfiguration;
import org.saga.config.GeneralConfiguration;
import org.saga.dependencies.EconomyDependency;
import org.saga.dependencies.PermissionsDependency;
import org.saga.factions.Faction;
import org.saga.messages.*;
import org.saga.player.GuardianRune;
import org.saga.player.SagaPlayer;
import org.saga.saveload.Directory;
import org.saga.settlements.Bundle;
import org.saga.settlements.BundleManager;
import org.saga.settlements.SagaChunk;
import org.saga.settlements.Settlement;
import org.sk89q.Command;
import org.sk89q.CommandContext;
import org.sk89q.CommandPermissions;

public class PlayerCommands {

	// Info:
	@Command(aliases = { "stats" }, usage = "[page]", flags = "", desc = "Shows player stats.", min = 0, max = 1)
	@CommandPermissions({ "saga.user.player.stats" })
	public static void stats(CommandContext args, Saga plugin,
			SagaPlayer sagaPlayer) {

		Integer page = null;

		// Arguments:
		if (args.argsLength() == 1) {

			String argsPage = args.getString(0);

			try {
				page = Integer.parseInt(argsPage);
			} catch (NumberFormatException e) {
				sagaPlayer.message(GeneralMessages.notNumber(argsPage));
				return;
			}

		} else {
			page = 1;
		}

		// Inform:
		sagaPlayer.message(StatsMessages.stats(sagaPlayer, page - 1));

	}

	// Guardian stone:
	@Command(aliases = { "grdisable" }, usage = "", flags = "", desc = "Disable guardian rune.", min = 0, max = 0)
	@CommandPermissions({ "saga.user.player.guardrune.disable" })
	public static void disableGuardianStone(CommandContext args, Saga plugin,
			SagaPlayer sagaPlayer) {

		GuardianRune rune = sagaPlayer.getGuardRune();

		// Already disabled:
		if (!rune.isEnabled()) {
			sagaPlayer.message(PlayerMessages.alreadyDisabled(rune));
			return;
		}

		// Disable:
		rune.setEnabled(false);

		// Inform:
		sagaPlayer.message(PlayerMessages.disabled(rune));

	}

	@Command(aliases = { "grenable" }, usage = "", flags = "", desc = "Enable guardian rune.", min = 0, max = 0)
	@CommandPermissions({ "saga.user.player.guardrune.enable" })
	public static void enableGuardianStone(CommandContext args, Saga plugin,
			SagaPlayer sagaPlayer) {

		GuardianRune rune = sagaPlayer.getGuardRune();

		// Already enabled:
		if (rune.isEnabled()) {
			sagaPlayer.message(PlayerMessages.alreadyEnabled(rune));
			return;
		}

		// Disable:
		rune.setEnabled(true);

		// Inform:
		sagaPlayer.message(PlayerMessages.enabled(rune));

	}

	// Special chat:
	@Command(aliases = { "p" }, usage = "<message>", flags = "", desc = "Sends a message in the special chat.", min = 1)
	@CommandPermissions({ "saga.special.player.chat" })
	public static void specialChat(CommandContext args, Saga plugin,
			SagaPlayer sagaPlayer) {

		// Send special message:
		String message = PlayerMessages.specialChatMessage(
				sagaPlayer.getName(), args.getJoinedStrings(0));

		chatMessage(message);

	}

	private static void chatMessage(String message) {

		// Send the message to all players who have the correct permission:
		Collection<SagaPlayer> allPlayers = Saga.plugin().getLoadedPlayers();

		for (SagaPlayer loadedPlayer : allPlayers) {

			if (PermissionsDependency.hasPermission(loadedPlayer,
					PermissionsDependency.SPECIAL_CHAT_PERMISSION)) {
				loadedPlayer.message(message);
			}

		}

		// Log:
		SagaLogger.message(message);

	}

	// Info:
	@Command(aliases = { "phelp" }, usage = "[page]", flags = "", desc = "Display player help.", min = 0, max = 1)
	@CommandPermissions({ "saga.user.help.player" })
	public static void help(CommandContext args, Saga plugin,
			SagaPlayer sagaPlayer) {

		Integer page = null;

		// Arguments:
		if (args.argsLength() == 1) {
			try {
				page = Integer.parseInt(args.getString(0));
			} catch (NumberFormatException e) {
				sagaPlayer
						.message(GeneralMessages.notNumber(args.getString(0)));
				return;
			}
		} else {
			page = 0;
		}

		// Inform:
		sagaPlayer.message(HelpMessages.phelp(page - 1));

	}

	@Command(aliases = { "ability", "pabilityinfo", "abilityinfo" }, usage = "<ability_name>", flags = "", desc = "Display ability information.", min = 1)
	@CommandPermissions({ "saga.user.help.player.abilityinfo" })
	public static void abilityInfo(CommandContext args, Saga plugin,
			SagaPlayer sagaPlayer) {

		AbilityDefinition definition = null;

		String abilityName = null;

		// Arguments:
		abilityName = GeneralMessages.nameFromArg(args.getJoinedStrings(0));
		definition = AbilityConfiguration.config().getDefinition(abilityName);
		if (definition == null) {
			sagaPlayer.message(AbilityMessages.invalidAbility(abilityName));
			return;
		}

		// Inform:
		sagaPlayer.message(HelpMessages.ability(definition));

	}

	//wild
	// Reset:
	@Command(aliases = { "wilderness", "wild" }, usage = "", flags = "", desc = "Teleport to the wilderness.", min = 0, max = 0)
	@CommandPermissions({ "saga.user.player.wild" })
	public static void wild(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {

		Random random = new Random();

		int minX = GeneralConfiguration.config().getRandomTPCentreX() + GeneralConfiguration.config().getRandomTPMinX();
		int maxX = GeneralConfiguration.config().getRandomTPCentreX() + GeneralConfiguration.config().getRandomTPMaxX();
		int minZ = GeneralConfiguration.config().getRandomTPCentreZ() + GeneralConfiguration.config().getRandomTPMinZ();
		int maxZ = GeneralConfiguration.config().getRandomTPCentreZ() + GeneralConfiguration.config().getRandomTPMaxZ();

		int chunkx,chunkz;

		Location spawnLocation;

		do {

			chunkx = random.nextInt((maxX - minX) + 1) + minX;
			chunkz = random.nextInt((maxZ - minZ) + 1) + minZ;

			SagaChunk sagaChunk = BundleManager.manager().getSagaChunk(sagaPlayer.getLocation().getWorld().getName(),chunkx,chunkz);

			// Displacement:
			double spreadRadius = 6;
			Double x = 2 * spreadRadius * (Saga.RANDOM.nextDouble() - 0.5);
			Double z = 2 * spreadRadius * (Saga.RANDOM.nextDouble() - 0.5);
			Vector displacement = new Vector(x.intValue(), 2, z.intValue());

			// Shifted location:
			Double dx = 16 * chunkx + 7.5 + displacement.getX();
			Double dz = 16 * chunkz + 7.5 + displacement.getZ();
			Double dy = displacement.getY();

			spawnLocation = new Location(sagaPlayer.getPlayer().getWorld(), dx, sagaPlayer.getPlayer().getWorld().getHighestBlockYAt(dx.intValue(), dz.intValue()) + dy, dz);

			if (sagaChunk != null ||  spawnLocation.getY() < 10) {
				spawnLocation = null;
			}

		} while (spawnLocation == null);

		sagaPlayer.message(PlayerMessages.teleporting());
		sagaPlayer.teleport(spawnLocation);

	}

	// Reset:
	@Command(aliases = { "reset" }, usage = "", flags = "", desc = "Start again.", min = 0, max = 1)
	@CommandPermissions({ "saga.user.player.reset" })
	public static void resetData(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {

		ResetManager.getInstance().insertTicket(sagaPlayer.getPlayer().getUniqueId());
		sagaPlayer.message(PlayerMessages.resetMessage());

	}

	// Reset:
	@Command(aliases = { "resetconfirm" }, usage = "", flags = "", desc = "Confirm to start again.", min = 0, max = 0)
	@CommandPermissions({ "saga.user.player.reset.confirm" })
	public static void resetConfirm(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {

		if (ResetManager.getInstance().checkTicket(sagaPlayer.getPlayer().getUniqueId())) {

			sagaPlayer.message(PlayerMessages.resetBegun());

			Bundle selBundle = sagaPlayer.getBundle();

			if (selBundle != null) {

				if (selBundle.isOwner(sagaPlayer.getName())) {

					for (String player: selBundle.getMembers()) {

						if (!player.equals(sagaPlayer.getName())) {

							SagaPlayer sagaPlayer1 = Saga.plugin().getLoadedPlayer(player);

							selBundle.removeMember(sagaPlayer1);
							sagaPlayer1.message(SettlementMessages.wasKicked(sagaPlayer1, selBundle));

						} else {

							selBundle.removeMember(sagaPlayer);
							sagaPlayer.message(SettlementMessages.dissolved(selBundle));

						}

					}

					selBundle.delete();

				} else {

					selBundle.removeMember(sagaPlayer);

				}

			}

			Faction selFaction = sagaPlayer.getFaction();

			if (selFaction != null) {

				if (selFaction.isOwner(sagaPlayer.getName())) {

					for (String player: selFaction.getMembers()) {

						if (!player.equals(sagaPlayer.getName())) {

							SagaPlayer sagaPlayer1 = Saga.plugin().getLoadedPlayer(player);

							selFaction.removeMember(sagaPlayer1);
							sagaPlayer1.message(FactionMessages.wasKicked(sagaPlayer1, selFaction));

						} else {

							selFaction.removeMember(sagaPlayer);
							sagaPlayer.message(FactionMessages.disbanded(selFaction));

						}

					}

					selFaction.delete();

				} else {

					selFaction.removeMember(sagaPlayer);

				}

			}

			sagaPlayer.setExp(0);

			sagaPlayer.getGuardRune().discharge();

			for (String attribute: AttributeConfiguration.config().getAttributeNames()) {

				sagaPlayer.setAttributeScore(attribute,0);

			}

			for (String ability: AbilityConfiguration.config().getAbilityNames()) {

				sagaPlayer.setAblityScore(ability, 0);

			}

			sagaPlayer.getBundleInvites().clear();
			sagaPlayer.getFactionInvites().clear();

			EconomyDependency.removeCoins(sagaPlayer,sagaPlayer.getCoins());
			EconomyDependency.addCoins(sagaPlayer, 2000.0);

			File essentialsData = new File(Directory.ESSENTIALS_PLAYER_DATA.getDirectory()+sagaPlayer.getPlayer().getUniqueId()+".yml");

			if (essentialsData.exists()) {

				essentialsData.delete();

			}

			sagaPlayer.getPlayer().getInventory().clear();

			sagaPlayer.setOriginWorld(GeneralConfiguration.config().getDefaultWorld());

			sagaPlayer.getPlayer().performCommand("spawn "+sagaPlayer.getName()+" default");
			ResetManager.getInstance().removeTicket(sagaPlayer.getPlayer().getUniqueId());
			sagaPlayer.message(PlayerMessages.resetSuccessful());

		} else {

			sagaPlayer.message(PlayerMessages.resetFailedNoTicket());

		}

	}

}
