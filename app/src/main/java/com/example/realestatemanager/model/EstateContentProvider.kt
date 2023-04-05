package com.example.realestatemanager.model
import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.example.realestatemanager.activities.EstateDataBase.Companion.getDatabase
class EstateContentProvider:ContentProvider(){
    companion object {
        const val AUTHORITY = "com.example.realestatemanager.model"
        val URI_ITEM: Uri = Uri.parse("content://$AUTHORITY/estateData")
    }
    override fun onCreate(): Boolean {
       return true
    }
    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?,
    ): Cursor {
        if (context != null) {
            val db = getDatabase(context!!)
            val cursor = db.estateDao().readAllDataWithCursor()
            cursor.setNotificationUri(context!!.contentResolver, uri)
            return cursor
        }
        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.item/$AUTHORITY.estateData"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri {
        if (context != null) {
            val db = getDatabase(context!!)
            val id = db.estateDao().addEstateForContentProvider(EstateData.fromContentValues(values!!))
            if (id != 0L) {
                context!!.contentResolver.notifyChange(uri, null)
                return ContentUris.withAppendedId(uri, id)
            }
        }
        throw IllegalArgumentException("Failed to insert row into $uri")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw IllegalArgumentException("No data can be deleted")
    }
    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?,
    ): Int {
        if (context != null) {
            val db = getDatabase(context!!)
            val count =
                db.estateDao().updateEstateForContentProvider(EstateData.fromContentValues(values!!))
            context!!.contentResolver.notifyChange(uri, null)
            return count
        }
        throw IllegalArgumentException("Failed to update row into $uri")
    }
}