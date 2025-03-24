package com.example.uxassignment3

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class ProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var progress: Float = 0f
        private set

    private var barColor: Int = resources.getColor(R.color.green_accent)
    private var backgroundColor: Int = resources.getColor(R.color.white_warm)
    private var textColor: Int = resources.getColor(R.color.black_full)
    private var textLabelSize: Float = context.spToPx(12f)
    private var cornerRadius: Float = context.dpToPx(60f)
    private var desiredHeight: Float = context.dpToPx(6f)
    private var checkpointRadius: Float = context.dpToPx(8f)
    private var checkpointCount: Int = 5
    var checkpointLabels: List<String> = listOf("30", "60", "120", "240", "400")

    private var animatedProgress: Float = progress
    private var animator: ValueAnimator? = null
    private var barTop: Float = 0f
    private var barBottom: Float = 0f
    private var checkpointPositions: FloatArray = FloatArray(0)
    var activeCheckpoint: Int = 0

    private val backgroundPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = backgroundColor
    }

    private val barPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = barColor
    }

    private val checkpointPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = barColor
    }

    private val textPaint = Paint().apply {
        isAntiAlias = true
        color = textColor
        textSize = textLabelSize
        textAlign = Paint.Align.CENTER
    }

    init {
        initAttributes(context, attrs)
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomDotIndicator)
        try {
            progress = typedArray.getFloat(R.styleable.CustomDotIndicator_progress, progress)
            barColor = typedArray.getColor(R.styleable.CustomDotIndicator_barColor, barColor)
            checkpointRadius = typedArray.getDimension(R.styleable.CustomDotIndicator_checkpointRadius, checkpointRadius)
        } finally {
            typedArray.recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackgroundBar(canvas)
        drawProgressBar(canvas)
        drawCheckpointsAndLabels(canvas)
    }

    private fun drawBackgroundBar(canvas: Canvas) {
        canvas.drawRoundRect(0f, barTop, width.toFloat(), barBottom, cornerRadius, cornerRadius, backgroundPaint)
    }

    private fun drawProgressBar(canvas: Canvas) {
        val barWidth = width * (animatedProgress / 100)
        canvas.drawRoundRect(0f, barTop, barWidth, barBottom, cornerRadius, cornerRadius, barPaint)
    }

    private fun drawCheckpointsAndLabels(canvas: Canvas) {
        for (i in checkpointPositions.indices) {
            val checkpointX = checkpointPositions[i]
            val checkpointY = barTop + (desiredHeight / 2)

            val checkpointProgress = (i.toFloat() / (checkpointCount - 1)) * 100
            checkpointPaint.color = if (animatedProgress >= checkpointProgress) barColor else backgroundColor
            textPaint.typeface = if (animatedProgress >= checkpointProgress) Typeface.DEFAULT_BOLD else Typeface.DEFAULT

            canvas.drawCircle(checkpointX, checkpointY, checkpointRadius, checkpointPaint)
            val labelY = checkpointY + checkpointRadius + context.dpToPx(16f)
            canvas.drawText(checkpointLabels[i], checkpointX, labelY, textPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val minHeight = (2 * checkpointRadius) + desiredHeight + 100
        val height = resolveSize(minHeight.toInt(), heightMeasureSpec)
        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        computeBarDimensions()
        computeCheckpointPositions(w)
    }

    private fun computeBarDimensions() {
        barTop = checkpointRadius
        barBottom = barTop + desiredHeight
    }

    private fun computeCheckpointPositions(width: Int) {
        val totalWidth = width.toFloat()
        val padding = totalWidth * 0.1f
        val availableWidth = totalWidth - 2 * padding
        val spacing = availableWidth / (checkpointCount - 1)
        checkpointPositions = FloatArray(checkpointCount) { i -> padding + (spacing * i) }
    }

    fun setProgress(value: Float) {
        progress = value.coerceIn(0f, 100f)
        animateProgress(progress)
    }

    fun setBarColor(color: Int) {
        barColor = color
        checkpointPaint.color = barColor
        invalidate()
    }

    private fun animateProgress(targetProgress: Float, duration: Long = 500, startDelay: Long = 0) {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(animatedProgress, targetProgress).apply {
            this.duration = duration
            this.startDelay = startDelay
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                animatedProgress = animation.animatedValue as Float
                invalidate()
            }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    checkCheckpointReached()
                }
            })
            start()
        }
    }

    private fun checkCheckpointReached() : Int{
        for (index in 1 until checkpointCount) {
            val checkpointProgress = (index.toFloat() / checkpointCount) * 100
            if (progress >= checkpointProgress && animatedProgress >= checkpointProgress) {
                animateCheckpoint(index)
                activeCheckpoint ++
                Log.d("Activee Checkpoint", activeCheckpoint.toString())
            }
        }
        return activeCheckpoint
    }

    private fun animateCheckpoint(checkpointIndex: Int) {
        val scaleAnimator = ValueAnimator.ofFloat(1f, 1.5f, 1f).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                val scale = animation.animatedValue as Float
                checkpointPaint.color = barColor
                checkpointPaint.alpha = (255 * scale).toInt()
                invalidate()
            }
            start()
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animatedProgress = -1f
        animateProgress(progress, 1500, 500)
    }

    fun Context.pxToDp(px: Float): Float {
        return px / resources.displayMetrics.density
    }

    fun Context.pxToSp(px: Float): Float {
        return px / resources.displayMetrics.scaledDensity
    }

    fun Context.spToPx(sp: Float): Float {
        return sp * resources.displayMetrics.scaledDensity
    }

    fun Context.dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }


