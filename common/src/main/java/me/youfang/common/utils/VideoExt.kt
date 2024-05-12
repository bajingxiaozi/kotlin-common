package me.youfang.common.utils

import org.apache.commons.io.FileUtils
import org.apache.commons.io.IOCase
import org.apache.commons.io.filefilter.SuffixFileFilter
import org.apache.commons.io.filefilter.TrueFileFilter
import java.io.File
import java.io.IOException
import java.util.regex.Pattern


val File.videoInfo: VideoInfoBean?
    get() = exe("ffprobe", "-v", "quiet", "-print_format", "json", "-show_format", "-show_streams", absolutePath).toBean()

val File.videoFrameCount: Int
    get() {
        val videoInfo = videoInfo!!
        val duration = videoInfo.format!!.duration!!.toDouble()
        val fpsString = videoInfo.streams!![0]!!.rFrameRate!!
        val matcher = FPS_PATTERN.matcher(fpsString)
        if (matcher.matches()) {
            val fps = matcher.group("a").toDouble() / matcher.group("b").toDouble()
            return (fps * duration).toInt()
        }

        throw IOException("error video info: $this")
    }

private val FPS_PATTERN = Pattern.compile("^(?<a>[0-9]+)/(?<b>[0-9]+)$")

private val FRAME_PATTERN = Pattern.compile("^frame=(?<frame>[ 0-9]+)fps.*")

fun getFrameIndex(message: String): Int {
    val matcher = FRAME_PATTERN.matcher(message)
    if (matcher.matches()) {
        val frame = matcher.group("frame")
        return frame.trim { it <= ' ' }.toInt()
    }
    return -1
}


private val VIDEO_FILTER = SuffixFileFilter(arrayOf(".webm", ".mkv", ".flv", ".avi", ".ts", ".mov", ".wmv", ".mp4", ".mpg", ".mpeg"), IOCase.INSENSITIVE)

val File.allVideos: List<File>
    get() = FileUtils.listFiles(this, VIDEO_FILTER, TrueFileFilter.TRUE).toList()
