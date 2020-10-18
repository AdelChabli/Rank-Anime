package com.example.rankanime.modele

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rankanime.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.row.view.*
import java.util.*

class RvAdapter(private val dataList: List<RvItem>) : RecyclerView.Adapter<RvAdapter.RvViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row, parent, false)

        return RvViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RvViewHolder, position: Int) {
        if(position == 0) {
            holder.idRow.setTextColor(Color.parseColor("#f1c40f"))
            holder.idRow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 38f)
        }

        if(position == 1) {
            holder.idRow.setTextColor(Color.parseColor("#bdc3c7"))
            holder.idRow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34f)
        }

        if(position == 2) {
            holder.idRow.setTextColor(Color.parseColor("#cd6133"))
            holder.idRow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30f)
        }

        val currentItem = dataList[position]

        holder.idRow.text = "#" + currentItem.rang
        holder.nameRow.text = currentItem.nom
    }

    override fun getItemCount() = dataList.size

    class RvViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val idRow : TextView = itemView.id_row
        val nameRow : TextView = itemView.name_row
    }

    fun onItemMove(fromPosition: Int?, toPosition: Int?): Boolean {
        fromPosition?.let {
            toPosition?.let {
                if (fromPosition < toPosition) {
                    for (i in fromPosition until toPosition) {
                        Collections.swap(dataList, i, i + 1)
                    }
                } else {
                    for (i in fromPosition downTo toPosition + 1) {
                        Collections.swap(dataList, i, i - 1)
                    }
                }
                notifyItemMoved(fromPosition, toPosition)
                return true
            }
        }
        return false
    }

    fun changeRankOrder() {
        val currentUser = FirebaseAuth.getInstance().currentUser!!
        val database = FirebaseDatabase.getInstance()
        val ref = database.getReference("animes"+currentUser.uid)

    }
}