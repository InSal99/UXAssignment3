package com.example.uxassignment3

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView

class ProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val MAX_PROGRESS = 500f
    }

    private val checkpointData = listOf(
        CheckpointInfo("30", 0.10f, 30f),    // 10% of progress bar
        CheckpointInfo("60", 0.30f, 60f),    // 30% of progress bar
        CheckpointInfo("120", 0.50f, 120f),  // 50% of progress bar
        CheckpointInfo("240", 0.70f, 240f),  // 70% of progress bar
        CheckpointInfo("400", 0.90f, 400f)   // 90% of progress bar
    )

    // Progress
    var progress: Float = 0f
        private set
    private var animatedProgress: Float = progress
    private var checkpointPositions: FloatArray = FloatArray(checkpointData.size)

    // Colors
    private var currentLevelColor: Int = context.getColor(R.color.green_accent)
    private val greenLevelBarColor: Int = context.getColor(R.color.green_accent)
    private val goldLevelBarColor: Int = context.getColor(R.color.gold_full)
    private val backgroundColor: Int = context.getColor(R.color.white_warm)
    private val textColor: Int = context.getColor(R.color.black_full)
    private val inactiveTextColor: Int = context.getColor(R.color.black_cool)

    // Dimensions
    private var textLabelSize: Float = context.spToPx(12f)
    private var desiredHeight: Float = context.dpToPx(6f)
    private var checkpointRadius: Float = context.dpToPx(8f)
    private var cornerRadius: Float = context.dpToPx(60f)

    // Typeface
    private val activeTextStyle = R.style.Body_SemiBold
    private val inactiveTextStyle = R.style.Body_Regular

    // Drawing coordinates
    private var barTop: Float = 0f
    private var barBottom: Float = 0f

    // Animation
    private var animator: ValueAnimator? = null

    // Paints
    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = backgroundColor
    }

    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = currentLevelColor
    }

    private val checkpointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = currentLevelColor
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = textColor
        textSize = textLabelSize
        textAlign = Paint.Align.CENTER
    }

    val activeCheckpointsCount: Int
        get() = checkpointData.count { progress >= it.threshold }

    init {
        initAttributes(context, attrs)
    }

    private fun initAttributes(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomDotIndicator)
        try {
            progress = typedArray.getFloat(R.styleable.CustomDotIndicator_progress, progress)
            currentLevelColor = typedArray.getColor(R.styleable.CustomDotIndicator_barColor, currentLevelColor)
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
        updateBarColor()
        val barWidth = calculateVisualProgress(animatedProgress)
        canvas.drawRoundRect(0f, barTop, barWidth, barBottom, cornerRadius, cornerRadius, barPaint)
    }


    private fun drawCheckpointsAndLabels(canvas: Canvas) {
        checkpointData.forEachIndexed { i, checkpointInfo ->
            val isActive = animatedProgress >= checkpointInfo.threshold
            val checkpointX = checkpointPositions[i]
            val checkpointY = barTop + (desiredHeight / 2)

            checkpointPaint.color = if (isActive) currentLevelColor else backgroundColor
            canvas.drawCircle(checkpointX, checkpointY, checkpointRadius, checkpointPaint)

            TextView(context).apply {
                setTextAppearance(if (isActive) activeTextStyle else inactiveTextStyle)
                textPaint.color = currentTextColor
                textPaint.typeface = typeface
                textPaint.textSize = textSize
            }
            val labelY = checkpointY + checkpointRadius + context.dpToPx(16f)
            canvas.drawText(checkpointInfo.label, checkpointX, labelY, textPaint)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val minHeight = (2 * checkpointRadius) + desiredHeight + context.dpToPx(16f)
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
        checkpointPositions = checkpointData.map { width.toFloat() * it.percentage }.toFloatArray()
    }

    private fun calculateVisualProgress(currentProgress: Float): Float {
        return when {
            currentProgress <= 0f -> 0f
            currentProgress >= MAX_PROGRESS -> width.toFloat()
            currentProgress > 400f -> {
                val lastCheckpointPercentage = checkpointData.last().percentage
                val progressBeyondLastCheckpoint = currentProgress - 400f
                val remainingProgressRange = MAX_PROGRESS - 400f
                val additionalPercentage = (progressBeyondLastCheckpoint / remainingProgressRange) * (1f - lastCheckpointPercentage)
                width.toFloat() * (lastCheckpointPercentage + additionalPercentage)
            }
            else -> calculateProgressBetweenCheckpoints(currentProgress)
        }
    }

    private fun calculateProgressBetweenCheckpoints(currentProgress: Float): Float {
        val matchedCheckpoints = checkpointData
            .zipWithNext()
            .firstOrNull { (lower, upper) -> currentProgress in lower.threshold..upper.threshold }

        return if (matchedCheckpoints != null) {
            val (lowerCheckpoint, upperCheckpoint) = matchedCheckpoints
            val segmentProgress = (currentProgress - lowerCheckpoint.threshold) / (upperCheckpoint.threshold - lowerCheckpoint.threshold)
            val interpolatedPercentage = lowerCheckpoint.percentage + (upperCheckpoint.percentage - lowerCheckpoint.percentage) * segmentProgress
            width.toFloat() * interpolatedPercentage
        } else {
            width.toFloat() * (currentProgress / MAX_PROGRESS)
        }
    }

    fun setupProgress(value: Float) {
        progress = value.coerceIn(0f, MAX_PROGRESS)
        animatedProgress = 0f
        animateProgress(progress, 1500, 500)
    }

    private fun animateProgress(targetProgress: Float, duration: Long = 500, startDelay: Long = 0) {
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f, targetProgress).apply {
            this.duration = duration
            this.startDelay = startDelay
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                animatedProgress = it.animatedValue as Float
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
        checkpointData.forEachIndexed { index, checkpointInfo ->
            if (progress >= checkpointInfo.threshold) {
                animateCheckpoint(index)
            }
        }
    }

    private fun animateCheckpoint(checkpointIndex: Int) {
        ValueAnimator.ofFloat(1f, 1.5f, 1f).apply {
            duration = 300
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                val scale = it.animatedValue as Float
                checkpointPaint.color = currentLevelColor
                checkpointPaint.alpha = (255 * scale).toInt()
                invalidate()
            }
            start()
        }
    }

    private fun updateBarColor() {
        currentLevelColor = if (progress >= 300f) goldLevelBarColor else greenLevelBarColor
        barPaint.color = currentLevelColor
        checkpointPaint.color = currentLevelColor
    }

    fun setBarColor(color: Int) {
        currentLevelColor = color
        checkpointPaint.color = currentLevelColor
        invalidate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        setupProgress(progress)
    }

    private fun Context.spToPx(sp: Float): Float = sp * resources.displayMetrics.scaledDensity
    private fun Context.dpToPx(dp: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
}