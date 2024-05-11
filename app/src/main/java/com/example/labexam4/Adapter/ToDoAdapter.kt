package com.example.labexam4.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.labexam4.AddNewTask
import com.example.labexam4.MainActivity
import com.example.labexam4.Model.ToDoModel
import com.example.labexam4.R
import com.example.labexam4.Utils.DatabaseHandler

class ToDoAdapter(
    private var activity: MainActivity,
    private var todoList: MutableList<ToDoModel>,
    private var db:DatabaseHandler
) : RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {
    fun getContext(): Context = activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.task_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        db.openDatabase()
        var item: ToDoModel =  todoList[position]
        holder.task.setText(item.getTask())
        holder.task.setChecked(toBoolean(item.getStatus()))
        holder.task.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener{
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                if (isChecked) {
                   db.updateStatus(item.getId(),1)
                } else {
                    db.updateStatus(item.getStatus(),0)
                }
            }
        })
    }

    private fun toBoolean( n : Int): Boolean{
        return n!= 0
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTasks(todoList: MutableList<ToDoModel>) {
        this.todoList = todoList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
       return todoList.size
    }

    fun deleteItem(position:Int){
        val item = todoList[position]
        db.deleteTask(item.getId())
        todoList.removeAt(position)
        notifyItemRemoved(position)
    }


    fun editItem(position:Int){
        val item:ToDoModel = todoList[position]
        val bundle: Bundle = Bundle()
        bundle.putInt("id",item.getId())
        bundle.putString("task",item.getTask())
        val fragment:AddNewTask = AddNewTask()
        fragment.arguments = bundle
        fragment.show(activity.supportFragmentManager,AddNewTask.TAG)

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var  task: CheckBox = view.findViewById(R.id.todoCheckBox)


    }
}