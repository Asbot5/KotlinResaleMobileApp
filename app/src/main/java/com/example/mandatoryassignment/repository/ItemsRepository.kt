package com.example.mandatoryassignment.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.mandatoryassignment.models.Item
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItemsRepository {
    private val url = "https://anbo-restresale.azurewebsites.net/api/"

    private val itemStoreService: ItemStoreService
    val itemsLiveData: MutableLiveData<List<Item>> = MutableLiveData<List<Item>>()
    val errorMessageLiveData: MutableLiveData<String> = MutableLiveData()
    val updateMessageLiveData: MutableLiveData<String> = MutableLiveData()

    init {
        val build: Retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create()).build()
        itemStoreService = build.create(ItemStoreService::class.java)
        getPosts()
    }

    fun getPosts() {
        itemStoreService.getAllItems().enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if (response.isSuccessful) {
                    //Log.d("TESTTEST", response.body().toString())
                    itemsLiveData.postValue(response.body())
                    errorMessageLiveData.postValue("")
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("TESTTEST", message)
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                //itemsLiveData.postValue(null)
                errorMessageLiveData.postValue(t.message)
                Log.d("TESTTEST", t.message!!)
            }
        })
    }

    fun add(item: Item) {
        itemStoreService.saveItem(item).enqueue(object : Callback<Item> {
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if (response.isSuccessful) {
                    Log.d("TESTTEST", "Added: " + response.body())
                    updateMessageLiveData.postValue("Added: " + response.body())
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("TESTTEST", message)
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("TESTTEST", t.message!!)
            }
        })
    }

    fun delete(id: Int) {
        itemStoreService.deleteItem(id).enqueue(object : Callback<Item> {
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if (response.isSuccessful) {
                    Log.d("TESTTEST", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Deleted: " + response.body())
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("TESTTEST", message)
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("TESTTEST", t.message!!)
            }
        })
    }

    fun update(item: Item) {
        itemStoreService.updateItem(item.id, item).enqueue(object : Callback<Item> {
            override fun onResponse(call: Call<Item>, response: Response<Item>) {
                if (response.isSuccessful) {
                    Log.d("TESTTEST", "Updated: " + response.body())
                    updateMessageLiveData.postValue("Updated: " + response.body())
                } else {
                    val message = response.code().toString() + " " + response.message()
                    errorMessageLiveData.postValue(message)
                    Log.d("TESTTEST", message)
                }
            }

            override fun onFailure(call: Call<Item>, t: Throwable) {
                errorMessageLiveData.postValue(t.message)
                Log.d("TESTTEST", t.message!!)
            }
        })
    }
}