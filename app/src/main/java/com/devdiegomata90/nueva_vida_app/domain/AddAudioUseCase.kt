package com.devdiegomata90.nueva_vida_app.domain

import android.content.ContentResolver
import android.net.Uri
import com.devdiegomata90.nueva_vida_app.data.model.Audio
import com.devdiegomata90.nueva_vida_app.data.repository.AddAudioRepository


class AddAudioUseCase {

    private val repository = AddAudioRepository()

    suspend operator fun invoke(audio: Audio,RutaArchivoUri:Uri?,RutaAudioUri:Uri,extensiones:Pair<String?,String?>):Boolean{

       return repository.addAudio(audio,RutaArchivoUri,RutaAudioUri,extensiones)

    }

}