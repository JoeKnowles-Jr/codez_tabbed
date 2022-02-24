package com.jk.codez.item

import androidx.room.*
import java.lang.StringBuilder
import java.util.*

@Entity(tableName = "items", indices = [Index(value = arrayOf("_id"), unique = true)])
class Item {
    constructor(
        id: String,
        number: Int?,
        street: String,
        codes: Array<String>?,
        notes: String,
        lat: Double?,
        lng: Double?,
        precise: Boolean?
    ) {
        this.number = number
        this.street = street
        this.codes = codes
        this.notes = notes
        this.lat = lat
        this.lng = lng
        this.precise = precise
        _id = id
    }

    @Ignore
    constructor(
        number: Int?,
        street: String,
        codes: Array<String>?,
        notes: String,
        lat: Double?,
        lng: Double?,
        precise: Boolean?
    ) {
        this.number = number
        this.street = street
        this.codes = codes
        this.notes = notes
        this.lat = lat
        this.lng = lng
        this.precise = precise
        _id = ""
    }

    @Ignore
    constructor() {
        number = null
        street = ""
        codes = null
        notes = ""
        lat = null
        lng = null
        precise = false
        _id = ""
    }

    override fun toString(): String {
        return String.format(
            Locale.getDefault(), "%d %s - %d codes - %s\n%f - %f",
            number, street, codes!!.size, notes, lat, lng
        )
    }

    val codesString: String
        get() {
            if (codes!!.isEmpty()) return "No Codes!"
            val sb = StringBuilder()
            for (i in codes!!.indices) {
                sb.append(codes!![i])
                if (i < codes!!.size - 1) sb.append(" ")
            }
            return sb.toString()
        }

    fun getId(): String {
        return _id
    }

    fun setId(id: String) {
        this._id = id
    }

    fun getNumber(): Int? {
        return number
    }

    fun setNumber(number: Int?) {
        this.number = number
    }

    fun getStreet(): String {
        return street
    }

    fun setStreet(street: String) {
        this.street = street
    }

    fun getCodes(): Array<String>? {
        return codes
    }

    fun setCodes(codes: Array<String>?) {
        this.codes = codes
    }

    fun getNotes(): String {
        return notes
    }

    fun setNotes(notes: String) {
        this.notes = notes
    }

    fun getLat(): Double? {
        return lat
    }

    fun setLat(lat: Double?) {
        this.lat = lat
    }

    fun getLng(): Double? {
        return lng
    }

    fun setLng(lng: Double?) {
        this.lng = lng
    }

    fun getPrecise(): Boolean? {
        return precise
    }

    fun setPrecise(precise: Boolean) {
        this.precise = precise
    }

    @PrimaryKey
    @ColumnInfo(name = "_id")
    private var _id: String

    @ColumnInfo(name = "number")
    private var number: Int?

    @ColumnInfo(name = "street")
    private var street: String

    @ColumnInfo(name = "codes")
    private var codes: Array<String>?

    @ColumnInfo(name = "notes")
    private var notes: String

    @ColumnInfo(name = "lat")
    private var lat: Double?

    @ColumnInfo(name = "lng")
    private var lng: Double?

    @ColumnInfo(name = "precise")
    private var precise: Boolean?
}