package com.healthmate.common.customView

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.*
import android.view.View
import java.util.*

class RichTextView(private val syntax: String) :
    SpannableString(syntax) {
    fun setTextColor(word: String, color: Int): RichTextView {
        setSpan(
            ForegroundColorSpan(color),
            syntax.toLowerCase(Locale.getDefault()).indexOf(word.toLowerCase(Locale.getDefault())),
            syntax.toLowerCase(Locale.getDefault()).indexOf(word.toLowerCase(Locale.getDefault())) + word.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return this
    }

    fun setSize(word: String, howMuch: Float): RichTextView {
        setSpan(
            RelativeSizeSpan(howMuch),
            syntax.indexOf(word),
            syntax.indexOf(word) + word.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return this
    }

    fun setStrikeOut(word: String): RichTextView {
        setSpan(
            StrikethroughSpan(),
            syntax.indexOf(word),
            syntax.indexOf(word) + word.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return this
    }

    fun setUrl(word: String, redirectUrl: String?): RichTextView {
        setSpan(
            URLSpan(redirectUrl),
            syntax.indexOf(word),
            syntax.indexOf(word) + word.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return this
    }

    fun setBold(word: String): RichTextView {
        val boldSpan = StyleSpan(Typeface.BOLD)
        setSpan(
            boldSpan,
            syntax.toLowerCase().indexOf(word.toLowerCase()),
            syntax.toLowerCase().indexOf(word.toLowerCase()) + word.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return this
    }

    fun setClickable(
        word: String,
        listener: setOnLinkClickListener?
    ): RichTextView {
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(view: View) {
                listener?.onLinkClicked()
            }
        }
        setSpan(
            clickableSpan,
            syntax.indexOf(word),
            syntax.indexOf(word) + word.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return this
    }

    interface setOnLinkClickListener {
        fun onLinkClicked()
    }

}