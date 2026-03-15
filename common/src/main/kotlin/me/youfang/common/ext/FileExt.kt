package me.youfang.common.ext

import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.attribute.BasicFileAttributeView
import java.nio.file.attribute.DosFileAttributeView
import java.nio.file.attribute.FileTime
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
        val file = if (isDirectory || extension.isEmpty()) File(parentFile, "${name}_${index}") else File(parentFile, "${nameWithoutExtension}_${index}.${extension}")
        if (!file.exists()) return file
        index++
    }
}

fun String.toNormalizeFile(): File {
    val fixLink = this.trimFilePath()
    require(fixLink.isNotBlank()) { "empty link" }
    return FileUtils.getFile(*fixLink.split('\\', '/').filter { it.isNotEmpty() }.toTypedArray());
}

fun String.fixFilePath() = replace("/", File.separator).replace("\\", File.separator)

fun File.randomFile() = File(this, UUID.randomUUID().toString())

fun randomTempFile() = FileUtils.getTempDirectory().randomFile()

fun File.deleteRecursivelyWithNewThread() {
    if (!exists()) return
    if (!isDirectory) return
    val tempDir = File(parentFile, "tmp_${UUID.randomUUID()}")
    renameTo(tempDir)
    object : Thread() {
        override fun run() {
            tempDir.deleteRecursively()
        }
    }.start()
}

fun File.deleteRecursivelyWithTempDir() {
    if (!exists()) return
    if (!isDirectory) return
    val tempDir = File(parentFile, "tmp_${UUID.randomUUID()}")
    renameTo(tempDir)
    tempDir.deleteRecursively()
}

fun File.copyRecursivelyWithTempDir(target: File) {
    val tempDir = target.brother(UUID.randomUUID().toString())
    copyRecursively(tempDir)
    FileUtils.moveDirectory(tempDir, target)
}

fun File.setHidden() {
    Files.getFileAttributeView(toPath(), DosFileAttributeView::class.java).setHidden(true)
}

fun File.contain(other: File): Boolean {
    val parent = canonicalFile
    var child: File? = other.normalize().absoluteFile
    while (true) {
        if (child?.exists() == true) {
            child = child.canonicalFile
        }
        if (child == null) return false
        if (child == parent) return true
        child = child.parentFile
    }
}

fun modifyFolderCreationTime(file: File, millis: Long) = modifyFolderCreationTime(file.toPath(), millis)

fun modifyFolderCreationTime(path: Path, millis: Long) {
    // 将 LocalDateTime 转换为 FileTime
    val fileTime = FileTime.fromMillis(millis)

    // 获取基础属性视图
    val view = Files.getFileAttributeView(path, BasicFileAttributeView::class.java)

    /*
     * setTimes(lastModifiedTime, lastAccessTime, createTime)
     * 如果不需要修改某个属性，可以传 null
     */
    view.setTimes(null, null, fileTime)
}