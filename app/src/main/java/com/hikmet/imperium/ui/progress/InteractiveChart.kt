package com.hikmet.imperium.ui.progress

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import android.view.GestureDetector
import android.view.MotionEvent
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.hikmet.imperium.ui.theme.Primary
import com.hikmet.imperium.ui.theme.Secondary
import java.time.format.DateTimeFormatter

/**
 * Interactive chart component with gesture support
 */
@Composable
fun InteractiveChart(
    modifier: Modifier = Modifier,
    onTimeViewChange: (TimeView) -> Unit = {},
    chartData: List<ProgressEntry> = emptyList(),
    currentTimeView: TimeView = TimeView.WEEKLY
) {
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Time view indicator
        TimeViewIndicator(currentTimeView = currentTimeView)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Chart card
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Progress Over ${currentTimeView.displayName}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Interactive chart
                AndroidView(
                    factory = { context ->
                        createLineChart(context, currentTimeView) { newTimeView ->
                            onTimeViewChange(newTimeView)
                            Toast.makeText(context, "Switched to ${newTimeView.displayName} view", Toast.LENGTH_SHORT).show()
                        }
                    },
                    update = { chart ->
                        updateChartData(chart, currentTimeView, chartData)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
        }
    }
}

/**
 * Time view indicator showing current view
 */
@Composable
private fun TimeViewIndicator(currentTimeView: TimeView) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Primary.copy(alpha = 0.1f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = when (currentTimeView) {
                TimeView.WEEKLY -> Icons.Default.AccessTime
                TimeView.MONTHLY -> Icons.Default.CalendarToday
                TimeView.ALL_TIME -> Icons.Default.Timeline
            }
            
            Icon(
                imageVector = icon,
                contentDescription = "Time view icon",
                tint = Primary,
                modifier = Modifier.width(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Text(
                text = "Current View: ${currentTimeView.displayName}",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Primary
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Text(
                text = "Swipe ← → to change view",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Create line chart with gesture detection
 */
private fun createLineChart(
    context: Context,
    initialTimeView: TimeView,
    onTimeViewChange: (TimeView) -> Unit
): LineChart {
    val chart = LineChart(context)
    
    // Basic chart setup
    chart.description.isEnabled = false
    chart.setTouchEnabled(true)
    chart.isDragEnabled = true
    chart.setScaleEnabled(true)
    chart.setPinchZoom(true)
    chart.setDrawGridBackground(false)
    chart.legend.isEnabled = false
    chart.setNoDataText("Loading...")
    
    // X-axis setup
    chart.xAxis.apply {
        position = XAxis.XAxisPosition.BOTTOM
        setDrawGridLines(false)
        textColor = Color.Gray.toArgb()
        textSize = 10f
    }
    
    // Y-axis setup
    chart.axisLeft.apply {
        setDrawGridLines(true)
        gridColor = Color.LightGray.toArgb()
        textColor = Color.Gray.toArgb()
        textSize = 10f
        axisMinimum = 0f
        axisMaximum = 100f
    }
    
    chart.axisRight.isEnabled = false
    
    // Gesture detection for swipe
    val gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            val currentTimeView = when (chart.tag) {
                TimeView.WEEKLY.name -> TimeView.WEEKLY
                TimeView.MONTHLY.name -> TimeView.MONTHLY
                TimeView.ALL_TIME.name -> TimeView.ALL_TIME
                else -> TimeView.WEEKLY
            }
            
            val newTimeView = when {
                velocityX > 500 -> { // Swipe right
                    when (currentTimeView) {
                        TimeView.WEEKLY -> TimeView.ALL_TIME
                        TimeView.MONTHLY -> TimeView.WEEKLY
                        TimeView.ALL_TIME -> TimeView.MONTHLY
                    }
                }
                velocityX < -500 -> { // Swipe left
                    when (currentTimeView) {
                        TimeView.WEEKLY -> TimeView.MONTHLY
                        TimeView.MONTHLY -> TimeView.ALL_TIME
                        TimeView.ALL_TIME -> TimeView.WEEKLY
                    }
                }
                else -> currentTimeView
            }
            
            if (newTimeView != currentTimeView) {
                chart.tag = newTimeView.name
                onTimeViewChange(newTimeView)
                return true
            }
            return false
        }
    })
    
    // Set touch listener for gesture detection
    chart.setOnTouchListener { _, event ->
        gestureDetector.onTouchEvent(event)
        false // Let the chart handle other touch events
    }
    
    // Set initial data
    chart.tag = initialTimeView.name
    updateChartData(chart, initialTimeView)
    
    return chart
}

/**
 * Update chart data based on time view
 */
private fun updateChartData(chart: LineChart, timeView: TimeView, data: List<ProgressEntry> = emptyList()) {
    // Data will be provided from the parent component
    
    // Convert data to chart entries
    val entries = data.mapIndexed { index, progressEntry ->
        Entry(index.toFloat(), progressEntry.score)
    }
    
    // Create dataset
    val dataSet = LineDataSet(entries, "Progress").apply {
        color = Primary.toArgb()
        setCircleColor(Primary.toArgb())
        circleHoleColor = Secondary.toArgb()
        lineWidth = 3f
        circleRadius = 6f
        circleHoleRadius = 3f
        setDrawValues(false)
        setDrawFilled(true)
        fillColor = Primary.toArgb()
        fillAlpha = 50
        mode = LineDataSet.Mode.CUBIC_BEZIER
        cubicIntensity = 0.2f
    }
    
    // Set up X-axis formatter
    val formatter = object : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val index = value.toInt()
            if (index >= 0 && index < data.size) {
                val entry = data[index]
                return when (timeView) {
                    TimeView.WEEKLY -> entry.date.format(DateTimeFormatter.ofPattern("E"))
                    TimeView.MONTHLY -> entry.date.format(DateTimeFormatter.ofPattern("MMM dd"))
                    TimeView.ALL_TIME -> entry.date.format(DateTimeFormatter.ofPattern("MMM yy"))
                }
            }
            return ""
        }
    }
    
    chart.xAxis.valueFormatter = formatter
    chart.data = LineData(dataSet)
    chart.animateY(1000)
    chart.invalidate()
} 