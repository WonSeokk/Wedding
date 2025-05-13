package com.wwon_seokk.wedding

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import wedding.composeapp.generated.resources.MaruBuri_Bold
import wedding.composeapp.generated.resources.MaruBuri_ExtraLight
import wedding.composeapp.generated.resources.MaruBuri_Regular
import wedding.composeapp.generated.resources.MaruBuri_SemiBold
import wedding.composeapp.generated.resources.Parisienne_Regular
import wedding.composeapp.generated.resources.Res

val fontFamily
    @Composable
    get() = Typography().run {
        val fontFamily = FontFamily(
            Font(
                Res.font.MaruBuri_Bold,
                weight = FontWeight.Bold,
            ),
            Font(
                Res.font.MaruBuri_Regular,
                weight = FontWeight.Normal,
            ),
            Font(
                Res.font.MaruBuri_Regular,
                weight = FontWeight.Medium,
            ),
            Font(
                Res.font.MaruBuri_SemiBold,
                weight = FontWeight.SemiBold,
            ),
            Font(
                Res.font.MaruBuri_ExtraLight,
                weight = FontWeight.Thin,
            )
        )

        copy(
            titleLarge = titleLarge.copy(
                lineHeight = 52.17.sp,
                fontSize = 40.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9A70E2),
            ),
            titleMedium = titleMedium.copy(
                lineHeight = 52.17.sp,
                fontSize = 40.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
            ),
            titleSmall = titleSmall.copy(fontFamily = fontFamily),
            bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
            bodySmall = bodySmall.copy(fontFamily = fontFamily)
        )
    }

val enFontFamily
    @Composable
    get() = Typography().run {
        val fontFamily = FontFamily(
            Font(
                Res.font.Parisienne_Regular,
                weight = FontWeight.Bold,
            ),
            Font(
                Res.font.Parisienne_Regular,
                weight = FontWeight.Normal,
            ),
            Font(
                Res.font.Parisienne_Regular,
                weight = FontWeight.Medium,
            ),
            Font(
                Res.font.Parisienne_Regular,
                weight = FontWeight.SemiBold,
            ),
            Font(
                Res.font.Parisienne_Regular,
                weight = FontWeight.Thin,
            )
        )

        copy(
            titleLarge = titleLarge.copy(
                lineHeight = 52.17.sp,
                fontSize = 40.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9A70E2),
            ),
            titleMedium = titleMedium.copy(
                lineHeight = 52.17.sp,
                fontSize = 40.sp,
                fontFamily = fontFamily,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
            ),
            titleSmall = titleSmall.copy(fontFamily = fontFamily),
            bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
            bodySmall = bodySmall.copy(fontFamily = fontFamily)
        )
    }
