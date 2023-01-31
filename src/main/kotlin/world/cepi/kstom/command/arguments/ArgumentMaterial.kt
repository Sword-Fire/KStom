package world.cepi.kstom.command.arguments

import net.minestom.server.command.builder.arguments.Argument
import net.minestom.server.command.builder.arguments.minecraft.ArgumentItemStack
import net.minestom.server.item.Material

class ArgumentMaterial(id: String) : Argument<Material>(id) {
    override fun parse(input: String) = parse(ArgumentItemStack(input)).material()
    override fun parser(): String =
        "minecraft:item_stack"
}