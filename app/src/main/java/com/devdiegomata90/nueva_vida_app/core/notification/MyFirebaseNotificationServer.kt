package com.devdiegomata90.nueva_vida_app.core.notification

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.devdiegomata90.nueva_vida_app.R
import com.devdiegomata90.nueva_vida_app.ui.view.Evento.EventoActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseNotificationServer : FirebaseMessagingService() {

    //Metodo para la validad si envio o notificacion

    fun envioNotificacion(title: String, message: String) {

        // Crea un objeto NotificationBuilder
        val notificationBuilder = NotificationBuilder(this, "my_channel")

        val flag = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0

        // Crea una notificación
        val notification = notificationBuilder.createNotification(
            title = title,
            message = message,
            smallIcon = R.drawable.ic_noti,
            action = PendingIntent.getActivity(this, 0, Intent(this, EventoActivity::class.java), flag)
        )

        // Envia la notificación
        notificationBuilder.notify(notification)
    }

    override fun onMessageReceived(message: RemoteMessage) {

        Looper.prepare()

        Handler().post {
            val title = message.notification?.title.toString()
            val mesage = message.notification?.body.toString()

            Toast.makeText(this, "Titulo : $title Menssaje: $mesage" , Toast.LENGTH_LONG).show()
            Log.d("Notificaciones", title)
            Log.d("Notificaciones", mesage)

            envioNotificacion(title, mesage)
        }

        Looper.loop()
    }
}