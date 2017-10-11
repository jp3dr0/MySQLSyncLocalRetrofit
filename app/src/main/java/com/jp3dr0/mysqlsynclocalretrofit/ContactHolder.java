package com.jp3dr0.mysqlsynclocalretrofit;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactHolder extends RecyclerView.ViewHolder {

    ImageView sync_status;
    TextView name;

    public ContactHolder(View itemView) {
        super(itemView);
        sync_status = (ImageView) itemView.findViewById(R.id.imgSync);
        name = (TextView) itemView.findViewById(R.id.txtName);
    }
}