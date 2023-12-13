package me.youfang.common.utils


fun ByteArray.findPositionIn(array: ByteArray): Int {
    for (i in array.indices) {
        if (arrayMatch(array, i, this)) return i
    }
    return -1
}

private fun arrayMatch(source: ByteArray, position: Int, sub: ByteArray): Boolean {
    if (position + sub.size >= source.size) return false
    for (i in sub.indices) {
        if (sub[i] != source[position + i]) return false
    }
    return true
}

fun <T> List<T>.getFromLast(index: Int) = get(size - index - 1)