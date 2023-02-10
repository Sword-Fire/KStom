package world.cepi.kstom.command.arguments

import net.minestom.server.command.builder.arguments.ArgumentWord
import net.minestom.server.command.builder.exception.ArgumentSyntaxException
import world.cepi.kstom.Manager

fun ArgumentOnlinePlayer(id: String) = ArgumentWord(id)
    .map { Manager.connection.getPlayer(it) ?: throw ArgumentSyntaxException("Invalid player", it, 1) }
    .suggest(SuggestionIgnoreOption.IGNORE_CASE) {suggestion ->
        Manager.connection.onlinePlayers.forEach { suggestion.add(it.username) }
//        Manager.connection.onlinePlayers.map { it }
    }