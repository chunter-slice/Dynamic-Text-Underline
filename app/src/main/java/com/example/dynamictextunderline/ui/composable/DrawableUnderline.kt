package com.example.dynamictextunderline.ui.composable

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dynamictextunderline.ui.theme.DynamicTextUnderlineTheme
import com.example.dynamictextunderline.R

@Composable
fun DrawableUnderline(
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes: Int,
) {
    Image(
        modifier = modifier,
        painter = painterResource(id = drawableRes),
        contentDescription = "Underline",
        contentScale = ContentScale.FillBounds,
    )
}

@Preview
@Composable
fun DrawableUnderlinePreview() {
    DynamicTextUnderlineTheme {
        DrawableUnderline(
            modifier = Modifier.width(128.dp),
            drawableRes = R.drawable.ic_underline
        )
    }
}