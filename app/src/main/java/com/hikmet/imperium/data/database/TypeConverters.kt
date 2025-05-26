package com.hikmet.imperium.data.database

import androidx.room.TypeConverter

object MapIntIntConverter {
    @TypeConverter
    @JvmStatic
    fun fromString(value: String?): Map<Int, Int>? {
        if (value == null || value.isEmpty()) {
            return emptyMap()
        }
        return try {
            value.split(',').associate {
                val (key, v) = it.split(':')
                key.toInt() to v.toInt()
            }
        } catch (e: Exception) {
            // Handle parsing error, e.g., return empty map or log error
            emptyMap() // Or null if you prefer to represent invalid data as null
        }
    }

    @TypeConverter
    @JvmStatic
    fun fromMap(map: Map<Int, Int>?): String? {
        if (map == null || map.isEmpty()) {
            return ""
        }
        return map.entries.joinToString(",") { "${it.key}:${it.value}" }
    }
} 