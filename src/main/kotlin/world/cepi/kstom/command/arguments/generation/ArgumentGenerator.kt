package world.cepi.kstom.command.arguments.generation

import net.minestom.server.command.builder.arguments.Argument
import world.cepi.kstom.command.kommand.ArgumentCallbackContext
import world.cepi.kstom.command.kommand.Kommand
import world.cepi.kstom.command.kommand.SyntaxContext
import kotlin.reflect.KClass

abstract class ArgumentGenerator<T : Any>(
    open val clazz: KClass<T>,
    val arguments: List<List<Argument<*>>>
) {

    init {
        CallbackGenerator.applyCallback(this)
    }

    var callback: ArgumentCallbackContext.() -> Unit = {  }
        set(value) {
            arguments.forEach { subArgs ->
                subArgs.forEach {
                    it.setCallback { sender, exception -> value(ArgumentCallbackContext(sender, exception)) }
                }
            }
            field = value
        }

    fun applySyntax(
        command: Kommand,
        vararg argumentsBefore: Argument<*>,
        lambda: SyntaxContext.(T) -> Unit
    ) = applySyntax(command, argumentsBefore, emptyArray(), lambda)

    @JvmName("arrayApplySyntax")
    fun applySyntax(
        command: Kommand,
        argumentsBefore: Array<out Argument<*>>,
        argumentsAfter: Array<out Argument<*>>,
        lambda: SyntaxContext.(T) -> Unit
    ) = arguments.forEachIndexed { index, it ->
        command.syntax(*argumentsBefore, *it.toTypedArray(), *argumentsAfter) {
            val instance = generate(SyntaxContext(sender, context), it.map { it.id }, index)

            lambda(this, instance)
        }
    }

    abstract fun generate(syntax: SyntaxContext, args: List<String>, index: Int): T
}