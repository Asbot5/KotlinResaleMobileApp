package com.example.mandatoryassignment.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mandatoryassignment.repository.ItemsRepository

class ItemsViewModel : ViewModel() {
    private val repository = ItemsRepository()
    val itemsLiveData: LiveData<List<Item>> = repository.itemsLiveData
    val errorMessageLiveData: LiveData<String> = repository.errorMessageLiveData
    val itemsLiveDataNew: MutableLiveData<List<Item>> = repository.itemsLiveData

    init {
        reload()
    }

    fun reload() {
        repository.getPosts()
    }

    operator fun get(index: Int): Item? {
        return itemsLiveData.value?.get(index)
    }

    fun add(item: Item) {
        repository.add(item)
    }

    fun delete(id: Int) {
        repository.delete(id)
    }

    fun sortPriceAscending() {
        itemsLiveDataNew.value = itemsLiveData.value?.sortedBy { it.price }
    }

    fun sortDateAscending() {
        itemsLiveDataNew.value = itemsLiveData.value?.sortedBy { it.date }
    }

    fun filterPriceAscending(min: Int, max: Int) {
        itemsLiveDataNew.value =
            itemsLiveDataNew.value?.filter { i -> i.price in (min + 1) until max }
    }
}