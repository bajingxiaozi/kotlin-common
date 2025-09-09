package me.youfang.common.ext

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.json.JSONObject

inline fun <reified T> String.toBean(): T? = kotlin.runCatching { Gson().fromJson(this, T::class.java) }.getOrNull()

fun Any.toJSONString(): String = Gson().toJson(this)

fun String.toJSONObject(): JSONObject? = kotlin.runCatching { JSONObject(this) }.getOrNull()

fun Any.toPrettyJSONString(): String = GsonBuilder().setPrettyPrinting().create().toJson(this)

fun String.startWithAny(strings: List<String>) = strings.any { startsWith(it) }

fun String.startWithAny(vararg strings: String) = strings.any { startsWith(it) }

fun String.trimFilePath(): String = trim('"').trim()

fun String.replaceFirstAny(strings: List<String>): String {
    var s = this
    strings.forEach {
        s = s.replaceFirst(it, "")
    }
    return s
}

fun String.trimMultiLineStart() = lines().joinToString("\n") { it.trimStart() }