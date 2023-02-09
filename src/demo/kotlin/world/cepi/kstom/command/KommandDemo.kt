package world.cepi.kstom.command

import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import world.cepi.kstom.command.arguments.literal
import world.cepi.kstom.command.kommand.Kommand

/**
 * This demo's XY:
 *
 * Create a command that manages the level number for a player.
 */

object Old : Command("hey") {
    init {
        val add = ArgumentType.Literal("add")
        val remove = ArgumentType.Literal("remove")
        val set = ArgumentType.Literal("set")

        val amount = ArgumentType.Integer("amount").min(0)

        addSyntax({ sender, _ ->
            sender.sendMessage("Usage: add|remove|set <amount>")
        })

        addConditionalSyntax({ sender, _ -> sender is Player }, { sender, context ->
            val player = sender as Player

            player.level += context[amount]
        }, add, amount)

        addConditionalSyntax({ sender, _ -> sender is Player }, { sender, context ->
            val player = sender as Player

            player.level = (player.level - context[amount]).coerceAtLeast(0)

        }, remove, amount)

        addConditionalSyntax({ sender, _ -> sender is Player }, { sender, context ->
            val player = sender as Player

            player.level = context[amount]

        }, set, amount)

    }
}

object New : Kommand({
    val add by literal
    val remove by literal
    val set by literal

    val amount = ArgumentType.Integer("amount").min(0)

    syntax {
        sender.sendMessage("Usage: add|remove|set <amount>")
    }

    subcommand("sub") {
//        onlyPlayer()

        val delete by literal

        syntax(delete, amount) {
            player.level += !amount
        }
    }

    syntax(add, amount) {
        player.level += !amount
    }

    syntax(remove, amount) {
        player.level = (player.level - !amount).coerceAtLeast(0)
    }

    syntax(set, amount) {
        player.level = !amount
    }.condition { player?.level == 5 } // not realistic but demonstrates custom conditions


}, "hey")