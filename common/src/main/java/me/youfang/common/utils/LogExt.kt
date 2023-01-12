package me.youfang.common.utils

fun ld(head: String, message: Any?) = println("${head}->>> $message")

fun lw(head: String, message: Any?) = ld(head, message)

fun lds(head: String, message: Any?) = ld("$head start", message)
fun lde(head: String) = println("<---${head}")