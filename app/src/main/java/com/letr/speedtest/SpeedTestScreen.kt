package com.letr.speedtest

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.Wallpapers
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.letr.speedtest.theme.ComposeSpeedTestTheme
import kotlinx.coroutines.launch
import java.lang.Float.max
import kotlin.math.floor
import kotlin.math.roundToInt

suspend fun startAnimation(animation: Animatable<Float, AnimationVector1D>) {
    animation.animateTo(0.84f, keyframes {
        durationMillis = 9000
        0f at 0 using CubicBezierEasing(0f, 1.5f, 0.8f, 1f)
        0.72f at 1000 using CubicBezierEasing(0.2f, -1.5f, 0f, 1f)
        0.76f at 2000 using CubicBezierEasing(0.2f, -2f, 0f, 1f)
        0.78f at 3000 using CubicBezierEasing(0.2f, -1.5f, 0f, 1f)
        0.82f at 4000 using CubicBezierEasing(0.2f, -2f, 0f, 1f)
        0.85f at 5000 using CubicBezierEasing(0.2f, -2f, 0f, 1f)
        0.89f at 6000 using CubicBezierEasing(0.2f, -1.2f, 0f, 1f)
        0.82f at 7500 using LinearOutSlowInEasing
    })
}

fun Animatable<Float, AnimationVector1D>.toUiState(maxSpeed: Float) = UiState(
    arcValue = value,
    speed = "%.1f".format(maxSpeed),
    ping = if (value > 0.2f) "${(value * 15).roundToInt()} ms" else "-",
    maxSpeed = if (maxSpeed > 0f) "%.1f mbps".format(maxSpeed) else "-",
    inProgress = isRunning
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpeedTestScreen(state: UiState, onClick: () -> Unit) {
    val navItemList = listOf(
        NavItem("Home", ImageVector.vectorResource(R.drawable.wifi)),
        NavItem("Search", ImageVector.vectorResource(R.drawable.speed)),
        NavItem("Settings", ImageVector.vectorResource(R.drawable.settings))
    )
    ComposeSpeedTestTheme{
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = colorScheme.surface,
            topBar = {
                CenterAlignedTopAppBar(
                    title = {Text("SpeedTest")},
                    colors = TopAppBarColors(
                        containerColor = colorScheme.primaryContainer,
                        titleContentColor = colorScheme.primary,
                        scrolledContainerColor = colorScheme.surfaceContainer,
                        navigationIconContentColor = colorScheme.background,
                        actionIconContentColor = colorScheme.onSurface
                    )
                ) },
                bottomBar = {
                    NavigationBar {
                        navItemList.forEachIndexed { index, item ->
                            NavigationBarItem(
                                selected = true,
                                onClick = {},
                                label = { navItemList[index].label},
                                icon = { Icon(item.icon, contentDescription = item.label) },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = colorScheme.primary,
                                    selectedTextColor = colorScheme.surfaceContainer,
                                    unselectedIconColor = colorScheme.onSurface,
                                    unselectedTextColor = colorScheme.background
                                )
                            )
                        }
                    }
                }
        ){ padding ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(padding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    SpeedIndicator(state = state)
                    //AnimatedVisibility(state.inProgress) { }
                    AdditionalInfo(state = state, onClick = onClick)
                }
        }
    }
}
@Composable
fun SpeedTestScreen() {
    val coroutineScope = rememberCoroutineScope()
    val animation = remember { Animatable(0f) }
    val maxSpeed = remember { mutableFloatStateOf(0f) }
    val networkspeed = SpeedChecker()
    //maxSpeed.floatValue = max(maxSpeed.floatValue, animation.value * 100f)
    //maxSpeed.floatValue = maxSpeed.floatValue
    SpeedTestScreen(animation.toUiState(maxSpeed.floatValue)) {
        coroutineScope.launch {
            maxSpeed.floatValue = networkspeed.measureDownloadSpeed()
            startAnimation(animation)
        }
    }
}

