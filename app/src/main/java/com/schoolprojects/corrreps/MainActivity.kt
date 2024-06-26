package com.schoolprojects.corrreps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.schoolprojects.corrreps.holder.HolderScreen
import com.schoolprojects.corrreps.providers.LocalNavHost
import com.schoolprojects.corrreps.ui.theme.CORREPSTheme
import dagger.hilt.android.AndroidEntryPoint
import com.schoolprojects.corrreps.utils.LocalScreenSize
import com.schoolprojects.corrreps.utils.getScreenSize

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val defaultStatusBarColor = MaterialTheme.colorScheme.background.toArgb()
            var statusBarColor by remember { mutableStateOf(defaultStatusBarColor) }
            window.statusBarColor = statusBarColor

            /** Our navigation controller */
            val navController = rememberNavController()

            /** Getting screen size */
            val size = LocalContext.current.getScreenSize()

            CORREPSTheme {
                // A surface container using the 'background' color from the theme
                CompositionLocalProvider(
                    LocalScreenSize provides size,
                    LocalNavHost provides navController
                ) {
                    // A surface container using the 'background' color from the theme
                    Surface(modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background) {
                        HolderScreen(
                            onStatusBarColorChange = {
                                //** Updating the color of the status bar *//*
                                //** Updating the color of the status bar *//*
                                //** Updating the color of the status bar *//*

                                //** Updating the color of the status bar *//*
                                statusBarColor = it.toArgb()
                            }
                        )
                    }
                }
            }
        }
    }
}
