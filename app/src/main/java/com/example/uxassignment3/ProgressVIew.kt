package com.example.uxassignment3

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

class ProgressVIew @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val checkpoints = listOf(30, 60, 120, 240, 400)
    private var currentProgress = 0

    private val progressPaint = Paint().apply {
        style = Paint.Style.FILL
        strokeCap = Paint.Cap.ROUND
    }

    private val backgroundPaint = Paint().apply {
        style = Paint.Style.FILL
        color = 0xFFEEEEEE.toInt() // Light gray color for background
        strokeCap = Paint.Cap.ROUND
    }

    private val checkpointPaint = Paint().apply {
        style = Paint.Style.FILL
        strokeCap = Paint.Cap.ROUND
    }

    private val textPaint = Paint().apply {
        color = 0xFF000000.toInt() // Black color for text
        textAlign = Paint.Align.CENTER
        textSize = 30f
    }

    private val progressBarHeight = 20f
    private val checkpointRadius = 15f

    fun setProgress(progress: Int) {
        currentProgress = progress
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        // Calculate the width of each segment
        val segmentWidth = width / (checkpoints.size - 1)

        // Determine progress color based on current progress
        progressPaint.color = if (currentProgress > 300) {
            0xFFFFD700.toInt() // Gold color
        } else {
            0xFF2E7D32.toInt() // Green color
        }

        // Draw background line
        canvas.drawLine(
            0f,
            checkpointRadius,
            width,
            checkpointRadius,
            backgroundPaint
        )

        // Calculate progress width
        val progressWidth = if (currentProgress >= checkpoints.last()) {
            width
        } else {
            var checkpointIndex = 0
            for (i in checkpoints.indices) {
                if (currentProgress < checkpoints[i]) {
                    checkpointIndex = i
                    break
                }
            }

            val prevCheckpoint = if (checkpointIndex > 0) checkpoints[checkpointIndex - 1] else 0
            val nextCheckpoint = checkpoints[checkpointIndex]

            val prevX = if (checkpointIndex > 0) (checkpointIndex - 1) * segmentWidth else 0f
            val progressPercentInSegment = (currentProgress - prevCheckpoint).toFloat() / (nextCheckpoint - prevCheckpoint)
            prevX + progressPercentInSegment * segmentWidth
        }

        // Draw progress line
        canvas.drawLine(
            0f,
            checkpointRadius,
            progressWidth,
            checkpointRadius,
            progressPaint
        )

        // Draw checkpoints and labels
        for (i in checkpoints.indices) {
            val x = i * segmentWidth
            val isCheckpointMet = currentProgress >= checkpoints[i]

            // Set checkpoint color based on whether it's met
            checkpointPaint.color = if (isCheckpointMet) {
                if (currentProgress > 300) 0xFFFFD700.toInt() else 0xFF2E7D32.toInt()
            } else {
                0xFFEEEEEE.toInt()
            }

            // Draw checkpoint circle
            canvas.drawCircle(
                x,
                checkpointRadius,
                checkpointRadius,
                checkpointPaint
            )

            // Set text style based on whether checkpoint is met
            textPaint.typeface = if (isCheckpointMet) Typeface.DEFAULT_BOLD else Typeface.DEFAULT

            // Draw checkpoint label
            canvas.drawText(
                checkpoints[i].toString(),
                x,
                checkpointRadius * 4,
                textPaint
            )
        }
    }
}