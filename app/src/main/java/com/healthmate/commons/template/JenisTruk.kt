package com.healthmate

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "jenis_truk")
data class JenisTruk(
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int = 0,

    @field:SerializedName("nama")
    val nama: String = "",

    @field:SerializedName("updated_at")
    val updatedAt: String = "",

    @field:SerializedName("created_at")
    val createdAt: String = ""
)