package com.fireesports.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fireesports.ui.theme.Montserrat
import com.fireesports.ui.theme.NeonAqua
import com.fireesports.ui.theme.NeonPink
import com.fireesports.ui.theme.NeonPurple

@Composable
fun GlowButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    glowColor: Color = NeonAqua,
    enabled: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )

    Box(
        modifier = modifier
            .height(56.dp)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        // Glow effect
        Box(
            modifier = Modifier
                .matchParentSize()
                .blur(20.dp)
                .background(
                    color = glowColor.copy(alpha = if (enabled) glowAlpha else 0.1f),
                    shape = RoundedCornerShape(16.dp)
                )
        )
        
        // Button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.horizontalGradient(
                        colors = if (enabled) {
                            listOf(glowColor.copy(alpha = 0.8f), glowColor)
                        } else {
                            listOf(Color.Gray.copy(alpha = 0.3f), Color.Gray.copy(alpha = 0.5f))
                        }
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    }
}
