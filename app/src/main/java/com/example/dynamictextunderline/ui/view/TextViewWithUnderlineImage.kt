package com.example.dynamictextunderline.ui.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.withStyledAttributes
import androidx.core.view.updateLayoutParams
import com.example.dynamictextunderline.R
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.airbnb.lottie.LottieAnimationView

class TextViewWithUnderlineImage : FrameLayout {

    private val underlineTextPath = Path()
    private val underlineTextBounds = RectF()

    private lateinit var textView: TextView

    private var imageView: ImageView? = null
    private var textToUnderline: String = ""
    private var startIndex: Int = -1
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

            textToUnderline = (getString(R.styleable.TextViewWithUnderlineImage_textToUnderline) ?: "").let {
                if (getBoolean(R.styleable.TextViewWithUnderlineImage_android_textAllCaps, false)) {
                    it.uppercase()
                } else {
                    it
                }
            }

            initTitleTextView(this)
            initUnderlineView(this)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (!changed) return

        // Now that the TextView is correctly laid out get the exact bounds of the underline text so
        // that the underline can be correctly positioned
        textView.layout.getSelectionPath(startIndex, endIndex, underlineTextPath)
        underlineTextPath.computeBounds(underlineTextBounds, true)

        // Search the TextView lines for the underlined text
        var selectedLine = 0
        for (lineIndex in 1..textView.lineCount) {
            if (textView.layout.getLineStart(lineIndex) > startIndex) {
                selectedLine = lineIndex - 1
                break
            }
        }

        imageView?.updateLayoutParams<MarginLayoutParams> {
            // Set top margin to be its line base line so that there is no noticeably gap between the underline and the text
            topMargin = textView.layout.getLineBaseline(selectedLine)
            marginStart = underlineTextBounds.left.toInt()
        }

        (imageView as? LottieAnimationView)?.also { it.playAnimation() }
    }

    private fun initTitleTextView(typedArray: TypedArray) {
        val titleText = typedArray.getString(R.styleable.TextViewWithUnderlineImage_android_text) ?: ""

        startIndex = titleText.indexOf(textToUnderline, ignoreCase = true)
        endIndex = startIndex + textToUnderline.length

        textView = TextView(
            context,
            null,
            0,
            typedArray.getResourceId(R.styleable.TextViewWithUnderlineImage_textStyle, 0)
        ).apply {
            text = if (typedArray.getBoolean(R.styleable.TextViewWithUnderlineImage_android_textAllCaps, false)) {
                titleText.uppercase()
            } else {
                titleText
            }
            gravity = typedArray.getInt(R.styleable.TextViewWithUnderlineImage_android_gravity, 0)
        }
        addView(textView)
    }

    private fun initUnderlineView(typedArray: TypedArray) {
        if (startIndex == -1) {
            Log.e(TAG, "$textToUnderline does not exist in text, no underline rendered")
            return
        }

        val imageView = if (typedArray.hasValue(R.styleable.TextViewWithUnderlineImage_lottieUnderline)) {
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
        this.imageView = imageView
    }

    companion object {

        private const val TAG = "TextViewWithUnderlineImage"
    }
}