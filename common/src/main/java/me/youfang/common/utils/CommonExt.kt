package me.youfang.common.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

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

val currentFormatTime: String
    get() = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss").apply { timeZone = TimeZone.getTimeZone("Asia/Shanghai") }.format(Date())
