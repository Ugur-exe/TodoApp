package com.example.adaptertodoapp.view.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoTask (
    @ColumnInfo(name = "title")
    var title: String ,
    @ColumnInfo(name = "subtitle")
    var subtitle:String
){
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
}