package com.example.currencyconverter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    lateinit var lv: ListView
    lateinit var etcv: EditText
    lateinit var tvac: TextView

    lateinit var cl: MutableMap<String, Double>

    var CV: Double = 1.0
    var CVN: String = "RUB"

    val url = "https://api.exchangeratesapi.io/latest?base="

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lv = findViewById(R.id.currencys);
        etcv = findViewById(R.id.editTextCValue)
        tvac = findViewById(R.id.textViewActualCurrency)

        etcv.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                Log.d("edit", s.toString())
                CV = if (s.toString() == "") 1.0 else s.toString().toDouble()
                calc()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        cload("RUB")

        lv.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            val n = cl.keys.elementAt(position)
            Log.d("set", n)
            if (CVN != n) cload(n)
        })

        //Toast.makeText(applicationContext,response, Toast.LENGTH_SHORT).show()
    }

    fun cload(base:String) {
        cl = mutableMapOf()
        tvac.text = base

        val httpAsync = (url + base)
            .httpGet()
            .responseString { request, response, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Toast.makeText(this, ex.toString(), Toast.LENGTH_SHORT).show()
                    }
                    is Result.Success -> {
                        val data = result.get()
                       // Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
                        var jsono = JSONObject(data)
                        var rates: JSONObject = jsono.getJSONObject("rates")
                        for (k in rates.keys()) {
                            //Toast.makeText(this, "$k ~ ${rates.getDouble(k)}", Toast.LENGTH_SHORT).show()
                            cl.put(k, rates.getDouble(k))

                            calc()
                        }
                    }
                }
            }
        httpAsync.join()
    }

    fun calc() {
        var currencyList: List<Currency>
        currencyList = ArrayList()
        val adapter = ListViewAdapter(this, R.layout.list_item, currencyList)
        lv.setAdapter(adapter)
        for ((k,v) in cl) currencyList.add(Currency(k, Math.round((CV/v) * 1000.0) / 1000.0, v))
    }

}