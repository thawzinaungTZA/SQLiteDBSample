package com.example.sqlitesample.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import android.util.Log
import com.example.sqlitesample.model.Contact

class ContactDBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "contract.db"
        const val DATABASE_VERSION = 1

        //const val SQL_CREATE_ENTRIES = "CREATE TABLE contact (_id INTEGER PRIMARY KEY, name TEXT, phone TEXT, address TEXT)"
        const val SQL_CREATE_ENTRIES = "CREATE TABLE ${ContactContract.ContactEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY, " +
                "${ContactContract.ContactEntry.COLUMN_NAME_USERNAME} TEXT, " +
                "${ContactContract.ContactEntry.COLUMN_NAME_PHONE_NUMBER} TEXT, " +
                "${ContactContract.ContactEntry.COLUMN_NAME_ADDRESS} TEXT)"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${ContactContract.ContactEntry.TABLE_NAME}"

        private var instance: ContactDBHelper? = null

        fun getInstance(context: Context): ContactDBHelper {
            if (instance == null) {
                instance = ContactDBHelper(context)
            }
            return instance as ContactDBHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun add(contact: Contact) {
        val db = this.writableDatabase

        val values = ContentValues().apply {
            put(ContactContract.ContactEntry.COLUMN_NAME_USERNAME, contact.name)
            put(ContactContract.ContactEntry.COLUMN_NAME_PHONE_NUMBER, contact.phone)
            put(ContactContract.ContactEntry.COLUMN_NAME_ADDRESS, contact.address)
        }

        // Insert the new row, returning the primary key value of the new row
        val newRowId = db.insert(ContactContract.ContactEntry.TABLE_NAME, null, values)
        Log.d("NewRowId", "$newRowId")
    }

    fun getDataList(): List<Contact> {
        val db = this.readableDatabase

        // Define a projection that specifies which columns from the database
        // you will actually use after this query
        val projection = arrayOf(
            BaseColumns._ID, ContactContract.ContactEntry.COLUMN_NAME_USERNAME,
            ContactContract.ContactEntry.COLUMN_NAME_PHONE_NUMBER, ContactContract.ContactEntry.COLUMN_NAME_ADDRESS
        )

        // Filter results WHERE "name" = 'Mg Mg'
        // val selection = "${ContactContract.ContactEntry.COLUMN_NAME_USERNAME} = ?"
        // val selectionArgs = arrayOf("Mg Mg")

        // How you want the results sorted in the resulting Cursor
        val sortOrder = "${ContactContract.ContactEntry.COLUMN_NAME_USERNAME} ASC" // or DESC

        val cursor = db.query(
            ContactContract.ContactEntry.TABLE_NAME,     // The table to query
            projection,                                 // The array of columns to return (pass null to get all)
            null,                               // The columns for the WHERE clause
            null,                           // The values for the WHERE clause
            null,                             // don't group the rows
            null,                             // don't filter by row groups
            sortOrder                              // The sort order
        )

        val contactList = mutableListOf<Contact>()
        with(cursor) {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(android.provider.BaseColumns._ID))
                val name =
                    getString(getColumnIndexOrThrow(com.example.sqlitesample.database.ContactContract.ContactEntry.COLUMN_NAME_USERNAME))
                val phoneNumber =
                    getString(getColumnIndexOrThrow(com.example.sqlitesample.database.ContactContract.ContactEntry.COLUMN_NAME_PHONE_NUMBER))
                val address =
                    getString(getColumnIndexOrThrow(com.example.sqlitesample.database.ContactContract.ContactEntry.COLUMN_NAME_ADDRESS))
                contactList.add(com.example.sqlitesample.model.Contact(id, name, phoneNumber, address))
            }
        }
        return contactList
    }

    fun delete(contact: Contact) {
        val db = this.writableDatabase

        // Define 'where' part of query.
        val selection = "${BaseColumns._ID} = ?"

        // Specify arguments in placeholder order.
        val selectionArgs = arrayOf(contact.id.toString())

        // The return value indicates the number of rows that were deleted from the database.
        val deletedRow = db.delete(ContactContract.ContactEntry.TABLE_NAME, selection, selectionArgs)
        Log.d("DeletedRow", "$deletedRow")
    }

    fun update(contact: Contact) {
        val db = this.writableDatabase

        // New value for one column
        val values = ContentValues().apply {
            put(ContactContract.ContactEntry.COLUMN_NAME_USERNAME, contact.name)
            put(ContactContract.ContactEntry.COLUMN_NAME_PHONE_NUMBER, contact.phone)
            put(ContactContract.ContactEntry.COLUMN_NAME_ADDRESS, contact.address)
        }

        // Which row to updateContact
        val selection = "${BaseColumns._ID} = ?"
        val selectionArgs = arrayOf(contact.id.toString())

        // The return value is the number of rows affected in the database.
        val count = db.update(
            ContactContract.ContactEntry.TABLE_NAME,
            values,
            selection,
            selectionArgs
        )
        Log.d("Number of rows affected", "$count")
    }
}