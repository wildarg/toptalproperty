package com.homesoftwaretools.toptalproperty.features.dashboard.apartmentlist.adapter

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

class ApartmentListAdapter(
    context: Context,
    private val fmt: NumberFormatter,
    private val rp: ResourceProvider,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<ApartmentViewHolder>() {

    private var data: List<Pair<Apartment, User>> = emptyList()
    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApartmentViewHolder {
        return ApartmentViewHolder(
            inflater.inflate(R.layout.apartment_card, parent, false),
            fmt,
            rp
        )
    }

    override fun getItemCount(): Int = data.size

    fun swapData(list: List<Pair<Apartment, User>>) {
        data = list
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return data[position].first.id.hashCode().toLong()
    }

    override fun onBindViewHolder(holder: ApartmentViewHolder, position: Int) {
        holder.populate(data[position].first, data[position].second)
        holder.setOnClickListener { onClick(data[position].first.id!!) }
    }

}

class ApartmentViewHolder(
    private val view: View,
    private val fmt: NumberFormatter,
    private val rp: ResourceProvider
) : RecyclerView.ViewHolder(view) {

    private val rentedBand by lazy { view.findViewById<ImageView>(R.id.rented_band) }
    private val realtorName by lazy { view.findViewById<TextView>(R.id.realtor_name) }
    private val card by lazy { view.findViewById<MaterialCardView>(R.id.card) }
    private val name by lazy { view.findViewById<TextView>(R.id.name) }
    private val description by lazy { view.findViewById<TextView>(R.id.description) }
    private val area by lazy { view.findViewById<TextView>(R.id.area) }
    private val rooms by lazy { view.findViewById<TextView>(R.id.rooms) }
    private val price by lazy { view.findViewById<TextView>(R.id.price) }

    fun populate(apartment: Apartment, user: User) {
        realtorName.text = rp.string(R.string.realtor_name_format_string).format(user.name)
        name.text = apartment.name
        description.text = apartment.description
        area.text = fmt.formatArea(apartment.area)
        rooms.text = fmt.formatRooms(apartment.rooms)
        price.text = fmt.formatCurrency(apartment.price)
        rentedBand.visible = apartment.rented ?: false
    }

    fun setOnClickListener(listener: (View) -> Unit) {
        card.setOnClickListener(listener)
    }

}