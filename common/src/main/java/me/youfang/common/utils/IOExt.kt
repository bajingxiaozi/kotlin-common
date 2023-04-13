package me.youfang.common.utils

import org.apache.commons.io.FileUtils
import java.io.File

fun String.writeToFile(file: File) = apply {
    FileUtils.forceMkdirParent(file)
    file.writeText(this)
}

fun File.readTextSafe(): String? = kotlin.runCatching { readText() }.getOrNull()