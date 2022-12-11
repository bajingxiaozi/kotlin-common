package me.youfang.common.utils

import org.apache.commons.io.FileUtils
import java.io.File

fun File.forceMkdir() = apply { FileUtils.forceMkdir(this) }

fun String.toFile() = File(this)