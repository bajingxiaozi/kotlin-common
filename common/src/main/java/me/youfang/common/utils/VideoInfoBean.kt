package me.youfang.common.utils

import com.google.gson.annotations.SerializedName

class VideoInfoBean {

    @SerializedName("format")
    var format: FormatBean? = null

    @SerializedName("streams")
    var streams: List<StreamsBean?>? = null

    class FormatBean {

        @SerializedName("filename")
        var filename: String? = null

        @SerializedName("nb_streams")
        var nbStreams = 0

        @SerializedName("nb_programs")
        var nbPrograms = 0

        @SerializedName("format_name")
        var formatName: String? = null

        @SerializedName("format_long_name")
        var formatLongName: String? = null

        @SerializedName("start_time")
        var startTime: String? = null

        @SerializedName("duration")
        var duration: String? = null

        @SerializedName("size")
        var size: String? = null

        @SerializedName("bit_rate")
        var bitRate: String? = null

        @SerializedName("probe_score")
        var probeScore = 0

        @SerializedName("tags")
        var tags: TagsBean? = null

        class TagsBean {
            @SerializedName("major_brand")
            var majorBrand: String? = null

            @SerializedName("minor_version")
            var minorVersion: String? = null

            @SerializedName("compatible_brands")
            var compatibleBrands: String? = null

            @SerializedName("encoder")
            var encoder: String? = null

            @SerializedName("comment")
            var comment: String? = null

            @SerializedName("genre")
            var genre: String? = null
        }
    }

    class StreamsBean {
        @SerializedName("index")
        var index = 0

        @SerializedName("codec_name")
        var codecName: String? = null

        @SerializedName("codec_long_name")
        var codecLongName: String? = null

        @SerializedName("profile")
        var profile: String? = null

        @SerializedName("codec_type")
        var codecType: String? = null

        @SerializedName("codec_time_base")
        var codecTimeBase: String? = null

        @SerializedName("codec_tag_string")
        var codecTagString: String? = null

        @SerializedName("codec_tag")
        var codecTag: String? = null

        @SerializedName("width")
        var width = 0

        @SerializedName("height")
        var height = 0

        @SerializedName("coded_width")
        var codedWidth = 0

        @SerializedName("coded_height")
        var codedHeight = 0

        @SerializedName("has_b_frames")
        var hasBFrames = 0

        @SerializedName("pix_fmt")
        var pixFmt: String? = null

        @SerializedName("level")
        var level = 0

        @SerializedName("chroma_location")
        var chromaLocation: String? = null

        @SerializedName("refs")
        var refs = 0

        @SerializedName("is_avc")
        var isAvc: String? = null

        @SerializedName("nal_length_size")
        var nalLengthSize: String? = null

        @SerializedName("r_frame_rate")
        var rFrameRate: String? = null

        @SerializedName("avg_frame_rate")
        var avgFrameRate: String? = null

        @SerializedName("time_base")
        var timeBase: String? = null

        @SerializedName("start_pts")
        var startPts = 0

        @SerializedName("start_time")
        var startTime: String? = null

        @SerializedName("duration_ts")
        var durationTs: Long = 0

        @SerializedName("duration")
        var duration: String? = null

        @SerializedName("bit_rate")
        var bitRate: String? = null

        @SerializedName("bits_per_raw_sample")
        var bitsPerRawSample: String? = null

        @SerializedName("nb_frames")
        var nbFrames: String? = null

        @SerializedName("disposition")
        var disposition: DispositionBean? = null

        @SerializedName("tags")
        var tags: TagsBeanX? = null

        @SerializedName("sample_fmt")
        var sampleFmt: String? = null

        @SerializedName("sample_rate")
        var sampleRate: String? = null

        @SerializedName("channels")
        var channels = 0

        @SerializedName("channel_layout")
        var channelLayout: String? = null

        @SerializedName("bits_per_sample")
        var bitsPerSample = 0

        @SerializedName("max_bit_rate")
        var maxBitRate: String? = null

        class DispositionBean {
            @SerializedName("default")
            var defaultX = 0

            @SerializedName("dub")
            var dub = 0

            @SerializedName("original")
            var original = 0

            @SerializedName("comment")
            var comment = 0

            @SerializedName("lyrics")
            var lyrics = 0

            @SerializedName("karaoke")
            var karaoke = 0

            @SerializedName("forced")
            var forced = 0

            @SerializedName("hearing_impaired")
            var hearingImpaired = 0

            @SerializedName("visual_impaired")
            var visualImpaired = 0

            @SerializedName("clean_effects")
            var cleanEffects = 0

            @SerializedName("attached_pic")
            var attachedPic = 0

            @SerializedName("timed_thumbnails")
            var timedThumbnails = 0
        }

        class TagsBeanX {
            @SerializedName("language")
            var language: String? = null

            @SerializedName("handler_name")
            var handlerName: String? = null
        }

    }

}