package com.example.stepcount.data.model

import android.graphics.Bitmap
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "history_data")
data class HistoryData(
    @ColumnInfo(name = "image")
    val img: Bitmap,
    @ColumnInfo(name = "steps_count")
    val steps: Float,
    @ColumnInfo(name = "date")
    val date: Date,
    @ColumnInfo(name = "distance")
    val distance: Float,
    @ColumnInfo(name = "calories")
    val calories: Float,
    @ColumnInfo(name = "time")
    val time: Long
):Parcelable{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0
}
