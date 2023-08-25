package me.youfang.common.utils

import org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.CopyOption
import java.nio.file.StandardCopyOption
import java.util.UUID

fun String.writeToFile(file: File) = apply {
    FileUtils.forceMkdirParent(file)
    file.writeText(this)
}

fun String.writeToFileWithTempFile(file: File) {
    val tempFile = File(file.parentFile, UUID.randomUUID().toString())
    writeToFile(tempFile)
    if (file.exists()) {
        file.delete()
    }
    FileUtils.moveFile(tempFile, file)
}


fun File.readTextSafe(): String? = kotlin.runCatching { readText() }.getOrNull()

fun File.toOutputStream() = FileOutputStream(this)

fun File.toInputStream() = FileInputStream(this)