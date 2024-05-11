package com.example.labexam4.Model

class ToDoModel {

    private var status: Int = 0
    private var id: Int = 0
    internal var task: String = ""

    fun getStatus(): Int {
        return status
    }

    fun setStatus(status: Int) {
        this.status = status
    }


    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }


    fun getTask(): String {
        return task
    }

    fun setTask(value: String) {
        task = value
    }

}