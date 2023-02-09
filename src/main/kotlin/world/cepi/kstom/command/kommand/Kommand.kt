package world.cepi.kstom.command.kommand

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.minestom.server.command.CommandSender
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.CommandExecutor
import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.arguments.ArgumentLiteral
import org.jetbrains.annotations.Contract
import world.cepi.kstom.Manager
import kotlin.coroutines.CoroutineContext

open class Kommand(initAction: Kommand.() -> Unit = {}, name: String, vararg aliases: String) : Kondition<Kommand>() {

    override val conditions: MutableList<ConditionContext.() -> Boolean> = mutableListOf()
    override val t: Kommand
        get() = this
    override val kommandReference: Kommand by ::t

    val command = Command(name, *aliases)
    val helpArg = ArgumentLiteral("help")
    private var helpAction: SyntaxContext.() -> Unit = {}
    var notPlayerAction: (CommandSender) -> Unit = { }
    var notConsoleAction: (CommandSender) -> Unit = { }
    var enableHelpArg = false

    init {
        default { helpAction() }
        initAction()
        // 必须放在 initAction() 之后
        if (enableHelpArg) {
            syntax(helpArg) { helpAction() }
        }
    }

    @Contract(pure = true)
    fun syntax(
        vararg arguments: Argument<*> = arrayOf(),
    ) = KSyntax(*arguments, conditions = conditions.toMutableList(), kommandReference = this)

    fun syntax(
        vararg arguments: Argument<*> = arrayOf(),
        executor: SyntaxContext.() -> Unit
    ) = KSyntax(*arguments, conditions = conditions.toMutableList(), kommandReference = this).also { it.applyExecutor(executor) }

    fun syntaxSuspending(
        context: CoroutineContext = Dispatchers.IO,
        vararg arguments: Argument<*> = arrayOf(),
        executor: suspend SyntaxContext.() -> Unit
    ) = KSyntax(*arguments, conditions = conditions.toMutableList(), kommandReference = this).applyExecutor {
        CoroutineScope(context).launch { executor() }
    }

    inline fun argumentCallback(arg: Argument<*>, crossinline lambda: ArgumentCallbackContext.() -> Unit) {
        command.setArgumentCallback({ source, value -> lambda(ArgumentCallbackContext(source, value)) }, arg)
    }

    inline fun <T> Argument<T>.failCallback(crossinline lambda: ArgumentCallbackContext.() -> Unit) {
        setCallback { sender, exception -> lambda(ArgumentCallbackContext(sender, exception)) }
    }

    inline fun default(crossinline block: SyntaxContext.() -> Unit) {
        command.defaultExecutor = CommandExecutor { sender, args -> block(SyntaxContext(sender, args)) }
    }

    inline fun defaultSuspending(context: CoroutineContext = Dispatchers.IO, crossinline block: suspend SyntaxContext.() -> Unit) {
        command.defaultExecutor = CommandExecutor { sender, args ->
            CoroutineScope(context).launch { block(SyntaxContext(sender, args)) }
        }
    }

    fun addSubcommands(vararg subcommands: Command) {
        subcommands.forEach { command.addSubcommand(it) }
    }

    fun addSubcommands(vararg subcommands: Kommand) {
        subcommands.forEach { command.addSubcommand(it.command) }
    }

    /**
     * 创建一个新的 Kommand 并将它作为当前 Kommand 的子命令。新的 Kommand 默认使用现有 Kommand 的 [notPlayerAction] , [notConsoleAction] , [helpAction]。
     */
    fun subcommand(name: String, vararg aliases: String, subK: Kommand.() -> Unit) {
        val kommand = Kommand(subK, name, *aliases)
        kommand.helpAction = helpAction
        kommand.notPlayerAction = notPlayerAction
        kommand.notConsoleAction = notConsoleAction
        addSubcommands(kommand)
    }

    fun register() {
        Manager.command.register(command)
    }

    fun unregister() {
        Manager.command.unregister(command)
    }

    /**
     * 注册帮助信息。子命令会继承父命令的帮助信息，但子命令默认不启用 help 命令参数。
     */
    fun help(action: SyntaxContext.() -> Unit) {
        helpAction = action
        enableHelpArg = true
    }

}