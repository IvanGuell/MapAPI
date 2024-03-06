package com.example.mapapp.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapViewModel: ViewModel() {
    private val _loading = MutableLiveData(true)
    val loading = _loading

    private val _searchText = MutableLiveData<String>()
    val searchText = _searchText

    private val _show = MutableLiveData<Boolean>()
    val show = _show

    fun onSearchTextChange(text: String?) {
        // Verifica si el texto no es nulo antes de realizar la búsqueda
        text?.let {
            // Realiza la búsqueda o cualquier acción que necesites con el texto no nulo
            // Por ejemplo, puedes imprimir el texto de búsqueda
            println("Texto de búsqueda: $it")
        }
    }
    fun changeShow() {
        _show.value = show.value != true
    }




}