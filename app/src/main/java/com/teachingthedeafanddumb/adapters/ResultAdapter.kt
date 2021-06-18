package com.teachingthedeafanddumb.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.teachingthedeafanddumb.R
import com.teachingthedeafanddumb.data.model.LessonModel
import com.teachingthedeafanddumb.data.model.ResultModel
import com.teachingthedeafanddumb.other.Constants
import kotlinx.android.synthetic.main.item_result.view.*
import java.text.SimpleDateFormat
import java.util.*

class ResultAdapter() : RecyclerView.Adapter<ResultAdapter.ViewHolder>() {

    private var calender = Calendar.getInstance()

    @SuppressLint("SimpleDateFormat")
    internal var simpleDateFormat = SimpleDateFormat("dd MMM yyyy HH:mm")

    val diffCallback = object : DiffUtil.ItemCallback<ResultModel>() {
        override fun areItemsTheSame(oldItem: ResultModel, newItem: ResultModel): Boolean {

            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(
            oldItem: ResultModel,
            newItem: ResultModel
        ): Boolean {

            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
       return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_result ,parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val resultModel = differ.currentList[position]

        holder.itemView.apply {

            val date = Date(resultModel.createAt!!.toLong())

            txt_date.text = StringBuilder()
                .append(" ")
                .append(simpleDateFormat.format(date))

            txt_lesson_number.text = resultModel.lessonNumber
            txt_result.text =  "${resultModel.resultCorrect} / ${resultModel.resultCorrect + resultModel.resultWrong + resultModel.resultMissed}"

            setOnClickListener {
                onItemClickListener?.let { it(resultModel, holder.itemView) }
            }

        }
    }


    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)


    private var onItemClickListener: ((ResultModel, View) -> Unit)? = null

    fun setOnItemClickListener(listener: (ResultModel, View) -> Unit) {
        onItemClickListener = listener
    }

}