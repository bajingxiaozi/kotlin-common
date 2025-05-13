package me.youfang.common.ext

import org.jline.terminal.Size
import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import org.jline.utils.AttributedStringBuilder
import org.jline.utils.AttributedStyle
import java.awt.Color

//    prompt = AttributedStringBuilder()
//        .style(AttributedStyle.DEFAULT.background(AttributedStyle.GREEN))
//        .append("foo")
//        .style(AttributedStyle.DEFAULT)
//        .append("@bar")
//        .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN))
//        .append("\nbaz")
//        .style(AttributedStyle.DEFAULT)
//        .append("> ").toAnsi()
val terminal: Terminal by lazy { TerminalBuilder.terminal() }
val TERMINAL_SIZE: Size by lazy { terminal.size }

fun String.toGrayItalicString(): String = AttributedStringBuilder().style(AttributedStyle.DEFAULT.foregroundRgb(Color.GRAY.rgb).italic()).append(this).toAnsi(terminal)

fun String.toColorTerminalString(color: Color): String = AttributedStringBuilder().style(AttributedStyle.DEFAULT.foregroundRgb(color.rgb)).append(this).toAnsi(terminal)