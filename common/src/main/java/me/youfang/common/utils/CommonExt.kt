package me.youfang.common.utils

import java.lang.StringBuilder

fun String.toStringBuilder() = StringBuilder(this)

fun input(hint: String): String {
    print("${hint}->>> ")
    return readlnOrNull() ?: ""
}

fun inputPassword(hint: String): CharArray {
    print("${hint}->>> ")
//    return System.console().readPassword()
    return (readlnOrNull() ?: "").toCharArray()
}