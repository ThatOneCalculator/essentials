package com.sameerasw.essentials

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.sameerasw.essentials.ui.theme.EssentialsTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel.check(this)
        setContent {
            EssentialsTheme {
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
                Scaffold(
                    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        MediumTopAppBar(
                            title = {
                                Text(
                                    "Essentials",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
//                            navigationIcon = {
//                                IconButton(onClick = { /* do something */ }) {
//                                    Icon(
//                                        painter = painterResource(id = R.mipmap.ic_launcher),
//                                        contentDescription = "App Icon"
//                                    )
//                                }
//                            },
                            actions = {
                                IconButton(onClick = { /* do something */ }) {
                                    Icon(
                                        imageVector = Icons.Filled.Search,
                                        contentDescription = "Search"
                                    )
                                }
                            },
                            scrollBehavior = scrollBehavior
                        )
                    }
                ) { innerPadding ->
                    ScreenOffWidgetSetup(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.check(this)
    }
}

@Composable
fun ScreenOffWidgetSetup(modifier: Modifier = Modifier) {
    val viewModel: MainViewModel = viewModel()
    val isAccessibilityEnabled by viewModel.isAccessibilityEnabled
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Card(modifier = Modifier.padding(16.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (isAccessibilityEnabled) {
                    Text("Accessibility permission granted. You can now add the Screen Off widget to your home screen.")
                } else {
                    Text("To use the Screen Off widget, enable accessibility permission for this app.")
                    Button(onClick = {
                        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                        context.startActivity(intent)
                    }) {
                        Text("Go to Accessibility Settings")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenOffWidgetSetupPreview() {
    EssentialsTheme {
        ScreenOffWidgetSetup()
    }
}