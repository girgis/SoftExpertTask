package com.example.githubproject.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.githubproject.R
import com.example.githubproject.data.model.Forecastday
import com.example.githubproject.data.model.Region
import com.example.githubproject.data.model.User
import com.example.githubproject.ui.main.viewmodel.MainViewModel
import com.example.githubproject.utils.Status
import com.mindorks.framework.mvvm.ui.main.adapter.MainAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel : MainViewModel by viewModels()
    private lateinit var adapter: MainAdapter
    private var page = 1
    var is_update_selected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        search_icon_im.setOnClickListener { v: View? ->
            search_layout.visibility = View.VISIBLE
        }

        arrow_left_im.setOnClickListener { v: View? ->
            search_layout.visibility = View.GONE
        }

        search_result_auto.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                mainViewModel.fetchRegions(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })


        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf(), this@MainActivity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter

    }

    private fun setupObserver() {
        mainViewModel.users.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.let { Log.d("zzz", "--- temp_f= ${it.current.temp_f}")
                        Log.d("zzz", "--- 2day= ${it.forecast.forecastday.get(1).day.maxtemp_f}")
                        current_city_tv.text = it.location.name
                        current_city_values_tv.text = it.location.localtime
                        Glide.with(this@MainActivity).load("https://" + it.current.condition.icon).into(sun_stroke_imgV)
                        setImages(it.forecast)
                        temp_in_f_tv.text = it.current.temp_f.toString() + "F"
                        temp_description_tv.text = it.current.condition.text
                        wind_value.text = it.current.wind_mph.toString() + " mph"
                        humidity_value.text = it.current.humidity.toString() + "%"
                        setThreeDaysValues(it.forecast)
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })

        mainViewModel.regions.observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data.let { Log.d("zzzz", "--- ${it?.size}")
//                        fillAutoList(it)
                        if (!is_update_selected) {
                            it?.let { it1 -> renderList(it1) }
                        }
                        is_update_selected = false
                    }
                }
                Status.LOADING -> {

                }
                Status.ERROR -> {
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setImages(forecast: Forecastday){
        Glide.with(this@MainActivity).load("https://" + forecast.forecastday.get(0).day.condition.icon).into(today_weather_img)
        Glide.with(this@MainActivity).load("https://" + forecast.forecastday.get(1).day.condition.icon).into(next_one_weather_img)
        Glide.with(this@MainActivity).load("https://" + forecast.forecastday.get(2).day.condition.icon).into(next_two_weather_img)
    }

    private fun setThreeDaysValues(forecast: Forecastday){
        today_weather_value_tv.text = forecast.forecastday.get(0).day.maxtemp_f.toString() + "/" +
                forecast.forecastday.get(0).day.mintemp_f.toString() + "F"

        next_one_weather_value_tv.text = forecast.forecastday.get(1).day.maxtemp_f.toString() + "/" +
                forecast.forecastday.get(1).day.mintemp_f.toString() + "F"

        next_two_weather_value_tv.text = forecast.forecastday.get(2).day.maxtemp_f.toString() + "/" +
                forecast.forecastday.get(2).day.mintemp_f.toString() + "F"

        next_one_weather_title_tv.text =  forecast.forecastday.get(1).date

        next_two_weather_title_tv.text =  forecast.forecastday.get(2).date
    }

    private fun fillAutoList(list: List<Region>?){
        var adapter = list?.map { it.name + " , " + it.region}?.let {
            ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line,
                it
            )
        }
        search_result_auto.setAdapter(adapter) //my autocomplete tv
    }

    private fun renderList(users: List<Region>) {
        recyclerView.visibility = View.VISIBLE
        adapter.addData(users, page)
        adapter.notifyDataSetChanged()
    }

    fun setSelectedRegion(name: String){
        recyclerView.visibility = View.GONE
        is_update_selected = true
        search_result_auto.setText(name)
        mainViewModel.fetchUsers(name)
    }

}