package me.youfang.common.ext

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract
import kotlin.random.Random

fun String.toStringBuilder() = StringBuilder(this)

fun inputWithCheck(hint: String): String {
    print("${hint}->>> ")
    return readlnOrNull()?.trim()?.takeIf { it.isNotBlank() } ?: throw IllegalArgumentException("没有输入任何内容，请确认！！")
}

fun inputPassword(hint: String): String {
    print("${hint}->>> ")
//    return System.console().readPassword()
    return readlnOrNull()?.takeIf { it.isNotBlank() } ?: throw IllegalArgumentException("没有输入任何内容，请确认！！")
}

fun String.containAny(vararg elements: String): Boolean = elements.any { contains(it) }

fun List<String>.containAny(vararg elements: String): Boolean = any { it.containAny(*elements) }

val windowsSystem: Boolean = System.getProperty("os.name").contains("Windows")

val currentTimeReadable: String
    get() = newSimpleDataFormatter("yyyy年MM月dd日 HH:mm:ss").format(Date())

val currentTimeForLog: String
    get() = newSimpleDataFormatter("yyyy_MM_dd__HH_mm_ss").format(Date())

fun newSimpleDataFormatter(format: String) = SimpleDateFormat(format).apply { timeZone = TimeZone.getTimeZone("Asia/Shanghai") }

fun newShanghaiCalendar(): Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"))

@OptIn(ExperimentalContracts::class)
inline fun <R> runWithRetry(retryCount: Int = 3, block: () -> R): R {
    contract { callsInPlace(block, InvocationKind.AT_LEAST_ONCE) }
    var count = retryCount
    while (--count > 0) {
        try {
            return block()
        } catch (exception: Exception) {
            exception.printStackTrace()
            Thread.sleep(Random.nextLong(60, 120) * 1000)
        }
    }
    return block()
}

val publicIP: String
    get() = URL("http://checkip.amazonaws.com").openStream().bufferedReader().use { it.readLine() } ?: throw IOException("获取不到外网IP地址，请检查是否能正常上网")

fun String.copyToClipBoard() = Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(this), null)