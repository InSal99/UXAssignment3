package com.example.uxassignment3

data class CheckpointInfo(val label: String, val percentage: Float, val threshold: Float = label.toFloat())