package com.example.adaptertodoapp.view.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.room.Room
import com.example.adaptertodoapp.R
import com.example.adaptertodoapp.databinding.FragmentListBinding
import com.example.adaptertodoapp.databinding.FragmentTodoEditBinding
import com.example.adaptertodoapp.view.adapter.TodoAdapter
import com.example.adaptertodoapp.view.model.TodoTask
import com.example.adaptertodoapp.view.roomDb.TodoDAO
import com.example.adaptertodoapp.view.roomDb.TodoDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.FlowableSubscriber
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class TodoEditFragment : Fragment() {

    private var _binding: FragmentTodoEditBinding? = null
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
        _binding= FragmentTodoEditBinding.inflate(inflater,container,false)
        val view=binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val argsId=TodoEditFragmentArgs.fromBundle(requireArguments()).id
        binding.BackArrowIcon.setOnClickListener{
            fragmentManager?.popBackStack()
        }
       mDisposable.add(
           todoDao.get(argsId).observeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
               .subscribe(this::handleResponse)
       )
        binding.updateButton.setOnClickListener { updateTask(it) }

    }
    private fun handleResponse( todoTask: TodoTask) {
        binding.titleEditText.setText(todoTask.title)
        binding.detailEditText.setText(todoTask.subtitle)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null

    }

    private fun updateTask(view: View){
        val title = binding.titleEditText.text.toString()
        val subtitle = binding.detailEditText.text.toString()
        val argsId=TodoEditFragmentArgs.fromBundle(requireArguments()).id
        val todoTask = TodoTask( title = title, subtitle = subtitle)

        mDisposable.add(
            todoDao.updateId(argsId,title,subtitle)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    fragmentManager?.popBackStack()
                    Toast.makeText(context, "Görev güncellendi", Toast.LENGTH_SHORT).show()
                }, {
                    Toast.makeText(context, "Güncelleme başarısız oldu", Toast.LENGTH_SHORT).show()
                })
        )
    }





}