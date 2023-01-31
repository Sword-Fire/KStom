package world.cepi.kstom.command.arguments

import net.minestom.server.command.builder.arguments.Argument

object ShellArgument : Argument<Unit>("shell") {
    override fun parse(input: String) {

    }

    override fun parser(): String {
        return ""
    }

    override fun toString() = "Shell"
}