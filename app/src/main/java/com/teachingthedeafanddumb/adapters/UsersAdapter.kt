package com.teachingthedeafanddumb.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teachingthedeafanddumb.R
import com.teachingthedeafanddumb.data.model.ResultModel
import com.teachingthedeafanddumb.data.model.UserModel
import kotlinx.android.synthetic.main.item_user.view.*

class UsersAdapter() : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {


    val diffCallback = object : DiffUtil.ItemCallback<UserModel>() {
        override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {

            return oldItem.id == newItem.id

        }

        override fun areContentsTheSame(
            oldItem: UserModel,
            newItem: UserModel
        ): Boolean {

            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
       return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user ,parent,false))
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val user = differ.currentList[position]

        holder.itemView.apply {

            txt_user.text = user.fullName

            Glide.with(context)
                .load(user.photoUrl)
                .into(img_user)

            setOnClickListener {
                onItemClickListener?.let { it(user, holder.itemView) }
            }

        }
    }


    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)


    private var onItemClickListener: ((UserModel, View) -> Unit)? = null

    fun setOnItemClickListener(listener: (UserModel, View) -> Unit) {
        onItemClickListener = listener
    }

}