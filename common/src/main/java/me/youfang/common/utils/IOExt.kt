package me.youfang.common.utils

import org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun String.writeToFile(file: File) = apply {
    FileUtils.forceMkdirParent(file)
    file.writeText(this)
}

fun File.readTextSafe(): String? = kotlin.runCatching { readText() }.getOrNull()

fun File.toOutputStream() = FileOutputStream(this).buffered()

fun File.toInputStream() = FileInputStream(this).buffered()