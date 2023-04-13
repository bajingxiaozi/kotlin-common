package me.youfang.common.utils

fun ld(head: String, message: Any?) = println("$head>>> $message")

fun lw(head: String, message: Any?) = ld(head, message)

fun lds(head: String, message: Any?) {
    println("=".repeat(100))
    ld(head, message)
    println("-".repeat(100))
}

fun lde(head: String) {
    println("=".repeat(40))
}