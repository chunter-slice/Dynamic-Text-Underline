package com.example.dynamictextunderline

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Modifier
import com.example.dynamictextunderline.ui.composable.TextWithUnderlineImage
import com.example.dynamictextunderline.ui.theme.DynamicTextUnderlineTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isView = true
        if (isView) {
            setContentView(R.layout.activity_main)
        } else {
            val title = "Find a pizzeria today!"

            setContent {
                DynamicTextUnderlineTheme {
                    Column {
                        TextWithUnderlineImage(
                            modifier = Modifier.fillMaxWidth(),
                            text = title,
                            underlineText = "today!",
                            textStyle = MaterialTheme.typography.h4,
                            drawableRes = R.drawable.ic_underline,
                        )
                        TextWithUnderlineImage(
                            modifier = Modifier.fillMaxWidth(),
                            text = title,
                            underlineText = "pizzeria",
                            textStyle = MaterialTheme.typography.h4,
                            lottieFile = "animations/underline.json",
                        )
                    }
                }
            }
        }
    }
}