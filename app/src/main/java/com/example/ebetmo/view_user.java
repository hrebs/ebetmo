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
    SQLiteDatabase sqLiteDatabase;
    DBHelper dbHelper;

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

        try{
            dbHelper = new DBHelper(getApplicationContext());
            sqLiteDatabase = dbHelper.getWritableDatabase();
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error. Reload App!", Toast.LENGTH_SHORT).show();

        }

        if(getIntent().getBundleExtra("user_data")!=null){
            Bundle bundle = getIntent().getBundleExtra("user_data");
            user_id = String.valueOf(bundle.getInt("user_id"));

            displayUser(user_id);


        }
    }

    private void displayUser(String user_id) {
        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM users WHERE id =  ?", new String[]{user_id});
        if (c.getCount()>0){
            while(c.moveToNext()){
                user_name.setText(c.getString(1));
                user_email.setText(c.getString(4));
                user_contact.setText(c.getString(2));
                user_location.setText(c.getString(3));

                byte[] image = c.getBlob(6);
                Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0,image.length);
                user_image.setImageBitmap(bitmap);
            }
        }else{
            Toast.makeText(this, "Failed to fetch user", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}