package com.users.ui.view_users

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.users.R
import com.users.data.model.User

class Adapter(private val itemClickListener: (User) -> Unit):
    RecyclerView.Adapter<Adapter.UserViewHolder>() {
    private var users = listOf<User>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData: List<User>) {
        users = newData
        notifyDataSetChanged()
    }
    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.findViewById(R.id.name)
        val emailView: TextView = itemView.findViewById(R.id.userEmail)

        fun setClickListener(user: User) {
            itemView.setOnClickListener { itemClickListener(user)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        val name = "${user.firstName} ${user.lastName}"
        holder.nameView.text = name
        holder.emailView.text = user.email
        holder.setClickListener(user)
    }
}