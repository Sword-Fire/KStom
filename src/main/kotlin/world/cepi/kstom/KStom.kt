package world.cepi.kstom

import net.minestom.server.extensions.Extension
import world.cepi.kstom.util.log

class KStom : Extension() {

    override fun initialize() {
        log.info("[KStom] has been enabled!")
    }

    override fun terminate() {
        log.info("[KStom] has been disabled!")
    }

}