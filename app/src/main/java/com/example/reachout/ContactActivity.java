package com.example.reachout;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        List<item> items = new ArrayList<item>();
        items.add(new item("Eleanor Pena" , "078 2824 3334", R.drawable.n));
        items.add(new item("Jenny Wilson", "077 5465 8785", R.drawable.c));
        items.add(new item("Robert Fox", "070 8786 0987", R.drawable.l));
        items.add(new item("Albert Flores", "078 2824 3334", R.drawable.d));
        items.add(new item("Ronald Eduardo", "077 6764 8570", R.drawable.h));
        items.add(new item("Devon Lane", "070 3397 6621", R.drawable.f));
        items.add(new item("Darrell Steward", "070 6302 8446", R.drawable.g));
        items.add(new item("Dianne Russell" , "073 8570 6764", R.drawable.r));
        items.add(new item("Arlene McCoy", "070 8446 6302 ", R.drawable.i));
        items.add(new item("Savannah Nguyen", "034 6129 6058", R.drawable.q));
        items.add(new item("Ralph Edwards", "075 4523 7865", R.drawable.k));
        items.add(new item("Arthur Shawn", "011 2345 6789", R.drawable.p));
        items.add(new item("Philip Morgan", "072 4432 6782", R.drawable.m));
        items.add(new item("Jane Cooper", "034 2293 2452", R.drawable.a));
        items.add(new item("Hawkins Lee", "076 1111 8733", R.drawable.o));
        items.add(new item("johannes Fox", "074 3412 6820", R.drawable.b));
        items.add(new item("Edwards Garrett", "073 4234 5646", R.drawable.j));
        items.add(new item("Floyd Miles", "071 1212 5690", R.drawable.e));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(),items));

    }
}