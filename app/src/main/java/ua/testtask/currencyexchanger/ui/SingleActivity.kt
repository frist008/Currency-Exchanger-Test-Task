package ua.testtask.currencyexchanger.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.toArgb
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import ua.testtask.currencyexchanger.ui.surface.RootSurface
import ua.testtask.currencyexchanger.ui.theme.color.Palette

@AndroidEntryPoint class SingleActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        val systemColor = Palette.BLUE_LIGHT.toArgb()
        val systemBarStyle = SystemBarStyle.dark(systemColor)
        enableEdgeToEdge(
            statusBarStyle = systemBarStyle,
            navigationBarStyle = systemBarStyle,
        )
        super.onCreate(savedInstanceState)
        setContent { RootSurface() }
    }
}
