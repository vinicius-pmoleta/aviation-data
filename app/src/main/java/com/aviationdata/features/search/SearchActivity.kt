package com.aviationdata.features.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aviationdata.R
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        setSupportActionBar(search_toolbar)
    }
}