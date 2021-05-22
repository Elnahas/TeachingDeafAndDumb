package com.teachingthedeafanddumb.adapters

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
import kotlinx.android.synthetic.main.item_lesson.view.*

class LessonAdapter() : RecyclerView.Adapter<LessonAdapter.ViewHolder>() {


    val diffCallback = object : DiffUtil.ItemCallback<LessonModel>() {
        override fun areItemsTheSame(oldItem: LessonModel, newItem: LessonModel): Boolean {

            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(
            oldItem: LessonModel,
            newItem: LessonModel
        ): Boolean {

            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
       return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_lesson ,parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val lessonModel = differ.currentList[position]

        holder.itemView.apply {

            val uri =
                "@drawable/home_gradient_" + (if (differ.currentList.size < position) 2 else position+1)

            val imageResource =
                context.resources.getIdentifier(uri, null, context.packageName)
            root.background = context.getDrawable(imageResource)

            val uriImageSign =
                "@drawable/ic_home_sign_" +(if (differ.currentList.size < position) 2 else position+1)
            val imageSign =
                context.resources.getIdentifier(uriImageSign, null, context.packageName)
            img_lesson.setImageResource(imageSign)

            txt_lesson_number.text = lessonModel.lessonNumber
            txt_lesson_title.text = lessonModel.lessonTitle

            txt_result.text = "${0} / ${lessonModel.quiz!!.size}"

            FirebaseDatabase
                .getInstance()
                .getReference(Constants.REF_RESULTS)
                .orderByChild("uid_lessonId")
                .equalTo("id_"+lessonModel.id)
                .limitToLast(1)
                .addListenerForSingleValueEvent(object:ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {


                    }

                    override fun onDataChange(snapshot: DataSnapshot) {

                        snapshot.children.forEach{
                            val resultModel = it.getValue(ResultModel::class.java)
                            txt_result.text = "${resultModel!!.resultCorrect} / ${lessonModel.quiz!!.size}"

                        }

                    }

                })

            setOnClickListener {
                onItemClickListener?.let { it(lessonModel, holder.itemView) }
            }

        }
    }


    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)


    private var onItemClickListener: ((LessonModel, View) -> Unit)? = null

    fun setOnItemClickListener(listener: (LessonModel, View) -> Unit) {
        onItemClickListener = listener
    }

}