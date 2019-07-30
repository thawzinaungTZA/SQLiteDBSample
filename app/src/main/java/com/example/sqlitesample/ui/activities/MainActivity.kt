package com.example.sqlitesample.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.sqlitesample.ui.adapters.ContactListAdapter
import com.example.sqlitesample.R
import com.example.sqlitesample.model.Contact
import com.example.sqlitesample.model.DataModel
import kotlinx.android.synthetic.main.activity_main.*
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric


class MainActivity : AppCompatActivity() {

    private val mAdapter: ContactListAdapter by lazy { ContactListAdapter(this::onClickItem, this::onLongClickItem) }
    private lateinit var dataModel: DataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setContentView(R.layout.activity_main)
        rvContact.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }

        dataModel = DataModel.getInstance(this)
    }

    override fun onResume() {
        super.onResume()
        mAdapter.setContactList(dataModel.getContactList())
    }

    private fun onClickItem(contact: Contact) {
        val intent = AddContactInfoActivity.newActivity(
            this,
            isEdit = true,
            id = contact.id,
            name = contact.name,
            phone = contact.phone,
            address = contact.address
        )
        startActivity(intent)
    }

    private fun onLongClickItem(contact: Contact) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Delete")
            .setMessage("Are you sure to deleteContact?")
            .setPositiveButton("OK") { _, _ ->
                dataModel.deleteContact(contact)
                mAdapter.setContactList(dataModel.getContactList())
                Toast.makeText(applicationContext, "Successfully deleted ${contact.name}", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { _, _ ->

            }
            .create()
        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menuAdd) {
//            val intent = Intent(this, AddContactInfoActivity::class.java)
//            intent.putExtra("isEdit", false)
//            startActivity(intent)

            val intent = AddContactInfoActivity.newActivity(this, isEdit = false)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}
