package com.example.HealthyMode.TodoDatabase

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "TODO")
data class Todo (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="status")
    val status:Int,
    @ColumnInfo(name="Desc")
    val Desc:String,
    @ColumnInfo(name="Start_date")
    val Start_date:String,
    @ColumnInfo(name="Start_time")
    val Start_time:String,
    @ColumnInfo(name="End_date")
    val End_date:String,
    @ColumnInfo(name="End_time")
    val End_time:String,
    @ColumnInfo(name="reminder")
    val reminder: String
):Parcelable