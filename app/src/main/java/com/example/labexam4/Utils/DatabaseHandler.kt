package com.example.labexam4.Utils

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.labexam4.Model.ToDoModel


class DatabaseHandler (context: Context): SQLiteOpenHelper(context, NAME, null, VERSION) {

    companion object {
        private const val VERSION: Int = 1
        private const val NAME: String = "toDoListDatabase"
        private const val TODO_TABLE: String = "todo"
        private const val ID: String = "id"
        private const val TASK: String = "task"
        private const val STATUS: String = "status"
        private val CREATE_TODO_TABLE: String =
            "CREATE TABLE $TODO_TABLE($ID INTEGER PRIMARY KEY AUTOINCREMENT, $TASK TEXT, $STATUS INTEGER)"
    }

    private lateinit var db: SQLiteDatabase

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TODO_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE)
        onCreate(db)
    }

    fun openDatabase() {
        db = this.writableDatabase
    }

    fun insertTask(task: ToDoModel) {
        val cv = ContentValues()
        cv.put(TASK, task.getTask())
        cv.put(STATUS, 0)
        db.insert(TODO_TABLE, null, cv)
    }

    @SuppressLint("Range")
    fun getAllTasks(): MutableList<ToDoModel> {
        val taskList: MutableList<ToDoModel> = ArrayList()
        var cur : Cursor? = null
        db.beginTransaction()
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null,null,null)
            if (cur != null && cur.moveToFirst()) {
                do {
                    var task = ToDoModel()
                    task.setId(cur.getInt(cur.getColumnIndex(ID)))
                    task.setTask(cur.getString(cur.getColumnIndex(TASK)))
                    task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)))
                    taskList.add(task)
                } while (cur.moveToNext())
            }
        } finally{
            db.endTransaction()
            cur?.close()

        }

         return taskList
    }

    fun updateStatus(id:Int, status:Int){
        val cv = ContentValues()
        cv.put(STATUS,status)
        val args = arrayOf(id.toString())
        db.update(TODO_TABLE, cv, "$ID = ?", args)
    }

    fun updateTask(id:Int, task:String){
        val cv = ContentValues()
        cv.put(TASK,task)
        val args = arrayOf(id.toString())
        db.update(TODO_TABLE, cv, "$ID = ?", args)
    }

    fun deleteTask(id:Int) {
        val args = arrayOf(id.toString())
        db.delete(TODO_TABLE, "$ID = ?", args)
    }


}