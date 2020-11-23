package com.example.perfectweather.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.perfectweather.APIWeatherService
import com.example.perfectweather.Model.ModelWeather
import com.example.perfectweather.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    lateinit var shared: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var v = inflater.inflate(R.layout.fragment_home, container, false)

        shared = requireActivity().getSharedPreferences("Test", Context.MODE_PRIVATE)

        val button = v.findViewById(R.id.search) as ImageButton
        button.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                if (EditCountry.text.toString().trim() != "") {

                    var call: retrofit2.Call<ModelWeather> =
                        APIWeatherService.invoke()
                            .getCurrentWeather(EditCountry.text.toString())

                    call.enqueue(object : retrofit2.Callback<ModelWeather> {
                        override fun onFailure(call: Call<ModelWeather>, t: Throwable) {
                            Log.e("error", t.message.toString())
                        }

                        override fun onResponse(
                            call: Call<ModelWeather>,
                            response: Response<ModelWeather>
                        ) {
                            if (response.body() != null) {
                                scrol.visibility = View.INVISIBLE
                                scrol.visibility = View.VISIBLE

                                name.text = response.body()!!.name

                                dt.text =
                                    SimpleDateFormat(/*"yyyy.MM.dd H" - по желанию*/"H:mm:ss").format(
                                        Date((response.body()!!.dt + "000").toLong())
                                    )
                                        .toString()
                                temp.text = response.body()!!.main!!.temp
                                feels_like.text =
                                    "Ощущается как: ${response.body()!!.main!!.feels_like}"
                                temp_max.text = "Min: ${response.body()!!.main!!.temp_min}"
                                temp_min.text = "Max: ${response.body()!!.main!!.temp_max}"
                                pressure.text =
                                    "Давление: ${response.body()!!.main!!.pressure} hPa"
                                humidity.text =
                                    "Влажность: ${response.body()!!.main!!.humidity}%"
                                Picasso.with(context)
                                    .load(
                                        "http://openweathermap.org/img/wn/${response.body()!!.weather?.get(
                                            0
                                        )!!.icon}@4x.png"
                                    )
                                    .into(image, object : Callback {
                                        override fun onSuccess() {
                                            if (progressbar != null) {
                                                progressbar.visibility = View.GONE
                                            }
                                        }

                                        override fun onError() {
                                            progressbar.visibility = View.GONE
                                            image.visibility = View.GONE
                                        }
                                    })
                            } else {
                                scrol.visibility = View.GONE
                            }
                        }
                    })

                }
            }
        })

        val butt = v.findViewById<ImageButton>(R.id.favorite)
        butt.setOnClickListener {
            if (scrol.visibility == View.VISIBLE) {
                val editor: SharedPreferences.Editor = shared.edit()
                var City = shared.all
                if (!City!!.containsValue(name.text.toString())) {
                    editor.putString(
                        "${City.size+1}",
                        name.text.toString()
                    )
                    editor.apply()
                }
            }
        }
        val liner = v.findViewById<ImageButton>(R.id.description)
        var boolean = true
        liner.setOnClickListener {
            if (boolean) {
                linear_description.visibility = View.VISIBLE
                boolean = false
            } else {
                linear_description.visibility = View.INVISIBLE
                boolean = true
            }
        }
        return v
    }
}