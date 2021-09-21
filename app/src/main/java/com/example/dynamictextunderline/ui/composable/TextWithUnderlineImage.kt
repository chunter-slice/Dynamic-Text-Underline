package com.example.dynamictextunderline.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dynamictextunderline.R
import com.example.dynamictextunderline.ui.theme.DynamicTextUnderlineTheme

@Composable
fun TextWithUnderlineImage(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.h1,
    underlineText: String,
    @DrawableRes drawableRes: Int = -1,
    lottieFile: String? = null,
) {
    var state: TextWithUnderlineImageState by remember { mutableStateOf(TextWithUnderlineImageState()) }

    Box {
        Text(
            modifier = modifier,
            text = text,
            style = textStyle,
            onTextLayout = { textLayoutResult ->
                val underlineTextIndex = text.indexOf(underlineText)
                val underlineTextBounds = textLayoutResult.getPathForRange(
                    underlineTextIndex,
                    underlineTextIndex + underlineText.length
                ).getBounds()

                state = TextWithUnderlineImageState(
                    underlineWidth = underlineTextBounds.width,
                    underlineHeight = underlineTextBounds.height,
                    underlinePaddingTop = underlineTextBounds.top + textLayoutResult.firstBaseline,
                    underlinePaddingStart = underlineTextBounds.left
                )
            }
        )
        val imageModifier = with(LocalDensity.current) {
            Modifier
                .paddingFromBaseline(top = state.underlinePaddingTop.toDp())
                .padding(start = state.underlinePaddingStart.toDp())
                .size(width = state.underlineWidth.toDp(), height = 24.dp)
        }
        if (drawableRes != -1) {
            DrawableUnderline(
                modifier = imageModifier,
                drawableRes = drawableRes,
            )
        } else if (lottieFile != null) {
            LottieUnderline(
                modifier = imageModifier,
                lottieFile = lottieFile,
            )
        }
    }
}

data class TextWithUnderlineImageState(
    val underlineWidth: Float = 0f,
    val underlineHeight: Float = 0f,
    val underlinePaddingTop: Float = 0f,
    val underlinePaddingStart: Float = 0f,
)

@Preview
@Composable
fun TextWithUnderlineImageDrawablePreview() {
    DynamicTextUnderlineTheme {
        TextWithUnderlineImage(
            text = "Find a pizzeria today!",
            underlineText = "today!",
            drawableRes = R.drawable.ic_underline
        )
    }
}

@Preview
@Composable
fun TextWithUnderlineImageLottiePreview() {
    DynamicTextUnderlineTheme {
        TextWithUnderlineImage(
            text = "Find a pizzeria today!",
            underlineText = "today!",
            lottieFile = "animations/underline.json"
        )
    }
}