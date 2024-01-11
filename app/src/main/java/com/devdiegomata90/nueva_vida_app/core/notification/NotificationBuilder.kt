package com.devdiegomata90.nueva_vida_app.core.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.devdiegomata90.nueva_vida_app.R
import kotlin.random.Random

class NotificationBuilder(context: Context, channelId: String) {
    private val context: Context
    private val channelId: String

    init {
        this.context = context
        this.channelId = channelId
    }

    fun createNotification(
        title: String,
        message: String,
        smallIcon: Int,
        largeIcon: Int = 0,
        action: PendingIntent? = null
    ): Notification {
        // Crea un canal de notificación
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(smallIcon)

        if (largeIcon != 0) {
            notification.setLargeIcon(BitmapFactory.decodeResource(context.resources, largeIcon))
        }

        if (action != null) {
            notification.addAction(
                R.drawable.btn_personalizado,
                "Abrir",
                action
            )
        }

        return notification.build()
    }

    fun notify(notification: Notification) {
        //Obtiene un id diferente para la notificacion
        val idNotifacion = Random.nextInt()

        // Obtiene el administrador de notificaciones
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Envía la notificación
        notificationManager.notify(idNotifacion, notification)
    }
}



