package me.youfang.common.utils

import me.youfang.common.manager.updateVideoConvertSpeed
import java.io.File
import java.util.*

class ConvertProgressHelper(file: File) {

    private val fileSize = file.length()
    private val totalFrame = file.videoFrameCount

    fun tick(message: String) {
        val frameIndex = getFrameIndex(message)
        if (frameIndex < 0) return
        progresses.addLast(Progress(frameIndex))
        while (progresses.size > COUNT) {
            progresses.removeFirst()
        }
        if (totalFrame <= 0) return
        updateVideoConvertSpeed(fileSize / totalFrame * speed)
    }

    /**
     * 帧/秒
     */
    private val speed: Float
        get() {
            if (progresses.size <= 1) return -1f
            return 1000f * (progresses.last.frame - progresses.first.frame) / (progresses.last.time - progresses.first.time)
        }

    val progress: Float
        get() {
            if (progresses.isEmpty()) return -1f
            return 1f * progresses.last.frame / totalFrame
        }

    val leftTime: Long
        get() {
            if (progresses.isEmpty()) return -1
            return ((totalFrame - progresses.last.frame) / speed).toLong()
        }

    /**
     * 数组靠前的元素是旧的进度信息
     */
    private val progresses = LinkedList<Progress>()

    private companion object {
        const val COUNT = 5
    }

    private data class Progress(val frame: Int, val time: Long = System.currentTimeMillis())

}