package me.youfang.common.utils

import org.apache.commons.io.FileUtils
import java.io.File

fun File.forceMkdir(): File = apply { FileUtils.forceMkdir(this) }