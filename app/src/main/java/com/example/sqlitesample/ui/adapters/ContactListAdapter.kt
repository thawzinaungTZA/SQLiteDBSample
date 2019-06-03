package com.example.sqlitesample.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.sqlitesample.ui.adapters.viewholders.ContactViewHolder
import com.example.sqlitesample.R
import com.example.sqlitesample.model.Contact

class ContactListAdapter(private val onClick: (contact: Contact) -> Unit, private val onLongClick: (contact: Contact) -> Unit) :
    RecyclerView.Adapter<ContactViewHolder>() {
    private var contactDataList = emptyList<Contact>()
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ContactViewHolder {
        val view = LayoutInflater.from(p0.context).inflate(R.layout.list_item_contact, p0, false)
        return ContactViewHolder(view = view, onClick = onClick, onLongClick = onLongClick)
    }

    override fun getItemCount(): Int {
        return contactDataList.size
    }

    override fun onBindViewHolder(viewholder: ContactViewHolder, position: Int) {
        viewholder.setData(contactDataList[position])
    }

    fun setContactList(contactList: List<Contact>) {
        this.contactDataList = contactList
        notifyDataSetChanged()
    }
}