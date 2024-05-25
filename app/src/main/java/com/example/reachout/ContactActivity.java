package com.example.reachout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity implements MyAdapter.OnItemLongClickListener {

    private static final int REQUEST_CREATE_CONTACT = 1;
    private List<item> items;
    private MyAdapter adapter;
    private SearchView searchView;

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.search_view);
        items = new ArrayList<>();
        items.add(new item("Eleanor Pena", "078 282 4333", R.drawable.n));
        items.add(new item("Jenny Wilson", "077 545 8785", R.drawable.c));
        items.add(new item("Robert Fox", "070 878 6087", R.drawable.l));
        items.add(new item("Albert Flores", "078 282 3334", R.drawable.d));
        items.add(new item("Ronald Eduardo", "077 664 8570", R.drawable.h));
        items.add(new item("Devon Lane", "070 339 3621", R.drawable.f));
        items.add(new item("Darrell Steward", "070 602 8446", R.drawable.g));
        items.add(new item("Dianne Russell", "073 870 6764", R.drawable.r));
        items.add(new item("Arlene McCoy", "070 844 6302", R.drawable.i));
        items.add(new item("Savannah Nguyen", "034 612 9658", R.drawable.q));
        items.add(new item("Ralph Edwards", "075 452 3865", R.drawable.k));
        items.add(new item("Arthur Shawn", "011 234 5789", R.drawable.p));
        items.add(new item("Philip Morgan", "072 443 2782", R.drawable.m));
        items.add(new item("Jane Cooper", "034 229 2452", R.drawable.a));
        items.add(new item("Hawkins Lee", "076 111 8733", R.drawable.o));
        items.add(new item("Johannes Fox", "074 341 6820", R.drawable.b));
        items.add(new item("Edwards Garrett", "073 424 5646", R.drawable.j));
        items.add(new item("Floyd Miles", "071 112 5690", R.drawable.e));

        adapter = new MyAdapter(this, items, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.filter(newText); // Filter the RecyclerView items based on the search query
                return true;
            }
        });


        fab = findViewById(R.id.fab_add_contact);

        fab.setOnClickListener(v -> {
            Intent intent = new Intent(ContactActivity.this, CreateContactActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onItemLongClick(int position) {
        // Show confirmation dialog
        new AlertDialog.Builder(this)
                .setTitle("Delete Contact")
                .setMessage("Are you sure you want to delete this contact?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Remove the item from the list
                    items.remove(position);
                    // Notify the adapter about the removed item
                    adapter.notifyItemRemoved(position);
                    Toast.makeText(ContactActivity.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CREATE_CONTACT && resultCode == RESULT_OK && data != null) {
            // Retrieve the new contact data from the result intent
            String name = data.getStringExtra("name");
            String phone = data.getStringExtra("phone");
            String imageUri = data.getStringExtra("imageUri");

            if (name != null && phone != null && imageUri != null) {
                // Add the new contact to the list
                items.add(new item(name, phone, Uri.parse(imageUri)));
                adapter.notifyItemInserted(items.size() - 1);
            }
        }
    }

}
