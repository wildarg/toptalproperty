package com.homesoftwaretools.toptalproperty.features.userlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.ui.visible
import com.homesoftwaretools.toptalproperty.core.utils.NumberFormatter
import com.homesoftwaretools.toptalproperty.core.utils.ResourceProvider
import com.homesoftwaretools.toptalproperty.domain.Apartment
import com.homesoftwaretools.toptalproperty.domain.User
import com.homesoftwaretools.toptalproperty.features.usercard.getName

class UserListAdapter(
    context: Context,
    private val rp: ResourceProvider,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<UserCardHolder>() {

    private var data: List<User> = emptyList()
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCardHolder {
        return UserCardHolder(
            inflater.inflate(R.layout.user_card, parent, false),
            rp
        )
    }

    override fun getItemCount(): Int = data.size

    fun swapData(list: List<User>) {
        data = list
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return data[position].authId.hashCode().toLong()
    }

    override fun onBindViewHolder(holder: UserCardHolder, position: Int) {
        holder.populate(data[position])
        holder.setOnClickListener { onClick(data[position].authId) }
    }

}

class UserCardHolder(
    private val view: View,
    private val rp: ResourceProvider
) : RecyclerView.ViewHolder(view) {

    private val userName by lazy { view.findViewById<TextView>(R.id.user_name) }
    private val userRole by lazy { view.findViewById<TextView>(R.id.user_role) }
    private val email by lazy { view.findViewById<TextView>(R.id.user_email) }

    fun populate(user: User) {
        userName.text = user.name
        userRole.text = user.role.getName(rp)
        email.text = user.email
    }

    fun setOnClickListener(listener: (View) -> Unit) {
        view.setOnClickListener(listener)
    }

}