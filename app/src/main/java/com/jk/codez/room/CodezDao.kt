package com.jk.codez.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.jk.codez.connection.Connection
import com.jk.codez.item.Item

@Dao
interface CodezDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: Item?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateItem(item: Item?)

    @Delete
    fun removeItem(item: Item?)

    @Query("SELECT * FROM items ORDER BY street ASC")
    fun allItemsAsLiveData(): LiveData<List<Item>>

    @Query("SELECT * FROM items")
    fun allItemsAsList(): List<Item?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertConnection(conn: Connection?)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateConnection(conn: Connection?)

    @Delete
    fun removeConnection(conn: Connection?)

    @Query("SELECT * FROM connections ORDER BY lastAccess DESC")
    fun allConnectionsAsLiveData(): LiveData<List<Connection?>?>?

    @Query("SELECT * from connections WHERE _id LIKE :id")
    fun getLastConnection(id: String?): Connection?
}