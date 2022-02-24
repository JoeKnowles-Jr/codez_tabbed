package com.jk.codez.connection

import androidx.room.*
import java.util.*

@Entity(tableName = "connections", indices = [Index(value = arrayOf("_id"), unique = true)])
class Connection(id: String, url: String, name: String, description: String, last: Date?, created: Date?, updated: Date?) {

    constructor() : this("", "", "", "", null, null, null) {

    }

    @PrimaryKey
    @ColumnInfo(name = "_id")
    private var _id: String = id

    @ColumnInfo(name = "url")
    private var mUrl: String = url

    @ColumnInfo(name = "name")
    private var mName: String = name

    @ColumnInfo(name = "description")
    private var mDescription: String = description

    @ColumnInfo(name = "lastAccess")
    private var mLastAccessedAt: Date

    @ColumnInfo(name = "cratedAt")
    private var mCreatedAt: Date

    @ColumnInfo(name = "lng")
    private var mUpdatedAt: Date

    init {
        mLastAccessedAt = last ?: Date()
        mCreatedAt = created ?: Date()
        mUpdatedAt = updated ?: Date()
    }

    fun getId(): String {
        return _id
    }

    fun setId(_id: String) {
        this._id = _id
    }

    fun getUrl(): String {
        return mUrl
    }

    fun setUrl(url: String) {
        this.mUrl = url
    }

    fun getName(): String {
        return mName
    }

    fun setName(name: String) {
        this.mName = name
    }

    fun getDescription(): String {
        return mDescription
    }

    fun setDescription(desc: String) {
        this.mDescription = desc
    }

    fun getLastAccessedAt(): Date {
        return mLastAccessedAt
    }

    fun setLastAccessedAt(mLastAccessedAt: Date) {
        this.mLastAccessedAt = mLastAccessedAt
    }

    fun getCreatedAt(): Date {
        return mCreatedAt
    }

    fun setCreatedAt(mCreatedAt: Date) {
        this.mCreatedAt = mCreatedAt
    }

    fun getUpdatedAt(): Date {
        return mUpdatedAt
    }

    fun setUpdatedAt(mUpdatedAt: Date) {
        this.mUpdatedAt = mUpdatedAt
    }

    override fun toString(): String {
        return String.format(Locale.getDefault(), "%s %s - %s", mName, mDescription, mUrl)
    }
}