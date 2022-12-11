package com.stoic.weatherapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.resources.Compatibility.Api15Impl
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val CITY: String  = "kocaeli,tr"
    val API: String = "1d802c67407539429c431afea184cb5f"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        weatherTask().execute()
    }

    inner class weatherTask(): AsyncTask<String, Void, String>(){
        override fun onPreExecute(){
            super.onPreExecute()
            findViewById<ProgressBar>(R.id.loader).visibility = View.VISIBLE
            findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.GONE
            findViewById<TextView>(R.id.errorText).visibility = View.GONE
        }

        override fun doInBackground(vararg p0: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$CITY&units=metric&appid=$API")
                    .readText(Charsets.UTF_8)
            }
            catch (e: Exception){
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jSonObj = JSONObject(result)
                val main = jSonObj.getJSONObject("main")
                val sys = jSonObj.getJSONObject("sys")
                val wind = jSonObj.getJSONObject("wind")
                val weather = jSonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt: Long  = jSonObj.getLong("dt")
                val updatedAtText = "Updated At: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(Date(updatedAt * 1000))
                val temp = main.getString("temp") + "°C"
                val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
                val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescrpition = weather.getString("description")
                val address = jSonObj.getString("name") + ", " + sys.getString("country")

                findViewById<TextView>(R.id.address).setText(address)
                findViewById<TextView>(R.id.updated_at).setText(updatedAtText)
                findViewById<TextView>(R.id.status).setText(weatherDescrpition.capitalize())
                findViewById<TextView>(R.id.temp).setText(temp)
                findViewById<TextView>(R.id.temp_min).setText(tempMin)
                findViewById<TextView>(R.id.temp_max).setText(tempMax)
                findViewById<TextView>(R.id.sunrise).setText(SimpleDateFormat("hh mm a", Locale.ENGLISH).format(Date(sunrise * 1000)))
                findViewById<TextView>(R.id.sunset).setText(SimpleDateFormat("hh mm a", Locale.ENGLISH).format(Date(sunset * 1000)))
                findViewById<TextView>(R.id.wind).setText(windSpeed)
                findViewById<TextView>(R.id.pressure).setText(pressure)
                findViewById<TextView>(R.id.humidity).setText(humidity)
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.mainContainer).visibility = View.VISIBLE
            }

            catch (e: Exception){
                findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
                findViewById<RelativeLayout>(R.id.errorText).visibility = View.VISIBLE
            }
        }
    }
}