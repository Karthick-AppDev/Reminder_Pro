package com.example.reminderwithalaram

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderwithalaram.database.AlarmDataEntity
import com.example.reminderwithalaram.utilities.alarmData

 class recyclerViewAdaptor(private val list: List<AlarmDataEntity>) :
    RecyclerView.Adapter<recyclerViewAdaptor.viewHolder>() {
    private  val TAG = "recyclerViewAdaptor"
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return viewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder:  : " + list[position].toString())
        val alarmData = list[position]
        holder.dataTxtView.text = "${alarmData.day}/${alarmData.month}/${alarmData.year} "
        holder.timeTextView2.text = "${alarmData.hourOfDay} : ${alarmData.minute}"
        holder.remindertextview.text = alarmData.reminderText

        holder.deleteBtn.setOnClickListener {
            Log.d(TAG, "onBindViewHolder: To be Delete : " + list[position].toString())
        }

    }

    override fun getItemCount(): Int {
        Log.d(TAG, "onBindViewHolder: getItemCount : " + list.size)
        return list.size
    }


    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var remindertextview:TextView = itemView.findViewById(R.id.remindertextview)
        var timeTextView2:TextView = itemView.findViewById(R.id.timeTextView2)
        var dataTxtView:TextView = itemView.findViewById(R.id.dataTxtView)
        var deleteBtn:ImageButton = itemView.findViewById(R.id.deleteBtn)
    }
}