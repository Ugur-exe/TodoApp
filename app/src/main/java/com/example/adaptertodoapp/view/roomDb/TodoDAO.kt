package com.example.adaptertodoapp.view.roomDb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.adaptertodoapp.view.model.TodoTask
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable

@Dao
interface TodoDAO {
    @Query("SELECT * FROM TodoTask")
    fun getAll(): Flowable<List<TodoTask>>

    @Query("SELECT * FROM TodoTask WHERE id = :id")
    fun get(id: Int): Flowable<TodoTask>

   @Insert
   fun insert(todoTask: TodoTask) : Completable

   @Delete
   fun delete(todoTask: TodoTask):Completable

   @Update
   fun update(todoTask: TodoTask):Completable
}