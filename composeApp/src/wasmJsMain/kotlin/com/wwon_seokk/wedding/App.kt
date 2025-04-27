package com.wwon_seokk.wedding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource

import wedding.composeapp.generated.resources.Res
import wedding.composeapp.generated.resources.test

@Composable
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            delay(500)
            showContent = true
        }
        Column(
            Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AnimatedVisibility(showContent) {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        modifier = Modifier.heightIn(250.dp),
                        painter = painterResource(Res.drawable.test),
                        contentDescription = null
                    )
                    Text(
                        text = "떠영♥️원석",
                        style = fontFamily.h2
                    )
                }
            }
        }
    }
}
