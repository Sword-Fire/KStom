package world.cepi.kstom.command.kommand

import net.minestom.server.command.ConsoleSender
import net.minestom.server.entity.Player

abstract class Kondition<T : Kondition<T>> {
    abstract val conditions: MutableList<ConditionContext.() -> Boolean>
    abstract val t: T
    abstract val kommandReference: Kommand

    /**
     * 检查是否满足所有的条件。
     */
    fun conditionPasses(context: ConditionContext): Boolean = conditions.all { it(context) }

    /**
     * 添加条件。
     */
    fun condition(lambda: ConditionContext.() -> Boolean): T {
        conditions += lambda
        return t
    }

    fun onlyPlayers(): T = condition {
        if (sender !is Player) {
            kommandReference.notPlayerAction(sender)
            return@condition false
        }
        return@condition true
    }

    fun onlyConsole(): T = condition {
        if (sender !is ConsoleSender) {
            kommandReference.notConsoleAction(sender)
            return@condition false
        }
        return@condition true
    }
}