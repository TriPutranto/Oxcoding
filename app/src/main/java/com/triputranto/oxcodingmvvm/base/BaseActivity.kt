package com.triputranto.oxcodingmvvm.base

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import com.triputranto.oxcodingmvvm.data.network.ApiClient
import com.triputranto.oxcodingmvvm.data.network.ApiService

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    val apiService : ApiService = ApiClient.getClient()

}