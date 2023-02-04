package world.cepi.kstom.command.kommand

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.CommandContext
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.entity.Player

data class SyntaxContext(val sender: CommandSender, val context: CommandContext) {
    val player by lazy { sender as Player }
    val commandName = context.commandName
    operator fun <T> get(argument: Argument<T>): T = context[argument]
    operator fun <T> Argument<T>.not(): T = context[this]
    fun raw(argument: Argument<*>): String = context.getRaw(argument)
}