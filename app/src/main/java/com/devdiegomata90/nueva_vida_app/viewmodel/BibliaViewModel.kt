package com.devdiegomata90.nueva_vida_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devdiegomata90.nueva_vida_app.model.Book
import com.devdiegomata90.nueva_vida_app.model.Verse
import com.devdiegomata90.nueva_vida_app.repository.BooksRepository
import com.devdiegomata90.nueva_vida_app.repository.VersesRepository
import kotlinx.coroutines.launch


class BibliaViewModel(
    private val booksRepository: BooksRepository,
    private val versesRepository: VersesRepository,
) : ViewModel() {

    // LiveData para la lista de libros
    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    // LiveData para la lista de capítulos
    private val _chapters = MutableLiveData<List<String>>()
    val chapters: LiveData<List<String>> get() = _chapters

    // LiveData para la lista de versículos
    private val _verses = MutableLiveData<List<Verse>>()
    val verses: LiveData<List<Verse>> get() = _verses

    // Función para obtener la lista de libros
    fun fetchBooks(url: String) {
        viewModelScope.launch {
            try {
                // Utiliza el repository para obtener la lista de libros
                val booksList = booksRepository.getBooks(url)
                _books.value = booksList
            } catch (e: Exception) {
                // Manejo de errores
                _books.value = emptyList()
            }
        }

    }
    // Función para obtener la lista de capítulos basada en el libro seleccionado
    fun fetchChapters(bookSelected: Book) {
        // Obtener capítulos basados en el libro seleccionado
        val chaptersList = bookSelected.capitulos.map { it.n_chapter.toString() }
        _chapters.value = chaptersList
    }

    // Función para obtener la lista de versículos
    fun fetchVerses(url: String) {
        viewModelScope.launch {
            try {
                // Utiliza el repository para obtener la lista de verses
                val versesList = versesRepository.getVerses(url)
                _verses.value = versesList
            } catch (e: Exception) {
                // Manejo de errores
                _verses.value = emptyList()
            }
        }

    }

}




