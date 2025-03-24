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

class ProgressViewTest @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    // Checkpoint data with custom percentages
    private val checkpointData = listOf(
        CheckpointInfo("30", 0.10f),   // 10% of progress bar
        CheckpointInfo("60", 0.30f),   // 30% of progress bar
        CheckpointInfo("120", 0.50f),  // 50% of progress bar
        CheckpointInfo("240", 0.70f),  // 70% of progress bar
        CheckpointInfo("400", 0.90f)   // 90% of progress bar
    )

    // Data class to hold checkpoint information
    data class CheckpointInfo(
        val label: String,
        val percentage: Float,
        val threshold: Float = label.toFloat()
    )

    var progress: Float = 0f
        private set

    private var barColor: Int = resources.getColor(R.color.green_accent)
    private var backgroundColor: Int = resources.getColor(R.color.white_warm)
    private var textColor: Int = resources.getColor(R.color.black_full)
    private var textLabelSize: Float = context.spToPx(12f)
    private var cornerRadius: Float = context.dpToPx(60f)
    private var desiredHeight: Float = context.dpToPx(6f)
    private var checkpointRadius: Float = context.dpToPx(8f)

    private val maxProgress: Float = 400f
    private var animatedProgress: Float = progress
    private var checkpointPositions: FloatArray = FloatArray(checkpointData.size)
    private var animator: ValueAnimator? = null
    private var barTop: Float = 0f
    private var barBottom: Float = 0f

    val activeCheckpointsCount: Int
        get() = checkpointData.count { progress >= it.threshold }

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
        val barWidth = calculateVisualProgress(animatedProgress)
        canvas.drawRoundRect(0f, barTop, barWidth, barBottom, cornerRadius, cornerRadius, barPaint)
    }

    private fun drawCheckpointsAndLabels(canvas: Canvas) {
        for (i in checkpointData.indices) {
            val checkpointInfo = checkpointData[i]
            val isActive = progress >= checkpointInfo.threshold

            checkpointPaint.color = if (isActive) barColor else backgroundColor
            textPaint.typeface = if (isActive) Typeface.DEFAULT_BOLD else Typeface.DEFAULT

            val checkpointX = checkpointPositions[i]
            val checkpointY = barTop + (desiredHeight / 2)
            canvas.drawCircle(checkpointX, checkpointY, checkpointRadius, checkpointPaint)

            val labelY = checkpointY + checkpointRadius + context.dpToPx(16f)
            canvas.drawText(checkpointInfo.label, checkpointX, labelY, textPaint)
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
        // Map checkpoints based on predefined percentages
        checkpointPositions = checkpointData.map {
            width.toFloat() * it.percentage
        }.toFloatArray()
    }

    private fun calculateVisualProgress(currentProgress: Float): Float {
        return when {
            currentProgress <= 0f -> 0f
            currentProgress >= maxProgress -> width.toFloat()
            else -> {
                // Direct linear mapping based on progress percentage
                val progressPercentage = currentProgress / maxProgress
                width.toFloat() * progressPercentage
            }
        }
    }

    fun setupProgress(value: Float) {
        progress = value.coerceIn(0f, maxProgress)
        animatedProgress = progress  // Sync immediately
        Log.d("ProgressView", "When set progress: $progress")
        animateProgress(progress)
    }

    fun getCurrentProgress(): Float {
        Log.d("ProgressView", "When get current progress: $progress")
        return if (animator?.isRunning == true) {
            animatedProgress // Return live animation value
        } else {
            progress // Return final value
        }
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
                progress = animatedProgress
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

    private fun checkCheckpointReached() {
        // Check against specific thresholds
        for (index in checkpointData.indices) {
            val checkpointInfo = checkpointData[index]
            if (progress >= checkpointInfo.threshold) {
                animateCheckpoint(index)
            }
        }
    }

    private fun animateCheckpoint(checkpointIndex: Int) {
        val scaleAnimator = ValueAnimator.ofFloat(1f, 1.5f, 1f).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { animation ->
                val scale = animation.animatedValue as Float
                checkpointPaint.color = barColor
                checkpointPaint.alpha = (255 * scale).toInt()
                Log.d("ProgressView", "When animate checkpoint: $progress")
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

    fun Context.spToPx(sp: Float): Float {
        return sp * resources.displayMetrics.scaledDensity
    }

    fun Context.dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }
}