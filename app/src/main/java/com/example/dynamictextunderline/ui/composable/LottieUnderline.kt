package com.example.dynamictextunderline.ui.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.dynamictextunderline.ui.theme.DynamicTextUnderlineTheme

@Composable
fun LottieUnderline(
    modifier: Modifier = Modifier,
    lottieFile: String,
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(lottieFile))
    val progress by animateLottieCompositionAsState(composition)
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = progress,
        contentScale = ContentScale.FillBounds,
    )
}

@Preview
@Composable
fun LottieUnderlinePreview() {
    DynamicTextUnderlineTheme {
        LottieUnderline(lottieFile = "animations/underline.json")
    }
}