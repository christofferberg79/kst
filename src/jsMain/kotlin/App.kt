import io.kvision.Application
import io.kvision.html.div
import io.kvision.panel.root
import io.kvision.startApplication

class App : Application() {
    override fun start() {
        root("root") {
            div("Hello from KVision")
        }
    }
}

fun main() {
    startApplication(::App)
}