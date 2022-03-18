package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class view_user extends AppCompatActivity {
    String user_id;
    ImageButton close;
    TextView user_name, user_email,user_location, user_contact;
    ImageView user_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);
        close = findViewById(R.id.close);
        user_name = findViewById(R.id.user_name);
        user_email = findViewById(R.id.user_email);
        user_location = findViewById(R.id.user_location);
        user_contact = findViewById(R.id.user_contact);
        user_image = findViewById(R.id.user_image);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

}