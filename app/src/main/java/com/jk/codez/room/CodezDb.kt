package com.jk.codez.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jk.codez.connection.Connection
import com.jk.codez.connection.DateConverter
import com.jk.codez.item.Item

@Database(entities = [Item::class, Connection::class], version = 1)
@TypeConverters(
    StringArrayConverter::class, DateConverter::class
)
abstract class CodezDb : RoomDatabase() {
    abstract fun codezDao(): CodezDao?

    companion object {
        private var _DBINSTANCE: CodezDb? = null
        private const val CODEZ_DB_NAME = "codes.db"
        fun getDatabase(context: Context): CodezDb? {
            if (_DBINSTANCE == null) {
                synchronized(CodezDb::class.java) {
                    if (_DBINSTANCE == null) {
                        _DBINSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            CodezDb::class.java, CODEZ_DB_NAME
                        )
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .addCallback(object : Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)
                                }

                                override fun onOpen(db: SupportSQLiteDatabase) {
                                    super.onOpen(db)
                                    val count = "SELECT count(*) FROM items"
                                    val cursor = db.query(count, null)
                                    cursor.moveToFirst()
                                    println(cursor.toString())
                                }
                            }).build()
                    }
                }
            }
            return _DBINSTANCE
        }
    }
}