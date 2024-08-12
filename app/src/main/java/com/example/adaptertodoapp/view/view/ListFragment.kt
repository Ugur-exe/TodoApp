package com.example.adaptertodoapp.view.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.adaptertodoapp.R
import com.example.adaptertodoapp.databinding.FragmentListBinding
import com.example.adaptertodoapp.view.adapter.TodoAdapter
import com.example.adaptertodoapp.view.model.TodoTask
import com.example.adaptertodoapp.view.roomDb.TodoDAO
import com.example.adaptertodoapp.view.roomDb.TodoDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class ListFragment : Fragment(), TodoAdapter.OnItemClickListener {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: TodoDatabase
    private lateinit var todoDao: TodoDAO
    private var mDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(requireContext(), TodoDatabase::class.java, "TodoTask").build()
        todoDao = db.todoDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
        binding.floatingActionButton.setOnClickListener { addTask(it) }
        binding.todoRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun addTask(view: View) {
        val action = ListFragmentDirections.actionListFragmentToTodoAddFragment(id = 0, informataion = true)
        Navigation.findNavController(view).navigate(action)
    }

    private fun getData() {
        mDisposable.add(
            todoDao.getAll().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(this::handleResponse)
        )
    }

    private fun handleResponse(todoList: List<TodoTask>) {
        val adapter = TodoAdapter(todoList, this)
        binding.todoRecyclerView.adapter = adapter
    }

    override fun onEditClick(view: View,task: TodoTask, position: Int) {
       val action=ListFragmentDirections.actionListFragmentToTodoEditFragment(position)
        Navigation.findNavController(view).navigate(action)

    }

    override fun onDeleteClick(task: TodoTask, position: Int) {
        mDisposable.add(
            todoDao.delete(task)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    (binding.todoRecyclerView.adapter as TodoAdapter).apply {
                        notifyItemRemoved(position)
                    }
                }
        )
    }
}
