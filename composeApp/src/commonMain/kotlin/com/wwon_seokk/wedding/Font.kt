package com.wwon_seokk.wedding

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import wedding.composeapp.generated.resources.Res
import wedding.composeapp.generated.resources.pretendard_bold
import wedding.composeapp.generated.resources.pretendard_extra_bold
import wedding.composeapp.generated.resources.pretendard_extra_light
import wedding.composeapp.generated.resources.pretendard_medium
import wedding.composeapp.generated.resources.pretendard_regular
import wedding.composeapp.generated.resources.pretendard_semi_bold
import wedding.composeapp.generated.resources.pretendard_thin

val fontFamily
    @Composable
    get() = Typography().run {
        val fontFamily = FontFamily(
            Font(
                Res.font.pretendard_bold,
                weight = FontWeight.Bold,
            ),
            Font(
                Res.font.pretendard_regular,
                weight = FontWeight.Normal,
            ),
            Font(
                Res.font.pretendard_extra_bold,
                weight = FontWeight.ExtraBold,
            ),
            Font(
                Res.font.pretendard_extra_light,
                weight = FontWeight.ExtraLight,
            ),
            Font(
                Res.font.pretendard_extra_light,
                weight = FontWeight.Light,
            ),
            Font(
                Res.font.pretendard_medium,
                weight = FontWeight.Medium,
            ),
            Font(
                Res.font.pretendard_semi_bold,
                weight = FontWeight.SemiBold,
            ),
            Font(
                Res.font.pretendard_thin,
                weight = FontWeight.Thin,
            ),
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
