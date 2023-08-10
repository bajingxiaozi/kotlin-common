package me.youfang.common.utils

import java.io.File
import java.lang.StringBuilder

fun String.toStringBuilder() = StringBuilder(this)

fun inputWithCheck(hint: String): String {
    print("${hint}->>> ")
    return readlnOrNull()?.trim()?.takeIf { it.isNotBlank() } ?: throw IllegalArgumentException("should input something")
}

fun inputPassword(hint: String): String {
    print("${hint}->>> ")
//    return System.console().readPassword()
    return readlnOrNull()?.takeIf { it.isNotBlank() } ?: throw IllegalArgumentException("should input something")
}

fun String.fixFilePath() = replace("/", File.separator).replace("\\", File.separator)