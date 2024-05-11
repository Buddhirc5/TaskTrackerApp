package com.example.labexam4

import android.app.Activity
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.core.content.ContextCompat
import com.example.labexam4.Model.ToDoModel
import com.example.labexam4.Utils.DatabaseHandler
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddNewTask: BottomSheetDialogFragment() {


    private lateinit var newTaskText: EditText
    private lateinit var  newTaskSaveButton: Button
    private lateinit var db: DatabaseHandler

    companion object {
        const val TAG : String = "ActionBottomDialog"
        fun newInstance(): AddNewTask {
            return AddNewTask()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL,R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.new_task, container, false)
        dialog?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newTaskText = getView()?.findViewById(R.id.newTaskText) as EditText
        newTaskSaveButton = getView()?.findViewById(R.id.newTaskButton) as Button

        db = DatabaseHandler(requireActivity())
        db.openDatabase()

        var isUpdate:Boolean = false
        var bundle:Bundle? = arguments

        if(bundle!= null){
            isUpdate = true
            val task:String?  = bundle.getString("task")
            newTaskText.setText(task)
            if(task?.length!! > 0){
                val color = ContextCompat.getColor(requireContext(), R.color.DarkOrange)
                newTaskSaveButton.setTextColor(color)
            }
        }
        newTaskText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(s.toString() == ""){
                    newTaskSaveButton.isEnabled = false
                    newTaskSaveButton.setTextColor(Color.GRAY)
                }

                else{
                    newTaskSaveButton.isEnabled = true
                    newTaskSaveButton.setTextColor( ContextCompat.getColor(requireContext(), R.color.DarkOrange))
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        newTaskSaveButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val text:String = newTaskText.text.toString()
                if(isUpdate){
                    db.updateTask(bundle!!.getInt("id"),text)
                }
                else{
                    val task:ToDoModel = ToDoModel()
                    task.setTask(text)
                    task.setStatus(0)
                    db.insertTask(task);
                }
                dismiss()
            }
        })
    }

    override fun onDismiss(dialog: DialogInterface) {
        val activity: Activity? = activity
        if(activity is DialogCloseListener){
            activity.handleDialogClose(dialog)
        }

    }
}