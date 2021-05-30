package com.shivam.newsfresh

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var adapter: NewsListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.layoutManager=LinearLayoutManager(this)
        fetchData()
         adapter= NewsListAdapter(this)
        recyclerView.adapter=adapter
    }
    private fun fetchData(){
        val url="https://saurav.tech/NewsAPI/top-headlines/category/health/in.json"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url,null,
            Response.Listener { it ->
                // Display the first 500 characters of the response string.
                val newsJsonArray=it.getJSONArray("articles")
                val newsArray=ArrayList<News>()
                for(i in 0 until newsJsonArray.length()){
                    val newsJsonObject=newsJsonArray.getJSONObject(i)
                    val news=News(newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage"))
                    newsArray.add(news)
                }
                adapter.updateNews(newsArray)
            },
            Response.ErrorListener {
                Toast.makeText(this,"Someting is Wrong...",Toast.LENGTH_LONG).show()
            })

// Add the request to the RequestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }
}