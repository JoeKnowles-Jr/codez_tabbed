package com.jk.codez.room

import androidx.room.TypeConverter
import java.lang.StringBuilder

class StringArrayConverter {
    @TypeConverter
    fun stringToStringArray(codes: String): Array<String> {
        return codes.split(" ").toTypedArray()
    }

    @TypeConverter
    fun stringArrayToString(arr: Array<String?>): String {
        val sb = StringBuilder()
        for (i in arr.indices) {
            sb.append(arr[i])
            if (i < arr.size - 1) sb.append(" ")
        }
        return sb.toString()
    }
}