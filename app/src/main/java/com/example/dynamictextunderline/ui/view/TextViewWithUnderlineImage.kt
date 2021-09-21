package com.example.dynamictextunderline.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import androidx.core.view.updateLayoutParams
import com.example.dynamictextunderline.R
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.airbnb.lottie.LottieAnimationView
import kotlin.math.absoluteValue

class TextViewWithUnderlineImage : FrameLayout {

    private val underlineTextBounds = Rect()
    private val leadingTextBounds = Rect()

    private lateinit var textView: TextView
    private lateinit var imageView: ImageView

    private var textToUnderline: String = ""
    private var startIndex: Int = 0
    private var endIndex: Int = 0

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        context.withStyledAttributes(
            attrs,
            R.styleable.TextViewWithUnderlineImage,
            defStyleAttr,
            0
        ) {
            if (!hasValue(R.styleable.TextViewWithUnderlineImage_textToUnderline)) {
                throw IllegalArgumentException("Must provide textToUnderline")
            }

            if (!hasValue(R.styleable.TextViewWithUnderlineImage_lottieUnderline)
                && !hasValue(R.styleable.TextViewWithUnderlineImage_drawableUnderline)
            ) {
                throw IllegalArgumentException("Must provide either lottieUnderline or drawableUnderline")
            }

            if (hasValue(R.styleable.TextViewWithUnderlineImage_lottieUnderline)
                && hasValue(R.styleable.TextViewWithUnderlineImage_drawableUnderline)
            ) {
                throw IllegalArgumentException("Provide only one of either lottieUnderline or drawableUnderline")
            }

            textToUnderline = getString(R.styleable.TextViewWithUnderlineImage_textToUnderline) ?: ""

            initTitleTextView(this)
            initUnderlineView(this)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (!changed) return

        var selectedLine = 0
        var selectedLineStartIndex = 0
        var selectedLineEndIndex = 0
        for (lineIndex in 1..textView.lineCount) {
            if (textView.layout.getLineStart(lineIndex) > startIndex) {
                selectedLine = lineIndex - 1
                selectedLineStartIndex = textView.layout.getLineStart(selectedLine)
                selectedLineEndIndex = textView.layout.getLineEnd(selectedLine)
                break
            }
        }

        imageView.updateLayoutParams<MarginLayoutParams> {
            topMargin = textView.layout.getLineBaseline(selectedLine)
            marginStart = when {
                startIndex == 0 || selectedLineStartIndex == startIndex -> 0
                endIndex == textView.text.length || selectedLineEndIndex == endIndex -> {
                    textView.layout.getLineWidth(selectedLine).toInt() - underlineTextBounds.width()
                }
                else -> {
                    textView.paint.getTextBounds(
                        textView.text.toString(),
                        selectedLineStartIndex,
                        startIndex,
                        leadingTextBounds
                    )

                    // Spaces are trimmed when measuring text bounds so we must add it manually if it exists
                    val spaceOffset = if (textView.text[startIndex - 1].isWhitespace()) {
                        textView.paint.measureText(" ").toInt()
                    } else {
                        0
                    }
                    leadingTextBounds.width() + spaceOffset
                }
            }
        }

        (imageView as? LottieAnimationView)?.also { it.playAnimation() }
    }

    private fun initTitleTextView(typedArray: TypedArray) {
        val titleText = typedArray.getString(R.styleable.TextViewWithUnderlineImage_text) ?: ""
        startIndex = titleText.indexOf(textToUnderline, ignoreCase = true)
        if (startIndex == -1) throw IllegalStateException("textToUnderline must exist in text")

        endIndex = startIndex + textToUnderline.length

        textView = TextView(
            context,
            null,
            0,
            typedArray.getResourceId(R.styleable.TextViewWithUnderlineImage_textStyle, 0)
        ).apply { text = titleText }
        addView(textView)

        textView.paint.getTextBounds(
            textToUnderline,
            0,
            textToUnderline.length,
            underlineTextBounds
        )
    }

    private fun initUnderlineView(typedArray: TypedArray) {
        imageView = if (typedArray.hasValue(R.styleable.TextViewWithUnderlineImage_lottieUnderline)) {
            LottieAnimationView(context).apply {
                setAnimation(
                    typedArray.getString(R.styleable.TextViewWithUnderlineImage_lottieUnderline) ?: ""
                )
            }
        } else {
            ImageView(context).apply {
                setImageDrawable(
                    VectorDrawableCompat.create(
                        resources,
                        typedArray.getResourceId(
                            R.styleable.TextViewWithUnderlineImage_drawableUnderline,
                            0
                        ),
                        null
                    )
                )
            }
        }

        imageView.scaleType = ImageView.ScaleType.FIT_XY

        addView(
            imageView,
            MarginLayoutParams(
                textView.paint.measureText(textToUnderline).toInt(),
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, resources.displayMetrics).toInt()
            )
        )
    }
}