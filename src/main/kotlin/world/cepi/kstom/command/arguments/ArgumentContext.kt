package world.cepi.kstom.command.arguments

import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.arguments.ArgumentType
import java.util.*

class ArgumentContextValue<T>(val lambda: CommandSender.() -> T?) {
    fun from(sender: CommandSender) = lambda(sender)
}

class ArgumentContext<T>(
    id: String? = null,
    val argument: Argument<out T>? = null,
    val lambda: CommandSender.() -> T?
) : Argument<ArgumentContextValue<T>>(id ?: "context${UUID.randomUUID()}") {

    init {
        setDefaultValue(ArgumentContextValue(lambda))
    }

    override fun parse(input: String): ArgumentContextValue<T> =
        ArgumentContextValue(argument?.let { { it.parse(input) } } ?: lambda)

    override fun parser(): String =
        (argument ?: ArgumentType.Integer(id)).parser()

    override fun toString() = "Context<$id>"

}