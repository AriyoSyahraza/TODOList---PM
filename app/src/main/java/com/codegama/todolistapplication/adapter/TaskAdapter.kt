package com.codegama.todolistapplication.adapter

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.codegama.todolistapplication.R
import com.codegama.todolistapplication.activity.MainActivity
import com.codegama.todolistapplication.adapter.TaskAdapter.TaskViewHolder
import com.codegama.todolistapplication.bottomSheetFragment.CreateTaskBottomSheetFragment
import com.codegama.todolistapplication.bottomSheetFragment.CreateTaskBottomSheetFragment.setRefreshListener
import com.codegama.todolistapplication.database.DatabaseClient
import com.codegama.todolistapplication.model.Task
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskAdapter(
    private val context: MainActivity,
    private val taskList: List<Task>,
    var setRefreshListener: setRefreshListener
) : RecyclerView.Adapter<TaskViewHolder?>() {
    private val inflater: LayoutInflater
    var dateFormat = SimpleDateFormat("EE dd MMM yyyy", Locale.US)
    var inputDateFormat = SimpleDateFormat("dd-M-yyyy", Locale.US)
    var date: Date? = null
    var outputDateString: String? = null

    init {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): TaskViewHolder {
        val view = inflater.inflate(R.layout.item_task, viewGroup, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.title.setText(task.taskTitle)
        holder.description.setText(task.taskDescrption)
        holder.time.setText(task.lastAlarm)
        holder.status!!.text = if (task.isComplete) "COMPLETED" else "UPCOMING"
        holder.options!!.setOnClickListener { view: View? -> showPopUpMenu(view, position) }
        try {
            date = inputDateFormat.parse(task.date)
            outputDateString = dateFormat.format(date)
            val items1 =
                outputDateString.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            val day = items1[0]
            val dd = items1[1]
            val month = items1[2]
            holder.day!!.text = day
            holder.date!!.text = dd
            holder.month!!.text = month
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showPopUpMenu(view: View?, position: Int) {
        val task = taskList[position]
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(R.menu.menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.menuDelete -> {
                    val alertDialogBuilder = AlertDialog.Builder(
                        context, R.style.AppTheme_Dialog
                    )
                    alertDialogBuilder.setTitle(R.string.delete_confirmation)
                        .setMessage(R.string.sureToDelete)
                        .setPositiveButton(R.string.yes) { dialog: DialogInterface?, which: Int ->
                            deleteTaskFromId(
                                task.taskId,
                                position
                            )
                        }
                        .setNegativeButton(R.string.no) { dialog: DialogInterface, which: Int -> dialog.cancel() }
                        .show()
                }

                R.id.menuUpdate -> {
                    val createTaskBottomSheetFragment = CreateTaskBottomSheetFragment()
                    createTaskBottomSheetFragment.setTaskId(task.taskId, true, context, context)
                    createTaskBottomSheetFragment.show(
                        context.supportFragmentManager,
                        createTaskBottomSheetFragment.tag
                    )
                }

                R.id.menuComplete -> {
                    val completeAlertDialog = AlertDialog.Builder(
                        context, R.style.AppTheme_Dialog
                    )
                    completeAlertDialog.setTitle(R.string.confirmation)
                        .setMessage(R.string.sureToMarkAsComplete)
                        .setPositiveButton(R.string.yes) { dialog: DialogInterface?, which: Int ->
                            showCompleteDialog(
                                task.taskId,
                                position
                            )
                        }
                        .setNegativeButton(R.string.no) { dialog: DialogInterface, which: Int -> dialog.cancel() }
                        .show()
                }
            }
            false
        }
        popupMenu.show()
    }

    fun showCompleteDialog(taskId: Int, position: Int) {
        val dialog = Dialog(context, R.style.AppTheme)
        dialog.setContentView(R.layout.dialog_completed_theme)
        val close = dialog.findViewById<Button>(R.id.closeButton)
        close.setOnClickListener { view: View? ->
            deleteTaskFromId(taskId, position)
            dialog.dismiss()
        }
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun deleteTaskFromId(taskId: Int, position: Int) {
        class GetSavedTasks : AsyncTask<Void?, Void?, List<Task?>?>() {
            protected override fun doInBackground(vararg voids: Void): List<Task> {
                DatabaseClient.Companion.getInstance(context)
                    .getAppDatabase()
                    .dataBaseAction()
                    .deleteTaskFromId(taskId)
                return taskList
            }

            protected override fun onPostExecute(tasks: List<Task?>) {
                super.onPostExecute(tasks)
                removeAtPosition(position)
                setRefreshListener.refresh()
            }
        }

        val savedTasks = GetSavedTasks()
        savedTasks.execute()
    }

    private fun removeAtPosition(position: Int) {
        taskList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, taskList.size)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }

    inner class TaskViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {
        @JvmField
        @BindView(R.id.day)
        var day: TextView? = null

        @JvmField
        @BindView(R.id.date)
        var date: TextView? = null

        @JvmField
        @BindView(R.id.month)
        var month: TextView? = null

        @JvmField
        @BindView(R.id.title)
        var title: TextView? = null

        @JvmField
        @BindView(R.id.description)
        var description: TextView? = null

        @JvmField
        @BindView(R.id.status)
        var status: TextView? = null

        @JvmField
        @BindView(R.id.options)
        var options: ImageView? = null

        @JvmField
        @BindView(R.id.time)
        var time: TextView? = null

        init {
            ButterKnife.bind(this, view)
        }
    }
}