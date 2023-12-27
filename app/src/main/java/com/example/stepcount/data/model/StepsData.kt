package com.example.stepcount.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "steps_data")
data class StepsData(
    @ColumnInfo(name = "steps_count")
    var steps: Float? = null,
    @ColumnInfo(name = "name_day")
    var nameDay: String? = null,
    @ColumnInfo(name = "day")
    var day: Int? = null,
    @ColumnInfo(name = "month")
    var month: Int? = 0,
    @ColumnInfo(name = "year")
    var year: Int? = 0,
    @ColumnInfo(name = "distance")
    var distance: Float? = null,
    @ColumnInfo(name = "calories")
    var calories: Float? = null,
    @ColumnInfo(name = "time")
    var time: Long? = null,
    var state: Boolean? = false
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}