package com.example.reachout;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView nameView, numberView;

    public MyViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.contactImage);
        nameView = itemView.findViewById(R.id.name);
        numberView = itemView.findViewById(R.id.number);
    }
}
