package com.halliSanthe.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val price: Double,
    val category: String,
    val description: String,
    val location: String,
    val artisanName: String,
    val imageUri: String = "",          // local URI string from gallery
    val createdAt: Long = System.currentTimeMillis()
)
