package com.example.perfectweather

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.perfectweather.Model.ModelWeather
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_item_city.*
import kotlinx.android.synthetic.main.activity_item_city.dt
import kotlinx.android.synthetic.main.activity_item_city.feels_like
import kotlinx.android.synthetic.main.activity_item_city.humidity
import kotlinx.android.synthetic.main.activity_item_city.image
import kotlinx.android.synthetic.main.activity_item_city.name
import kotlinx.android.synthetic.main.activity_item_city.pressure
import kotlinx.android.synthetic.main.activity_item_city.progressbar
import kotlinx.android.synthetic.main.activity_item_city.scrol
import kotlinx.android.synthetic.main.activity_item_city.temp
import kotlinx.android.synthetic.main.activity_item_city.temp_max
import kotlinx.android.synthetic.main.activity_item_city.temp_min
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ItemCity : AppCompatActivity() {
    lateinit var shared: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_city)

        var item = intent.getParcelableExtra<Date>("OBJECK")

        var call: retrofit2.Call<ModelWeather> =
            APIWeatherService.invoke()
                .getCurrentWeather(item!!.City!!)
        call.enqueue(object : retrofit2.Callback<ModelWeather> {
            override fun onFailure(call: Call<ModelWeather>, t: Throwable) {
                Log.e("error", t.message.toString())
            }

            override fun onResponse(call: Call<ModelWeather>, response: Response<ModelWeather>
            ) {
                if (response.body() != null) {
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
                    Picasso.with(this@ItemCity)
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
                }
            }
        })

        Delete.setOnClickListener()
        {
            shared = this.getSharedPreferences("Test", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = shared.edit()
            editor.remove(item!!.position.toString())
            editor.apply()
            var intent: Intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}