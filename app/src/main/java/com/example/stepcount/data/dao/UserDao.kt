package com.example.stepcount.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.stepcount.data.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user_database WHERE id = :id")
    suspend fun getUserById(id: Long): User

    @Query("UPDATE user_database SET goal_col= :goal WHERE id = :id")
    suspend fun updateGoalByUserId(id: Long, goal: Int)

    @Query("UPDATE user_database SET gender_col= :gender WHERE id = :id")
    suspend fun updateGenderByUserId(id: Long, gender: String)
    @Query("UPDATE user_database SET stepLength_col= :stepLength WHERE id = :id")
    suspend fun updateStepLengthByUserId(id: Long, stepLength: Int)

    @Query("UPDATE user_database SET height_col= :height WHERE id = :id")
    suspend fun updateHeightByUserId(id: Long, height: Int)

    @Query("UPDATE user_database SET weight_col= :weight WHERE id = :id")
    suspend fun updateWeightByUserId(id: Long, weight: Double)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM user_database")
    suspend fun getUserData(): User?
}