package world.cepi.kstom.command.arguments

import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.arguments.ArgumentLiteral
import net.minestom.server.command.builder.suggestion.SuggestionEntry
import org.jetbrains.annotations.Contract
import world.cepi.kstom.Manager
import world.cepi.kstom.command.kommand.Kommand
import kotlin.reflect.KProperty

class Literal {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): ArgumentLiteral {
        return ArgumentLiteral(property.name)
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: ArgumentLiteral) {

    }
}

/**
 * Automatically generates an [ArgumentLiteral] based on the String being passed
 *
 * @return an [ArgumentLiteral] based on the String being passed
 */
val literal get() = Literal()

fun String.literal() = ArgumentLiteral(this)

fun <T> Argument<T>.defaultValue(value: T): Argument<T> =
    this.setDefaultValue { value }

open class SuggestionIgnoreOption(val modifier: (String) -> String) {
    object NONE : SuggestionIgnoreOption({ it })
    object IGNORE_CASE : SuggestionIgnoreOption({ it.lowercase() })
}

/**
 * Suggests a set of [SuggestionEntry]s.
 * Will automatically sort and filter entries to match with input
 *
 * @param lambda The lambda to process the args
 *
 * @return The argument that had its suggestion callback set
 */
@Contract("_ -> this")
fun <T> Argument<T>.suggestEntries(
    suggestionIgnoreOption: SuggestionIgnoreOption = SuggestionIgnoreOption.IGNORE_CASE,
    lambda: Kommand.SyntaxContext.() -> List<SuggestionEntry>
): Argument<T> = this.setSuggestionCallback { sender, context, suggestion ->
    lambda(Kommand.SyntaxContext(sender, context))
        .filter {
            val raw = context.getRaw(this)
            if (raw.isNullOrBlank() || (raw.length == 1 && raw[0] == '\u0000')) true
            else suggestionIgnoreOption.modifier(it.entry).startsWith(suggestionIgnoreOption.modifier(context.getRaw(this)))
        }
        .sortedBy { it.entry }
        .forEach { suggestion.addEntry(it) }
}

fun <T> Argument<T>.suggest(
    suggestionIgnoreOption: SuggestionIgnoreOption = SuggestionIgnoreOption.IGNORE_CASE,
    lambda: Kommand.SyntaxContext.(MutableList<String>) -> Unit
): Argument<T> {
    return this.suggestEntries(suggestionIgnoreOption) {
        val list = mutableListOf<String>()
        lambda(this, list)
        list.map { SuggestionEntry(it) }
    }
}

fun <T> Argument<T>.suggestAllPlayers() = suggest { list ->
    Manager.connection.onlinePlayers.forEach { list.add(it.username) }
}