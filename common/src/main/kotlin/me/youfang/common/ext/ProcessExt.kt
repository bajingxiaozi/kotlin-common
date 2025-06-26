package me.youfang.common.ext

import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.util.Enumeration
import java.util.StringTokenizer
import java.util.UUID

private val Process.normalExit
    get() = exitValue() == 0

class ProcessBuilderWrapper(vararg commands: String) {

    var dir: File? = null
    var silent = false
    var readOutput = false
    var printToScreen = true
    var printLog = true
    var extraCommandReadable: String? = null
    var outputCallback: (String) -> Unit = {}
    private val fixCommands: List<String> = if (commands.size > 1) commands.toList() else (StringTokenizer(commands[0]) as Enumeration<String>).toList()
    private val commandReadable by lazy {
        val stringBuilder = StringBuilder("命令=${fixCommands.joinToString(" ")}")
        if (dir != null) {
            stringBuilder.append(", 目录=$dir")
        }
        if (extraCommandReadable != null) {
            stringBuilder.appendLine()
            stringBuilder.append(extraCommandReadable)
        }
        stringBuilder.toString()
    }

    fun exe(): ProcessResult {
        var stage: LogStage? = null
        if (printLog) {
            stage = ds("执行命令", commandReadable)
        }
        val process = ProcessBuilder().command(fixCommands).redirectErrorStream(true).apply {
            if (!readOutput) {
                redirectOutput(ProcessBuilder.Redirect.INHERIT)
                redirectInput(ProcessBuilder.Redirect.INHERIT)
            }
        }.directory(dir ?: generateDefaultWorkDir()).start()
        val shutdownHook = object : Thread() {
            override fun run() {
                d("中断操作", "操作仍在执行中, $commandReadable")
                process.destroy()
            }
        }
        Runtime.getRuntime().addShutdownHook(shutdownHook)
        val output = mutableListOf<String>()
        if (readOutput) {
            val reader = process.inputStream.bufferedReader()
            val line = StringBuilder()
            while (true) {
                val cInt = reader.read()
                if (cInt == -1) {
                    val s = line.toString()
                    outputCallback(s)
                    output.add(s)
                    if (printToScreen) {
                        print(s)
                    }
                    line.clear()
                    break
                }
                val c = cInt.toChar()
                if (c == '\r' || c == '\n') {
                    val s = line.toString()
                    outputCallback(s)
                    output.add(s)
                    if (printToScreen) {
                        print(s)
                        print(c)
                    }
                    line.clear()
                } else {
                    line.append(c)
                }
            }
        }
        process.waitFor()
        Runtime.getRuntime().removeShutdownHook(shutdownHook)
        if (printLog) {
            stage?.de()
        }
        if (process.normalExit) return ProcessResult(true, output)
        if (printLog) {
            w("操作执行失败!!!", commandReadable)
        }
        if (!silent) throw IOException("操作执行失败, $commandReadable")
        return ProcessResult(false, output)
    }

}

fun exeWithOutput(vararg commands: String) = exeWithOutput(null, *commands)

fun exeWithOutput(dir: File?, vararg commands: String) = ProcessBuilderWrapper(*commands).apply {
    this.dir = dir
    readOutput = true
}.exe()

fun exe(vararg commands: String) = exe(null, *commands)

fun exe(dir: File?, vararg commands: String) = ProcessBuilderWrapper(*commands).apply { this.dir = dir }.exe()

fun exeSilent(dir: File?, vararg commands: String) = ProcessBuilderWrapper(*commands).apply {
    this.dir = dir
    this.silent = true
}.exe()

fun exeSilent(vararg commands: String) = exeSilent(null, *commands)

fun readCommandSafe(vararg commands: String) = readCommandSafe(null, *commands)

fun readCommandSafe(dir: File? = null, vararg commands: String) = ProcessBuilderWrapper(*commands).apply {
    readOutput = true
    printToScreen = false
    printLog = false
    silent = true
    this.dir = dir
}.exe()

fun readCommand(vararg commands: String): ProcessResult = readCommand(null, *commands)

fun readCommand(dir: File? = null, vararg commands: String) = ProcessBuilderWrapper(*commands).apply {
    readOutput = true
    printToScreen = false
    printLog = false
    this.dir = dir
}.exe()

fun addShutdownHook(hook: () -> Unit) {
    Runtime.getRuntime().addShutdownHook(object : Thread() {
        override fun run() {
            hook()
        }
    })
}

var generateDefaultWorkDir: () -> File? = { null }

fun File.runBash(command: String) {
    val tempMakeScriptFile = File(this, "tmp_${UUID.randomUUID()}.sh").apply {
        FileUtils.forceMkdirParent(this)
        writeText(command)
        deleteOnExit()
    }

    readCommand("chmod 777 ${tempMakeScriptFile.absolutePath}")
    ProcessBuilderWrapper("bash", tempMakeScriptFile.absolutePath).apply {
        extraCommandReadable = command.trim()
        this.dir = this@runBash
    }.exe()
}

fun runBash(command: String) = FileUtils.getTempDirectory().runBash(command)

fun runBashWithOutput(command: String) = FileUtils.getTempDirectory().runBashWithOutput(command)

fun File.runBashWithOutput(command: String): ProcessResult {
    val tempMakeScriptFile = File(this, "tmp_${UUID.randomUUID()}.sh").apply {
        FileUtils.forceMkdirParent(this)
        writeText(command)
        deleteOnExit()
    }
    readCommand("chmod 777 ${tempMakeScriptFile.absolutePath}")
    return ProcessBuilderWrapper("bash", tempMakeScriptFile.absolutePath).apply {
        readOutput = true
        printToScreen = true
        printLog = true
        silent = true
        extraCommandReadable = command.trim()
        this.dir = this@runBashWithOutput
    }.exe()
}

data class ProcessResult(
    val success: Boolean,
    val output: List<String>
)