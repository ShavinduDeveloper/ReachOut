package com.example.reachout;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<item> items;
    private List<item> itemsFiltered; // List to hold filtered items
    private OnItemLongClickListener longClickListener;

    public MyAdapter(Context context, List<item> items, OnItemLongClickListener longClickListener) {
        this.context = context;
        this.items = items;
        this.itemsFiltered = new ArrayList<>(items); // Initialize filtered list with all items
        this.longClickListener = longClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.nameView.setText(itemsFiltered.get(position).getName());
        holder.numberView.setText(itemsFiltered.get(position).getPhone());
        holder.imageView.setImageResource(itemsFiltered.get(position).getImage());

        // Set click listener for the item
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ContactDetailActivity.class);
            intent.putExtra("name", itemsFiltered.get(holder.getAdapterPosition()).getName());
            intent.putExtra("phone", itemsFiltered.get(holder.getAdapterPosition()).getPhone());
            intent.putExtra("image", itemsFiltered.get(holder.getAdapterPosition()).getImage());
            intent.putExtra("position", holder.getAdapterPosition()); // Pass position to the ContactDetailActivity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

        // Set long click listener for the item
        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onItemLongClick(holder.getAdapterPosition());
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return itemsFiltered.size(); // Return size of filtered list
    }

    // Method to filter items based on search query
    public void filter(String query) {
        itemsFiltered.clear(); // Clear the current filtered list

        if (TextUtils.isEmpty(query)) {
            itemsFiltered.addAll(items); // If query is empty, show all items
        } else {
            String filterPattern = query.toLowerCase().trim();
            for (item item : items) {
                if (item.getName().toLowerCase().contains(filterPattern)) {
                    itemsFiltered.add(item); // Add item to filtered list if it matches the query
                }
            }
        }

        notifyDataSetChanged(); // Notify adapter about the changed data
    }

    // Interface for long click listener
    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }


}
