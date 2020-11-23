package com.example.perfectweather

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.parcel.Parcelize

class ItemAdapter(private val context: Context,
                  private val movie: ArrayList<Date>,
                  private val listener: (Date) -> Unit
) : RecyclerView.Adapter<ItemAdapter.ImageViewHolder>() {
    class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
        val city = view.findViewById<TextView>(R.id.title_City)
        fun View(
            City: Date,
            listener: (Date) -> Unit
        ) {
            city.text = City.City
            itemView.setOnClickListener { listener(City) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder =
        ImageViewHolder(
            LayoutInflater.from(context).inflate(R.layout.weather_item, parent, false)
        )

    override fun getItemCount(): Int = movie!!.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.View(movie!![position],listener)
    }

}

@Parcelize
class Date(var position:String, var City:String):Parcelable
{

}