/*
package com.example.shimmerrecyclerview

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.AutoCompleteTextView
import org.json.JSONArray

class AutoTextCompleteViewCistomize : AppCompatActivity() {

    private lateinit var autoCompleteTextView: AutoCompleteTextView
    private lateinit var adapter: CustomAutoCompleteAdapter
  //  private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_text_complete_view_cistomize)
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView)

        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.length > 2) {
                       // fetchResults(it.toString())
                    }
                }
            }
        })
    }

*/
/*    private fun fetchResults(query: String) {
        val request = Request.Builder()
            .url("https://example.com/api/search?query=$query")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let {
                    val results = parseResults(it)
                    runOnUiThread {
                        adapter = CustomAutoCompleteAdapter(this@MainActivity, results)
                        autoCompleteTextView.setAdapter(adapter)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })
    }*//*


    private fun parseResults(json: String): List<SearchResult> {
        val results = mutableListOf<SearchResult>()
        val jsonArray = JSONArray(json)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val value1 = jsonObject.getString("value1")
            val value2 = jsonObject.getString("value2")
            val value3 = jsonObject.getString("value3")
            results.add(SearchResult(value1, value2, value3))
        }

        return results
    }
}

data class SearchResult(val value1: String, val value2: String, val value3: String)
*/
