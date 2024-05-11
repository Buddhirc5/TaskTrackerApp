package com.example.labexam4

import RecyclerItemTouchHelper
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.labexam4.Adapter.ToDoAdapter
import com.example.labexam4.Model.ToDoModel
import com.example.labexam4.Utils.DatabaseHandler
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), DialogCloseListener {

    private lateinit var taskRecyclerView: RecyclerView
    private lateinit var tasksAdapter: ToDoAdapter
    private var taskList: MutableList<ToDoModel> = mutableListOf()
    private lateinit var db: DatabaseHandler
    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        db = DatabaseHandler(this)
        db.openDatabase()

        taskRecyclerView = findViewById(R.id.taskRecycleView)
        taskRecyclerView.layoutManager = LinearLayoutManager(this)
        tasksAdapter = ToDoAdapter(this, taskList, db)
        taskRecyclerView.adapter = tasksAdapter

        var itemTouchHelper: ItemTouchHelper = ItemTouchHelper(RecyclerItemTouchHelper(tasksAdapter))
        itemTouchHelper.attachToRecyclerView(taskRecyclerView)

        taskList = db.getAllTasks()
        taskList.reverse()
        tasksAdapter.setTasks(taskList)

        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            showNewTaskDialog()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun handleDialogClose(dialog: DialogInterface) {
        taskList = db.getAllTasks()
        taskList.reverse()
        tasksAdapter.setTasks(taskList)
        tasksAdapter.notifyDataSetChanged()
    }

    private fun showNewTaskDialog() {
        val inflater = LayoutInflater.from(this)
        val dialogLayout = inflater.inflate(R.layout.new_task, null)
        val editText = dialogLayout.findViewById<EditText>(R.id.newTaskText)
        val button = dialogLayout.findViewById<Button>(R.id.newTaskButton)

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogLayout)
            .setCancelable(false)  // Makes the dialog non-cancelable
            .create()

        button.setOnClickListener {
            val task = editText.text.toString().trim()
            if (task.isNotEmpty()) {
                val newTask = ToDoModel()
                newTask.task = task
                db.insertTask(newTask)  // Assuming `insertTask` is a method in your `DatabaseHandler` for inserting a task
                handleDialogClose(dialog)
            }
            dialog.dismiss()
        }

        dialog.show()
    }
}
