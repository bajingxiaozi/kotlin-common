package me.youfang.common.ext

import org.apache.commons.io.FileUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.util.UUID

fun String.writeToFile(file: File) = apply {
    FileUtils.forceMkdirParent(file)
    file.writeText(this)
}

fun String.writeToFileWithTempFile(file: File) {
    val tempFile = File(file.parentFile, "tmp_${UUID.randomUUID()}").apply {
        FileUtils.forceMkdirParent(this)
        deleteOnExit()
    }
    writeToFile(tempFile)
    if (file.exists()) {
        file.delete()
    }
    FileUtils.moveFile(tempFile, file)
}

fun InputStream.writeToFileWithTempFile(file: File) {
    val tempFile = File(file.parentFile, "tmp_${UUID.randomUUID()}").apply {
        FileUtils.forceMkdirParent(this)
        deleteOnExit()
    }
    FileOutputStream(tempFile).use {
        copyTo(it)
    }
    if (file.exists()) {
        file.delete()
    }
    FileUtils.moveFile(tempFile, file)
}


fun File.readTextSafe(): String? = kotlin.runCatching { readText() }.getOrNull()

fun File.toOutputStream() = FileOutputStream(this)

fun File.toInputStream() = FileInputStream(this)