package com.example.adaptertodoapp.view.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.example.adaptertodoapp.R
import com.example.adaptertodoapp.databinding.FragmentListBinding
import com.example.adaptertodoapp.databinding.FragmentTodoAddBinding
import com.example.adaptertodoapp.view.model.TodoTask
import com.example.adaptertodoapp.view.roomDb.TodoDAO
import com.example.adaptertodoapp.view.roomDb.TodoDatabase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class TodoAddFragment : Fragment() {
    private var _binding: FragmentTodoAddBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: TodoDatabase
    private lateinit var todoDao: TodoDAO

    private var  mDisposable= CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db= Room.databaseBuilder(requireContext(),TodoDatabase::class.java,"TodoTask").build()
        todoDao=db.todoDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= FragmentTodoAddBinding.inflate(inflater,container,false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener { addTask(it) }
        binding.BackArrowIcon.setOnClickListener{
            fragmentManager?.popBackStack()
        }
        arguments?.let {
            var information=TodoAddFragmentArgs.fromBundle(it).informataion
            if (information){
                //yeni tarif eklenecek
            }else{
                //update sayfasına gidecek
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
        mDisposable.clear()

    }
    private fun addTask(view: View){
        val title=binding.titleEditText.text.toString()
        val subtitle=binding.detailEditText.text.toString()

        val todoTask= TodoTask(title,subtitle)
        mDisposable.add(
           todoDao.insert(todoTask).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
               .subscribe(this::handleResponseForInsert)
        )
    }
    private fun handleResponseForInsert(){
        //Önceki sayfaya dön
        val action=TodoAddFragmentDirections.actionTodoAddFragmentToListFragment()
        findNavController().navigate(action)

    }


}