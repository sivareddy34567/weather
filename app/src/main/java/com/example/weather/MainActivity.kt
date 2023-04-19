package com.example.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.location.Geocoder
import com.example.login.database.WeatherDatabase
import com.example.login.model.Weather
import com.example.login.repository.WeatherRepository
import com.example.login.utils.Constants
import com.example.login.viewmodel.WeatherViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var weatherDatabase: WeatherDatabase
    private lateinit var repository: WeatherRepository
    lateinit var mainActivityViewModel: WeatherViewModel
    private lateinit var factory: WeatherViewModelFactory

    var etEmail: EditText? = null
    var etPassword:EditText? = null
    var bLogin: Button? = null
    var latitudeText: TextView? = null
    var longitudeText: TextView? = null
    var addressLocation: TextView? = null
    var dateTime: TextView? = null
    var progressBar: ProgressBar? = null
    private val LOCATION_PERMISSION_REQ_CODE = 1000;
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    var spinner:Spinner? = null
    var weatherList:ArrayList<Weather>?=null
    var isFirst:Boolean=false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
           getWeatherData(entercityname.text.toString())
        }
        weatherDatabase=WeatherDatabase(this)
        repository=WeatherRepository(weatherDatabase)

        factory = WeatherViewModelFactory(repository)

        mainActivityViewModel =  ViewModelProvider(this, factory)[WeatherViewModel::class.java]


        // initialize fused location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            // Calling Location Manager
            val mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

            // Checking GPS is enabled
            val mGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            if(!mGPS)
            {
                Toast.makeText(this, "You need to enable gps",Toast.LENGTH_LONG).show()
            }else{
                  getCurrentLocation()
            }
    }

    private fun getWeatherData(address: String) {
        mainActivityViewModel.weather(address,Constants.APP_ID)?.
        observe(this, androidx.lifecycle.Observer {
            //   weatherList!!.addAll(it.base)
            when (it) {

                is BaseResponse.Success -> {
                    if(it?.data?.sys?.country=="US")
                    {
                        Toast.makeText(applicationContext,"the application"+it?.data?.base,Toast.LENGTH_LONG).show()
                        it.data.weather[0].main.also { main.text = it };
                        description.text= it.data.weather[0].description;

                    }else{
                        Toast.makeText(applicationContext,"Please enter USA locations only",Toast.LENGTH_LONG).show()

                    }


                }
                is BaseResponse.Error -> {
                    Toast.makeText(applicationContext,"the application"+it.message,Toast.LENGTH_LONG).show()

                }


            }


        })    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   getCurrentLocation()
                } else {
                    // permission denied
                    Toast.makeText(this, "You need to grant permission to access location",
                        Toast.LENGTH_SHORT).show()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.dialogTitle)
        builder.setMessage(R.string.dialogMessage)
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton("Yes"){dialogInterface, which ->
            super.onBackPressed()
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
    private fun getCurrentLocation() {
        // checking location permission
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE
            );

        }else{
            getLatlongs()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLatlongs() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // getting the last known or current location

                if(location?.latitude!=null) {
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                    val current = LocalDateTime.now().format(formatter)
                    dateTime?.text=current
                    latitude = location.latitude
                    longitude = location.longitude
                    latitudeText?.text = "Latitude: ${location.latitude}"
                    longitudeText?.text = "Longitude: ${location.longitude}"
                    val geocoder: Geocoder
                    val addresses: List<Address>?
                    geocoder = Geocoder(this, Locale.getDefault())
                    addresses = geocoder.getFromLocation(
                        latitude,
                        longitude,
                        1
                    ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    val address: String =
                        addresses!![0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    lo.text=address
                    if(address!=null){
                        isFirst=true
                        if(isFirst)
                        {
                            getWeatherData(address)
                            isFirst=false
                        }
                    }

                }
                //etEmail?.text = "Provider: ${location.provider}"
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed on getting current location",
                    Toast.LENGTH_SHORT).show()
            }
    }
}