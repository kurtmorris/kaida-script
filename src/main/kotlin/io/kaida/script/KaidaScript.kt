package io.kaida.script

import kotlin.script.experimental.annotations.KotlinScript
import kotlin.script.experimental.api.*
import kotlin.script.experimental.jvm.dependenciesFromClassloader
import kotlin.script.experimental.jvm.jvm

// The KotlinScript annotation marks a class that can serve as a reference to the script definition for
// `createJvmCompilationConfigurationFromTemplate` call as well as for the discovery mechanism
// The marked class also become the base class for defined script type (unless redefined in the configuration)
@KotlinScript(
    // file name extension by which this script type is recognized by mechanisms built into scripting compiler plugin
    // and IDE support, it is recommended to use double extension with the last one being "kts", so some non-specific
    // scripting support could be used, e.g. in IDE, if the specific support is not installed.
    displayName = "Kaida Script",
    fileExtension = "kaida.kts",
    compilationConfiguration = KaidaScriptDefinition::class
)
abstract class KaidaScript  {
    abstract fun on()
    open fun stop() {}
}

internal object KaidaScriptDefinition : ScriptCompilationConfiguration(
    {
        jvm {
            dependenciesFromClassloader(wholeClasspath = true)
        }
        ide {
            acceptedLocations(ScriptAcceptedLocation.Everywhere)
        }
        compilerOptions.append("-Xadd-modules=ALL-MODULE-PATH")
    }
) {
    private fun readResolve(): Any = KaidaScriptDefinition
}