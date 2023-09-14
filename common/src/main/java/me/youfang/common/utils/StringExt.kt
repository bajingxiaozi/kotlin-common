package me.youfang.common.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.apache.commons.text.StringEscapeUtils
import org.json.JSONObject

inline fun <reified T> String.toBean(): T? = kotlin.runCatching { Gson().fromJson(this, T::class.java) }.getOrNull()

fun Any.toJSONString(): String = Gson().toJson(this)

fun String.toJSONObject(): JSONObject? = kotlin.runCatching { JSONObject(this) }.getOrNull()

fun Any.toPrettyJSONString(): String = GsonBuilder().setPrettyPrinting().create().toJson(this)

fun String.containsAny(vararg str: String) = str.any { this.contains(it) }

fun String.containsAny(strings: List<String>) = strings.any { this.contains(it) }

fun String.unescapeHtml(): String = StringEscapeUtils.unescapeHtml4(this)