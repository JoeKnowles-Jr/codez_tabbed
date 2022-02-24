package com.jk.codez

import android.app.Activity
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.underscore.U
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jk.codez.ad.AestheticDialog
import com.jk.codez.ad.DialogManager
import com.jk.codez.connection.Connection
import com.jk.codez.http.JkResponseHandler
import com.jk.codez.http.Network
import com.jk.codez.http.TestCallback
import com.jk.codez.item.Item
import com.jk.codez.room.CodezDao
import com.jk.codez.http.TestResponse
import cz.msebera.android.httpclient.Header
import java.util.function.Supplier

class CodezViewModel : ViewModel() {

    var localItems: LiveData<List<Item>> = MutableLiveData()
    var remoteItems: MutableLiveData<List<Item>> = MutableLiveData()
    var connected: MutableLiveData<Boolean> = MutableLiveData(false)

    fun initialize(dao: CodezDao?, dMgr: DialogManager?) {
        dialogManager = dMgr
        if (dao != null) {
            mDao = dao
            localItems = mDao!!.allItemsAsLiveData()

            val lastConnection: String = Settings.getLastConnection()
            if (lastConnection != "none") {
                val conn = mDao?.getLastConnection(lastConnection)
                if (conn != null) {
                    mNet = Network(conn)
                    connected.value = true
                    refreshRemoteItems()
                }
            } else {
                connected.value = false
            }
        }
    }

    fun showConnectDialog() {
        dialogManager?.showConnectionDialog()
    }

    fun showItemDialog(item: Item?, remote: Boolean) {
        dialogManager?.showItemDialog(item, remote)
    }

    fun testUrl(url: String?, cb: TestCallback) {
        val testNetwork = Network(Connection("", url!!, "none", "", null, null, null))
        testNetwork.testUrl(object : JkResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseString: String,
                throwable: Throwable
            ) {
                println("fail")
                mNet = null
                cb.done(TestResponse("Failed!", url, ""))
            }

            override fun onNotConnected(conn: Connection) {

            }

            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseString: String
            ) {
                mNet = testNetwork
                refreshRemoteItems()
                val type = object : TypeToken<TestResponse?>() {}.type
                val tr: TestResponse = Gson().fromJson(responseString, type)
                val msg =
                    Supplier<Void?> {
                        cb.done(tr)
                        null
                    }
                U.setTimeout(msg, 750)
                val desc =
                    Supplier<Void?> {
                        null
                    }
                U.setTimeout(desc, 2750)
            }
        })
    }

    fun refreshRemoteItems() {
        mNet?.getCodes(object: JkResponseHandler() {
            override fun onNotConnected(conn: Connection) {

            }

            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?
            ) {
                val type = object : TypeToken<ArrayList<Item?>?>() {}.type
                remoteItems.value = (Gson().fromJson<ArrayList<Item>>(responseString, type))
            }


            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?,
                throwable: Throwable?
            ) {
                println("Failure!!!")
            }
        })
    }

    fun addLocalItem(dialog: AestheticDialog.Builder) {
        val item = dialog.getItem()
        item.setId(System.currentTimeMillis().toString())
        mDao?.insertItem(dialog.getItem())
        dialog.dismiss()
    }

    fun addRemoteItem(dialog: AestheticDialog.Builder) {
        mNet?.addCode(dialog.getItem(), object : JkResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseString: String,
                throwable: Throwable
            ) {
            }

            override fun onNotConnected(conn: Connection) {

            }

            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseString: String
            ) {
                val type = object : TypeToken<java.util.ArrayList<Item?>?>() {}.type
                remoteItems.value = Gson().fromJson<List<Item>>(responseString, type)
                dialog.dismiss()
            }
        })
    }

    fun saveConnection(conn: Connection) {
        mDao?.insertConnection(conn)
    }

    companion object {
        private var mDao: CodezDao? = null
        private var mNet: Network? = null
        private var dialogManager: DialogManager? = null
    }

}