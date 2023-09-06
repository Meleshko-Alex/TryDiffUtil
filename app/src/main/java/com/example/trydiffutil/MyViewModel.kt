package com.example.trydiffutil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MyViewModel : ViewModel() {

    private val itemFlow = MutableStateFlow(createListOfItems())

    val stateFlow: Flow<State> = itemFlow.map { itemsList -> State(items = itemsList) }

    private fun createListOfItems(): List<Item> = List(24) {
        Item(
            id = it,
            title = "List item ${it + 1}",
            isDeleting = false
        )
    }

    fun delete(item: Item) {
        viewModelScope.launch {
            val updatedItem = item.copy(isDeleting = true)
            updateItem(updatedItem)
            delay(2000)
            deleteItem(item)
        }
    }

    private fun updateItem(newItem: Item) {
        val oldItemList = itemFlow.value
        val newItemList = ArrayList(oldItemList)
        newItemList[findIndex(newItem)] = newItem
        itemFlow.value = newItemList
    }

    private fun deleteItem(item: Item) {
        val oldItemList = itemFlow.value
        val newItemList = ArrayList(oldItemList)
        newItemList.removeAt(findIndex(item))
        itemFlow.value = newItemList
    }

    private fun findIndex(item: Item): Int {
        return itemFlow.value.indexOfFirst { it.id == item.id }
    }

    class State(val items: List<Item>) {
        val totalCount: Int get() = items.size
    }
}