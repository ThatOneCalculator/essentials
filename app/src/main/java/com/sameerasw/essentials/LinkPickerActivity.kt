package com.sameerasw.essentials

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.sameerasw.essentials.ui.LinkPickerScreen
import com.sameerasw.essentials.ui.theme.EssentialsTheme

class LinkPickerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val uri = intent.data
        if (uri == null) {
            finish()
            return
        }

        enableEdgeToEdge()

        setContent {
            EssentialsTheme {
                LinkPickerScreen(uri = uri, onFinish = { finish() }, modifier = Modifier.fillMaxSize())
            }
        }
    }
}
