package com.homesoftwaretools.toptalproperty.features.dashboard.apartmentlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.homesoftwaretools.toptalproperty.R
import com.homesoftwaretools.toptalproperty.core.utils.NumberFormatter
import com.homesoftwaretools.toptalproperty.domain.Apartment

class ApartmentListAdapter(
    context: Context,
    private val fmt: NumberFormatter,
    private var data: List<Apartment> = emptyList()
) : RecyclerView.Adapter<ApartmentViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApartmentViewHolder {
        return ApartmentViewHolder(
            inflater.inflate(R.layout.apartment_card, parent, false),
            fmt
        )
    }

    override fun getItemCount(): Int = data.size

    fun swapData(list: List<Apartment>) {
        data = list
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return data[position].id.hashCode().toLong()
    }

    override fun onBindViewHolder(holder: ApartmentViewHolder, position: Int) {
        holder.populate(data[position])
    }

}

class ApartmentViewHolder(private val view: View, private val fmt: NumberFormatter) : RecyclerView.ViewHolder(view) {

    private val name by lazy { view.findViewById<TextView>(R.id.name) }
    private val description by lazy { view.findViewById<TextView>(R.id.description) }
    private val area by lazy { view.findViewById<TextView>(R.id.area) }
    private val rooms by lazy { view.findViewById<TextView>(R.id.rooms) }
    private val price by lazy { view.findViewById<TextView>(R.id.price) }

    fun populate(apartment: Apartment) {
        name.text = apartment.name
        description.text = apartment.description
        area.text = fmt.formatNum(apartment.area)
        rooms.text = fmt.formatInt(apartment.rooms)
        price.text = fmt.formatNum(apartment.price)
    }

}