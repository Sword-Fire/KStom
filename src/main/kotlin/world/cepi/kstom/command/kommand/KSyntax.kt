package world.cepi.kstom.command.kommand

import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.entity.Player

class KSyntax(
    vararg val arguments: Argument<*>,
    override val conditions: MutableList<ConditionContext.() -> Boolean> = mutableListOf(),
    override val kommandReference: Kommand
) : Kondition<KSyntax>() {
    override val t: KSyntax
        get() = this

    /**
     * 将 [KSyntax] 对象所代表的语句添加到命令中。
     * 语句的定义是，命令的一组参数与一个执行器组成一条语句。
     */
    fun applyExecutor(executor: SyntaxContext.() -> Unit) {
        if (arguments.isEmpty()) {
            kommandReference.command.setDefaultExecutor { sender, context ->
                if (!conditionPasses(ConditionContext(sender, sender as? Player, context.input))) {
                    return@setDefaultExecutor
                }
                executor(SyntaxContext(sender, context))
            }
            return
        }
        kommandReference.command.addConditionalSyntax(
            { sender, string -> conditionPasses(ConditionContext(sender, sender as? Player, string ?: "")) },
            { sender, context -> executor(SyntaxContext(sender, context)) },
            *arguments
        )
        return
    }
}