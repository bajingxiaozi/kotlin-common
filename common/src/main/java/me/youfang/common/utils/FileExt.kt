package me.youfang.common.utils

import org.apache.commons.io.FileUtils
import java.io.File
import java.util.*

fun File.forceMkdir() = apply { FileUtils.forceMkdir(this) }

fun File.forceMkdirParent() = apply { FileUtils.forceMkdirParent(this) }

fun String.toFile() = File(this)

fun File.child(name: String) = File(this, name)

fun File.brother(name: String) = File(parentFile, name)

fun File.nextAvailableFile(): File {
    if (!exists()) return this
    var index = 2
    while (true) {
        val file = if (extension.isEmpty()) File(parentFile, "${name}_${index}") else File(parentFile, "${nameWithoutExtension}_${index}.${extension}")
        if (!file.exists()) return file
        index++
    }
}

fun String.toNormalizeFile(): File {
    val fixLink = this.normalize()
    require(fixLink.isNotBlank()) { "empty link" }
    return FileUtils.getFile(*fixLink.split('\\', '/').filter { it.isNotEmpty() }.toTypedArray());
}

fun String.fixFilePath() = replace("/", File.separator).replace("\\", File.separator)

fun File.randomFile() = File(this, UUID.randomUUID().toString())

fun randomTempFile() = FileUtils.getTempDirectory().randomFile()