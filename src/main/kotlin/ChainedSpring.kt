import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

@Composable
fun ChainedSpring() {
    MaterialTheme {
        Surface(
            Modifier.fillMaxSize(),
            color = Color.Black
        ) {
            var moved by remember { mutableStateOf(false) }

            LaunchedEffect(true) {
                infiniteLoopFlow.collect {
                    moved = it % 2 == 0
                }
            }

            Box(
                contentAlignment = Alignment.Center
            ) {
                for (i in 0 until 8) {
                    Ring(
                        moved = moved,
                        idx = i,
                        size = (16 + 22 * i).dp,
                        strokeWidth = 16f
                    )
                }
            }
        }
    }
}

@Composable
private fun Ring(
    idx: Int,
    moved: Boolean,
    size: Dp,
    strokeWidth: Float
) {
    val offset = remember { Animatable(-150f)}

    LaunchedEffect(moved) {
        delay((1000 + 40 * idx).toLong())
        offset.animateTo(
            targetValue = if (moved) 150f else -150f,
            animationSpec = spring(
                dampingRatio = 0.22f - 0.002f * idx,
                stiffness = 200f + 10f * idx
            )
        )
    }

    Canvas(
        Modifier
            .size(size)
            .offset(y = (offset.value).dp)
            .graphicsLayer { rotationX = 72f }
    ) {
        drawRing(strokeWidth)
    }
}

private fun DrawScope.drawRing(strokeWidth: Float) {
    drawArc(
        color = Color.White,
        startAngle = 0f,
        sweepAngle = 360f,
        useCenter = true,
        style = Stroke(strokeWidth)
    )
}

val infiniteLoopFlow: Flow<Int> = flow {
    var idx = 0
    while (true) {
        emit(idx)
        idx++
        delay(2500L)
    }
}