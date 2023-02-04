package world.cepi.kstom.command.kommand

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.exception.ArgumentSyntaxException

data class ArgumentCallbackContext(val sender: CommandSender, val exception: ArgumentSyntaxException)