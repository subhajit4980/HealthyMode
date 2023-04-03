package com.example.HealthyMode.TodoDatabase

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "TODO")
data class Todo(
    @ColumnInfo(name="status")
    val status:Boolean,
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
    val reminder: String,
    @ColumnInfo(name="time")
    val time: Long,
    @PrimaryKey(autoGenerate = true) val id:Int=0
):Parcelable