package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;
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
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    SessionManager sessionManager;
    String current_session_id;
    RecyclerView recyclerView, coin_rv;
    NotifyAdapter notifyAdapter;
    CoinAdapter coinAdapter;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        close = findViewById(R.id.close);
        recyclerView = findViewById(R.id.notification_rv);
        coin_rv = findViewById(R.id.TopUp_rv);


        try{
            sessionManager = new SessionManager(this);
            dbHelper = new DBHelper(getApplicationContext());
            sqLiteDatabase = dbHelper.getWritableDatabase();
            dialog = new Dialog(this);
            current_session_id = sessionManager.getId();
            displayNotification(current_session_id);
            displayTopUp(current_session_id);

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error. Reload App!", Toast.LENGTH_SHORT).show();

        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void displayTopUp(String current_session_id) {
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM topUp WHERE owner_id=? ORDER BY id DESC", new String[]{current_session_id});
        ArrayList<ModelCoin> modelCoins = new ArrayList<>();
        if (c.getCount()>0){
            coin_rv.setVisibility(View.VISIBLE);
            while(c.moveToNext()){
                int id = c.getInt(0);
                String owner = c.getString(1);
                String context = c.getString(2);
                String date = c.getString(3);

                modelCoins.add(new ModelCoin(id,owner,context,date));

            }
            coinAdapter = new CoinAdapter(this,R.layout.single_topup, modelCoins,sqLiteDatabase,dbHelper);
            coin_rv.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));
            coin_rv.setAdapter(coinAdapter);

        }else coin_rv.setVisibility(View.GONE);
        c.close();
    }

    private void displayNotification(String current_session_id) {
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM bets WHERE owner_id=? ORDER BY id DESC", new String[]{current_session_id});
        ArrayList<NotifyModel> notifyModels = new ArrayList<>();
        if (c.getCount()>0){
            recyclerView.setVisibility(View.VISIBLE);
            while(c.moveToNext()){
                int id = c.getInt(0);
                String owner_id = c.getString(1);
                String item_id = c.getString(2);
                String item_owner_id = c.getString(3);
                byte[] image = c.getBlob(4);
                String status = c.getString(5);
                String chose = c.getString(6);
                String date = c.getString(7);

                notifyModels.add(new NotifyModel(id,owner_id,item_id,item_owner_id,status,chose,image,date));

            }
            notifyAdapter = new NotifyAdapter(this,R.layout.single_notify,notifyModels,sqLiteDatabase,dbHelper,sessionManager,dialog);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));
            recyclerView.setAdapter(notifyAdapter);
            c.close();

        }else {
            recyclerView.setVisibility(View.GONE);
        }
    }
}