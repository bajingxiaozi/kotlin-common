package me.youfang.common.ext

import java.io.IOException
import java.nio.charset.Charset

fun exe(vararg params: String) {
    println("exe: ${params.joinToString(" ")}")
    val process = ProcessBuilder(*params).redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT).start()
    val shutdownHook = object : Thread() {
        override fun run() {
            process.destroy()
        }
    }
    Runtime.getRuntime().addShutdownHook(shutdownHook)
    process.waitFor()
    Runtime.getRuntime().removeShutdownHook(shutdownHook)
    if (process.exitValue() == 0) return
    throw IOException("exe error ${process.exitValue()}: ${params.joinToString(" ")}")
}

fun readCommand(vararg params: String): String {
    println("exe: ${params.joinToString(" ")}")
    val process = ProcessBuilder(*params).redirectErrorStream(true).start()
    val shutdownHook = object : Thread() {
        override fun run() {
            process.destroy()
        }
    }
    Runtime.getRuntime().addShutdownHook(shutdownHook)
    val consoleOutput = process.inputReader(Charset.forName("utf-8")).readText()
    process.waitFor()
    Runtime.getRuntime().removeShutdownHook(shutdownHook)
    return consoleOutput
}