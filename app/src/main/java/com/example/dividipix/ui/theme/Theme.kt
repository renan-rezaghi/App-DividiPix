package com.example.dividipix.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = VerdePix,
    onPrimary = Branco,

    primaryContainer = VerdeClaro,
    onPrimaryContainer = VerdeEscuro,

    secondary = VerdeEscuro,
    onSecondary = Branco,

    secondaryContainer = VerdeClaro,
    onSecondaryContainer = VerdeEscuro,

    tertiary = VerdeDestaque,
    onTertiary = Branco,

    background = FundoClaro,
    onBackground = TextoPrincipal,

    surface = Branco,
    onSurface = TextoPrincipal,

    surfaceVariant = VerdeClaro,
    onSurfaceVariant = TextoSecundario,

    error = Erro,
    onError = Branco
)

private val DarkColorScheme = darkColorScheme(
    primary = VerdeDestaque,
    onPrimary = TextoPrincipal,

    primaryContainer = VerdeEscuro,
    onPrimaryContainer = Branco,

    secondary = VerdePix,
    onSecondary = Branco,

    secondaryContainer = VerdeEscuro,
    onSecondaryContainer = Branco,

    tertiary = VerdeDestaque,
    onTertiary = TextoPrincipal,

    background = TextoPrincipal,
    onBackground = Branco,

    surface = TextoPrincipal,
    onSurface = Branco,

    surfaceVariant = VerdeEscuro,
    onSurfaceVariant = Branco,

    error = Erro,
    onError = Branco
)

@Composable
fun DividiPixTheme(
    darkTheme: Boolean = false,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}