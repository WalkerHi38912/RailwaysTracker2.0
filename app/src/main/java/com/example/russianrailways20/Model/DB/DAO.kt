package com.example.russianrailways20.Model.DB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(PrevTripsEntity: PrevTripsEntity)
    @Delete
    suspend fun deleteItem(PrevTripsEntity: PrevTripsEntity)
    @Query("DELETE FROM PrevTripsEntity")
    suspend fun deleteAllItems()
    @Query("SELECT * FROM PrevTripsEntity")
    fun getAllItems() : Flow<List<PrevTripsEntity>>
    @Query("SELECT COUNT(*) FROM PrevTripsEntity")
    suspend fun getItemCount(): Int
}