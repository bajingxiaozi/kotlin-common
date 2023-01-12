package me.youfang.common.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder

inline fun <reified T> String.toBean(): T? = kotlin.runCatching { Gson().fromJson(this, T::class.java) }.getOrNull()

fun Any.toJSONString(): String = Gson().toJson(this)

fun Any.toPrettyJSONString(): String = GsonBuilder().setPrettyPrinting().create().toJson(this)