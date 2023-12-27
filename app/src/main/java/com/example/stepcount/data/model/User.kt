package com.example.stepcount.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "user_database")
data class User(
    @ColumnInfo(name = "gender_col")
    var gender: String? = "",
    @ColumnInfo(name = "stepLength_col")
    var stepLength: Int? = 0,
    @ColumnInfo(name = "height_col")
    var height: Int? = 0,
    @ColumnInfo(name = "weight_col")
    var weight: Int? = 0,
    @ColumnInfo(name = "goal_col")
    val goal: Int? = 0
){
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
