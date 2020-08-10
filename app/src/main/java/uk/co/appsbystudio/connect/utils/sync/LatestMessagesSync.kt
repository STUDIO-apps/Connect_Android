package uk.co.appsbystudio.connect.utils.sync

import android.content.Context
import android.database.Cursor
import android.os.AsyncTask

class LatestMessagesSync(val context: Context): AsyncTask<Void, Void, Void>() {

    val cursor: Cursor? = null

    override fun onPreExecute() {
        super.onPreExecute()

    }

    override fun doInBackground(vararg params: Void?): Void {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}