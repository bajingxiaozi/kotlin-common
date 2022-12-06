package me.youfang.common.utils

import java.io.File

fun String.writeToFile(file: File) = file.writeText(this)
