package com.healthmate

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface JenisTrukDao {
    @Query("SELECT * FROM jenis_truk ORDER BY id")
    fun getAll(): LiveData<List<JenisTruk>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(datas: List<JenisTruk>)
}