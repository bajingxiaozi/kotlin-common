package me.youfang.common.utils

import java.lang.StringBuilder

fun String.toStringBuilder() = StringBuilder(this)

fun input(hint: String): String {
    print(hint)
    return readlnOrNull() ?: ""
}
