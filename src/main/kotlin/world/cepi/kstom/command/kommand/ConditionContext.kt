package world.cepi.kstom.command.kommand

import net.minestom.server.command.CommandSender
import net.minestom.server.entity.Player

data class ConditionContext(val sender: CommandSender, val player: Player?, val input: String)