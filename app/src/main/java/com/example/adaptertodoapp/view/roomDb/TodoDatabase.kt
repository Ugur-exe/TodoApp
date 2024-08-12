package com.example.adaptertodoapp.view.roomDb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.adaptertodoapp.view.model.TodoTask

@Database(entities = [TodoTask::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDAO
}