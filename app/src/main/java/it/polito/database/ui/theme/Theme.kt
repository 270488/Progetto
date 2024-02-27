package it.polito.database.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Blue40, //colore blu chiaro sfondo
    onPrimary = White00,
    primaryContainer = Grey40, //colore caselle
    secondary = Blue20, //colore blu scuro elementi in risalto
    onSecondary = White00,
    secondaryContainer = Red20, //colore logout
    tertiary = Yellow40, //colore dettagli icone e bottoni
    onTertiary = Blue40,
    background = Grey90, //sfondo secondario
    onBackground = Black00,
    outline = Black00,
    outlineVariant = Yellow40,
    tertiaryContainer = Yellow60,
    errorContainer = Red40, //colore undo buttons
    onErrorContainer = White00,
)
//COMMENTATO LIGHT SCHEME PERCHE USIAMO UN SOLO TEMA
/*private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)*/

@Composable
fun DatabaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, //FALSE perchè non vogliamo i colori dinamici
    content: @Composable () -> Unit
) {
    /*val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }*/
    val colorScheme = DarkColorScheme //----> ASSEGNA IL TEMA ALL'APP CHE SARA SEMPRE QUELLO DARK
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Black.toArgb()  //DarkColorScheme.primary.toArgb() //ContextCompat.getColor(view.context, R.color.primary)
            window.navigationBarColor = Color.Black.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}