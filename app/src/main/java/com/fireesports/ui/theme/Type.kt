package com.fireesports.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.fireesports.R

val Montserrat = FontFamily(
    Font(R.font.montserrat_regular),
    Font(R.font.montserrat_bold, FontWeight.Bold)
)

val Orbitron = FontFamily(
    Font(R.font.orbitron_regular),
    Font(R.font.orbitron_bold, FontWeight.Bold)
)

val Typography = Typography(
    displayLarge = androidx.compose.material3.Typography().displayLarge.copy(
        fontFamily = Orbitron
    ),
    displayMedium = androidx.compose.material3.Typography().displayMedium.copy(
        fontFamily = Orbitron
    ),
    displaySmall = androidx.compose.material3.Typography().displaySmall.copy(
        fontFamily = Orbitron
    ),
    headlineLarge = androidx.compose.material3.Typography().headlineLarge.copy(
        fontFamily = Orbitron
    ),
    headlineMedium = androidx.compose.material3.Typography().headlineMedium.copy(
        fontFamily = Orbitron
    ),
    headlineSmall = androidx.compose.material3.Typography().headlineSmall.copy(
        fontFamily = Orbitron
    ),
    titleLarge = androidx.compose.material3.Typography().titleLarge.copy(
        fontFamily = Montserrat
    ),
    titleMedium = androidx.compose.material3.Typography().titleMedium.copy(
        fontFamily = Montserrat
    ),
    titleSmall = androidx.compose.material3.Typography().titleSmall.copy(
        fontFamily = Montserrat
    ),
    bodyLarge = androidx.compose.material3.Typography().bodyLarge.copy(
        fontFamily = Montserrat
    ),
    bodyMedium = androidx.compose.material3.Typography().bodyMedium.copy(
        fontFamily = Montserrat
    ),
    bodySmall = androidx.compose.material3.Typography().bodySmall.copy(
        fontFamily = Montserrat
    ),
    labelLarge = androidx.compose.material3.Typography().labelLarge.copy(
        fontFamily = Montserrat
    ),
    labelMedium = androidx.compose.material3.Typography().labelMedium.copy(
        fontFamily = Montserrat
    ),
    labelSmall = androidx.compose.material3.Typography().labelSmall.copy(
        fontFamily = Montserrat
    )
)
