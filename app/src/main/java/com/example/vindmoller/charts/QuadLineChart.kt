package com.example.vindmoller.charts




import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vindmoller.algorithms.getProducedEnergyOFOneWindSpeed
import com.example.vindmoller.data.sourceforecast.SourceForecast
import com.example.vindmoller.ui.theme.darkPrimaryColor
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun EnergyChart(
    modifier: Modifier = Modifier,
    data: List<Pair<Int, Double>> = emptyList()
) {
    val spacing = 100f
    val graphColor = darkPrimaryColor
    val transparentGraphColor = remember { graphColor.copy(alpha = 0.8f) }
    val upperValue = remember { 1200 }
    val lowerValue = remember { 0 }
    val density = LocalDensity.current

    val textPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 8.sp.toPx() }
        }
    }

    val xAxisLabelPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 8.sp.toPx() }
            textSkewX = -0.25f // Italic font
        }
    }

    val yAxisLabelPaint = remember(density) {
        Paint().apply {
            color = android.graphics.Color.BLACK
            textAlign = Paint.Align.CENTER
            textSize = density.run { 8.sp.toPx() }
            textSkewX = -0.25f
        }
    }


    Canvas(modifier = modifier) {
        val spacePerHour = (size.width - spacing) / data.size
        (data.indices step 2).forEach { i ->
            val hour = data[i].first
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    hour.toString(),
                    spacing + i * spacePerHour,
                    size.height,
                    textPaint
                )
            }
        }

        val priceStep = (upperValue - lowerValue) / 5f
        (0..4).forEach { i ->
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    round(lowerValue + priceStep * i).toString(),
                    30f,
                    size.height - spacing - i * size.height / 5f,
                    textPaint
                )
            }
        }
        drawContext.canvas.nativeCanvas.apply {
            drawText(
                "Next 24h",
                size.width / 2f,
                size.height - 40f,
                xAxisLabelPaint
            )

            save()
            rotate(-90f)
            drawText(
                "Generated Power  Fluctuations Kwh",
                -size.height / 2f,
                90f,
                yAxisLabelPaint
            )
            restore()
        }



        var medX: Float
        var medY: Float
        val strokePath = Path().apply {
            val height = size.height
            data.indices.forEach { i ->
                val nextInfo = data.getOrNull(i + 1) ?: data.last()
                val firstRatio = (data[i].second - lowerValue) / (upperValue - lowerValue)
                val secondRatio = (nextInfo.second - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + i * spacePerHour
                val y1 = height - spacing - (firstRatio * height).toFloat()
                val x2 = spacing + (i + 1) * spacePerHour
                val y2 = height - spacing - (secondRatio * height).toFloat()
                if (i == 0) {
                    moveTo(x1, y1)
                } else {
                    medX = (x1 + x2) / 2f
                    medY = (y1 + y2) / 2f
                    quadraticBezierTo(x1 = x1, y1 = y1, x2 = medX, y2 = medY)
                }
            }
        }

        drawPath(
            path = strokePath,
            color = Color.Blue,
            style = Stroke(
                width =1.dp.toPx(),
                cap = StrokeCap.Round
            )
        )

        val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {
            lineTo(size.width - spacePerHour, size.height - spacing)
            lineTo(spacing, size.height - spacing)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    transparentGraphColor,
                    Color.Transparent
                ),
                endY = size.height - spacing
            )
        )

    }
}
@Composable
fun DisplayChart(sourceForecasts: List<SourceForecast>) {
    val data = sourceForecasts.take(24).mapIndexed { index, it ->
        val producedEnergy = getProducedEnergyOFOneWindSpeed(
            1.25, // change here
            5000.0,
            0.4,
            it.windSpeed.toDouble(),
            0.95,
            0.95
        )
        Pair(index + 1, producedEnergy)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        EnergyChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .align(Alignment.CenterHorizontally),
            data = data
        )
    }

}

@Composable
fun ChartCard(sourceForecasts: List<SourceForecast>) {
    Card(
        modifier = Modifier
            .size(minOf(LocalConfiguration.current.screenWidthDp.dp, LocalConfiguration.current.screenHeightDp.dp) * 0.7f)
            .background(Color.Transparent).padding(start = 60.dp)
    ) {
        Box(modifier = Modifier.aspectRatio(0.8f)) {
            DisplayChart(sourceForecasts)
        }
    }
}

@Composable
fun calculateAverageWindSpeedFirstDay(sourceForecasts: List<SourceForecast>) : Double {
    val data = sourceForecasts.take(24)
    val windSpeedAverage = data.map { it.windSpeed }.average()
    return (windSpeedAverage * 100).roundToInt() / 100.0
}

