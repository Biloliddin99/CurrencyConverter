package com.example.currencyconverter

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.currencyconverter.adapters.RvAdapter
import com.example.currencyconverter.adapters.RvClick
import com.example.currencyconverter.models.MyCurrency
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uz.biloliddinuz.currencyconverter.databinding.ActivityMainBinding
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class MainActivity : AppCompatActivity(), RvClick {

    private lateinit var list: List<MyCurrency>
    private lateinit var itemList: ArrayList<MyCurrency>
    private var ccyNew = "Uzs"
    private var flag = "Uz"
    private var rate = "1"
    private var checked: Boolean = true
    private var changed: Boolean = true
    private var count = 0
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        itemList = ArrayList()
        if (isInternetAvailable()) {
            binding.myRv.visibility = View.VISIBLE
            binding.constraint.visibility = View.VISIBLE
            binding.tvNet.visibility = View.GONE
            checked = true

        } else {
            binding.myRv.visibility = View.GONE
            binding.constraint.visibility = View.GONE
            binding.tvNet.visibility = View.VISIBLE
            checked = false
        }

        binding.img1.countryCode = flag
        binding.img2.countryCode = "Uz"
        binding.tv1.text = ccyNew
        binding.tv2.text = "UZS"
        binding.edit1.setText("1")
        binding.edit2.setText(rate)



        myTask.execute()

    }

    @SuppressLint("SetTextI18n")
    private fun mainCalculate(updateList: List<MyCurrency>) {
        binding.btnExchange.setOnClickListener {
            if (!changed) {
                binding.img1.countryCode = updateList[count].Ccy.substring(0, 2)
                binding.img2.countryCode = "Uz"
                binding.tv1.text = updateList[count].Ccy
                binding.tv2.text = "UZS"
                binding.edit1.setText("1")
                binding.edit2.setText(updateList[count].Rate)
                changed = true

            } else {
                binding.img1.countryCode = "Uz"
                binding.img2.countryCode = updateList[count].Ccy.substring(0, 2)
                binding.tv1.text = "UZS"
                binding.tv2.text = updateList[count].Ccy
                binding.edit1.setText(updateList[count].Rate)
                binding.edit2.setText("1")
                changed = false
            }
        }

        binding.apply {
            binding.edit1.addTextChangedListener {
                if (it.toString().isNotEmpty()) {
                    if (!changed) {
                        binding.edit2.setText(
                            "%.2f".format(
                                it.toString().toDouble() / updateList[count].Rate.toDouble()

                            )
                        )
                    } else {
                        binding.edit2.setText(
                            "%.2f".format(
                                it.toString().toDouble() * updateList[count].Rate.toDouble()
                            )
                        )
                    }
                } else {
                    Toast.makeText(this@MainActivity, "PLease enter a value", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }

    private fun getCurrency(): List<MyCurrency> {
        val url = URL("https://cbu.uz/uzc/arkhiv-kursov-valyut/json/")
        val list = ArrayList<MyCurrency>()
        val connection: HttpsURLConnection = url.openConnection() as HttpsURLConnection
        connection.connect()
        val inputStream = connection.inputStream
        val bufferReader = inputStream.bufferedReader()
        val gsonString = bufferReader.readLine()
        val gson = Gson()

        val type = object : TypeToken<ArrayList<MyCurrency>>() {}.type
        list.addAll(gson.fromJson<ArrayList<MyCurrency>>(gsonString, type))

        return list
    }

    /** getting data in background */
    private val myTask = @SuppressLint("StaticFieldLeak")
    object : AsyncTask<Void, Void, Void>() {
        @Deprecated("Deprecated in Java")
        override fun onPreExecute() {
            super.onPreExecute()
            runOnUiThread {
                binding.myProgressBar.visibility = View.VISIBLE
            }

        }

        @Deprecated("Deprecated in Java")
        override fun doInBackground(vararg p0: Void?): Void? {
            if (checked) {
                list = getCurrency()
            }
            return null
        }

        @Deprecated("Deprecated in Java")
        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            if (checked) {
                runOnUiThread {
                    binding.myProgressBar.visibility = View.GONE
                    binding.myRv.adapter = RvAdapter(this@MainActivity, list, this@MainActivity)
                }
            }
        }

    }

    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }


    @SuppressLint("SetTextI18n")
    override fun itemClicked(user: MyCurrency, position: Int) {
        itemList.add(user)
        count = position
        binding.apply {

            img1.countryCode = user.Ccy.substring(0, 2)
            img2.countryCode = "Uz"
            tv1.text = user.Ccy
            tv2.text = "UZS"
            edit2.setText(user.Rate)

            mainCalculate(itemList)
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)


    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
    }
}


