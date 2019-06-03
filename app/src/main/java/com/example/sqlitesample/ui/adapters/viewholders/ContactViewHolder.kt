package com.example.sqlitesample.ui.adapters.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.sqlitesample.model.Contact
import kotlinx.android.synthetic.main.list_item_contact.view.*

class ContactViewHolder(
    private val view: View,
    private val onClick: (contact: Contact) -> Unit,
    private val onLongClick: (contact: Contact) -> Unit
) :
    RecyclerView.ViewHolder(view) {
    fun setData(contact: Contact) {
        view.apply {
            tvName.text = contact.name
            tvPhone.text = contact.phone
            tvAddress.text = contact.address
        }
        view.setOnClickListener { onClick(contact) }
        view.setOnLongClickListener {
            onLongClick(contact)
            true
        }
    }
}