package com.example.perfectweather.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.perfectweather.Date
import com.example.perfectweather.ItemAdapter
import com.example.perfectweather.ItemCity
import com.example.perfectweather.R

class BaselineFragment: Fragment(){
    lateinit var shared: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        shared = requireActivity().getSharedPreferences("Test", Context.MODE_PRIVATE)
        var City = shared.all
        var test : ArrayList<Date> = ArrayList<Date>()

        val v = inflater.inflate(R.layout.fragment_baseline, container, false)
        for(i in City)
        {
            var date : Date = Date(i.key,i.value.toString())
            test!!.add(date)
        }
        val recycler = v?.findViewById<RecyclerView>(R.id.imageRecyclerView)
        recycler?.layoutManager = LinearLayoutManager(activity)
        recycler?.setHasFixedSize(true)
        val adapter = ItemAdapter(v.context,test)
        {
            var intent: Intent = Intent(context, ItemCity::class.java)
            intent.putExtra("OBJECK", it)
            startActivity(intent)
        }
        recycler?.adapter = adapter

        return v
    }
}