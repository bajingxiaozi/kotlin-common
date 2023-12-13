package me.youfang.common.utils

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

fun List<String>.containAny(vararg elements: String): Boolean = elements.any { this.contains(it) }

val windowsSystem: Boolean = System.getProperty("os.name").contains("Windows")
