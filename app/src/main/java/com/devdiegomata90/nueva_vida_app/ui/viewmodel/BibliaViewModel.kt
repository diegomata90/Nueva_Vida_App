package com.devdiegomata90.nueva_vida_app.ui.viewmodel

import androidx.lifecycle.*
import com.devdiegomata90.nueva_vida_app.data.model.BookAndChapter
import com.devdiegomata90.nueva_vida_app.domain.GetBooksUseCase
import com.devdiegomata90.nueva_vida_app.domain.GetChaptersUseCase
import com.devdiegomata90.nueva_vida_app.domain.GetVersesUseCase
import com.devdiegomata90.nueva_vida_app.data.network.response.BooksResponse
import com.devdiegomata90.nueva_vida_app.data.network.response.VersesResponse
import kotlinx.coroutines.launch


class BibliaViewModel() : ViewModel() {

    // LiveData para la lista de libros
    var books = MutableLiveData<List<BooksResponse>?>()
    var chapters = MutableLiveData<List<String>?>()
    var verses = MutableLiveData<List<VersesResponse>?>()
    var bookAndChapter = MutableLiveData<BookAndChapter>()

    //LiveData para el isLoading
    var loading = MutableLiveData<Boolean>().apply { value = false }

    //Llamamos al caso de uso que obtiene los libros
    var getBooksUseCase = GetBooksUseCase()
    var getChaptersUseCase = GetChaptersUseCase()
    var getVersesUseCase = GetVersesUseCase()


    fun onCreate() {

        viewModelScope.launch {
           loading.postValue(true)

            val result = getBooksUseCase()

            if (!result.isNullOrEmpty()) {
                //ordena la lista
                val orderResult = result.sortedBy { it.order }
                books.postValue(orderResult)
            }
            loading.postValue(false)
        }

    }

    fun showChapters(bookSelected: String) {
        //Recibe la lista de la capitulos
        val result = getChaptersUseCase(bookSelected)

        if (!result.isNullOrEmpty()) {
            chapters.postValue(result)
        }
    }

    suspend fun showVerse(bookSelected: String, chapterSelected: String) {

        //Recibe la lista de la capitulos
        val result = getVersesUseCase(bookSelected, chapterSelected)

        //Valida que no sea nulo
        if (result != null) {

            result.let { (BookAndChapter, Verses) ->
                //Actualiza el lifedata
                bookAndChapter.postValue(BookAndChapter)
                verses.postValue(Verses)
            }

        }

    }


}




