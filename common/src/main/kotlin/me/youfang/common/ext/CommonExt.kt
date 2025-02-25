package me.youfang.common.ext

import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.io.IOException
import java.net.NetworkInterface
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
    return System.console().readPassword()?.takeIf { it.isNotEmpty() }?.let { String(it) } ?: throw IllegalArgumentException("没有输入任何内容，请确认！！")
//    return readlnOrNull()?.takeIf { it.isNotBlank() } ?: throw IllegalArgumentException("没有输入任何内容，请确认！！")
}

fun String.containAny(vararg subs: String): Boolean = subs.any { contains(it) }

fun String.containAny(subs: List<String>): Boolean = subs.any { contains(it) }

fun List<String>.containAny(vararg subs: String): Boolean = any { it.containAny(*subs) }

enum class OS {
    WINDOWS, LIUNX
}

val currentOS = if (System.getProperty("os.name").contains("Windows")) OS.WINDOWS else OS.LIUNX

val currentOSIsWindows: Boolean = currentOS == OS.WINDOWS

val currentTimeReadable: String
    get() = newSimpleDataFormatter("yyyy年MM月dd日 HH:mm:ss").format(Date())

val currentTimeForLog: String
    get() = newSimpleDataFormatter("yyyy_MM_dd__HH_mm_ss").format(Date())

fun newSimpleDataFormatter(format: String) = SimpleDateFormat(format).apply { timeZone = TimeZone.getTimeZone("Asia/Shanghai") }

fun newShanghaiCalendar(): Calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"))

@OptIn(ExperimentalContracts::class)
inline fun <R> requestWithRetry(retryCount: Int = 3, block: () -> R): R {
    contract { callsInPlace(block, InvocationKind.AT_LEAST_ONCE) }
    var count = retryCount
    while (--count > 0) {
        try {
            return block()
        } catch (e: Exception) {
            e.printStackTrace()
            Thread.sleep(Random.nextLong(1, 10) * 1000)
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
    get() = URL("http://checkip.amazonaws.com").openStream().bufferedReader().use { it.readLine() } ?: throw IOException("获取不到外网IP地址，请检查是否能正常上网")

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