//    private val checkpoints = listOf(30, 60, 120, 240, 400)
//    private var currentProgress = 0
//
//    private val progressPaint = Paint().apply {
//        style = Paint.Style.FILL
//        strokeCap = Paint.Cap.ROUND
//    }
//
//    private val backgroundPaint = Paint().apply {
//        style = Paint.Style.FILL
//        color = 0xFFEEEEEE.toInt() // Light gray color for background
//        strokeCap = Paint.Cap.ROUND
//    }
//
//    private val checkpointPaint = Paint().apply {
//        style = Paint.Style.FILL
//        strokeCap = Paint.Cap.ROUND
//    }
//
//    private val textPaint = Paint().apply {
//        color = 0xFF000000.toInt() // Black color for text
//        textAlign = Paint.Align.CENTER
//        textSize = 30f
//    }
//
//    private val progressBarHeight = 20f
//    private val checkpointRadius = 15f
//
//    fun setProgress(progress: Int) {
//        currentProgress = progress
//        invalidate()
//    }
//
//    override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas)
//
//        val width = width.toFloat()
//        val height = height.toFloat()
//
//        // Calculate the width of each segment
//        val segmentWidth = width / (checkpoints.size - 1)
//
//        // Determine progress color based on current progress
//        progressPaint.color = if (currentProgress > 300) {
//            0xFFFFD700.toInt() // Gold color
//        } else {
//            0xFF2E7D32.toInt() // Green color
//        }
//
//        // Draw background line
//        canvas.drawLine(
//            0f,
//            checkpointRadius,
//            width,
//            checkpointRadius,
//            backgroundPaint
//        )
//
//        // Calculate progress width
//        val progressWidth = if (currentProgress >= checkpoints.last()) {
//            width
//        } else {
//            var checkpointIndex = 0
//            for (i in checkpoints.indices) {
//                if (currentProgress < checkpoints[i]) {
//                    checkpointIndex = i
//                    break
//                }
//            }
//
//            val prevCheckpoint = if (checkpointIndex > 0) checkpoints[checkpointIndex - 1] else 0
//            val nextCheckpoint = checkpoints[checkpointIndex]
//
//            val prevX = if (checkpointIndex > 0) (checkpointIndex - 1) * segmentWidth else 0f
//            val progressPercentInSegment = (currentProgress - prevCheckpoint).toFloat() / (nextCheckpoint - prevCheckpoint)
//            prevX + progressPercentInSegment * segmentWidth
//        }
//
//        // Draw progress line
//        canvas.drawLine(
//            0f,
//            checkpointRadius,
//            progressWidth,
//            checkpointRadius,
//            progressPaint
//        )
//
//        // Draw checkpoints and labels
//        for (i in checkpoints.indices) {
//            val x = i * segmentWidth
//            val isCheckpointMet = currentProgress >= checkpoints[i]
//
//            // Set checkpoint color based on whether it's met
//            checkpointPaint.color = if (isCheckpointMet) {
//                if (currentProgress > 300) 0xFFFFD700.toInt() else 0xFF2E7D32.toInt()
//            } else {
//                0xFFEEEEEE.toInt()
//            }
//
//            // Draw checkpoint circle
//            canvas.drawCircle(
//                x,
//                checkpointRadius,
//                checkpointRadius,
//                checkpointPaint
//            )
//
//            // Set text style based on whether checkpoint is met
//            textPaint.typeface = if (isCheckpointMet) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
//
//            // Draw checkpoint label
//            canvas.drawText(
//                checkpoints[i].toString(),
//                x,
//                checkpointRadius * 4,
//                textPaint
//            )
//        }
//    }
}