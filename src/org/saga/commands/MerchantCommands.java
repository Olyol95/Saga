package org.saga.commands;

import org.saga.Saga;
import org.saga.merchants.Merchant;
import org.saga.player.SagaPlayer;
import org.sk89q.Command;
import org.sk89q.CommandContext;
import org.sk89q.CommandPermissions;

/**
 * Created by youle on 19/3/2016.
 */
public class MerchantCommands {

    @Command(aliases = { "mcreate" }, usage = "<merchant_name>", flags = "", desc = "Create a new merchant.", min = 1, max = 1)
    @CommandPermissions({ "saga.user.merchant.create" })
    public static void create(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {

        Merchant merchant = new Merchant(args.getString(0),sagaPlayer.getLocation());

    }

    @Command(aliases = { "mdestroy" }, usage = "<merchant_name>", flags = "", desc = "Destroy a merchant.", min = 1, max = 1)
    @CommandPermissions({ "saga.user.merchant.destroy" })
    public static void destroy(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {



    }

    @Command(aliases = { "mrelocate", "mmove" }, usage = "<merchant_name>", flags = "", desc = "Relocate a merchant.", min = 1, max = 1)
    @CommandPermissions({ "saga.user.merchant.relocate" })
    public static void relocate(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {



    }

    @Command(aliases = { "mrename" }, usage = "<old_merchant_name> <new_merchant_name>", flags = "", desc = "Rename a merchant.", min = 2, max = 2)
    @CommandPermissions({ "saga.user.merchant.rename" })
    public static void rename(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {



    }

    @Command(aliases = { "mdecorate" }, usage = "<merchant_name> <body_part>", flags = "", desc = "Decorate a merchant.", min = 2, max = 2)
    @CommandPermissions({ "saga.user.merchant.decorate" })
    public static void decorate(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {



    }

    @Command(aliases = { "mgive" }, usage = "<merchant_name> <amount> <price> [item_slot]", flags = "", desc = "Give items to a merchant to sell.", min = 3, max = 4)
    @CommandPermissions({ "saga.user.merchant.give" })
    public static void give(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {



    }

    @Command(aliases = { "mtake" }, usage = "<merchant_name> <item_slot> [amount]", flags = "", desc = "Take items back from a merchant.", min = 2, max = 3)
    @CommandPermissions({ "saga.user.merchant.take" })
    public static void take(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {



    }

    @Command(aliases = { "malter" }, usage = "<merchant_name> <item_slot> <new_price>", flags = "", desc = "Alter the price of an item that the merchant is selling.", min = 1, max = 1)
    @CommandPermissions({ "saga.user.merchant.alter" })
    public static void alter(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {



    }

    @Command(aliases = { "mlist" }, usage = "", flags = "[item_name] [page_number]", desc = "List the merchants selling certain items.", min = 0, max = 1)
    @CommandPermissions({ "saga.user.merchant.list" })
    public static void list(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {



    }

    @Command(aliases = { "minfo" }, usage = "<merchant_name> [page]", flags = "", desc = "Get information about a merchant.", min = 1, max = 2)
    @CommandPermissions({ "saga.user.merchant.info" })
    public static void info(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {



    }

    @Command(aliases = { "mhire" }, usage = "<merchant_name>", flags = "", desc = "Hire a merchant.", min = 1, max = 1)
    @CommandPermissions({ "saga.user.merchant.hire" })
    public static void hire(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {



    }

    @Command(aliases = { "mfire" }, usage = "<merchant_name>", flags = "", desc = "Fire a merchant.", min = 1, max = 1)
    @CommandPermissions({ "saga.user.merchant.fire" })
    public static void fire(CommandContext args, Saga plugin, SagaPlayer sagaPlayer) {



    }

}
