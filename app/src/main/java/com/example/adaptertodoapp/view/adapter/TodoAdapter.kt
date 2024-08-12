package com.example.adaptertodoapp.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.adaptertodoapp.databinding.FragmentTodoAddBinding
import com.example.adaptertodoapp.databinding.RecyclerRowBinding
import com.example.adaptertodoapp.view.model.TodoTask
import com.example.adaptertodoapp.view.roomDb.TodoDAO
import com.example.adaptertodoapp.view.roomDb.TodoDatabase
import com.example.adaptertodoapp.view.view.ListFragmentDirections
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class TodoAdapter(val taskList:List<TodoTask>,  private val listener: OnItemClickListener)
    :RecyclerView.Adapter<TodoAdapter.TodoAdapterViewHolder> (){

    class TodoAdapterViewHolder(val binding: RecyclerRowBinding):RecyclerView.ViewHolder(binding.root)

    interface OnItemClickListener {
        fun onEditClick(view:View,task: TodoTask, position: Int)
        fun onDeleteClick(task: TodoTask, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapterViewHolder {

        val recyclerRowBinding=RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TodoAdapterViewHolder(recyclerRowBinding)

    }

    override fun getItemCount(): Int {
       return  taskList.size
    }

    override fun onBindViewHolder(holder: TodoAdapterViewHolder, position: Int,) {

        holder.binding.todoTitle.text=taskList[position].title
        holder.binding.todoSubTitle.text=taskList[position].subtitle

        holder.itemView.setOnClickListener {
            println(taskList[position].id)
        }
        holder.binding.deleteIcon.setOnClickListener {
            listener.onDeleteClick(taskList[position],position)
        }
        holder.binding.editIcon.setOnClickListener {
            listener.onEditClick(holder.itemView,taskList[position],taskList[position].id)
        }

    }
    private fun handleDelete(position: Int){
        notifyItemRemoved(position);
    }
}