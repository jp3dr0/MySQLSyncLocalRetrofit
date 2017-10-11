package com.jp3dr0.mysqlsynclocalretrofit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactHolder> {

    private ArrayList<Contact> contacts = new ArrayList<>();

    public ContactAdapter(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {

        holder.name.setText(contacts.get(position).getName());
        int sync_status = contacts.get(position).getSync_status();
        if (sync_status == DbContract.SYNC_STATUS_OK) {
            holder.sync_status.setImageResource(R.drawable.check);
        }
        else {
            holder.sync_status.setImageResource(R.drawable.alert);
        }

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }


}