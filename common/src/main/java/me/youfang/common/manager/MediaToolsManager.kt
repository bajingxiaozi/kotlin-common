package me.youfang.common.manager

import java.util.*

/**
 * byte per second
 */
var videoConvertSpeed: Float = -1f
    private set

private const val SPEED_SAMPLE_COUNT = 60
private val speedSamples = LinkedList<Float>()

/**
 * byte per second
 */
fun updateVideoConvertSpeed(speed: Float) {
    speedSamples.add(speed)
    while (speedSamples.size > SPEED_SAMPLE_COUNT) {
        speedSamples.removeFirst()
    }
    if (speedSamples.isNotEmpty()) {
        videoConvertSpeed = speedSamples.sum() / speedSamples.size
    }
}