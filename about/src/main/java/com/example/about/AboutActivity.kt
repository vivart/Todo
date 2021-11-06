package com.example.about

import android.os.Bundle
import android.webkit.WebView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.material.composethemeadapter.MdcTheme

class AboutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MdcTheme {
                AboutScreen()
            }
        }
    }
}

@Composable
fun AboutScreen() {
    AndroidView(
        factory = {
            WebView(it).apply {
                loadUrl("file:///android_asset/about.html")
            }
        },
        modifier = Modifier
            .testTag("web")
            .fillMaxHeight()
    )
}
