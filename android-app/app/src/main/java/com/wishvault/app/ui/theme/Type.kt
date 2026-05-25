package com.wishvault.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.wishvault.app.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val CormorantGaramond = FontFamily(
    Font(googleFont = GoogleFont("Cormorant Garamond"), fontProvider = provider, weight = FontWeight.Medium),
    Font(googleFont = GoogleFont("Cormorant Garamond"), fontProvider = provider, weight = FontWeight.Normal)
)

val Inter = FontFamily(
    Font(googleFont = GoogleFont("Inter"), fontProvider = provider, weight = FontWeight.Light),
    Font(googleFont = GoogleFont("Inter"), fontProvider = provider, weight = FontWeight.Medium)
)

// Authored Editorial Typography Hierarchy
val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Medium,
        fontSize = 42.sp,
        letterSpacing = (-0.5).sp,
        lineHeight = 48.sp
    ),
    displayMedium = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Medium,
        fontSize = 34.sp,
        letterSpacing = (-0.25).sp,
        lineHeight = 40.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp, // Stronger presence
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = CormorantGaramond,
        fontWeight = FontWeight.Normal,
        fontSize = 26.sp, // Stronger presence
        lineHeight = 34.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Light,
        fontSize = 17.sp, // Larger body text
        lineHeight = 28.sp, // Taller line height for editorial rhythm
        letterSpacing = 0.2.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Light,
        fontSize = 15.sp, // Larger body text
        lineHeight = 24.sp,
        letterSpacing = 0.1.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp, // Larger metadata labels
        letterSpacing = 2.0.sp, // Wide tracking for labels
        lineHeight = 18.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp, // Larger nav labels
        letterSpacing = 1.0.sp,
        lineHeight = 16.sp
    )
)
