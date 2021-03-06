package net.ienlab.sogangassist.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import net.ienlab.sogangassist.database.*

class MarkFinishReceiver : BroadcastReceiver() {

    lateinit var nm: NotificationManager
    lateinit var dbHelper: DBHelper
    lateinit var notiDBHelper: NotiDBHelper

    override fun onReceive(context: Context, intent: Intent) {
        nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        dbHelper = DBHelper(context, DBHelper.dbName, DBHelper.dbVersion)
        notiDBHelper = NotiDBHelper(context, NotiDBHelper.dbName, NotiDBHelper.dbVersion)

        val id = intent.getIntExtra("ID", -1)
        val notiId = intent.getIntExtra("NOTI_ID", -1)

        if (id != -1) {
            dbHelper.getItemById(id).apply {
                isFinished = true
                dbHelper.updateItemById(this)
            }

            nm.cancel(693000 + id)
        }

        if (notiId != -1) {
            notiDBHelper.getItemById(notiId).apply {
                isRead = true
                notiDBHelper.updateItemById(this)
            }
        }
    }
}
