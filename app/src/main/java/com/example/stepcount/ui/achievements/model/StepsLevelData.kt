package com.example.stepcount.ui.achievements.model

class StepsLevelData(
    val sumSteps: Float,
    val sumStepLevel: Int,
    val level: String,
    val unit: String?=null,
    val date: String?=null,
    val description: String?=null,
    val lock: Int?=null
)