package me.youfang.common.ext

import org.apache.commons.io.FileSystem
import java.awt.Robot
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.event.KeyEvent
import java.io.IOException
import java.net.HttpURLConnection
import java.net.NetworkInterface
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.TimeUnit
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

fun String.toStringBuilder() = StringBuilder(this)

fun inputWithCheck(hint: String): String {
    print("${hint}->>> ")
    return readlnOrNull()?.trim()?.takeIf { it.isNotBlank() } ?: throw IllegalArgumentException("没有输入任何内容，请确认！！")
}

fun inputPassword(hint: String): String {
    // 控制台会打印访问异常，让他提前打印下
    System.console()
    print("${hint}->>> ")
    return System.console().readPassword()?.takeIf { it.isNotEmpty() }?.let { String(it) } ?: throw IllegalArgumentException("没有输入任何内容，请确认！！")
//    return readlnOrNull()?.takeIf { it.isNotBlank() } ?: throw IllegalArgumentException("没有输入任何内容，请确认！！")
}

fun String.containAnySubString(vararg subs: String, ignoreCase: Boolean = false): Boolean = subs.any { contains(it, ignoreCase) }

fun String.containAnySubString(subs: List<String>, ignoreCase: Boolean = false): Boolean = subs.any { contains(it, ignoreCase) }

fun List<String>.anyContainAnySubString(vararg subs: String, ignoreCase: Boolean = false): Boolean = any { it.containAnySubString(*subs, ignoreCase = ignoreCase) }

val currentOSIsWindows: Boolean = FileSystem.getCurrent() == FileSystem.WINDOWS

val currentOSIsLiunx = FileSystem.getCurrent() == FileSystem.LINUX

val currentTimeReadable: String
    get() = newSimpleDataFormatter("yyyy年MM月dd日 HH:mm:ss").format(Date())

val currentTimeForLog: String
    get() = newSimpleDataFormatter("yyyy.MM.dd_HH.mm.ss.SSSSSS").format(Date())

fun newSimpleDataFormatter(format: String) = SimpleDateFormat(format).apply { timeZone = TimeZone.getTimeZone("Asia/Shanghai") }

fun newShanghaiCalendar(): Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"))

private const val FIRST_RETRY_DURATION = 1 * 1000L
private const val SECOND_RETRY_DURATION = 3 * 1000L
private const val THIRD_RETRY_DURATION = 10 * 1000L

fun <R> requestWithRetry(retryCount: Int = 3, block: () -> R): R {
    var count = 0
    while (++count < retryCount) {
        try {
            return block()
        } catch (e: Exception) {
            e.printStackTrace()
            Thread.sleep(
                when (count) {
                    1 -> FIRST_RETRY_DURATION
                    2 -> SECOND_RETRY_DURATION
                    else -> THIRD_RETRY_DURATION
                }
            )
        }
    }
    return block()
}

@OptIn(ExperimentalContracts::class)
inline fun <R> runWithRetry(retryCount: Int = 3, block: () -> R): R {
    contract { callsInPlace(block, InvocationKind.AT_LEAST_ONCE) }
    var count = retryCount
    while (--count > 0) {
        try {
            return block()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    return block()
}

val publicIP: String
    get() {
        val url = URL("http://checkip.amazonaws.com")
        val connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 3 * 1000
        connection.readTimeout = 3 * 1000
        return connection.inputStream.bufferedReader().use { it.readLine() } ?: throw IOException("获取不到外网IP地址，请检查是否能正常上网")
    }

fun String.copyToClipBoard() {
    clipBoardString = this
}

var clipBoardString: String?
    get() = Toolkit.getDefaultToolkit().systemClipboard.getData(DataFlavor.stringFlavor)?.toString()
    set(value) = Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(value), null)

fun ByteArray.findPositionIn(array: ByteArray): Int {
    for (i in array.indices) {
        if (arrayMatch(array, i, this)) return i
    }
    return -1
}

private fun arrayMatch(source: ByteArray, position: Int, sub: ByteArray): Boolean {
    if (position + sub.size >= source.size) return false
    for (i in sub.indices) {
        if (sub[i] != source[position + i]) return false
    }
    return true
}

fun <T> List<T>.getFromLast(index: Int) = get(size - index - 1)

val mac by lazy { NetworkInterface.getNetworkInterfaces().toList().firstNotNullOf { network -> network.hardwareAddress.takeIf { it != null && it.isNotEmpty() } } }

val macReadable: String
    get() {
        val sb = java.lang.StringBuilder()
        for (i in mac.indices) {
            sb.append(String.format("%02X%s", mac[i], if ((i < mac.size - 1)) "-" else ""))
        }
        return sb.toString()
    }

fun Long.formatDuration(): String {
    val totalSecond = this / 1000
    val totalMinute = totalSecond / 60
    val totalHour = totalMinute / 60
    if (totalSecond <= 0) return "0秒"
    if (totalHour > 0) {
        val minute = totalMinute % 60
        val hour = totalMinute / 60
        val stringBuilder = StringBuilder()
        if (hour > 0) {
            stringBuilder.append("${hour}时")
        }
        if (minute > 0) {
            stringBuilder.append("${minute}分")
        }
        return stringBuilder.toString()
    } else {
        val minute = totalSecond / 60
        val second = totalSecond % 60
        val stringBuilder = StringBuilder()
        if (minute > 0) {
            stringBuilder.append("${minute}分")
        }
        if (second > 0) {
            stringBuilder.append("${second}秒")
        }
        return stringBuilder.toString()
    }
}

fun emulatorUserDoSomething() {
    runCatching {
        Robot().apply {
            keyPress(KeyEvent.VK_SCROLL_LOCK)
            keyRelease(KeyEvent.VK_SCROLL_LOCK)
            keyPress(KeyEvent.VK_SCROLL_LOCK)
            keyRelease(KeyEvent.VK_SCROLL_LOCK)
        }
    }.onFailure { it.printStackTrace() }
}

fun ExecutorService.submitAndGet(timeoutMills: Long, runnable: Runnable): Unit {
    submit(runnable).get(timeoutMills, TimeUnit.MILLISECONDS)
}

fun <T> ExecutorService.submitAndGet(callable: Callable<T>): T = submit(callable).get()