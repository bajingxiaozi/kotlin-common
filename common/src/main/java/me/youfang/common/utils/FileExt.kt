package me.youfang.common.utils

import org.apache.commons.io.FileUtils
import java.io.File

fun File.forceMkdir() = apply { FileUtils.forceMkdir(this) }

fun String.toFile() = File(this)

fun File.child(name: String) = File(this, name)

private fun File.nextAvailableDir(): File {
    if (!exists()) return this
    var index = 2
    while (true) {
        val dir = File(parentFile, "${name}_${index}")
        if (!dir.exists()) return dir
        index++
    }
}

fun String.trimToPath(): File {
    val fixLink = this.trim { it == '"' }.trim()
    require(fixLink.isNotBlank()) { "empty link" }
    return FileUtils.getFile(*fixLink.split('\\', '/').filter { it.isNotEmpty() }.toTypedArray());
}