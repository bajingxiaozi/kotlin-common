package me.youfang.common.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

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
    get() = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").apply { timeZone = TimeZone.getTimeZone("Asia/Shanghai") }.format(Date())

val currentTimeForLog:String
    get() = SimpleDateFormat("yyyy_MM_dd__HH_mm_ss").apply { timeZone = TimeZone.getTimeZone("Asia/Shanghai") }.format(Date())

@OptIn(ExperimentalContracts::class)
inline fun <R> runWithRetry(retryCount: Int = 3, block: () -> R): R {
    contract {
        callsInPlace(block, InvocationKind.AT_LEAST_ONCE)
    }
    var count = retryCount
    while (--count > 0) {
        try {
            return block()
        } catch (ignore: Throwable) {
            Thread.sleep(60 * 1000)
        }
    }
    return block()
}
