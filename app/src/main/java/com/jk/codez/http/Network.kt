package com.jk.codez.http

import com.jk.codez.connection.Connection
import com.jk.codez.item.Item
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.RequestParams
import com.loopj.android.http.TextHttpResponseHandler
import org.apache.commons.validator.routines.UrlValidator

class Network(private val conn: Connection) {
    private val connection = conn
    private val codezUrl: String

    init {
        val url = conn.getUrl()
        codezUrl = if (urlIsValid(url)) url
        else ""
    }

    fun testUrl(handler: JkResponseHandler?) {
        if (codezUrl == "") {
            handler?.onNotConnected(connection)
            return
        }
        AsyncHttpClient().get("$codezUrl/connect-test", handler)
    }

    fun getCodes(handler: JkResponseHandler) {
        if (codezUrl == "") {
            handler.onNotConnected(connection)
            return
        }
        AsyncHttpClient().get("$codezUrl/get", handler)
    }

    fun addCode(item: Item, handler: JkResponseHandler?) {
        if (codezUrl == "") {
            handler?.onNotConnected(connection)
            return
        }
        AsyncHttpClient().post("$codezUrl/post", createParams(item), handler)
    }

    fun editCode(item: Item, handler: JkResponseHandler?) {
        if (codezUrl == "") {
            handler?.onNotConnected(connection)
            return
        }
        AsyncHttpClient().put("$codezUrl/put", createParams(item), handler)
    }

    fun deleteCode(cid: String, handler: JkResponseHandler?) {
        if (codezUrl == "") {
            handler?.onNotConnected(connection)
            return
        }
        AsyncHttpClient().delete("$codezUrl/delete/$cid", handler)
    }

    private fun createParams(item: Item): RequestParams {
        val params = RequestParams("cid", item.getId())
        params.put("number", item.getNumber())
        params.put("street", item.getStreet())
        params.put("codes", item.codesString)
        params.put("notes", item.getNotes())
        params.put("lat", item.getLat())
        params.put("lng", item.getLng())
        params.put("precise", item.getPrecise())
        return params
    }

    private fun urlIsValid(url: String): Boolean {
        return UrlValidator().isValid(url)
    }
}