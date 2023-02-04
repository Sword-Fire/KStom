package world.cepi.kstom.command

import net.minestom.server.command.CommandManager
import net.minestom.server.command.builder.Command
import world.cepi.kstom.Manager
import world.cepi.kstom.command.kommand.Kommand
import world.cepi.kstom.command.kommand.SyntaxContext

fun commandUnregistered(name: String, block: Kommand.() -> Unit) =
    Kommand(block, name)

fun CommandManager.command(name: String, block: Kommand.() -> Unit): Unit =
    Kommand(block, name).register()

fun CommandManager.register(name: String, block: SyntaxContext.() -> Unit): Unit =
    register(Command(name).apply {
        setDefaultExecutor { sender, context -> block(SyntaxContext(sender, context)) }
    })

fun Command.register() {
    Manager.command.register(this)
}

fun Command.unregister() {
    Manager.command.unregister(this)
}