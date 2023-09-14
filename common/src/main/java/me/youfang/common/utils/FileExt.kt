package me.youfang.common.utils

import org.apache.commons.io.FileUtils
import java.io.File

fun File.forceMkdir() = apply { FileUtils.forceMkdir(this) }

fun String.toFile() = File(this)

fun File.child(name: String) = File(this, name)

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
    val fixLink = this.trim { it == '"' }.trim()
    require(fixLink.isNotBlank()) { "empty link" }
    return FileUtils.getFile(*fixLink.split('\\', '/').filter { it.isNotEmpty() }.toTypedArray());
}

fun String.fixFilePath() = replace("/", File.separator).replace("\\", File.separator)