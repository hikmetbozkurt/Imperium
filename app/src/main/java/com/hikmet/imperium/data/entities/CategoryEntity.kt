package com.hikmet.imperium.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity representing a category of historical content (e.g., Ancient, Medieval, etc.)
 */
@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val longDescription: String,
    val totalLevels: Int,
    val gradientStartColor: Int,
    val gradientEndColor: Int,
    val iconResourceName: String
) 