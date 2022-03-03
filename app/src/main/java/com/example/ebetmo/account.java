package com.example.ebetmo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class account extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    SessionManager sessionManager;
    TextView coin, name;
    ImageView user_image;
    ImageButton close;
    Button verification_btn, logout_btn, terms_btn, to_profile;
    double my_coin;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        coin = findViewById(R.id.coin);
        name = findViewById(R.id.user_name);
        user_image = findViewById(R.id.user_image);
        close = findViewById(R.id.close);
        verification_btn = findViewById(R.id.verification_btn);
        logout_btn = findViewById(R.id.logout_btn);
        terms_btn = findViewById(R.id.terms_btn);
        to_profile = findViewById(R.id.profile_btn);

        try{
            sessionManager = new SessionManager(getApplicationContext());
            dbHelper = new DBHelper(getApplicationContext());
            sqLiteDatabase = dbHelper.getWritableDatabase();
            my_coin = Double.parseDouble(sessionManager.getCoin());
            coin.setText("₱"+my_coin);

            name.setText(sessionManager.getName());
            displayUserImage(sessionManager.getId());
            dialog = new Dialog(account.this);

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


        terms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), terms.class));

            }
        });
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.logout_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                Button yes = dialog.findViewById(R.id.yes_btn);
                Button cancel = dialog.findViewById(R.id.cancel_btn);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //set login false
                        sessionManager.setLogin(false);
                        //ser username empty
//                        sessionManager.setUsername("");
                        sessionManager.editor.clear();
                        sessionManager.editor.commit();
                        //redirect to activity
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        // finish activity
                        finish();
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });




                dialog.show();
            }
        });

        verification_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setContentView(R.layout.verification_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(true);
                Button to_verify = dialog.findViewById(R.id.verify_btn);
                ImageButton close = dialog.findViewById(R.id.close);

                to_verify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String check_verify = sessionManager.getValid();
                        if (check_verify.equals("Verified")){
                            Toast.makeText(account.this, "You are Already Verified.", Toast.LENGTH_SHORT).show();
                        }else{
                            startActivity(new Intent(getApplicationContext(), verification.class));

                        }
                        dialog.dismiss();

                    }
                });

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

            }
        });

        to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), edit_profile.class));
                finish();
            }
        });
    }

    private void displayUserImage(String id) {
        Cursor image = sqLiteDatabase.rawQuery("SELECT * FROM users WHERE id=?", new String[]{id});
        if (image.getCount()>0){
            while (image.moveToNext()) {
                byte[]profile = image.getBlob(6);
                Bitmap bitmap = BitmapFactory.decodeByteArray(profile, 0,profile.length);
                user_image.setImageBitmap(bitmap);
            }

        }else Toast.makeText(this, "Error: Fetching User Image.", Toast.LENGTH_SHORT).show();

    }
}