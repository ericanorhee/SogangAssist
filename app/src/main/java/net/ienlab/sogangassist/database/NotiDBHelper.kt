package net.ienlab.sogangassist.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import net.ienlab.sogangassist.data.NotificationItem
import kotlin.collections.ArrayList

class NotiDBHelper(context: Context, name: String, version: Int): SQLiteOpenHelper(context, name, null, version) {

    val _TABLENAME0 = "NOTI_ASSIST"
    val _CREATE0 = "CREATE TABLE IF NOT EXISTS $_TABLENAME0("

    //DB 처음 만들때 호출. - 테이블 생성 등의 초기 처리.
    override fun onCreate(db: SQLiteDatabase) {
        val sb = StringBuffer()
        sb.append(" CREATE TABLE $_TABLENAME0 ( ")
        sb.append(" $ID INTEGER PRIMARY KEY AUTOINCREMENT, ")
        sb.append(" $CONTENT_TITLE TEXT, ")
        sb.append(" $CONTENT_TEXT TEXT, ")
        sb.append(" $TIMESTAMP INTEGER, ")
        sb.append(" $TYPE INTEGER, ")
        sb.append(" $DESTINATION INTEGER, ")
        sb.append(" $IS_READ INTEGER ) ")

        db.execSQL(sb.toString())
    }

    //DB 업그레이드 필요 시 호출. (version값에 따라 반응)
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $_TABLENAME0")
        onCreate(db)
    }

    fun addItem(item: NotificationItem): Int {
        val db = writableDatabase

        val sb = StringBuffer()
        sb.append(" INSERT INTO $_TABLENAME0 ( ")
        sb.append(" $CONTENT_TITLE, $CONTENT_TEXT, $TIMESTAMP, $TYPE, $DESTINATION, $IS_READ ) ")
        sb.append(" VALUES ( ?, ?, ?, ?, ?, ? )")

        db.execSQL(sb.toString(),
                arrayOf(
                    item.contentTitle,
                    item.contentText,
                    item.timeStamp,
                    item.type,
                    item.destination,
                    item.isRead.toInt()
                )
        )

        val cursor = db.rawQuery("SELECT LAST_INSERT_ROWID()", null)
        var lastIndex = -1
        while (cursor.moveToNext()) { lastIndex = cursor.getInt(0) }

        cursor.close()
        return lastIndex
    }

    fun updateItemById(item: NotificationItem) {
        val db = writableDatabase
        val value = ContentValues()

        value.put(ID, item.id)
        value.put(CONTENT_TITLE, item.contentTitle)
        value.put(CONTENT_TEXT, item.contentText)
        value.put(TIMESTAMP, item.timeStamp)
        value.put(TYPE, item.type)
        value.put(DESTINATION, item.destination)
        value.put(IS_READ, item.isRead.toInt())

        db.update(_TABLENAME0, value, "$ID=${item.id}", null)
    }

    fun getAllItem(): ArrayList<NotificationItem> {
        val sb = StringBuffer()
        sb.append(" SELECT $ID, $CONTENT_TITLE, $CONTENT_TEXT, $TIMESTAMP, $TYPE, $DESTINATION, $IS_READ FROM $_TABLENAME0 ")

        val db = readableDatabase
        val cursor = db.rawQuery(sb.toString(), null)

        val arr = ArrayList<NotificationItem>()

        while (cursor.moveToNext()) {
            val data = NotificationItem(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getLong(3),
                cursor.getInt(4),
                cursor.getInt(5),
                cursor.getInt(6).toBoolean()
            )

            arr.add(data)
        }

        cursor.close()
        return arr
    }

    fun getItemById(id: Int): NotificationItem {

        val sb = StringBuffer()
        sb.append(" SELECT $ID, $CONTENT_TITLE, $CONTENT_TEXT, $TIMESTAMP, $TYPE, $DESTINATION, $IS_READ FROM $_TABLENAME0 WHERE $ID=$id ")

        val db = readableDatabase
        val cursor = db.rawQuery(sb.toString(), null)

        var data = NotificationItem(-1, "", "", 0L, 0, -1, false)
        while (cursor.moveToNext()) {
            data = NotificationItem(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getLong(3),
                cursor.getInt(4),
                cursor.getInt(5),
                cursor.getInt(6).toBoolean()
            )
        }

        cursor.close()
        return data
    }

    fun deleteData(id: Int) {
        val db = writableDatabase
        db.execSQL(" DELETE FROM $_TABLENAME0 WHERE $ID = $id")
    }

    fun checkIsDataAlreadyInDBorNot(dbfield: String, fieldValue: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM $_TABLENAME0 WHERE $dbfield = $fieldValue"
        val cursor = db.rawQuery(query, null)
        if (cursor.count <= 0) {
            cursor.close()
            return false
        }
        cursor.close()
        return true
    }

    fun Boolean.toInt(): Int = if (this) 1 else 0
    fun Int.toBoolean(): Boolean = (this == 1)

    companion object {
        val dbName = "SogangLMSAssistNotification.db"
        val dbVersion = 3

        const val ID = "ID"
        const val CONTENT_TITLE = "CONTENT_TITLE"
        const val CONTENT_TEXT = "CONTENT_TEXT"
        const val TIMESTAMP = "TIMESTAMP"
        const val TYPE = "TYPE"
        const val DESTINATION = "DEST"
        const val IS_READ = "IS_READ"
    }
}

