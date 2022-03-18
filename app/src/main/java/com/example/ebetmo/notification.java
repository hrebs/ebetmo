package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class notification extends AppCompatActivity {
    ImageButton close;
    RecyclerView recyclerView, coin_rv;
    NotifAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        close = findViewById(R.id.close);
        recyclerView = findViewById(R.id.notification_rv);
        coin_rv = findViewById(R.id.TopUp_rv);

        displayItems();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void displayItems() {
        recyclerView.setVisibility(View.VISIBLE);
        itemAdapter = new NotifAdapter(this, FormData.itemName, FormData.image1, FormData.win, FormData.date, FormData.id1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(itemAdapter);
    }
}