@Composable
fun SpeedIndicator(state: UiState) {
    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        CircularSpeedIndicator(state.arcValue, 240f)
        SpeedValue(state.speed)
    }
}

@Composable
fun SpeedValue(value: String) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("DOWNLOAD", style = MaterialTheme.typography.bodySmall)
        Text(
            text = value,
            fontSize = 45.sp,
            color = colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Text("Mbps", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun StartButton(isEnabled: Boolean, onClick: () -> Unit) {
    FilledTonalButton(
        onClick = onClick,
        modifier = Modifier.padding(bottom = 24.dp),
        colors = ButtonColors(
            containerColor = colorScheme.primary,
            contentColor = colorScheme.surfaceContainer,
            disabledContentColor = Color.LightGray,
            disabledContainerColor = Color.Gray
        ),
        enabled = isEnabled
        ) {
        Text(
            text = "START",
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun AdditionalInfo(state: UiState, onClick: () -> Unit) {
    StartButton(!state.inProgress, onClick)
    @Composable
    fun RowScope.InfoColumn(title: String, value: String) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Text(title, fontWeight = FontWeight.Bold)
            Text(
                value,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }

    Row(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        InfoColumn(title = "PING", value = state.ping)
        VerticalDivider()
        InfoColumn(title = "MAX SPEED", value = state.maxSpeed)
    }
}

@Composable
fun VerticalDivider() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .background(Color(0xFF414D66))
            .width(1.dp)
    )
}

@Composable
fun CircularSpeedIndicator(value: Float, angle: Float) {
    val materialColors = listOf<Color>(colorScheme.primary, colorScheme.primaryContainer)
    Canvas(modifier = Modifier.fillMaxSize().padding(50.dp)){
        drawLines(value, angle, materialColors)
        drawArcs(value, angle, materialColors)
    }
}

fun DrawScope.drawArcs(progress: Float, maxValue: Float, primaryColor: List<Color>) {
    val startAngle = 270 - maxValue / 2
    val sweepAngle = maxValue * progress

    val topLeft = Offset(50f, 50f)
    val size = Size(size.width - 100f, size.height - 100f)

    fun drawBlur() {
        for (i in 0..20) {
            drawArc(
                color = primaryColor[0].copy(alpha = i / 900f),
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = size,
                style = Stroke(width = 80f + (20 - i) * 20, cap = StrokeCap.Round)
            )
        }
    }
    fun drawStroke() {
        drawArc(
            color = primaryColor[0],
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft,
            size = size,
            style = Stroke(width = 86f, cap = StrokeCap.Round)
        )
    }

    fun drawGradient() {
        drawArc(
            brush = Brush.linearGradient(
                colors = listOf(primaryColor[1], primaryColor[0]),
                start = Offset(0f, 0f),
                end = Offset(Float.POSITIVE_INFINITY, 0f)
            ),
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = false,
            topLeft = topLeft,
            size = size,
            style = Stroke(width = 80f, cap = StrokeCap.Round)
        )
    }

    drawBlur()
    drawStroke()
    drawGradient()
}
fun DrawScope.drawLines(progress: Float, maxValue: Float, primaryColor: List<Color>, numberOfLines: Int = 40) {
    val oneRotation = maxValue / numberOfLines
    val startValue = if (progress == 0f) 0 else floor(progress * numberOfLines).toInt() + 1

    for (i in startValue..numberOfLines) {
        rotate(i * oneRotation + (180 - maxValue) / 2) {
            drawLine(
                primaryColor[0],
                Offset(if (i % 5 == 0) 80f else 30f, size.height / 2),
                Offset(0f, size.height / 2),
                8f,
                StrokeCap.Round
            )
        }
    }
}

@Preview(showBackground = true, wallpaper = Wallpapers.GREEN_DOMINATED_EXAMPLE)
@Composable
fun DefaultPreview() {
    SpeedTestScreen(
        state = UiState(
        ping = "10 ms",
        speed = "80",
        maxSpeed = "100 mbps"
        )
    ) {}
}
