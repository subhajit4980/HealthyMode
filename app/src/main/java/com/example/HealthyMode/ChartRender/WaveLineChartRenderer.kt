package com.example.HealthyMode.ChartRender

import android.graphics.Canvas
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.renderer.LineChartRenderer
import com.github.mikephil.charting.utils.ViewPortHandler

class WaveLineChartRenderer(
    chart: LineDataProvider?,
    animator: ChartAnimator?,
    viewPortHandler: ViewPortHandler?
) : LineChartRenderer(chart, animator, viewPortHandler) {

    override fun drawLinear(canvas: Canvas?, dataSet: ILineDataSet?) {
        val cubicPath = cubicPath
        val cubicFillPath = cubicFillPath

        // Create a new path to draw the line
        cubicPath.reset()

        // Move to the first point in the dataset
        val entry = dataSet?.getEntryForIndex(0)
        cubicPath.moveTo(entry?.x ?: 0f, entry?.y ?: 0f)

        // Loop through the remaining points in the dataset and create a wave pattern
        var firstWave = true
        var previousEntry: Entry? = null
        if (dataSet != null) {
            if (dataSet.entryCount!=0)
                for (i in 1 until dataSet.entryCount) {
                    val currentEntry = dataSet.getEntryForIndex(i)


                    // Calculate the midpoint between the previous and current point
                    val midX = (previousEntry?.x ?: 0f) + (currentEntry?.x ?: 0f) / 2f
                    val midY = (previousEntry?.y ?: 0f) + (currentEntry?.y ?: 0f) / 2f

                    // Draw a curve from the previous point to the midpoint
                    if (firstWave) {
                        cubicPath.cubicTo(
                            (previousEntry?.x ?: 0f) + 0.2f,
                            (previousEntry?.y ?: 0f),
                            (midX - 0.2f),
                            midY,
                            midX,
                            midY
                        )
                        firstWave = false
                    } else {
                        cubicPath.cubicTo(
                            previousEntry?.x ?: 0f,
                            previousEntry?.y ?: 0f,
                            (midX - 0.2f),
                            midY,
                            midX,
                            midY
                        )
                    }

                    // Draw a curve from the midpoint to the current point
                    cubicPath.cubicTo(
                        (midX + 0.2f),
                        midY,
                        (currentEntry?.x ?: 0f) - 0.2f,
                        (currentEntry?.y ?: 0f),
                        (currentEntry?.x ?: 0f),
                        (currentEntry?.y ?: 0f)
                    )

                    // Set the previous entry to the current entry
                    previousEntry = currentEntry
                }
        }

        // Draw the path and fill the area under the line with the dataset's fill color
        cubicFillPath.reset()
        cubicFillPath.addPath(cubicPath)
        drawFilledPath(canvas, cubicFillPath, dataSet?.fillColor ?: 0, dataSet?.fillAlpha ?: 255)
        drawCubicBezier(dataSet)
    }
}