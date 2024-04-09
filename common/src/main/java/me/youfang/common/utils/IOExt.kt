package me.youfang.common.utils

import org.apache.commons.io.FileExistsException
import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
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

fun InputStream.writeToFileWithTempFile(file: File) {
    if (file.exists()) throw FileExistsException("文件已存在: $file")
    val tempFile = File(file.parentFile, UUID.randomUUID().toString())
    FileUtils.forceMkdirParent(tempFile)
    FileOutputStream(tempFile).use {
        copyTo(it)
    }
    FileUtils.moveFile(tempFile, file)
}


fun File.readTextSafe(): String? = kotlin.runCatching { readText() }.getOrNull()

fun File.toOutputStream() = FileOutputStream(this)

fun File.toInputStream() = FileInputStream(this)