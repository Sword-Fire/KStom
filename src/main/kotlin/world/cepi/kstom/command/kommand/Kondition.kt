package world.cepi.kstom.command.kommand

abstract class Kondition<T : Kondition<T>> {
    abstract val conditions: MutableList<Kommand.ConditionContext.() -> Boolean>
    abstract val t: T
    abstract val kommandReference: Kommand

    /**
     * 检查是否满足所有的条件。
     */
    fun conditionPasses(context: Kommand.ConditionContext): Boolean = conditions.all { it(context) }

    /**
     * 添加条件。
     */
    fun condition(lambda: Kommand.ConditionContext.() -> Boolean): T {
        conditions += lambda
        return t
    }

//    fun onlyPlayer(): T = condition {
//        if (sender !is Player) {
//            kommandReference.notPlayerAction(sender)
//            return@condition false
//        }
//        return@condition true
//    }
//
//    fun onlyConsole(): T = condition {
//        if (sender !is ConsoleSender) {
//            kommandReference.notConsoleAction(sender)
//            return@condition false
//        }
//        return@condition true
//    }
}