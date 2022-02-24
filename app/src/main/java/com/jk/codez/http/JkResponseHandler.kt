package com.jk.codez.http

import com.jk.codez.connection.Connection
import com.loopj.android.http.TextHttpResponseHandler

abstract class JkResponseHandler: TextHttpResponseHandler() {
    abstract fun onNotConnected(conn: Connection)
}